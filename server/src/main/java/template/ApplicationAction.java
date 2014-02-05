package template;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.beans.Field;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.core.CoreContainer;
import org.opendolphin.core.comm.Command;
import org.opendolphin.core.server.action.DolphinServerAction;
import org.opendolphin.core.server.comm.ActionRegistry;
import org.opendolphin.core.server.comm.CommandHandler;
import solrTest.Item;
import solrTest.SolrService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

            }
        });
    }
}