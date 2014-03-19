package solrTest;


import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.beans.Field;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.CoreContainer;
import org.apache.solr.client.solrj.SolrServerException;
import org.opendolphin.core.client.ClientAttribute;
import org.opendolphin.core.client.ClientDolphin;
import org.opendolphin.core.client.ClientPresentationModel;

import org.opendolphin.core.client.comm.OnFinishedHandler;
import org.opendolphin.core.comm.DefaultInMemoryConfig;


public class SolrService {
    private static final String SOLR_INDEX_DIR = "solr";
    private static final String CORE_NAME = "core";
    private static SolrServer solrServer;

    public SolrService() {
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

    public static void main(String[] args) throws SolrServerException, FileNotFoundException, IOException {
        long start = System.currentTimeMillis();
        SolrService solrService = new SolrService();
        solrServer = solrService.getSolrServer();

        InputStream stream = SolrService.class.getClassLoader().getResourceAsStream("stocks.csv");
        InputStream stream2 = SolrService.class.getClassLoader().getResourceAsStream("stocks2.csv");
        InputStream stream3 = SolrService.class.getClassLoader().getResourceAsStream("stocks3.csv");
        InputStream stream4 = SolrService.class.getClassLoader().getResourceAsStream("stocks4.csv");
        InputStream streamNDQ = SolrService.class.getClassLoader().getResourceAsStream("nasdaq.csv");
        ArrayList<RecordItem> allRecords = new ArrayList<RecordItem>();

        try {

            final BufferedReader reader =
                    new BufferedReader(new InputStreamReader(stream));
            reader.skip(42);
            reader.lines().forEach(line -> {
                final String[] dataItems = line.split(",");
                int id = allRecords.size();
                RecordItem item = new RecordItem();
                item.setId(id);
                item.setDate(dataItems[0] + "T00:00:00Z");
                item.setOpen(Float.parseFloat(dataItems[1]));
                item.setHigh(Float.parseFloat(dataItems[2]));
                item.setLow(Float.parseFloat(dataItems[3]));
                item.setClose(Float.parseFloat(dataItems[4]));
                item.setSeries(0);
//                item.setVolume(Integer.parseInt(dataItems[5]));
                item.setAdj_close(Float.parseFloat(dataItems[6]));

                allRecords.add(item);

            });
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }   try {

            final BufferedReader reader =
                    new BufferedReader(new InputStreamReader(stream2));
            reader.skip(42);

            reader.lines().forEach(line -> {
                final String[] dataItems = line.split(",");
                int id = allRecords.size();
                RecordItem item = new RecordItem();
                item.setId(id);
                item.setDate(dataItems[0] + "T00:00:00Z");
                item.setOpen(Float.parseFloat(dataItems[1]));
                item.setHigh(Float.parseFloat(dataItems[2]));
                item.setLow(Float.parseFloat(dataItems[3]));
                item.setClose(Float.parseFloat(dataItems[4]));
                item.setSeries(1);
                item.setSpike("normal");
                item.setAdj_close(Float.parseFloat(dataItems[6]));

                allRecords.add(item);

            });
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }   try {

            final BufferedReader reader =
                    new BufferedReader(new InputStreamReader(stream3));
            reader.skip(42);
            reader.lines().forEach(line -> {
                final String[] dataItems = line.split(",");
                int id = allRecords.size();
                RecordItem item = new RecordItem();
                item.setId(id);
                item.setDate(dataItems[0] + "T00:00:00Z");
                item.setOpen(Float.parseFloat(dataItems[1]));
                item.setHigh(Float.parseFloat(dataItems[2]));
                item.setLow(Float.parseFloat(dataItems[3]));
                item.setClose(Float.parseFloat(dataItems[4]));
                item.setSeries(2);
                item.setSpike("normal");
//                item.setVolume(Integer.parseInt(dataItems[5]));
                item.setAdj_close(Float.parseFloat(dataItems[6]));

                allRecords.add(item);

            });
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }   try {

            final BufferedReader reader =
                    new BufferedReader(new InputStreamReader(streamNDQ));
            reader.skip(42);
            reader.lines().forEach(line -> {
                final String[] dataItems = line.split(",");
                int id = allRecords.size();
                RecordItem item = new RecordItem();
                item.setId(id);
                item.setDate(dataItems[0] + "T00:00:00Z");
                item.setOpen(Float.parseFloat(dataItems[1]));
                item.setHigh(Float.parseFloat(dataItems[2]));
                item.setLow(Float.parseFloat(dataItems[3]));
                item.setClose(Float.parseFloat(dataItems[4]));
                item.setSpike("normal");
                item.setSeries(4);
//                item.setVolume(Integer.parseInt(dataItems[5]));
                item.setAdj_close(Float.parseFloat(dataItems[6]));

                allRecords.add(item);

            });
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }   try {

            final BufferedReader reader =
                    new BufferedReader(new InputStreamReader(stream4));
            reader.skip(42);
            reader.lines().forEach(line -> {
                final String[] dataItems = line.split(",");
                int id = allRecords.size();
                RecordItem item = new RecordItem();
                item.setId(id);
                item.setDate(dataItems[0] + "T00:00:00Z");
                item.setOpen(Float.parseFloat(dataItems[1]));
                item.setHigh(Float.parseFloat(dataItems[2]));
                item.setLow(Float.parseFloat(dataItems[3]));
                item.setClose(Float.parseFloat(dataItems[4]));
                item.setSpike("normal");
                item.setSeries(3);
//                item.setVolume(Integer.parseInt(dataItems[5]));
                item.setAdj_close(Float.parseFloat(dataItems[6]));

                allRecords.add(item);

            });
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }



        for (RecordItem record : allRecords) {
            solrServer.addBean(record);
        }
        solrServer.commit();
        SolrQuery solrQuery;

        solrQuery = new SolrQuery("series:(0 OR 1 OR 2 OR 3)");

        solrQuery.setStart(0);                          //startign index
        solrQuery.setRows(64468);                          //number of rows
        List<Item> list = new ArrayList<Item>();
        ArrayList<RecordItem> allNewRecords = new ArrayList<RecordItem>();
        QueryResponse response = null;          //make a response based on the above query
        try {
            response = getSolrServer().query(solrQuery);
            System.out.println("Solr took: " + response.getQTime());
        } catch (SolrServerException e) {
            e.printStackTrace();
        }
        ArrayList<Float> movingAverages = new ArrayList<Float>();
        SolrDocumentList results = response.getResults();
        int size = results.size()- 1;
        ArrayList<Integer> largeDelta = new ArrayList<>();

        for (int f = 1; f < size; f++){
            ArrayList<Float> neighbouringValues = new ArrayList<Float>();
            neighbouringValues.add(Float.valueOf(results.get(f-1).getFieldValue("high").toString()));
            SolrDocument document = results.get(f);
            neighbouringValues.add(Float.valueOf(document.getFieldValue("high").toString()));
            neighbouringValues.add(Float.valueOf(results.get(f+1).getFieldValue("high").toString()));
            Collections.sort(neighbouringValues);

            float diffBetweenValAndMedian = median(neighbouringValues) / Float.valueOf(document.getFieldValue("high").toString());
            movingAverages.add(median(neighbouringValues));

            RecordItem item = new RecordItem();
            item.setId(Integer.valueOf(document.getFieldValue("id").toString()));
            item.setDate(String.valueOf(toUtcDate(String.valueOf(document.getFieldValue("date")))).concat("Z"));
            item.setOpen(Float.parseFloat(String.valueOf(document.getFieldValue("open"))));
            item.setHigh(Float.parseFloat(String.valueOf(document.getFieldValue("high"))));
            item.setLow(Float.parseFloat(String.valueOf(document.getFieldValue("low"))));
            item.setClose(Float.parseFloat(String.valueOf(document.getFieldValue("close"))));
//                item.setVolume(Integer.parseInt(dataItems[5]));
            item.setSeries(Integer.parseInt(String.valueOf(document.getFieldValue("series"))));
            item.setAdj_close(Float.parseFloat(String.valueOf(document.getFieldValue("adj_close"))));

            if (Math.abs(diffBetweenValAndMedian) > 1.05 || Math.abs(diffBetweenValAndMedian) < .97){
                switch (Integer.parseInt(String.valueOf(document.getFieldValue("series")))){
                    case 0: item.setSpike("spike");
                            break;
                    default: item.setSpike("diffSpike");
                            break;
                }
//                largeDelta.add(f);
            }else{
                item.setSpike("normal");
            }
            allNewRecords.add(item);
        }
        solrServer.deleteByQuery("series:(0 OR 1 OR 2 OR 3)");
        solrServer.commit();
        for (RecordItem allNewRecord : allNewRecords) {
            solrServer.addBean(allNewRecord);
        }
        solrServer.commit();
        solrServer.shutdown();
        System.out.println("Time take to index everything: " + (System.currentTimeMillis() - start) + " ms");

    }

    public static List<Item> getResults(String startDate, String endDate)throws SolrServerException, FileNotFoundException, IOException  {

        SolrService solrService = new SolrService();
        solrServer = solrService.getSolrServer();


        SolrQuery solrQuery;

        solrQuery = new SolrQuery("date:[" + startDate + " TO " + endDate + "]");

        solrQuery.setStart(0);                          //startign index
        solrQuery.setRows(64486);                          //number of rows
        List<Item> list = new ArrayList<Item>();

        QueryResponse response = null;          //make a response based on the above query
        try {
            float start = System.currentTimeMillis();
            response = getSolrServer().query(solrQuery);
            System.out.println("Solr took: " + response.getQTime());
        } catch (SolrServerException e) {
            e.printStackTrace();
        }
        ArrayList<Float> movingAverages = new ArrayList<Float>();
        SolrDocumentList results = response.getResults();
        int size = results.size()- 1;
        ArrayList<String> datesUsed = new ArrayList<>();

        for (int f = 1; f < size; f++){
            ArrayList<Float> neighbouringValues = new ArrayList<Float>();
            neighbouringValues.add(Float.valueOf(results.get(f-1).getFieldValue("high").toString()));
            neighbouringValues.add(Float.valueOf(results.get(f).getFieldValue("high").toString()));
            neighbouringValues.add(Float.valueOf(results.get(f+1).getFieldValue("high").toString()));
            Collections.sort(neighbouringValues);

            float diffBetweenValAndMedian = median(neighbouringValues) / Float.valueOf(results.get(f).getFieldValue("high").toString());
            movingAverages.add(median(neighbouringValues));
            if (Math.abs(diffBetweenValAndMedian) > 1.05 || Math.abs(diffBetweenValAndMedian) < .97){
               System.out.println(diffBetweenValAndMedian);
            };
        }

        int x = 0;
        for (int i = 0; i < size; i++){
            if (i%((int) Math.ceil(size / 600)) == 0 && !(i==0) && !(results.get(size - i).getFieldValue("spike").equals("spike")) && results.get(size - i).getFieldValue("series").toString().equals("3")){
                try{
                Item item = new Item();
                int d = 0;
                Float median = new Float(0.0);
                ArrayList<Float> medians = new ArrayList<>();
//                while (d < ((int) Math.ceil(size / 600.0))) {
//                   medians.add(Float.valueOf(results.get(size - i - d).getFieldValue("high").toString()));
//                   d++;
//                }
//                median = median(medians);
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
//                item.setVolume(results.get(i).getFieldValue("volume").toString());
                    item.setSeries(results.get(size - i).getFieldValue("series").toString());
                    item.setLow(results.get(i).getFieldValue("low").toString());
                   item.setSpike("normal");

                    list.add(item);
                }catch (Exception e){}
            }else if(results.get(size - i).getFieldValue("spike").equals("spike")){
                Item item = new Item();
                x++;
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
//                item.setVolume(results.get(i).getFieldValue("volume").toString());
                item.setSeries(results.get(size - i).getFieldValue("series").toString());
                item.setLow(results.get(i).getFieldValue("low").toString());
//            item.setOpen(results.get(s).getFieldValue("open").toString());
                item.setSpike("spike");
                list.add(item);
            }else if (datesUsed.contains(results.get(size - i).getFieldValue("date").toString())){
                try{
                    Item item = new Item();
                    int d = 0;
                    Float median = new Float(0.0);
                    ArrayList<Float> medians = new ArrayList<>();
//                while (d < ((int) Math.ceil(size / 600.0))) {
//                   medians.add(Float.valueOf(results.get(size - i - d).getFieldValue("high").toString()));
//                   d++;
//                }
//                median = median(medians);
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
//                item.setVolume(results.get(i).getFieldValue("volume").toString());
                    item.setSeries(results.get(size - i).getFieldValue("series").toString());
                    item.setLow(results.get(i).getFieldValue("low").toString());
                    item.setSpike("normal");

                    list.add(item);
                }catch (Exception e){}
            }
        }

        return list;
    }

    public static float median(ArrayList<Float> m) {
        int middle = m.size()/2;
        if (m.size()%2 == 1) {
            return m.get(middle);
        } else {
            return (m.get(middle - 1) + m.get(middle)) / 2;
        }
    }

    public static class RecordItem {
        private static final String ID = "id";
        private static final String DATE = "date";
        private static final String OPEN = "open";
        private static final String HIGH = "high";
        private static final String LOW = "low";
        private static final String CLOSE = "close";
        private static final String VOLUME = "volume";
        private static final String ADJ_CLOSE = "adj_close";
        private static final String SPIKE = "spike";
        private static final String SERIES = "series";

        @Field(ID)
        private int id;

        @Field(DATE)
        private String date;

        @Field(OPEN)
        private float open;

        @Field(HIGH)
        private float high;

        @Field(LOW)
        private float low;

        @Field(CLOSE)
        private float close;

        @Field(VOLUME)
        private int volume;

        @Field(ADJ_CLOSE)
        private float adj_close;

        @Field(SPIKE)
        private String spike;

        @Field(SERIES)
        private int series;

        public RecordItem(/*int id, SimpleDateFormat date, float open, float high, float low, float close, int volume, float adj_close*/) {
            super();
            this.id = id;
            this.date = date;
            this.open = open;
            this.high = high;
            this.low = low;
            this.close = close;
            this.volume = volume;
            this.adj_close = adj_close;

        }

        public void setId(int id) {
            this.id = id;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public void setOpen(float open) {
            this.open = open;
        }

        public void setHigh(float high) {
            this.high = high;
        }

        public void setLow(float low) {
            this.low = low;
        }

        public void setClose(float close) {
            this.close = close;
        }

        public void setVolume(int volume) {
            this.volume = volume;
        }

        public void setSeries(int series) {this.series = series;}

        public void setAdj_close(float adj_close) {
            this.adj_close = adj_close;
        }
        public void setSpike(String spike) {
            this.spike = spike;
        }

        public int getId() {
            return id;
        }

        public String getDate() {
            return date;
        }

        public float getOpen() {
            return open;
        }

        public float getHigh() {
            return high;
        }

        public float getLow() {
            return low;
        }

        public float getClose() {
            return close;
        }

        public int getVolume() {
            return volume;
        }

        public float getAdj_close() {
            return adj_close;
        }

        public String getSpike(){
            return spike;
        }

        public int getSeries() {return series;}

    }
    public static String toUtcDate(String dateStr) {
        HashMap<String, String> monthToMonth = new HashMap<String, String>();
        monthToMonth.put("Jan", "01");
        monthToMonth.put("Feb", "02");
        monthToMonth.put("Mar", "03");
        monthToMonth.put("Apr", "04");
        monthToMonth.put("May", "05");
        monthToMonth.put("Jun", "06");
        monthToMonth.put("Jul", "07");
        monthToMonth.put("Aug", "08");
        monthToMonth.put("Sep", "09");
        monthToMonth.put("Oct", "10");
        monthToMonth.put("Nov", "11");
        monthToMonth.put("Dec", "12");
        String month = monthToMonth.get(dateStr.substring(4, 7));
        String year = dateStr.substring(24);
        String day = dateStr.substring(8, 10);
        dateStr = year + "-" + month + "-" + day;
        SimpleDateFormat out = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        String[] dateFormats = {"yyyy-MM-dd", "MMM dd, yyyy hh:mm:ss Z"};
        for (String dateFormat : dateFormats) {
            try {
                return out.format(new SimpleDateFormat(dateFormat).parse(dateStr));
            } catch (ParseException ignore) { }
        }
        throw new IllegalArgumentException("Invalid date: " + dateStr);
    }
}