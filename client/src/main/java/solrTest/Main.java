package solrTest;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;
import javafx.beans.property.SimpleDoubleProperty;
import org.opendolphin.core.client.ClientAttribute;
import org.opendolphin.core.client.ClientDolphin;
import org.opendolphin.core.client.ClientPresentationModel;

import org.opendolphin.core.client.comm.OnFinishedHandler;
import org.opendolphin.core.comm.DefaultInMemoryConfig;

import static java.time.temporal.ChronoUnit.*;
import static solrTest.ApplicationConstants.*;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main extends Application {
    static ClientDolphin clientDolphin;
    XYChart.Series<LocalDateTime, Number> seriesHighRaw = new XYChart.Series<>();
    XYChart.Series<LocalDateTime, Number> seriesHighRawProviderTwo = new XYChart.Series<>();
    XYChart.Series<LocalDateTime, Number> seriesHighRawProviderThree = new XYChart.Series<>();
    XYChart.Series<LocalDateTime, Number> seriesHighRawProviderFour = new XYChart.Series<>();
    XYChart.Series<LocalDateTime, Number> seriesHighRawNASDAQ = new XYChart.Series<>();
    XYChart.Series<LocalDateTime, Number> seriesDiffBar = new XYChart.Series<>();
    XYChart.Series<LocalDateTime, Number> seriesTotal = new XYChart.Series<>();
    XYChart.Series<LocalDateTime, Number> seriesAverageHigh = new XYChart.Series<>();
    XYChart.Series<LocalDateTime, Number> seriesAverageLow = new XYChart.Series<>();
    XYChart.Series<LocalDateTime, Number> seriesOpenRaw = new XYChart.Series<>();
    Double initYpos;
    String dragArea;
    ProgressBar progressBar;
    SimpleDoubleProperty progress;
    Text placeHolder;
    Label optionsLabel;
    Label fileLabel;
    Timeline progressLine;
    Button showHideButton = new Button("Hide providers");
    Button newProviderButton = new Button("Add provider");
    Button newComparatorButton = new Button("Add comparator");
    Button blinkButton = new Button("Blink providers");
    Button compareButton = new Button("Hide comparator");
    Label chartLowerBound = new Label();
    Label chartUpperBound = new Label();
    int markCount = 0;
    SplitPane splitPane;
    Pane simplePane;
    VBox chartBox;
    List<XYChart.Series> seriesList;
    Pane miniMapPane;
    double positionXOverviewChart;
    NumberAxis yLineAxis;
    DateAxis310 xCLineAxis;
    ListView<LocalDateTime> listOfMarks;
    List<Double> listOfPrices = new ArrayList<>();
    List<Item> listOfItems = new ArrayList<>();
    List<Double> uniquePositions;
    Pane optionsPane;
    Pane filePane;
    int n;
    DateAxis310 xNAxis;
    NumberAxis yNAxis;

    final StringConverter<LocalDateTime> STRING_CONVERTER = new StringConverter<LocalDateTime>() {
        @Override
        public String toString(LocalDateTime localDateTime) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            return dtf.format(localDateTime);
        }

        @Override
        public LocalDateTime fromString(String s) {
            return LocalDateTime.parse(s);
        }
    };
    String lastEarliestValue;
    String lastLatestValue;
    Button showHideThresh = new Button("Show bounds");
    CheckBox lockCB;
    Rectangle zoomBounds;
    Line trackX = new Line(0, 490, 0, 0);
    Label displayAtPosition = new Label();
    Label displayAtTarget = new Label();
    Label spikes;
    VBox listBox;
    SimpleDoubleProperty leftHookPosition = new SimpleDoubleProperty();
    SimpleDoubleProperty rightHookPosition = new SimpleDoubleProperty();
    SimpleDoubleProperty trackXPosition = new SimpleDoubleProperty();
    SimpleDoubleProperty trackXTargetPosition = new SimpleDoubleProperty();
    SimpleDoubleProperty trackYPosition = new SimpleDoubleProperty();

    boolean onOff = true;
    boolean startStop = true;
    boolean onOffThresh = false;
    Timer t;

    SimpleDoubleProperty rectinitX = new SimpleDoubleProperty();
    SimpleDoubleProperty rectinitY = new SimpleDoubleProperty();
    SimpleDoubleProperty rectX = new SimpleDoubleProperty();
    SimpleDoubleProperty rectY = new SimpleDoubleProperty();

    LineChart<LocalDateTime, Number> lineChartOverview;
    LineChart<LocalDateTime, Number> lineChart;
    LineChart<LocalDateTime, Number> lineChartNASDAQ;

    ArrayList<XYChart.Data<LocalDateTime, Number>> dataRemovedFromFront;
    ArrayList<XYChart.Data<LocalDateTime, Number>> dataRemovedFromBack;

    ArrayList<XYChart.Data<LocalDateTime, Number>> dataRemovedFromFrontProv2;
    ArrayList<XYChart.Data<LocalDateTime, Number>> dataRemovedFromBackProv2;

    ArrayList<XYChart.Data<LocalDateTime, Number>> dataRemovedFromFrontProv3;
    ArrayList<XYChart.Data<LocalDateTime, Number>> dataRemovedFromBackProv3;

    ArrayList<XYChart.Data<LocalDateTime, Number>> dataRemovedFromFrontProv4;
    ArrayList<XYChart.Data<LocalDateTime, Number>> dataRemovedFromBackProv4;

    ArrayList<XYChart.Data<LocalDateTime, Number>> dataRemovedFromFrontHighBound;
    ArrayList<XYChart.Data<LocalDateTime, Number>> dataRemovedFromBackHighBound;

    ArrayList<XYChart.Data<LocalDateTime, Number>> dataRemovedFromFrontLowBound;
    ArrayList<XYChart.Data<LocalDateTime, Number>> dataRemovedFromBackLowBound;

    ArrayList<XYChart.Data<LocalDateTime, Number>> dataRemovedFromFrontNASDAQ;
    ArrayList<XYChart.Data<LocalDateTime, Number>> dataRemovedFromBackNASDAQ;

    ArrayList<XYChart.Data<LocalDateTime, Number>> dataRemovedFromFrontDelta;
    ArrayList<XYChart.Data<LocalDateTime, Number>> dataRemovedFromBackDelta;

    double initialLeftHookPosition = 0.0;
    double initialRightHookPosition = 0.0;
    Line lineIndicator = new Line(0, 490, 0, 0);
    Rectangle hookRight = new Rectangle(15, 40);
    Rectangle hookLeft = new Rectangle(15, 40);
    Rectangle leftRect = new Rectangle(30, 150);
    Rectangle rightRect = new Rectangle(30, 150);
    Separator separator;
    Separator separator2 = new Separator();
    VBox buttonControls;
    VBox fileControls;
    FileChooser fileChooser;

    ArrayList<String> tickValues;
    ArrayList<String> valuesHighRaw;
    ArrayList<Double> changeOfvalues;

    SimpleDoubleProperty width;
    Delta zoomFactor;
    ArrayList<LocalDateTime> aboveAverages;
    ArrayList<LocalDateTime> providerDiff;
    ArrayList<LocalDateTime> providerDifferences;
    ObservableList<LocalDateTime> observableAboveAverages;


    Pane lineChartPane;
    NumberAxis yAxis;
    DateAxis310 xAxis;

    List<StackPane> stackPanes = new ArrayList<>();

    SimpleDoubleProperty upperBoundForY;
    SimpleDoubleProperty lowerBoundForY;

    SimpleDoubleProperty upperBoundForYMain;
    SimpleDoubleProperty lowerBoundForYMain;

    AreaChart<LocalDateTime, Number> diffBarChart;

    Text miniMapDetail;
    Text detail;
    Pane containingPane;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
    }

    @Override
    public void stop() throws Exception {
    }


    private static void initializePresentationModels() {
        clientDolphin.presentationModel(STATE,new ClientAttribute(STARTDATE),new ClientAttribute(ENDDATE), new ClientAttribute(DISABLED), new ClientAttribute(NEWFILE));

        clientDolphin.getClientModelStore().findPresentationModelById(STATE).findAttributeByPropertyName(STARTDATE).setValue("*");
        clientDolphin.getClientModelStore().findPresentationModelById(STATE).findAttributeByPropertyName(ENDDATE).setValue("*");
        clientDolphin.getClientModelStore().findPresentationModelById(STATE).findAttributeByPropertyName(DISABLED).setValue(false);
        clientDolphin.getClientModelStore().findPresentationModelById(STATE).findAttributeByPropertyName(NEWFILE).setValue("");

    }

    @Override
    public void start(Stage stage) throws Exception {
        final Group root = new Group();
        Scene scene = new Scene(root, 1750, 850, Color.WHITESMOKE);
        initComponents();
        initializePresentationModels();

        clientDolphin.findPresentationModelById(STATE).findAttributeByPropertyName(DISABLED).addPropertyChangeListener((evt) -> {
                splitPane.setDisable((Boolean) evt.getNewValue());
                if ((Boolean) evt.getNewValue()) {
                    progressBar.setVisible(true);
                    progress.set(0.0);
                    progressLine.setRate(1);
                    progressLine.playFromStart();
                }else {
                    progressLine.setRate(7);
                }
        });
        clientDolphin.getClientModelStore().findPresentationModelById(STATE).findAttributeByPropertyName(DISABLED).setValue(true);

        clientDolphin.send("Query", new OnFinishedHandler() {
            @Override
            public void onFinished(List<ClientPresentationModel> presentationModels) {
            }

            @Override
            public void onFinishedData(List<Map> data) {
                List<Item> dataList = new ArrayList<>();
                int f = 0;
                for (Map map : data) {
                    dataList.add((Item) map.get(f));
                    f++;
                }
                int size = dataList.size() - 1;

                for (int c = size; c >= 0; c--) {

                    Item item = dataList.get(size - c);

//                    clientDolphin.presentationModel(item.getId(),
//                            DATEENTRY                  ,
//                            new ClientAttribute(PRICE) ,
//                            new ClientAttribute(SERIES),
//                            new ClientAttribute(HIGH)  ,
//                            new ClientAttribute(LOW)   ,
//                            new ClientAttribute(SERIES),
//                            new ClientAttribute(SPIKE) ,
//                            new ClientAttribute(DATE)
//                    );
//
//                    clientDolphin.getClientModelStore().findPresentationModelById(item.getId()).findAttributeByPropertyName(PRICE).setValue(item.getHigh());
//                    clientDolphin.getClientModelStore().findPresentationModelById(item.getId()).findAttributeByPropertyName(HIGH).setValue(item.getOpen());
//                    clientDolphin.getClientModelStore().findPresentationModelById(item.getId()).findAttributeByPropertyName(LOW).setValue(item.getClose());
//                    clientDolphin.getClientModelStore().findPresentationModelById(item.getId()).findAttributeByPropertyName(DATE).setValue(item.getDate());
//                    clientDolphin.getClientModelStore().findPresentationModelById(item.getId()).findAttributeByPropertyName(SPIKE).setValue(item.getSpike());
//                    clientDolphin.getClientModelStore().findPresentationModelById(item.getId()).findAttributeByPropertyName(SERIES).setValue(item.getSeries());
                    if (item.getSpike().equals("spike") && item.getSeries().equals("0")) {
                        aboveAverages.add(LocalDateTime.parse(GraphActions.toUtcDate(item.getDate()).substring(0, 19)));
                        observableAboveAverages.add(LocalDateTime.parse(GraphActions.toUtcDate(item.getDate()).substring(0, 19)));
                        markCount++;
                    }
                    if (item.getSpike().equals("diffSpike")) {
                        providerDifferences.add(LocalDateTime.parse(GraphActions.toUtcDate(item.getDate()).substring(0, 19)));
                    }
                    try {
                        switch (item.getSeries()) {
                            case ("0"): {
                                seriesHighRaw.getData().add(new XYChart.Data<>(LocalDateTime.parse(GraphActions.toUtcDate(item.getDate()).substring(0, 19)), Float.valueOf(item.getHigh())));
                                seriesAverageLow.getData().add(new XYChart.Data<>(LocalDateTime.parse(GraphActions.toUtcDate(item.getDate()).substring(0, 19)), Float.valueOf(item.getClose())));
                                seriesAverageHigh.getData().add(new XYChart.Data<>(LocalDateTime.parse(GraphActions.toUtcDate(item.getDate()).substring(0, 19)), Float.valueOf(item.getOpen())));
                                seriesTotal.getData().add(new XYChart.Data<>(LocalDateTime.parse(GraphActions.toUtcDate(item.getDate()).substring(0, 19)), Float.valueOf(item.getHigh())));
                                listOfItems.add(item);

                                valuesHighRaw.add(item.getDate());

                                break;
                            }
                            case ("1"): {
                                seriesHighRawProviderTwo.getData().add(new XYChart.Data<>(LocalDateTime.parse(GraphActions.toUtcDate(item.getDate()).substring(0, 19)), Float.valueOf(item.getHigh())));
                                listOfItems.add(item);

                                break;
                            }
                            case ("2"): {
                                seriesHighRawProviderThree.getData().add(new XYChart.Data<>(LocalDateTime.parse(GraphActions.toUtcDate(item.getDate()).substring(0, 19)), Float.valueOf(item.getHigh())));
                                listOfItems.add(item);

                                break;
                            }
                            case ("3"): {
                                seriesHighRawProviderFour.getData().add(new XYChart.Data<>(LocalDateTime.parse(GraphActions.toUtcDate(item.getDate()).substring(0, 19)), Float.valueOf(item.getHigh())));
                                listOfItems.add(item);

                                break;
                            }
                            case ("4"): {
                                seriesHighRawNASDAQ.getData().add(new XYChart.Data<>(LocalDateTime.parse(GraphActions.toUtcDate(item.getDate()).substring(0, 19)), Float.valueOf(item.getHigh())));
                                break;
                            }

                        }
                    } catch (Exception e) {
                        System.out.println("exception at " + GraphActions.toUtcDate(item.getDate()));
                        System.out.println(e);
                    }
                }
                System.out.println(seriesHighRawNASDAQ.getData().size() + " NASDAQ size");
                System.out.println(seriesHighRaw.getData().size() + " prov1 size");
                System.out.println(seriesAverageHigh.getData().size() + " high size");
                System.out.println(seriesAverageLow.getData().size() + " low size");
                System.out.println(seriesHighRawProviderTwo.getData().size() + " prov2 size");
                System.out.println(seriesHighRawProviderThree.getData().size() + " prov3 size");
                System.out.println(seriesHighRawProviderFour.getData().size() + " prov4 size");
                listOfMarks.getItems().setAll(observableAboveAverages);



                for (n = 0; n < valuesHighRaw.size() - 1; n++) {
                    listOfPrices.clear();
                    listOfItems.forEach((item) -> {
                            if (item.getDate().equals(valuesHighRaw.get(n)))
                                listOfPrices.add(Double.valueOf(item.getHigh()));

                    });

                    Double maxDiff, maxNumber = 0.0, minNumber = Double.MAX_VALUE;
                    for (Double listOfPrice : listOfPrices) {
                        minNumber = Math.min(minNumber, listOfPrice);
                        maxNumber = Math.max(maxNumber, listOfPrice);
                    }
                    maxDiff = maxNumber - minNumber;
                    seriesDiffBar.getData().add(new XYChart.Data<>(LocalDateTime.parse(GraphActions.toUtcDate(valuesHighRaw.get(n)).substring(0, 19)), Float.valueOf(maxDiff.toString())));
                    if (!(maxDiff==0)){
                        seriesList.forEach((series) -> {
                            ((XYChart.Series)series).getData().forEach((dataItem) -> {
                                if (((XYChart.Data)dataItem).getXValue().equals(LocalDateTime.parse(GraphActions.toUtcDate(valuesHighRaw.get(n)).substring(0, 19)))){
                                    System.out.println(((XYChart.Data)dataItem).getYValue() + " belonging to " + series.getName());
                                }
                            });
                        });
                        System.out.println(LocalDateTime.parse(GraphActions.toUtcDate(valuesHighRaw.get(n)).substring(0, 19)));
                    }
                }
            }
        });

        lineChartOverview = new LineChart<LocalDateTime, Number>(xAxis, yAxis) {
            @Override
            protected void layoutPlotChildren() {
                super.layoutPlotChildren();

                for (Node mark : getPlotChildren()) {
                    if (!(mark instanceof StackPane)) {
                        Path g = (Path) mark;
                        g.setStrokeWidth(1.5);
                    }
                    if (mark instanceof StackPane) {
                        mark.setScaleX(0.0);
                        mark.setScaleY(0.0);
                        mark.setManaged(false);
                        Bounds bounds = mark.getBoundsInParent();
                        double posX = bounds.getMinX() + (bounds.getMaxX() - bounds.getMinX()) / 2.0;
                        double posY = bounds.getMinY() + (bounds.getMaxY() - bounds.getMinY()) / 2.0;
                        Double number = Double.valueOf(getYAxis().getValueForDisplay(posY).toString());
                        LocalDateTime date = getXAxis().getValueForDisplay(posX).truncatedTo(DAYS);
                        if (aboveAverages.contains(date)) {
                            mark.setManaged(true);
                            mark.setScaleX(.5);
                            mark.setScaleY(.5);
                            ImageView flag = new ImageView("flag.png");
                            flag.setScaleX(.4);
                            flag.setScaleY(.4);
                            flag.setTranslateY(-20);
                            flag.setTranslateX(11);
//                                        ((StackPane) mark).getChildren().add(flag);
                            mark.setEffect(new InnerShadow(2, Color.BLACK));

                            mark.setOnMouseEntered((mouseEvent) -> {
                                    if (mark.getScaleX() == 0.5) {
                                        miniMapDetail.setText(String.valueOf((double) Math.round(number * 10) / 10));
                                        miniMapDetail.setTranslateX(mouseEvent.getSceneX() - 10);
                                        miniMapDetail.setTranslateY(mouseEvent.getSceneY() - 30);
                                        miniMapDetail.setVisible(true);
                                    }
                            });
                            mark.setOnMouseExited((mouseEvent) -> {
                                if (miniMapDetail.isVisible()) {
                                    miniMapDetail.setVisible(false);
                                }
                            });
                        }

                    }
                }
            }
        };

        List<Double> positions = new ArrayList<>();

        lineChart = new LineChart<LocalDateTime, Number>(xCLineAxis, yLineAxis) {
            @Override
            protected void layoutPlotChildren() {
                super.layoutPlotChildren();

                uniquePositions.clear();
                for (Node mark : getPlotChildren()) {
                    if (!(mark instanceof StackPane)) {
                        Path g = (Path) mark;
                        g.setStrokeWidth(1.5);
                    }
                    if (mark instanceof StackPane) {
                        mark.setScaleX(0.0);
                        mark.setScaleY(0.0);
                        mark.setManaged(false);
                        stackPanes.add((StackPane) mark);
                        Bounds bounds = mark.getBoundsInParent();
                        double posX = bounds.getMinX() + (bounds.getMaxX() - bounds.getMinX()) / 2.0;
                        double posY = bounds.getMinY() + (bounds.getMaxY() - bounds.getMinY()) / 2.0;
                        if (!positions.contains(posX)) {
                            positions.add(posX);
                            Double number = (Double) getYAxis().getValueForDisplay(posY);
                            LocalDateTime date = getXAxis().getValueForDisplay(posX).truncatedTo(DAYS);
                            if (aboveAverages.contains(date)) {
                                mark.setManaged(true);
                                mark.setScaleX(.5);
                                mark.setScaleY(.5);
                            }
                            else if (providerDifferences.contains(date)){
                                if (!uniquePositions.contains(posX)){
                                  uniquePositions.add(posX);
                                }
                            }

                            mark.setOnMouseEntered((mouseEvent) -> {
                                if (mark.getScaleX() == .5) {
                                    detail.setText(String.valueOf((double) Math.round(number * 10) / 10));
                                    detail.setTranslateX(mouseEvent.getSceneX() - 10);
                                    detail.setTranslateY(mouseEvent.getSceneY() - 30);
                                    detail.setVisible(true);
                                }
                            });
                            mark.setOnMouseExited((mouseEvent) -> {
                                if (detail.isVisible()) {
                                    detail.setVisible(false);
                                }
                            });
                        }
                    }
                }
            }
        };

        GraphSetup.setupCharts(this);
        addListeners();



        newProviderButton.setOnMouseClicked((mouseEvent) -> {
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                clientDolphin.findPresentationModelById(STATE).findAttributeByPropertyName(NEWFILE).setValue(file.getAbsolutePath());
            }
        });

        clientDolphin.findPresentationModelById(STATE).findAttributeByPropertyName(NEWFILE).addPropertyChangeListener((evt) -> {
//                clientDolphin.send("Index");
        });

        listOfMarks.getItems().addAll(observableAboveAverages);

        miniMapPane.getChildren().addAll(lineChartOverview, leftRect, rightRect, hookRight, hookLeft, miniMapDetail);
        chartBox.getChildren().addAll(lineChartPane, separator, diffBarChart, separator2, miniMapPane);
        containingPane.getChildren().addAll(chartBox, zoomBounds, trackX, displayAtPosition, displayAtTarget, detail, optionsPane, progressBar, lineIndicator);
        simplePane.getChildren().addAll(listBox);
        optionsPane.getChildren().add(buttonControls);
        optionsPane.getStyleClass().add("b2");
        optionsPane.setLayoutY(-116);

        filePane.getChildren().add(fileControls);
        filePane.getStyleClass().add("b2");
        filePane.setLayoutY(-63);

        buttonControls.setSpacing(5);
        fileControls.setSpacing(5);
        splitPane.getItems().addAll(containingPane, simplePane);
        splitPane.setMinWidth(scene.getWidth());

        root.getChildren().addAll(splitPane);

        stage.setTitle("Calculating Important Points");
        stage.setScene(scene);
        scene.getStylesheets().add("demo.css");

        stage.show();
    }

    private void refreshGraphFromSolr() {
        XYChart.Data<LocalDateTime, Number> stringFloatData = seriesHighRaw.getData().get(seriesHighRaw.getData().size() - 1);
        XYChart.Data<LocalDateTime, Number> stringFloatDatafirst = seriesHighRaw.getData().get(0);
        String endDate = (String.valueOf(stringFloatData.getXValue())).concat(":00Z");
        String startDate = (String.valueOf(stringFloatDatafirst.getXValue())).concat(":00Z");
        clientDolphin.findPresentationModelById(STATE).findAttributeByPropertyName(STARTDATE).setValue(startDate);
        clientDolphin.findPresentationModelById(STATE).findAttributeByPropertyName(ENDDATE).setValue(endDate);
        clientDolphin.getClientModelStore().findPresentationModelById(STATE).findAttributeByPropertyName(DISABLED).setValue(true);

        clientDolphin.send("Query", new OnFinishedHandler() {
            @Override
            public void onFinished(List<ClientPresentationModel> presentationModels) {
            }

            @Override
            public void onFinishedData(List<Map> data) {
                queryServer(data);
            }
        });
        listOfMarks.getItems().clear();
        listOfMarks.getItems().addAll(observableAboveAverages);
    }

    EventHandler<MouseEvent> mouseHandler = (mouseEvent) -> {
        if (mouseEvent.getSceneX() > 1390 || mouseEvent.getSceneX() < 55) {
            trackX.setVisible(false);
            displayAtPosition.setVisible(false);
            return;
        }
        if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
            zoomBounds.setX(mouseEvent.getX());
            zoomBounds.setY(-20);
            rectinitX.set(mouseEvent.getX());
            rectinitY.set(0);
        } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED) {
            if (mouseEvent.getSceneX() > 1390 || mouseEvent.getSceneX() < 55) return;
            trackXTargetPosition.set(mouseEvent.getSceneX());
            rectX.set(mouseEvent.getX());
            rectY.set(lineChart.getHeight() - 40);
            displayAtTarget.setVisible(true);
            lineIndicator.setVisible(false);
        } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED) {
            displayAtTarget.setVisible(false);
            displayAtPosition.setVisible(false);
            if (rectX.getValue() - rectinitX.getValue() < 20) {
                rectX.set(0);
                rectY.set(0);
                return;
            }
            String valueForDisplayStart = "";
            String valueForDisplayEnd = "";
            try {
                valueForDisplayStart = String.valueOf(lineChart.getXAxis().getValueForDisplay(rectinitX.getValue() - 60));
                valueForDisplayEnd = String.valueOf(lineChart.getXAxis().getValueForDisplay(rectX.getValue() - 60));

                String startDate = valueForDisplayStart.substring(0, 19).concat("Z");
                String endDate = valueForDisplayEnd.substring(0, 19).concat("Z");

                clientDolphin.findPresentationModelById(STATE).findAttributeByPropertyName(STARTDATE).setValue(startDate);
                clientDolphin.findPresentationModelById(STATE).findAttributeByPropertyName(ENDDATE).setValue(endDate);

                double left = lineChartOverview.getXAxis().getDisplayPosition(LocalDateTime.parse(valueForDisplayStart));
                double right = lineChartOverview.getXAxis().getDisplayPosition(LocalDateTime.parse(valueForDisplayEnd));

                leftHookPosition.set(left);
                rightHookPosition.set(right + 60);
                chartLowerBound.setText("Chart lower bound: " + lineChart.getXAxis().getValueForDisplay(rectinitX.getValue() - 55.0));
                chartUpperBound.setText("Chart upper bound: " + lineChart.getXAxis().getValueForDisplay(rectX.getValue() - 55.0));

                lastLatestValue = valueForDisplayEnd;
                lastEarliestValue = valueForDisplayStart;

                try {
                    GraphActions.removePointsAtFront(seriesHighRaw, dataRemovedFromFront, lastLatestValue);
                    GraphActions.removePointsAtFront(seriesHighRawProviderTwo, dataRemovedFromFrontProv2, lastLatestValue);
                    GraphActions.removePointsAtFront(seriesHighRawProviderThree, dataRemovedFromFrontProv3, lastLatestValue);
                    GraphActions.removePointsAtFront(seriesHighRawProviderFour, dataRemovedFromFrontProv4, lastLatestValue);
                    GraphActions.removePointsAtFront(seriesDiffBar, dataRemovedFromFrontDelta, lastLatestValue);
                    GraphActions.removePointsAtFront(seriesHighRawNASDAQ, dataRemovedFromFrontNASDAQ, lastLatestValue);

                    GraphActions.removePointsAtBack(seriesHighRaw, dataRemovedFromBack, lastEarliestValue);
                    GraphActions.removePointsAtBack(seriesHighRawProviderTwo, dataRemovedFromBackProv2, lastEarliestValue);
                    GraphActions.removePointsAtBack(seriesHighRawProviderThree, dataRemovedFromBackProv3, lastEarliestValue);
                    GraphActions.removePointsAtBack(seriesHighRawProviderFour, dataRemovedFromBackProv4, lastEarliestValue);
                    GraphActions.removePointsAtBack(seriesDiffBar, dataRemovedFromBackDelta, lastEarliestValue);
                    GraphActions.removePointsAtBack(seriesHighRawNASDAQ, dataRemovedFromBackNASDAQ, lastEarliestValue);

                    GraphActions.removePointsAtBack(seriesAverageHigh, dataRemovedFromBackHighBound, lastEarliestValue);
                    GraphActions.removePointsAtBack(seriesAverageLow, dataRemovedFromBackLowBound, lastEarliestValue);
                    GraphActions.removePointsAtFront(seriesAverageHigh, dataRemovedFromFrontHighBound, lastLatestValue);
                    GraphActions.removePointsAtFront(seriesAverageLow, dataRemovedFromFrontLowBound, lastLatestValue);

                } catch (Exception e) {
                    System.out.println(e + " at " + this);
                    e.printStackTrace();
                }
                refreshXYBounds();

                clientDolphin.getClientModelStore().findPresentationModelById(STATE).findAttributeByPropertyName(DISABLED).setValue(true);

                clientDolphin.send("Query", new OnFinishedHandler() {
                    @Override
                    public void onFinished(List<ClientPresentationModel> presentationModels) {
                    }

                    @Override
                    public void onFinishedData(List<Map> datas) {
                        queryServer(datas);
                    }
                });
            }catch (Exception e) {
                System.out.println(e + " at " + this);
                e.printStackTrace();
            }

            listOfMarks.getItems().clear();
            listOfMarks.getItems().addAll(observableAboveAverages);

            if (seriesHighRaw.getData().size() == 0) {
                XYChart.Data<LocalDateTime, Number> frontData = dataRemovedFromFront.get(dataRemovedFromFront.size() - 1);
                dataRemovedFromFront.remove(frontData);
                chartUpperBound.setText("Chart upper bound: " + frontData.getXValue());
                Number valueFront = frontData.getYValue();
                seriesHighRaw.getData().add(new XYChart.Data<>(frontData.getXValue(), valueFront));

                XYChart.Data<LocalDateTime, Number> backData = dataRemovedFromBack.get(dataRemovedFromBack.size() - 1);
                dataRemovedFromBack.remove(backData);
                chartLowerBound.setText("Chart lower bound: " + backData.getXValue());
                Number valueBack = backData.getYValue();

                seriesHighRaw.getData().add(0, new XYChart.Data<>(backData.getXValue(), valueBack));
            }

            rectX.set(0);
            rectY.set(0);
        } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_MOVED && mouseEvent.getSceneX()>55) {
            trackX.setVisible(true);
            trackXPosition.set(mouseEvent.getSceneX());
            trackYPosition.set(mouseEvent.getSceneY());
        } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_EXITED || mouseEvent.getSceneX()<55) {
            trackX.setVisible(false);
            displayAtPosition.setVisible(false);
            displayAtTarget.setVisible(false);
        }
    };

    private void refreshXYBounds() {
        yNAxis.upperBoundProperty().unbind();
        yNAxis.lowerBoundProperty().unbind();
        yNAxis.setAutoRanging(true);

        yLineAxis.upperBoundProperty().unbind();
        yLineAxis.lowerBoundProperty().unbind();
        yLineAxis.setAutoRanging(true);
        XYChart.Data valueLower = (XYChart.Data) seriesHighRaw.getData().get(0);
        XYChart.Data valueUpper = (XYChart.Data) seriesHighRaw.getData().get(seriesHighRaw.getData().size() - 1);

        xNAxis.lowerBoundProperty().unbind();
        xNAxis.setLowerBound((LocalDateTime) valueLower.getXValue());

        xNAxis.upperBoundProperty().unbind();
        xNAxis.setUpperBound((LocalDateTime) valueUpper.getXValue());
    }

    private void queryServer(List<Map> datas) {
        seriesHighRaw.getData().clear();
        seriesHighRawProviderTwo.getData().clear();
        seriesHighRawProviderThree.getData().clear();
        seriesHighRawProviderFour.getData().clear();
        seriesHighRawNASDAQ.getData().clear();
        seriesAverageLow.getData().clear();
        seriesAverageHigh.getData().clear();

        seriesDiffBar.getData().clear();
        observableAboveAverages.clear();
        listOfItems.clear();
        valuesHighRaw.clear();
        List<Item> dataList = new ArrayList<>();
        int f = 0;
        for (Map map : datas) {
            dataList.add((Item) map.get(f));
            f++;
        }
        int size = dataList.size() - 1;
        markCount = 0;

        for (int c = size; c >= 0; c--) {
            Item item = dataList.get(size - c);
            LocalDateTime itemDate = LocalDateTime.parse(GraphActions.toUtcDate(item.getDate()).substring(0, 19));
            if (item.getSpike().equals("spike") && item.getSeries().equals("0")) {
                aboveAverages.add(itemDate);
                observableAboveAverages.add(itemDate);
                markCount++;
            }
            try {
                switch (item.getSeries()){
                    case ("0"):{
                        seriesHighRaw.getData().add(new XYChart.Data<>(LocalDateTime.parse(GraphActions.toUtcDate(item.getDate()).substring(0, 19)), Float.valueOf(item.getHigh())));
                        XYChart.Data lowData = new XYChart.Data(LocalDateTime.parse(GraphActions.toUtcDate(item.getDate()).substring(0, 19)), Float.valueOf(item.getClose()));
                        seriesAverageLow.getData().add(lowData);
                        XYChart.Data high = new XYChart.Data(LocalDateTime.parse(GraphActions.toUtcDate(item.getDate()).substring(0, 19)), Float.valueOf(item.getOpen()));
                        seriesAverageHigh.getData().add(high);
                        listOfItems.add(item);
                        valuesHighRaw.add(item.getDate());
                        break;
                    }case ("1"):{
                        seriesHighRawProviderTwo.getData().add(new XYChart.Data<>(LocalDateTime.parse(GraphActions.toUtcDate(item.getDate()).substring(0, 19)), Float.valueOf(item.getHigh())));
                        listOfItems.add(item);
                        break;
                    }case ("2"):{
                        seriesHighRawProviderThree.getData().add(new XYChart.Data<>(LocalDateTime.parse(GraphActions.toUtcDate(item.getDate()).substring(0, 19)), Float.valueOf(item.getHigh())));
                        listOfItems.add(item);
                        break;
                    }case ("3"):{
//                        seriesHighRawProviderFour.getData().add(new XYChart.Data<>(LocalDateTime.parse(GraphActions.toUtcDate(item.getDate()).substring(0, 19)), Float.valueOf(item.getHigh())));
                        listOfItems.add(item);
                        break;
                    }case ("4"):{
                        seriesHighRawNASDAQ.getData().add(new XYChart.Data<>(LocalDateTime.parse(GraphActions.toUtcDate(item.getDate()).substring(0, 19)), Float.valueOf(item.getHigh())));
                        break;
                    }
                }
            } catch (Exception e){
                System.out.println(e + " at " + this);
                e.printStackTrace();
            }
        }
        System.out.println(seriesAverageHigh.getData().size() + " high size");
        System.out.println(seriesAverageLow.getData().size() + " low size");
        XYChart.Data valueLower = (XYChart.Data) seriesHighRaw.getData().get(0);
        XYChart.Data valueUpper = (XYChart.Data) seriesHighRaw.getData().get(seriesHighRaw.getData().size() - 1);

        xNAxis.lowerBoundProperty().unbind();
        xNAxis.setLowerBound((LocalDateTime) valueLower.getXValue());

        xNAxis.upperBoundProperty().unbind();
        xNAxis.setUpperBound((LocalDateTime) valueUpper.getXValue());

        listOfMarks.getItems().clear();
        listOfMarks.getItems().addAll(observableAboveAverages);
        for (n = 0; n < valuesHighRaw.size()-1; n++){
            listOfPrices.clear();
            listOfItems.forEach((item) -> {
                if (item.getDate().equals(valuesHighRaw.get(n)))
                    listOfPrices.add(Double.valueOf(item.getHigh()));
            });
            Double maxDiff, maxNumber = 0.0, minNumber = Double.MAX_VALUE;
            for (Double listOfPrice : listOfPrices) {
                minNumber = Math.min(minNumber, listOfPrice);
                maxNumber = Math.max(maxNumber, listOfPrice);
            }
            maxDiff = maxNumber - minNumber;
            seriesDiffBar.getData().add(new XYChart.Data<>(LocalDateTime.parse(GraphActions.toUtcDate(valuesHighRaw.get(n)).substring(0, 19)), Float.valueOf(maxDiff.toString())));
        }
    }

    public void animatePane(Pane pane, Label name, Double initPos, Double openPos) {
        pane.setOnMouseEntered((mouseEvent) -> {
            final Timeline timeline = new Timeline();
            timeline.setCycleCount(1);
            timeline.setAutoReverse(false);
            final KeyValue kv2 = new KeyValue(pane.layoutYProperty(), openPos);
            final KeyFrame kf2 = new KeyFrame(Duration.millis(200), kv2);
            timeline.getKeyFrames().add(kf2);
            timeline.play();

            final Timeline timelineOpacity = new Timeline();
            timelineOpacity.setCycleCount(1);
            timelineOpacity.setAutoReverse(false);
            final KeyValue kv = new KeyValue(name.opacityProperty(), 0);
            final KeyFrame kf = new KeyFrame(Duration.millis(500), kv);
            timelineOpacity.getKeyFrames().add(kf);
            timelineOpacity.play();
        });

        pane.setOnMouseExited((mouseEvent) -> {
            final Timeline timeline = new Timeline();
            timeline.setCycleCount(1);
            timeline.setAutoReverse(false);
            final KeyValue kv2 = new KeyValue(pane.layoutYProperty(), initPos);
            final KeyFrame kf2 = new KeyFrame(Duration.millis(400), kv2);
            timeline.getKeyFrames().add(kf2);
            timeline.play();

            final Timeline timelineOpacity = new Timeline();
            timelineOpacity.setCycleCount(1);
            timelineOpacity.setAutoReverse(false);
            final KeyValue kv = new KeyValue(name.opacityProperty(), 1);
            final KeyFrame kf = new KeyFrame(Duration.millis(500), kv);
            timelineOpacity.getKeyFrames().add(kf);
            timelineOpacity.play();
        });
    }

    private void addListeners() {
        progressLine.setOnFinished((actionEvent) -> progressBar.setVisible(false));

       animatePane(optionsPane, optionsLabel, -116.0, -6.0);
       animatePane(filePane, fileLabel, -63.0, -4.0);

        hookLeft.setOnMousePressed((mouseEvent) -> {
            initialLeftHookPosition = mouseEvent.getSceneX();
        });


        hookRight.setOnMousePressed((mouseEvent) -> {
            initialRightHookPosition = mouseEvent.getSceneX();
        });

        trackXPosition.addListener(new Listeners(this, lineChart, displayAtPosition));

        trackXTargetPosition.addListener(new XTargetChangeListener());

        progressBar.progressProperty().bind(progress);

        hookLeft.setOnMouseDragged((mouseEvent) -> {
            if (leftRect.getWidth() >= (hookRight.getLayoutX() - 50) || mouseEvent.getSceneX() - 50 >= (hookRight.getLayoutX() - 50)) {
                leftHookPosition.set(hookRight.getLayoutX() - 60);
                return;
            }
            if (mouseEvent.getSceneX() < 55) {
                leftHookPosition.set(0);
                return;
            }
            double deltaLeft = initialLeftHookPosition - mouseEvent.getSceneX();
            leftHookPosition.set(hookLeft.getLayoutX() - deltaLeft);
            lastEarliestValue = String.valueOf(lineChartOverview.getXAxis().getValueForDisplay(initialLeftHookPosition - 45.0));
            System.out.println(lastEarliestValue + " lastEarliestValue");

            cutLeft(deltaLeft);
            initialLeftHookPosition = mouseEvent.getSceneX();
        });

        hookLeft.setOnMouseReleased((mouseEvent) -> {
            if (leftRect.getWidth() >= (hookRight.getLayoutX() - 50)) {
                leftHookPosition.set(hookRight.getLayoutX() - 60);
                return;
            }
            if (mouseEvent.getSceneX() < 55) {
                leftHookPosition.set(0);
                return;
            }
            refreshGraphFromSolr();
        });

        hookRight.setOnMouseDragged((mouseEvent) -> {
            System.out.println(rightRect.getLayoutX() + " rightRect layout");
            System.out.println(leftRect.getWidth() + " leftRect width");
            if (rightRect.getLayoutX() - 50 < leftRect.getWidth() || mouseEvent.getSceneX() - 55 < leftRect.getWidth()) {
                rightHookPosition.set(leftRect.getWidth() + 60);
                return;
            }
            if (mouseEvent.getSceneX() > 1375) {
                rightHookPosition.setValue(1375);
                return;
            }
            System.out.println(mouseEvent.getSceneX());
            double deltaRight = initialRightHookPosition - mouseEvent.getSceneX();
            rightHookPosition.set(hookRight.getLayoutX() - deltaRight/* + 75*/);

            lastLatestValue = String.valueOf(lineChartOverview.getXAxis().getValueForDisplay(initialRightHookPosition - 45.0));
            cutRight(deltaRight);

            initialRightHookPosition = mouseEvent.getSceneX();
        });

        hookRight.setOnMouseReleased((mouseEvent) -> {
            if (rightRect.getLayoutX() - 50 < leftRect.getWidth() || mouseEvent.getSceneX() - 55 < leftRect.getWidth()) {
                rightHookPosition.set(leftRect.getWidth() + 60);
                return;
            }
            if (mouseEvent.getSceneX() > 1375) {
                rightHookPosition.setValue(1375);
                return;
            }
            refreshGraphFromSolr();
//                updateIndicator();
        });
        listOfMarks.getItems().addListener((ListChangeListener<LocalDateTime>) (change) -> {
            while (change.next()) {
                if (listOfMarks.getItems().size() == 0) {
                    return;
                }
            }
        });

        listOfMarks.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            System.out.println(newValue);
            if (newValue == null){
                lineIndicator.setVisible(false);
            }else{
                lineIndicator.setVisible(true);
                lineIndicator.setLayoutX(lineChart.getXAxis().getDisplayPosition(newValue) + 50);
            }
        });

        compareButton.setOnMouseClicked((mouseEvent) -> {
            if (lineChartNASDAQ.isVisible()) {
                compareButton.setText("Show comparator");
                lineChartNASDAQ.setVisible(false);
            } else {
                compareButton.setText("Hide comparator");
                lineChartNASDAQ.setVisible(true);
            }
        });

        blinkButton.setOnMouseClicked((mouseEvent) -> {
            if (startStop){
                t = new Timer();
                t.schedule(
                        new TimerTask() {
                            @Override
                            public void run() {
                                Platform.runLater(() -> {
                                        if (onOff) {
                                            seriesHighRawProviderTwo.getNode().setVisible(false);
                                            seriesHighRawProviderThree.getNode().setVisible(false);
                                            seriesHighRawProviderFour.getNode().setVisible(false);

                                            seriesHighRawProviderTwo.getNode().setManaged(false);
                                            seriesHighRawProviderThree.getNode().setManaged(false);
                                            seriesHighRawProviderFour.getNode().setManaged(false);
                                            onOff = false;
                                        } else {
                                            seriesHighRawProviderTwo.getNode().setVisible(true);
                                            seriesHighRawProviderThree.getNode().setVisible(true);
                                            seriesHighRawProviderFour.getNode().setVisible(true);

                                            seriesHighRawProviderTwo.getNode().setManaged(true);
                                            seriesHighRawProviderThree.getNode().setManaged(true);
                                            seriesHighRawProviderFour.getNode().setManaged(true);
                                            onOff = true;
                                        }
                                });
                            }
                        }, 0, 500);
                startStop = false;

            }else{
                t.cancel();
                startStop = true;
            }
        });

        showHideButton.setOnMouseClicked((mouseEvent) -> {
            t.cancel();
            if (seriesHighRawProviderTwo.getNode().isVisible()){
                try{
                    showHideButton.setText("Show providers");
                    seriesHighRawProviderTwo.getNode().setVisible(false);
                    seriesHighRawProviderThree.getNode().setVisible(false);
                    seriesHighRawProviderFour.getNode().setVisible(false);

                    seriesHighRawProviderTwo.getNode().setManaged(false);
                    seriesHighRawProviderThree.getNode().setManaged(false);
                    seriesHighRawProviderFour.getNode().setManaged(false);

//                    lineChart.getData().remove(seriesHighRawProviderFour);
//                    lineChart.getData().remove(seriesHighRawProviderTwo);
//                    lineChart.getData().remove(seriesHighRawProviderThree);
                }catch (Exception e){
                    System.out.println(e + " at removing");
                    e.printStackTrace();
                }
            } else{
                try{
                    showHideButton.setText("Hide providers");
                    seriesHighRawProviderTwo.getNode().setVisible(true);
                    seriesHighRawProviderThree.getNode().setVisible(true);
                    seriesHighRawProviderFour.getNode().setVisible(true);

                    seriesHighRawProviderTwo.getNode().setManaged(true);
                    seriesHighRawProviderThree.getNode().setManaged(true);
                    seriesHighRawProviderFour.getNode().setManaged(true);

//                    lineChart.getData().remove(seriesHighRaw);
//                    lineChart.getData().add(seriesHighRawProviderFour);
//                    lineChart.getData().add(seriesHighRawProviderTwo);
//                    lineChart.getData().add(seriesHighRawProviderThree);
//                    lineChart.getData().add(seriesHighRaw);
                }catch (Exception e){
                    System.out.println(e + " at adding");
                    e.printStackTrace();
                }
            }
        });

        yNAxis.setOnMousePressed((mouseEvent) -> {
            yNAxis.setAutoRanging(false);
            lowerBoundForY.set(yNAxis.getLowerBound());
            upperBoundForY.set(yNAxis.getUpperBound());
            yNAxis.lowerBoundProperty().bind(lowerBoundForY);
            yNAxis.upperBoundProperty().bind(upperBoundForY);
            initYpos = mouseEvent.getSceneY();
            if (mouseEvent.getSceneY() > 326 ){
                dragArea="bottom";
            }else if (mouseEvent.getSceneY() > 163){
                dragArea="middle";
            }else{
                dragArea="top";
            }
        });

        yLineAxis.setOnMousePressed((mouseEvent) -> {
            yLineAxis.setAutoRanging(false);
            lowerBoundForYMain.set(yLineAxis.getLowerBound());
            upperBoundForYMain.set(yLineAxis.getUpperBound());
            yLineAxis.lowerBoundProperty().bind(lowerBoundForYMain);
            yLineAxis.upperBoundProperty().bind(upperBoundForYMain);
            initYpos = mouseEvent.getSceneY();
            if (mouseEvent.getSceneY() > 326 ){
                dragArea="bottom";
            }else if (mouseEvent.getSceneY() > 163){
                dragArea="middle";
            }else{
                dragArea="top";
            }
        });

        yNAxis.setOnMouseDragged((mouseEvent) -> {
            yNAxis.setAutoRanging(false);
            double lowerBound = yNAxis.getLowerBound();
            double upperBound = yNAxis.getUpperBound();

            double range = upperBound - lowerBound;
            double dragStrength = range / 425;
            switch (dragArea) {
                case "top":
                    upperBoundForY.set(upperBound - dragStrength * (initYpos - mouseEvent.getSceneY()));
                    break;
                case "middle":
                    upperBoundForY.set(upperBound - dragStrength * (initYpos - mouseEvent.getSceneY()));
                    lowerBoundForY.set(lowerBound - dragStrength * (initYpos - mouseEvent.getSceneY()));
                    break;
                case "bottom":
                    lowerBoundForY.set(lowerBound - dragStrength * (initYpos - mouseEvent.getSceneY()));
                    break;
            }
            initYpos = mouseEvent.getSceneY();
            mouseEvent.consume();
        });

        yLineAxis.setOnMouseDragged((mouseEvent) -> {
            yLineAxis.setAutoRanging(false);
            double lowerBound = yLineAxis.getLowerBound();
            double upperBound = yLineAxis.getUpperBound();

            double range = upperBound - lowerBound;
            double dragStrength = range / 425;
            switch (dragArea){
                case "top"    :
                    upperBoundForYMain.set(upperBound - dragStrength*(initYpos - mouseEvent.getSceneY()));
                    break;
                case "middle" :
                    upperBoundForYMain.set(upperBound - dragStrength*(initYpos - mouseEvent.getSceneY()));
                    lowerBoundForYMain.set(lowerBound - dragStrength*(initYpos - mouseEvent.getSceneY()));
                    break;
                case "bottom" :
                    lowerBoundForYMain.set(lowerBound - dragStrength*(initYpos - mouseEvent.getSceneY()));
                    break;
            }
            initYpos = mouseEvent.getSceneY();
            mouseEvent.consume();
        });

        lockCB.selectedProperty().addListener((observableValue, aBoolean, aBoolean2) -> {
            if (aBoolean){
                lineChart.getYAxis().setAutoRanging(true);
                lineChart.getXAxis().setAutoRanging(true);
            }else{
                lineChart.getYAxis().setAutoRanging(false);
                lineChart.getXAxis().setAutoRanging(false);
            }
        });

        showHideThresh.setOnMouseClicked((mouseEvent) -> {
            System.out.println(seriesAverageHigh.getData().size() + " high size");
            System.out.println(seriesAverageLow.getData().size() + " low size");
            if (onOffThresh){
                showHideThresh.setText("Show bounds");
                seriesAverageHigh.getNode().setVisible(false);
                seriesAverageLow.getNode().setVisible(false);

                seriesAverageHigh.getNode().setManaged(false);
                seriesAverageLow.getNode().setManaged(false);
                onOffThresh = false;
            }else{
                showHideThresh.setText("Hide bounds");
                seriesAverageHigh.getNode().setVisible(true);
                seriesAverageLow.getNode().setVisible(true);

                seriesAverageHigh.getNode().setManaged(true);
                seriesAverageLow.getNode().setManaged(true);
                onOffThresh = true;
            }
        });

        lineChart.setOnMouseClicked(mouseHandler);
        lineChart.setOnMouseDragged(mouseHandler);
        lineChart.setOnMouseMoved(mouseHandler);
        lineChart.setOnMousePressed(mouseHandler);
        lineChart.setOnMouseReleased(mouseHandler);
        lineChart.setOnMouseEntered(mouseHandler);
        lineChart.setOnMouseExited(mouseHandler);
        lineChartOverview.setOnMousePressed((mouseEvent) -> {
            positionXOverviewChart = mouseEvent.getSceneX();
        });

        lineChartOverview.setOnMouseDragged((mouseEvent) -> {
            double deltaX = positionXOverviewChart - mouseEvent.getSceneX();
            positionXOverviewChart = mouseEvent.getSceneX();
            if (positionXOverviewChart < 45 || positionXOverviewChart > 1375) return;
            if (rightHookPosition.getValue() - deltaX < 1375 && leftHookPosition.getValue() - deltaX >= 0) {

                leftHookPosition.set(leftHookPosition.getValue() - deltaX);
                rightHookPosition.set(rightHookPosition.getValue() - deltaX);
            }
            lastLatestValue = String.valueOf(lineChartOverview.getXAxis().getValueForDisplay(rightHookPosition.getValue() - 45.0));
            lastEarliestValue = String.valueOf(lineChartOverview.getXAxis().getValueForDisplay(leftHookPosition.getValue()));
            try {

                cutRight(deltaX);
                cutLeft(deltaX);
            } catch (Exception e) {
                System.out.println(e);
            }
            initialRightHookPosition = rightHookPosition.getValue();
            initialLeftHookPosition = leftHookPosition.getValue();
//                updateIndicator();
        });

        lineChartOverview.setOnMouseReleased((mouseEvent) -> refreshGraphFromSolr());
    }

    private void cutLeft(double deltaLeft) {
        try {
            if (deltaLeft < 0) {
                GraphActions.removePointsAtBack(seriesHighRaw, dataRemovedFromBack, lastEarliestValue);
                GraphActions.removePointsAtBack(seriesHighRawProviderTwo, dataRemovedFromBackProv2, lastEarliestValue);
                GraphActions.removePointsAtBack(seriesHighRawProviderThree, dataRemovedFromBackProv3, lastEarliestValue);
                GraphActions.removePointsAtBack(seriesHighRawProviderFour, dataRemovedFromBackProv4, lastEarliestValue);
                GraphActions.removePointsAtBack(seriesDiffBar, dataRemovedFromBackDelta, lastEarliestValue);
                GraphActions.removePointsAtBack(seriesHighRawNASDAQ, dataRemovedFromBackNASDAQ, lastEarliestValue);
                GraphActions.removePointsAtBack(seriesAverageHigh, dataRemovedFromBackHighBound, lastEarliestValue);
                GraphActions.removePointsAtBack(seriesAverageLow, dataRemovedFromBackLowBound, lastEarliestValue);
            } else if (deltaLeft > 0) {
                GraphActions.addPointsAtBack(seriesHighRaw, dataRemovedFromBack, lastEarliestValue);
                GraphActions.addPointsAtBack(seriesHighRawProviderTwo, dataRemovedFromBackProv2, lastEarliestValue);
                GraphActions.addPointsAtBack(seriesHighRawProviderThree, dataRemovedFromBackProv3, lastEarliestValue);
                GraphActions.addPointsAtBack(seriesHighRawProviderFour, dataRemovedFromBackProv4, lastEarliestValue);
                GraphActions.addPointsAtBack(seriesDiffBar, dataRemovedFromBackDelta, lastEarliestValue);
                GraphActions.addPointsAtBack(seriesHighRawNASDAQ, dataRemovedFromBackNASDAQ, lastEarliestValue);
                GraphActions.addPointsAtBack(seriesAverageHigh, dataRemovedFromBackHighBound, lastEarliestValue);
                GraphActions.addPointsAtBack(seriesAverageLow, dataRemovedFromBackLowBound, lastEarliestValue);
            }
            refreshXYBounds();
        } catch (Exception e) {
            refreshXYBounds();
            System.out.println(e + " at " + this);
            e.printStackTrace();

        }
    }

    private void cutRight(double deltaRight) {
        try {
            if (deltaRight > 0) {
              GraphActions.removePointsAtFront(seriesHighRaw, dataRemovedFromFront, lastLatestValue);
              GraphActions.removePointsAtFront(seriesHighRawProviderTwo, dataRemovedFromFrontProv2, lastLatestValue);
              GraphActions.removePointsAtFront(seriesHighRawProviderThree, dataRemovedFromFrontProv3, lastLatestValue);
              GraphActions.removePointsAtFront(seriesHighRawProviderFour, dataRemovedFromFrontProv4, lastLatestValue);
              GraphActions.removePointsAtFront(seriesDiffBar, dataRemovedFromFrontDelta, lastLatestValue);
              GraphActions.removePointsAtFront(seriesHighRawNASDAQ, dataRemovedFromFrontNASDAQ, lastLatestValue);
                GraphActions.removePointsAtFront(seriesAverageHigh, dataRemovedFromFrontHighBound, lastLatestValue);
                GraphActions.removePointsAtFront(seriesAverageLow, dataRemovedFromFrontLowBound, lastLatestValue);
            } else if (deltaRight < 0) {
              GraphActions.addPointsAtFront(seriesHighRaw, dataRemovedFromFront, lastLatestValue);
              GraphActions.addPointsAtFront(seriesHighRawProviderTwo, dataRemovedFromFrontProv2, lastLatestValue);
              GraphActions.addPointsAtFront(seriesHighRawProviderThree, dataRemovedFromFrontProv3, lastLatestValue);
              GraphActions.addPointsAtFront(seriesHighRawProviderFour, dataRemovedFromFrontProv4, lastLatestValue);
              GraphActions.addPointsAtFront(seriesDiffBar, dataRemovedFromFrontDelta, lastLatestValue);
              GraphActions.addPointsAtFront(seriesHighRawNASDAQ, dataRemovedFromFrontNASDAQ, lastLatestValue);
                GraphActions.addPointsAtFront(seriesAverageHigh, dataRemovedFromFrontHighBound, lastLatestValue);
                GraphActions.addPointsAtFront(seriesAverageLow, dataRemovedFromFrontLowBound, lastLatestValue);
            }
            refreshXYBounds();
        } catch (Exception e) {
            refreshXYBounds();
            System.out.println(e + " at " + this);
            e.printStackTrace();
        }
    }

    private void initComponents() {
        aboveAverages = new ArrayList<>();
        observableAboveAverages = FXCollections.observableArrayList(aboveAverages);
        valuesHighRaw = new ArrayList<>();
        tickValues = new ArrayList<>();
        dataRemovedFromFront = new ArrayList<>();
        dataRemovedFromBack = new ArrayList<>();
        changeOfvalues = new ArrayList<>();
        listOfMarks = new ListView<>();

        lineChartPane = new Pane();
        lineChartPane.setFocusTraversable(true);
        yLineAxis = new NumberAxis();
        yLineAxis.setForceZeroInRange(false);
        xCLineAxis = new DateAxis310();
        xCLineAxis.setTickLabelFormatter(STRING_CONVERTER);
        yAxis = new NumberAxis();
        xAxis = new DateAxis310();
        splitPane = new SplitPane();
        simplePane = new Pane();

        chartBox = new VBox();
        miniMapPane = new Pane();

        listOfMarks.setTranslateY(25);
        DefaultInMemoryConfig config = new DefaultInMemoryConfig();
        config.registerDefaultActions();
        config.getClientDolphin().getClientConnector().setUiThreadHandler(new JavaFXUiThreadHandler());
        registerApplicationActions(config);
        clientDolphin = config.getClientDolphin();
        separator = new Separator(Orientation.HORIZONTAL);
        leftRect.setTranslateX(45);
        leftRect.setFill(Color.web("blue", 0.1));
        leftRect.setStroke(Color.DODGERBLUE);
        leftRect.widthProperty().bind(leftHookPosition);
        t = new Timer();

        hookLeft.setFill(Color.web("gray", 0.6));
        hookLeft.setTranslateX(37.5);
        hookLeft.setTranslateY(55);
        hookLeft.setStroke(Color.DARKGRAY);
        hookLeft.layoutXProperty().bind(leftHookPosition);
        width = new SimpleDoubleProperty(1450);
        rightRect.setWidth(0);
        rightRect.setFill(Color.web("blue", 0.1));
        rightRect.setStroke(Color.DODGERBLUE);
        hookRight.setFill(Color.web("gray", 0.6));
        hookRight.setTranslateX(-7.5);
        hookRight.setLayoutX(1310);
        hookRight.setTranslateY(55);
        hookRight.setStroke(Color.DARKGRAY);
        rightRect.layoutXProperty().bind(rightHookPosition);
        rightHookPosition.set(1380);
        hookRight.layoutXProperty().bind(rightHookPosition);
        rightRect.widthProperty().bind(width.subtract(rightRect.layoutXProperty()));

        displayAtPosition.layoutYProperty().bind(trackYPosition);
        displayAtPosition.setMouseTransparent(true);
        displayAtPosition.setTranslateX(10);
        displayAtPosition.setTranslateY(-10);
        displayAtPosition.setEffect(new InnerShadow(2, Color.BLACK));
        displayAtPosition.setFont(Font.font(null, FontWeight.BOLD, 10));

        displayAtTarget.layoutYProperty().bind(trackYPosition);
        displayAtTarget.setMouseTransparent(true);
        displayAtTarget.setTranslateX(10);
        displayAtTarget.setTranslateY(-10);
        displayAtTarget.setEffect(new InnerShadow(2, Color.BLACK));
        displayAtTarget.setFont(Font.font(null, FontWeight.BOLD, 10));
        progress = new SimpleDoubleProperty();
        trackX.setMouseTransparent(true);
        trackX.layoutXProperty().bind(trackXPosition);
        trackX.setStroke(Color.DODGERBLUE);
        displayAtPosition.layoutXProperty().bind(trackXPosition);
        displayAtTarget.layoutXProperty().bind(trackXTargetPosition);

        zoomFactor = new Delta(1.0, 1.0);
        zoomBounds = new Rectangle();
        zoomBounds.setFill(Color.web("blue", 0.1));
        zoomBounds.setStroke(Color.DODGERBLUE);
        zoomBounds.setStrokeDashOffset(50);
        zoomBounds.setMouseTransparent(true);
        zoomBounds.widthProperty().bind(rectX.subtract(rectinitX));
        zoomBounds.heightProperty().bind(rectY.subtract(rectinitY));
        lockCB = new CheckBox("Lock view");
        spikes = new Label("List of spikes");
        spikes.setFont(new Font("Calibri", 22));
        spikes.setTranslateX(105);
        spikes.setTranslateY(20);
        listBox = new VBox(spikes, listOfMarks);
        optionsLabel = new Label();
        fileLabel = new Label("File");
        buttonControls = new VBox(blinkButton, showHideButton, showHideThresh, compareButton, optionsLabel);
        fileControls = new VBox(newProviderButton, newComparatorButton, fileLabel);
        buttonControls.setTranslateY(3);
        fileControls.setTranslateY(3);
        splitPane.setDividerPosition(0, 0.825);

        progressBar = new ProgressBar();

        dataRemovedFromFrontProv2 = new ArrayList<>();
        dataRemovedFromBackProv2 = new ArrayList<>();

        dataRemovedFromFrontProv3 = new ArrayList<>();
        dataRemovedFromBackProv3 = new ArrayList<>();

        dataRemovedFromFrontProv4 = new ArrayList<>();
        dataRemovedFromBackProv4 = new ArrayList<>();

        seriesHighRaw.setName("Provider 1");
        seriesHighRawProviderTwo.setName("Provider 2");
        seriesHighRawProviderThree.setName("Provider 3");
        seriesHighRawProviderFour.setName("Provider 4");
        seriesHighRawNASDAQ.setName("NASDAQ Composite");
        seriesAverageHigh.setName("Top average");
        seriesAverageLow.setName("Bottom average");
        seriesOpenRaw.setName("Open");
        providerDifferences = new ArrayList<>();
        xNAxis = new DateAxis310();
        yNAxis = new NumberAxis();
        yNAxis.setSide(Side.RIGHT);
        lineChartNASDAQ = new LineChart<LocalDateTime, Number>(xNAxis, yNAxis){
            @Override
            protected void layoutPlotChildren() {
                super.layoutPlotChildren();

                for (Node mark : getPlotChildren()) {
                    if (!(mark instanceof StackPane)) {
                        Path g = (Path) mark;
                        g.setStrokeWidth(1.5);
                    }
                }
            }
        };

        xNAxis.setAutoRanging(false);
        yNAxis.setTickLabelGap(15);
        yNAxis.setForceZeroInRange(false);

        xNAxis.setTickMarkVisible(false);
        xNAxis.setTickLabelsVisible(false);
        yLineAxis.setTickLabelGap(15);
        lineChartNASDAQ.setAnimated(false);
        miniMapDetail = new Text();
        miniMapDetail.setFill(Color.WHITE);
        miniMapDetail.setEffect(new InnerShadow(2, Color.ORANGE));
        miniMapDetail.setFont(Font.font(null, FontWeight.BOLD, 12));
        miniMapDetail.setVisible(false);
        uniquePositions = new ArrayList<>();

        detail = new Text();
        detail.setCache(true);
        detail.setFill(Color.BLACK);
        detail.setEffect(new InnerShadow(2, Color.WHITE));
        detail.setFont(Font.font(null, FontWeight.BOLD, 13));
        detail.setVisible(false);

        DateAxis310 xAxis = new DateAxis310();
        NumberAxis yAxis = new NumberAxis();
        diffBarChart = new AreaChart<LocalDateTime, Number>(xAxis,yAxis){
            @Override
            protected void layoutPlotChildren() {
                super.layoutPlotChildren();

                for (Node mark : getPlotChildren()) {
                    if (mark instanceof StackPane) {
                        mark.setScaleX(0.0);
                        mark.setScaleY(0.0);
                        mark.setManaged(false);
                        Bounds bounds = mark.getBoundsInParent();
                        double posX = bounds.getMinX() + (bounds.getMaxX() - bounds.getMinX()) / 2.0;
                        LocalDateTime date = getXAxis().getValueForDisplay(posX).truncatedTo(DAYS);
                        if (providerDifferences.contains(date)) {
                            mark.setManaged(true);
                            mark.setScaleX(1);
                            mark.setScaleY(1);
                        }
                    }
                }
            }
        };

        containingPane = new Pane();
        optionsPane = new Pane();
        optionsPane.setTranslateX(100);
        optionsPane.maxWidth(180);
        optionsPane.minWidth(180);

        filePane = new Pane();
        filePane.setTranslateX(45);
        filePane.maxWidth(180);
        filePane.minWidth(180);

        buttonControls.setAlignment(Pos.BOTTOM_LEFT);
        buttonControls.setPadding(new Insets(10, 10, 10, 10));
        buttonControls.setMaxWidth(150);
        buttonControls.setMaxHeight(180);

        fileControls.setAlignment(Pos.BOTTOM_LEFT);
        fileControls.setPadding(new Insets(10, 10, 10, 10));
        fileControls.setMaxWidth(150);
        fileControls.setMaxHeight(180);
        lineIndicator.setStroke(Color.DODGERBLUE);

        separator.prefWidthProperty().bind(miniMapPane.widthProperty());
        listOfMarks.prefWidthProperty().bind(simplePane.widthProperty());
        listOfMarks.prefHeightProperty().bind(simplePane.heightProperty());

        upperBoundForY = new SimpleDoubleProperty(2000);
        lowerBoundForY = new SimpleDoubleProperty(0);
        progressBar.setMinWidth(1460);
        progressBar.setMaxWidth(1460);

        progressBar.setTranslateY(841);
        progressBar.setTranslateX(-10);
        upperBoundForYMain = new SimpleDoubleProperty(2000);
        lowerBoundForYMain = new SimpleDoubleProperty(0);
        fileChooser = new FileChooser();

        providerDiff = new ArrayList<>();

        placeHolder = new Text();
        placeHolder.setFill(Color.WHITE);
        placeHolder.setEffect(new InnerShadow(2, Color.ORANGE));
        placeHolder.setFont(Font.font(null, FontWeight.BOLD, 13));
        placeHolder.setText("No Spikes");
        listOfMarks.setPlaceholder(placeHolder);
        seriesList = new ArrayList<>();
        seriesList.add(seriesHighRaw);
        seriesList.add(seriesHighRawProviderTwo);
        seriesList.add(seriesHighRawProviderThree);
        seriesList.add(seriesHighRawProviderFour);
        progressLine = new Timeline();
        progressBar.setVisible(true);
        progressBar.setMinHeight(20);
        progressBar.setMaxHeight(20);
        progressBar.setOpacity(0.6);
        progressBar.setBorder(Border.EMPTY);
        final KeyValue kv1 = new KeyValue(progress, 0.0);
        final KeyValue kv2 = new KeyValue(progress, 0.5);
        final KeyValue kv3 = new KeyValue(progress, 1.0);
        final KeyFrame kf1 = new KeyFrame(Duration.millis(4000), kv1, kv2, kv3);
        progressLine.getKeyFrames().add(kf1);
        progressLine.setCycleCount(1);
        yLineAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yLineAxis) {
            @Override
            public String toString(Number object) {
                String label;
                label = "$" + object.intValue();
//                if (label.length() < 5){
//                    label = label + ".";
//                    while (label.length() < 7){
//                        label = label + "0";
//                    }
//                }
                return label;
            }
        });

        yNAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yNAxis){
            @Override
            public String toString(Number object){
                String label;
                label = "$" + object.intValue();

                return label;

            }
        });

        dataRemovedFromFrontHighBound = new ArrayList<>();
        dataRemovedFromBackHighBound = new ArrayList<>();

        dataRemovedFromFrontLowBound  = new ArrayList<>();
        dataRemovedFromBackLowBound = new ArrayList<>();

        dataRemovedFromFrontNASDAQ = new ArrayList<>();
        dataRemovedFromBackNASDAQ = new ArrayList<>();

        dataRemovedFromFrontDelta = new ArrayList<>();
        dataRemovedFromBackDelta = new ArrayList<>();
    }

    private class XTargetChangeListener implements ChangeListener<Number> {
        @Override
        public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
            String displayValueAtLocation = (String.valueOf(lineChart.getXAxis().getValueForDisplay((Double) number2 - 60)));

            displayAtTarget.setText(displayValueAtLocation.substring(0, 10));
        }
    }

    private static void registerApplicationActions(DefaultInMemoryConfig config) {
        config.getServerDolphin().register(new ApplicationDirector());
    }


}