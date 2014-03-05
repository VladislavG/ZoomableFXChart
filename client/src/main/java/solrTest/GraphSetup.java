package solrTest;

import com.sun.javafx.charts.Legend;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;

/**
 * Created by vladislav on 19.02.14.
 */
public class GraphSetup {
    static void setupCharts(Main main) {
        ((DateAxis310) main.lineChart.getXAxis()).setTickLabelsVisible(true);
        ((DateAxis310) main.lineChart.getXAxis()).setTickMarkVisible(false);
        main.lineChart.setLegendVisible(true);
        main.lineChart.setAnimated(false);
        main.lineChart.setHorizontalGridLinesVisible(true);
        main.lineChart.getData().add(main.seriesHighRawProviderTwo);
        main.lineChart.getData().add(main.seriesHighRawProviderThree);
        main.lineChart.getData().add(main.seriesHighRawProviderFour);
        main.lineChart.getData().add(main.seriesHighRaw);
        main.diffBarChart.getData().addAll(main.seriesDiffBar);
        main.lineChartNASDAQ.getData().add(main.seriesHighRawNASDAQ);

        main.lineChart.setMaxWidth(1390);
        main.lineChart.setMinWidth(1390);
        main.lineChart.setMaxHeight(550);
        main.lineChart.setMinHeight(550);
        main.diffBarChart.setMaxWidth(1378);
        main.diffBarChart.setAnimated(false);
        main.lineChart.getCreateSymbols();
        main.lineChart.setVerticalZeroLineVisible(false);
        main.lineChart.setVerticalGridLinesVisible(false);
        main.lineChart.setHorizontalZeroLineVisible(false);
        main.lineChart.setVerticalZeroLineVisible(false);
        main.lineChart.setHorizontalZeroLineVisible(false);
        main.lineChart.setAnimated(false);
        main.lineChart.setTitle("Price of gold");
        main.lineChart.setCreateSymbols(true);
        main.lineChart.setVerticalZeroLineVisible(false);

        main.lineChartOverview.setCreateSymbols(true);
        main.lineChartOverview.legendVisibleProperty().setValue(false);
        main.lineChartOverview.getData().add(main.seriesTotal);
        main.lineChartOverview.setVerticalGridLinesVisible(false);
        main.lineChartOverview.setHorizontalZeroLineVisible(false);
        ((DateAxis310) main.lineChartOverview.getXAxis()).setTickLabelsVisible(false);
        ((DateAxis310) main.lineChartOverview.getXAxis()).setTickMarkVisible(false);
        main.lineChartOverview.setMaxHeight(140);
        main.lineChartOverview.setMinWidth(1390);
        main.lineChartOverview.setMaxWidth(1390);

        main.diffBarChart.setVerticalGridLinesVisible(false);
        main.diffBarChart.setHorizontalGridLinesVisible(false);
        main.diffBarChart.getXAxis().setTickMarkVisible(false);
        main.diffBarChart.getXAxis().setTickLabelsVisible(false);
        main.diffBarChart.setTranslateX(12);
        main.diffBarChart.setMaxHeight(150);

        main.xNAxis.lowerBoundProperty().bind(main.xAxis.lowerBoundProperty());
        main.xNAxis.upperBoundProperty().bind(main.xAxis.upperBoundProperty());

        main.optionsLabel.setText("Options");
        main.lineChartPane.getChildren().addAll(main.lineChartNASDAQ, main.lineChart, main.lineIndicator);
        main.lineChartNASDAQ.setMinWidth(1390);
        main.lineChartNASDAQ.setMaxWidth(1390);
        main.lineChartNASDAQ.setMinHeight(537);
        main.lineChartNASDAQ.setMaxHeight(537);
        main.lineChartNASDAQ.setFocusTraversable(true);
        Rectangle rectangle = new Rectangle(0,0, 1375, 550);
        main.lineChart.setClip(rectangle);
        main.lineChart.setVerticalGridLinesVisible(false);
        main.lineChartNASDAQ.setVerticalGridLinesVisible(false);
        main.lineChartNASDAQ.setHorizontalGridLinesVisible(false);
        main.lineChart.getStyleClass().add("transparent");
        main.lineChartNASDAQ.setCreateSymbols(false);
//        main.lineChartNASDAQ.setLegendVisible(false);
        for (Node legend : main.lineChartNASDAQ.getChildrenUnmodifiable()){
            if (legend instanceof Legend){
                legend.setTranslateX(200);
                legend.setTranslateY(12);
            }
        }
        main.lineChartOverview.getStyleClass().addAll("total");
        main.lineChartNASDAQ.setTranslateX(42);
    }
}
