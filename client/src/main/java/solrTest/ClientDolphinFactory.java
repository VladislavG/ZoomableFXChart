package solrTest;

import org.opendolphin.core.client.ClientDolphin;
import org.opendolphin.core.client.ClientModelStore;
import org.opendolphin.core.client.comm.ClientConnector;
import org.opendolphin.core.client.comm.HttpClientConnector;
import org.opendolphin.core.client.comm.InMemoryClientConnector;
import org.opendolphin.core.client.comm.UiThreadHandler;
import org.opendolphin.core.comm.JsonCodec;
/**
 * Factory to get ClientDolphin instance.
 * Setup require to connect Dolphin server.
 *
 */
public class ClientDolphinFactory {
    private  ClientDolphin clientDolphin;
    private  UiThreadHandler threadHandler;

    public ClientDolphinFactory(UiThreadHandler uiThreadHandler) {
        threadHandler = uiThreadHandler;
    }

    public ClientDolphin getClientDolphin(String url) {
        if(threadHandler == null){
            System.err.println("UiThreadHandler is not set..");
            return null;
        }
        if (clientDolphin == null) {
            clientDolphin = new ClientDolphin();
            clientDolphin.setClientModelStore(new ClientModelStore(clientDolphin));
            ClientConnector connector = new InMemoryClientConnector(clientDolphin);
            connector.setUiThreadHandler(threadHandler);
            connector.setCodec(new JsonCodec());
            clientDolphin.setClientConnector(connector);
        }

        return clientDolphin;
    }

}
