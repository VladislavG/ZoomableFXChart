package solrTest;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.core.CoreContainer;
import org.opendolphin.core.comm.Command;
import org.opendolphin.core.comm.DataCommand;
import org.opendolphin.core.server.ServerAttribute;
import org.opendolphin.core.server.action.DolphinServerAction;
import org.opendolphin.core.server.comm.ActionRegistry;
import org.opendolphin.core.server.comm.CommandHandler;
import solrTest.Item;
import solrTest.SolrService;

import java.util.*;

public class ApplicationAction extends DolphinServerAction{
    private static final String SOLR_INDEX_DIR = "solr";
    private static final String CORE_NAME = "core";
    private static SolrServer solrServer;


    public ApplicationAction() {
        super();

    }

    public static SolrServer getSolrServer() throws SolrServerException {
        if (null == solrServer) {
            String solrHome = "C:\\Users\\vladislav\\jumpstart-gradle\\client\\src\\main\\resources\\solr";
            CoreContainer coreContainer = new CoreContainer(solrHome);
            coreContainer.load();
            solrServer = new EmbeddedSolrServer(coreContainer, CORE_NAME);
        }

        return solrServer;
    }

    public void registerIn(ActionRegistry actionRegistry) {
        actionRegistry.register("Query", new CommandHandler<Command>() {
            public void handleCommand(Command command, List<Command> response) {

                SolrQuery solrQuery;
                String startDate = getServerDolphin().findPresentationModelById("STATE").findAttributeByPropertyName("STARTDATE").getValue().toString();
                String endDate = getServerDolphin().findPresentationModelById("STATE").findAttributeByPropertyName("ENDDATE").getValue().toString();
                changeValue((ServerAttribute) getServerDolphin().findPresentationModelById("STATE").findAttributeByPropertyName("DISABLED"), true);

                solrQuery = new SolrQuery("date:[" + startDate + " TO " + endDate + "]");

                solrQuery.setStart(0);                          //startign index
                solrQuery.setRows(64486);                          //number of rows
                List<Item> list = new ArrayList<Item>();

                QueryResponse SolrResponse = null;          //make a response based on the above query
                try {
                    float start = System.currentTimeMillis();
                    SolrResponse = getSolrServer().query(solrQuery);
                    System.out.println("Solr took: " + SolrResponse.getQTime());
                } catch (SolrServerException e) {
                    e.printStackTrace();
                }
                ArrayList<Float> movingAverages = new ArrayList<Float>();
                SolrDocumentList results = SolrResponse.getResults();
                int size = results.size()- 1;
                ArrayList<String> datesUsed = new ArrayList<>();

                for (int f = 1; f < size; f++){
                    ArrayList<Float> neighbouringValues = new ArrayList<Float>();
                    neighbouringValues.add(Float.valueOf(results.get(f-1).getFieldValue("high").toString()));
                    neighbouringValues.add(Float.valueOf(results.get(f).getFieldValue("high").toString()));
                    neighbouringValues.add(Float.valueOf(results.get(f+1).getFieldValue("high").toString()));
                    Collections.sort(neighbouringValues);

                    movingAverages.add(median(neighbouringValues));
                }
                    for (int i = 0; i < size; i++){
                        int factorsToKeep = (int) Math.ceil(size / 600);
                        if (factorsToKeep == 0) factorsToKeep=1;
                        if (i% factorsToKeep == 0 && !(i==0) && !(results.get(size - i).getFieldValue("spike").equals("spike")) && results.get(size - i).getFieldValue("series").toString().equals("3")){
                            try{
                                Item item = new Item();
                                int d = 0;
                                Float median = new Float(0.0);
                                ArrayList<Float> medians = new ArrayList<>();
                                item.setId(results.get(i).getFieldValue("id").toString());
                                item.setDate(results.get(size - i).getFieldValue("date").toString());
                                datesUsed.add(results.get(size - i).getFieldValue("date").toString());
                                try{
                                    float threshHold = movingAverages.get(size - i) / (Float) results.get(size - i).getFieldValue("high");
                                    item.setClose(String.valueOf(movingAverages.get(size - i) - (threshHold*6)));
                                    item.setOpen(String.valueOf(movingAverages.get(size - i) + (threshHold*6)));
                                }catch (Exception e){
                                    item.setClose("0");
                                    item.setOpen("0");
                                }
                                item.setHigh(results.get(size - i).getFieldValue("high").toString());
                                item.setAdj_close(results.get(i).getFieldValue("adj_close").toString());
                                item.setSeries(results.get(size - i).getFieldValue("series").toString());
                                item.setLow(results.get(i).getFieldValue("low").toString());
                                item.setSpike("normal");

                                list.add(item);
                            }catch (Exception e){}
                        }else if(results.get(size - i).getFieldValue("spike").equals("spike")){
                            Item item = new Item();
                            item.setId(results.get(i).getFieldValue("id").toString());
                            item.setDate(results.get(size - i).getFieldValue("date").toString());
                            datesUsed.add(results.get(size - i).getFieldValue("date").toString());
                            try{

                                float threshHold = movingAverages.get(size - i) / (Float) results.get(size - i).getFieldValue("high");
                                item.setClose(String.valueOf(movingAverages.get(size - i) - threshHold*6));
                                item.setOpen(String.valueOf(movingAverages.get(size - i) + threshHold*6));
                            }catch (Exception e){
                                item.setClose("0");
                                item.setOpen("0");
                            }
                            item.setHigh(results.get(size - i).getFieldValue("high").toString());
                            item.setAdj_close(results.get(i).getFieldValue("adj_close").toString());
                            item.setSeries(results.get(size - i).getFieldValue("series").toString());
                            item.setLow(results.get(i).getFieldValue("low").toString());
                            item.setSpike("spike");
                            list.add(item);
                        }else if(results.get(size - i).getFieldValue("spike").equals("diffSpike")){
                            Item item = new Item();
                            item.setId(results.get(i).getFieldValue("id").toString());
                            item.setDate(results.get(size - i).getFieldValue("date").toString());
                            datesUsed.add(results.get(size - i).getFieldValue("date").toString());
                            try{

                                float threshHold = movingAverages.get(size - i) / (Float) results.get(size - i).getFieldValue("high");
                                item.setClose(String.valueOf(movingAverages.get(size - i) - threshHold*6));
                                item.setOpen(String.valueOf(movingAverages.get(size - i) + threshHold*6));
                            }catch (Exception e){
                                item.setClose("0");
                                item.setOpen("0");
                            }
                            item.setHigh(results.get(size - i).getFieldValue("high").toString());
                            item.setAdj_close(results.get(i).getFieldValue("adj_close").toString());
                            item.setSeries(results.get(size - i).getFieldValue("series").toString());
                            item.setLow(results.get(i).getFieldValue("low").toString());
                            item.setSpike("diffSpike");
                            list.add(item);
                        }else if (datesUsed.contains(results.get(size - i).getFieldValue("date").toString())){
                            try{
                                Item item = new Item();
                                int d = 0;
                                Float median = new Float(0.0);
                                ArrayList<Float> medians = new ArrayList<>();
                                item.setId(results.get(i).getFieldValue("id").toString());
                                item.setDate(results.get(size - i).getFieldValue("date").toString());
                                datesUsed.add(results.get(size - i).getFieldValue("date").toString());
                                try{
                                    float threshHold = movingAverages.get(size - i) / (Float) results.get(size - i).getFieldValue("high");
                                    item.setClose(String.valueOf(movingAverages.get(size - i) - (threshHold*6)));
                                    item.setOpen(String.valueOf(movingAverages.get(size - i) + (threshHold*6)));
                                }catch (Exception e){
                                    item.setClose("0");
                                    item.setOpen("0");
                                }
                                item.setHigh(results.get(size - i).getFieldValue("high").toString());
                                item.setAdj_close(results.get(i).getFieldValue("adj_close").toString());
                                item.setSeries(results.get(size - i).getFieldValue("series").toString());
                                item.setLow(results.get(i).getFieldValue("low").toString());
                                item.setSpike("normal");

                                list.add(item);
                            }catch (Exception e){}
                        }
                    }
//                solrServer.shutdown();
                int f = 0;
                for (Item item : list) {
                    Map itemMap = new HashMap<>();
                    itemMap.put(f, item);
                    response.add(new DataCommand(itemMap));
                    f++;
                }
            }
        });
    }

    public static float median(ArrayList<Float> m) {
        int middle = m.size()/2;
        if (m.size()%2 == 1) {
            return m.get(middle);
        } else {
            return (m.get(middle - 1) + m.get(middle)) / 2;
        }
    }
}