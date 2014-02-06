package template;

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
import org.opendolphin.core.server.action.DolphinServerAction;
import org.opendolphin.core.server.comm.ActionRegistry;
import org.opendolphin.core.server.comm.CommandHandler;
import solrTest.Item;
import solrTest.SolrService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ApplicationAction extends DolphinServerAction{
    private static final String SOLR_INDEX_DIR = "solr";
    private static final String CORE_NAME = "core";
    private static SolrServer solrServer;


    public ApplicationAction() {
        super();

    }

    public static SolrServer getSolrServer() throws SolrServerException {
        if (null == solrServer) {
            String solrHome = "C:\\Users\\vladislav\\solrTest\\src\\main\\resources\\solr";
            CoreContainer coreContainer = new CoreContainer(solrHome);
            coreContainer.load();
            solrServer = new EmbeddedSolrServer(coreContainer, CORE_NAME);
        }

        return solrServer;
    }

    public void registerIn(ActionRegistry actionRegistry) {
        actionRegistry.register("Query", new CommandHandler<Command>() {
            public void handleCommand(Command command, List<Command> response) {
                long start = System.currentTimeMillis();
                SolrService solrService = new SolrService();
                try {
                    solrServer = solrService.getSolrServer();
                } catch (SolrServerException e) {
                    e.printStackTrace();
                }
                SolrQuery solrQuery;

                solrQuery = new SolrQuery("*:*");

                solrQuery.setStart(0);                          //startign index
                solrQuery.setRows(16000);                          //number of rows
                List<Item> list = new ArrayList<Item>();
                QueryResponse responses = null;
                try {
                    responses = getSolrServer().query(solrQuery);
                } catch (SolrServerException e) {
                }
                SolrDocumentList results = responses.getResults();

                for (SolrDocument result : results) {
                    response.add(new DataCommand(new HashMap(1, Float.valueOf(result.getFieldValue("high").toString()))));
                }
            }
        });
    }
}