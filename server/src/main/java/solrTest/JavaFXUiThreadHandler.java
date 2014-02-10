package solrTest;

/**
 * Created by vladislav on 10.02.14.
 */
import javafx.application.Platform;
import org.opendolphin.core.client.comm.UiThreadHandler;

public class JavaFXUiThreadHandler implements UiThreadHandler {
    @Override
    public void executeInsideUiThread(Runnable runnable) {
        Platform.runLater(runnable);
    }
}