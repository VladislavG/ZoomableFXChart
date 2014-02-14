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
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
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
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;
import javafx.beans.property.SimpleDoubleProperty;
import org.opendolphin.core.PresentationModel;
import org.opendolphin.core.client.ClientAttribute;
import org.opendolphin.core.client.ClientDolphin;
import org.opendolphin.core.client.ClientPresentationModel;

import org.opendolphin.core.client.comm.OnFinishedHandler;
import org.opendolphin.core.comm.DefaultInMemoryConfig;

import static java.time.temporal.ChronoUnit.*;
import static solrTest.ApplicationConstants.STATE;
import static solrTest.ApplicationConstants.DATE;
import static solrTest.ApplicationConstants.STARTDATE;
import static solrTest.ApplicationConstants.ENDDATE;
import static solrTest.ApplicationConstants.SPIKE;
import static solrTest.ApplicationConstants.SERIES;
import static solrTest.ApplicationConstants.PRICE;
import static solrTest.ApplicationConstants.LOW;
import static solrTest.ApplicationConstants.HIGH;
import static solrTest.ApplicationConstants.DATEENTRY;
import static solrTest.ApplicationConstants.DISABLED;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;

public class Main extends Application {
    static ClientDolphin clientDolphin;
    XYChart.Series seriesHighRaw = new XYChart.Series();
    XYChart.Series seriesHighRawProviderTwo = new XYChart.Series();
    XYChart.Series seriesHighRawProviderThree = new XYChart.Series();
    XYChart.Series seriesHighRawProviderFour = new XYChart.Series();
    XYChart.Series seriesHighRawNASDAQ = new XYChart.Series();
    XYChart.Series seriesDiffBar = new XYChart.Series();
    int idFirstPoint;
    Double firstPointX;
    Double firstPointY;
    Text placeHolder;
    Label optionsLabel;
    int s;
    Button showHideButton = new Button("Hide providers");
    Button blinkButton = new Button("Blink providers");
    Label chartItemSize = new Label();
    Label chartLowerBound = new Label();
    Label chartUpperBound = new Label();
    Label chartSpecialMarksCount = new Label();
    int markCount = 0;
    int idSecondPoint;
    Double secondPointX;
    Double secondPointY;
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
    List uniquePositions;
    Pane optionsPane;
    int n;
    List<XYChart.Data<LocalDateTime, Float>> importantProv2 = new ArrayList<XYChart.Data<LocalDateTime, Float>>();
    List<XYChart.Data<LocalDateTime, Float>> importantProv3 = new ArrayList<XYChart.Data<LocalDateTime, Float>>();
    List<XYChart.Data<LocalDateTime, Float>> importantProv4 = new ArrayList<XYChart.Data<LocalDateTime, Float>>();

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
    Line trackX = new Line(0, 550, 0, 0);
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
    boolean onOffLock = true;
    boolean onOffThresh = false;
    Timer t;
    int q;

    SimpleDoubleProperty rectinitX = new SimpleDoubleProperty();
    SimpleDoubleProperty rectinitY = new SimpleDoubleProperty();
    SimpleDoubleProperty rectX = new SimpleDoubleProperty();
    SimpleDoubleProperty rectY = new SimpleDoubleProperty();

    LineChart<LocalDateTime, Number> lineChartOverview;
    LineChart<LocalDateTime, Number> lineChart;
    LineChart<LocalDateTime, Number> lineChartNASDAQ;

    double distanceBetweenPoint;
    List<Item> results;

    ArrayList<XYChart.Data<LocalDateTime, Float>> dataRemovedFromFront;
    ArrayList<XYChart.Data<LocalDateTime, Float>> dataRemovedFromBack;

    ArrayList<XYChart.Data<LocalDateTime, Float>> dataRemovedFromFrontProv2;
    ArrayList<XYChart.Data<LocalDateTime, Float>> dataRemovedFromBackProv2;

    ArrayList<XYChart.Data<LocalDateTime, Float>> dataRemovedFromFrontProv3;
    ArrayList<XYChart.Data<LocalDateTime, Float>> dataRemovedFromBackProv3;

    ArrayList<XYChart.Data<LocalDateTime, Float>> dataRemovedFromFrontProv4;
    ArrayList<XYChart.Data<LocalDateTime, Float>> dataRemovedFromBackProv4;

    ArrayList<XYChart.Data<LocalDateTime, Float>> dataRemovedFromFrontHighBound;
    ArrayList<XYChart.Data<LocalDateTime, Float>> dataRemovedFromBackHighBound;

    ArrayList<XYChart.Data<LocalDateTime, Float>> dataRemovedFromFrontLowBound;
    ArrayList<XYChart.Data<LocalDateTime, Float>> dataRemovedFromBackLowBound;

    ArrayList<XYChart.Data<LocalDateTime, Float>> dataRemovedFromFrontNASDAQ;
    ArrayList<XYChart.Data<LocalDateTime, Float>> dataRemovedFromBackNASDAQ;

    ArrayList<XYChart.Data<LocalDateTime, Float>> dataRemovedFromFrontDelta;
    ArrayList<XYChart.Data<LocalDateTime, Float>> dataRemovedFromBackDelta;

    double initialLeftHookPosition = 0.0;
    double initialRightHookPosition = 0.0;
    Line lineIndicator = new Line(0, 550, 0, 0);
    Rectangle hookRight = new Rectangle(15, 40);
    Rectangle hookLeft = new Rectangle(15, 40);
    Rectangle leftRect = new Rectangle(30, 150);
    Rectangle rightRect = new Rectangle(30, 150);
    Separator separator;
    Separator separator2 = new Separator();
    String selectedDate;
    VBox buttonControls;


    ArrayList<String> tickValues;
    ArrayList<String> valuesHighRaw;
    ArrayList<Double> changeOfvalues;
    double[] olsRegression;

    SimpleDoubleProperty width;
    Delta zoomFactor;
    ArrayList<LocalDateTime> aboveAverages;
    ArrayList<LocalDateTime> providerDifferences;
    ObservableList<LocalDateTime> observableAboveAverages;
    XYChart.Series seriesTotal;
    XYChart.Series seriesAverageHigh;
    XYChart.Series seriesAverageLow;
    XYChart.Series seriesOpenRaw;

    Pane lineChartPane;
    NumberAxis yAxis;
    DateAxis310 xAxis;

    AreaChart<LocalDateTime,Float> diffBarChart;

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
        clientDolphin.presentationModel(STATE,new ClientAttribute(STARTDATE),new ClientAttribute(ENDDATE), new ClientAttribute(DISABLED));

        clientDolphin.getClientModelStore().findPresentationModelById(STATE).findAttributeByPropertyName(STARTDATE).setValue("*");
        clientDolphin.getClientModelStore().findPresentationModelById(STATE).findAttributeByPropertyName(ENDDATE).setValue("*");
        clientDolphin.getClientModelStore().findPresentationModelById(STATE).findAttributeByPropertyName(DISABLED).setValue(false);

    }

    @Override
    public void start(Stage stage) throws Exception {
        final Group root = new Group();
        Scene scene = new Scene(root, 1750, 850, Color.WHITESMOKE);
        initComponents();
        initializePresentationModels();

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
                        aboveAverages.add(LocalDateTime.parse(toUtcDate(item.getDate()).substring(0, 19)));
                        observableAboveAverages.add(LocalDateTime.parse(toUtcDate(item.getDate()).substring(0, 19)));
                        markCount++;
                    }
                    if (item.getSpike().equals("diffSpike")) {
                        providerDifferences.add(LocalDateTime.parse(toUtcDate(item.getDate()).substring(0, 19)));

                    }
                    try {
                        switch (item.getSeries().toString()) {
                            case ("0"): {
                                seriesHighRaw.getData().add(new XYChart.Data(LocalDateTime.parse(toUtcDate(item.getDate()).substring(0, 19)), Float.valueOf(item.getHigh())));
                                seriesAverageLow.getData().add(new XYChart.Data(LocalDateTime.parse(toUtcDate(item.getDate()).substring(0, 19)), Float.valueOf(item.getClose())));
                                seriesAverageHigh.getData().add(new XYChart.Data(LocalDateTime.parse(toUtcDate(item.getDate()).substring(0, 19)), Float.valueOf(item.getOpen())));
                                seriesTotal.getData().add(new XYChart.Data(LocalDateTime.parse(toUtcDate(item.getDate()).substring(0, 19)), Float.valueOf(item.getHigh())));
                                listOfItems.add(item);

                                valuesHighRaw.add(item.getDate());

                                break;
                            }
                            case ("1"): {
                                seriesHighRawProviderTwo.getData().add(new XYChart.Data(LocalDateTime.parse(toUtcDate(item.getDate()).substring(0, 19)), Float.valueOf(item.getHigh())));
                                listOfItems.add(item);

                                break;
                            }
                            case ("2"): {
                                seriesHighRawProviderThree.getData().add(new XYChart.Data(LocalDateTime.parse(toUtcDate(item.getDate()).substring(0, 19)), Float.valueOf(item.getHigh())));
                                listOfItems.add(item);

                                break;
                            }
                            case ("3"): {
                                seriesHighRawProviderFour.getData().add(new XYChart.Data(LocalDateTime.parse(toUtcDate(item.getDate()).substring(0, 19)), Float.valueOf(item.getHigh())));
                                listOfItems.add(item);

                                break;
                            }
                            case ("4"): {
                                seriesHighRawNASDAQ.getData().add(new XYChart.Data(LocalDateTime.parse(toUtcDate(item.getDate()).substring(0, 19)), Float.valueOf(item.getHigh())));
                                break;
                            }

                        }
                    } catch (Exception e) {
//                System.out.println(toUtcDate(item.getDate()));
                        System.out.println("exception at " + toUtcDate(item.getDate()));
                    }
                }
                System.out.println(seriesHighRawNASDAQ.getData().size() + " NASDAQ size");
                System.out.println(seriesHighRaw.getData().size() + " prov1 size");
                System.out.println(seriesHighRawProviderTwo.getData().size() + " prov2 size");
                System.out.println(seriesHighRawProviderThree.getData().size() + " prov3 size");
                System.out.println(seriesHighRawProviderFour.getData().size() + " prov4 size");
                listOfMarks.getItems().addAll(observableAboveAverages);
                clientDolphin.findPresentationModelById(STATE).findAttributeByPropertyName(DISABLED).setValue(false);
                for (n = 0; n < valuesHighRaw.size()-1; n++){
                    Double biggestDiff = 0.0;
                    listOfPrices.clear();
                    listOfItems.forEach(new Consumer<Item>() {
                        @Override
                        public void accept(Item item) {
                            if (item.getDate().toString().equals(valuesHighRaw.get(n)))
                            listOfPrices.add(Double.valueOf(item.getHigh()));
                        }
                    });

                    Double maxDiff, maxNumber = 0.0, minNumber = Double.MAX_VALUE;
                    for (Double listOfPrice : listOfPrices) {
                        minNumber = Math.min(minNumber, listOfPrice);
                        maxNumber = Math.max(maxNumber, listOfPrice);
                    }
                    maxDiff = maxNumber - minNumber;
//                    if (n % Math.floor((n-10)/3) == 0 && maxDiff == 0){
//                    }else{
                        seriesDiffBar.getData().add(new XYChart.Data(LocalDateTime.parse(toUtcDate(valuesHighRaw.get(n)).substring(0, 19)), Float.valueOf(maxDiff.toString())));
//                    }
                }
                System.out.println("");
            }
        });
        System.out.println("Java Version             : " + com.sun.javafx.runtime.VersionInfo.getVersion());
        System.out.println("Java getRuntimeVersion   : " + com.sun.javafx.runtime.VersionInfo.getRuntimeVersion());
        diffBarChart.setVerticalGridLinesVisible(false);
        diffBarChart.setHorizontalGridLinesVisible(false);
        diffBarChart.getXAxis().setTickMarkVisible(false);
        diffBarChart.getXAxis().setTickLabelsVisible(false);
//        diffBarChart.setBarGap(0);
//        diffBarChart.setCategoryGap(0);
        diffBarChart.setTranslateX(12);
        diffBarChart.setMaxHeight(150);
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
                        Bounds bounds = mark.getBoundsInParent();
                        double posX = bounds.getMinX() + (bounds.getMaxX() - bounds.getMinX()) / 2.0;
                        double posY = bounds.getMinY() + (bounds.getMaxY() - bounds.getMinY()) / 2.0;
                        Double number = Double.valueOf(getYAxis().getValueForDisplay(posY).toString());
                        LocalDateTime date = getXAxis().getValueForDisplay(posX).truncatedTo(DAYS);
                        if (aboveAverages.contains(date)) {
                            mark.setScaleX(.5);
                            mark.setScaleY(.5);
                            ImageView flag = new ImageView("flag.png");
                            flag.setScaleX(.4);
                            flag.setScaleY(.4);
                            flag.setTranslateY(-20);
                            flag.setTranslateX(11);
//                                        ((StackPane) mark).getChildren().add(flag);
                            mark.setEffect(new InnerShadow(2, Color.BLACK));

                            mark.setOnMouseEntered(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent mouseEvent) {
                                    if (mark.getScaleX() == 0.5) {
                                        miniMapDetail.setText(String.valueOf((double) Math.round(number * 10) / 10));
                                        miniMapDetail.setTranslateX(mouseEvent.getSceneX() - 10);
                                        miniMapDetail.setTranslateY(mouseEvent.getSceneY() - 30);
                                        miniMapDetail.setVisible(true);
                                    }
                                }
                            });
                            mark.setOnMouseExited(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent mouseEvent) {
                                    if (miniMapDetail.isVisible()) {
                                        miniMapDetail.setVisible(false);
                                    }
                                }
                            });
                        }

                    }
                }
            }
        };

        List positions = new ArrayList<>();

        lineChart = new LineChart<LocalDateTime, Number>(xCLineAxis, yLineAxis) {
            @Override
            protected void layoutPlotChildren() {
                super.layoutPlotChildren();

                for (Node node : getChildren()) {

                    node.setOnZoom(new EventHandler<ZoomEvent>() {
                        @Override
                        public void handle(ZoomEvent event) {
                            zoomFactor.setX(event.getZoomFactor());
                        }
                    });

                    node.setOnTouchPressed(new EventHandler<TouchEvent>() {
                        @Override
                        public void handle(TouchEvent touchEvent) {
                            idFirstPoint = touchEvent.getTouchPoints().get(0).getId();
                            firstPointX = touchEvent.getTouchPoints().get(0).getSceneX();
                            firstPointY = touchEvent.getTouchPoints().get(0).getSceneY();
                            try {
                                idSecondPoint = touchEvent.getTouchPoints().get(1).getId();
                                secondPointX = touchEvent.getTouchPoints().get(1).getSceneX();
                                secondPointY = touchEvent.getTouchPoints().get(1).getSceneY();

                                distanceBetweenPoint = firstPointX - secondPointX;
                            } catch (Exception e) {
                            }
                        }
                    });

                    node.setOnTouchMoved(new EventHandler<TouchEvent>() {
                        @Override
                        public void handle(TouchEvent touchEvent) {
                            List<TouchPoint> touchPoints = touchEvent.getTouchPoints();
                            for (TouchPoint touchPoint : touchPoints) {
                                if (touchPoint.getId() == idFirstPoint) {
                                    firstPointX = touchPoint.getSceneX();
                                    firstPointY = touchPoint.getSceneY();

                                } else if (touchPoint.getId() == idSecondPoint) {
                                    secondPointX = touchPoint.getSceneX();
                                    secondPointY = touchPoint.getSceneY();
                                }
                            }
                            distanceBetweenPoint = firstPointX - secondPointX;

                        }
                    });
                    node.setOnTouchReleased(new EventHandler<TouchEvent>() {
                        @Override
                        public void handle(TouchEvent touchEvent) {
                            refreshGraphFromSolr();
                        }
                    });
                }
                uniquePositions.clear();
                for (Node mark : getPlotChildren()) {
                    if (!(mark instanceof StackPane)) {
                        Path g = (Path) mark;
                        g.setStrokeWidth(1.5);
                    }
                    if (mark instanceof StackPane) {
                        mark.setScaleX(0.0);
                        mark.setScaleY(0.0);
                        Bounds bounds = mark.getBoundsInParent();
                        double posX = bounds.getMinX() + (bounds.getMaxX() - bounds.getMinX()) / 2.0;
                        double posY = bounds.getMinY() + (bounds.getMaxY() - bounds.getMinY()) / 2.0;
                        if (!positions.contains(posX)) {
                            positions.add(posX);
                            Double number = (Double) getYAxis().getValueForDisplay(posY);
                            LocalDateTime date = getXAxis().getValueForDisplay(posX).truncatedTo(DAYS);
                            if (aboveAverages.contains(date)) {
                                mark.setScaleX(.5);
                                mark.setScaleY(.5);
                            }
                            else if (providerDifferences.contains(date)){
//                                Map datas = new HashMap<>();
//                                datas.put(getSeriesName(bounds).get(0), number);
                                if (!uniquePositions.contains(posX)){
//                                    mark.setScaleX(.5);
//                                    mark.setScaleY(.5);
//                                    for (Node stackPane : getPlotChildren()){
//                                        if (!(stackPane == mark)){
//                                            Bounds boundsother = stackPane.getBoundsInParent();
//
//                                            double posXother = boundsother.getMinX() + (boundsother.getMaxX() - boundsother.getMinX()) / 2.0;
//                                            double posYother = boundsother.getMinY() + (boundsother.getMaxY() - boundsother.getMinY()) / 2.0;
//                                            Number numberother = getYAxis().getValueForDisplay(posYother);
//                                            LocalDateTime otherDate = getXAxis().getValueForDisplay(posXother).truncatedTo(DAYS);
//                                            if (date.toString().equals(otherDate.toString())){
//                                                System.out.println("added");
//                                                for (Object o : getSeriesNameFromX(posXother)) {
//                                                    datas.put(o.toString(), numberother);
//                                                }
//                                            }
//                                        }
//                                    }
//                                    ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
//                                    datas.forEach(new BiConsumer() {
//                                        @Override
//                                        public void accept(Object o, Object o2) {
//
//                                            pieChartData.add(new PieChart.Data(o.toString(), (Double) o2));
//                                        }
//                                    });
//                                    PieChart pieChart = new PieChart(pieChartData);
//                                pieChart.setScaleX(.5);
//                                pieChart.setScaleY(.5);
//                                    pieChart.setOpacity(.3);
//                                    ((StackPane) mark).getChildren().add(pieChart);
                                  uniquePositions.add(posX);
                                }
                            }

                            mark.setOnMouseEntered(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent mouseEvent) {
                                if (mark.getScaleX() == .5) {
                                    detail.setText(String.valueOf((double) Math.round(number * 10) / 10));
                                    detail.setTranslateX(mouseEvent.getSceneX() - 10);
                                    detail.setTranslateY(mouseEvent.getSceneY() - 30);
                                    detail.setVisible(true);
                                }
                                }
                            });
                            mark.setOnMouseExited(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent mouseEvent) {
                                    if (detail.isVisible()) {
                                        detail.setVisible(false);
                                    }
                                }
                            });
                        }
                    }
                }
            }
        };
        setupCharts();
        addListeners();

        listOfMarks.getItems().addAll(observableAboveAverages);
        chartSpecialMarksCount.setText("Total peaks in graph: " + markCount);
        optionsLabel.setText("Options");
        lineChartPane.getChildren().addAll(lineChart, lineIndicator);
//        lockCB.setTranslateY(75);
//        showHideThresh.setTranslateY(50);
//        showHideButton.setTranslateY(25);
        miniMapPane.getChildren().addAll(lineChartOverview, leftRect, rightRect, hookRight, hookLeft, miniMapDetail);
        chartBox.getChildren().addAll(lineChartPane, separator, diffBarChart, separator2, miniMapPane);
        containingPane.getChildren().addAll(chartBox, zoomBounds, trackX, displayAtPosition, displayAtTarget, detail, optionsPane);
        simplePane.getChildren().addAll(listBox);
        optionsPane.getChildren().add(buttonControls);
        optionsPane.getStyleClass().add("b2");
        optionsPane.setLayoutY(-115);
        buttonControls.setSpacing(5);
        splitPane.getItems().addAll(containingPane, simplePane);
        splitPane.setMinWidth(scene.getWidth());
        root.getChildren().addAll(splitPane);

        stage.setTitle("Calculating Important Points");
        stage.setScene(scene);
        scene.getStylesheets().add("demo.css");

        stage.show();
    }

    private void refreshGraphFromSolr() {
        XYChart.Data<LocalDateTime, Float> stringFloatData = (XYChart.Data<LocalDateTime, Float>) seriesHighRaw.getData().get(seriesHighRaw.getData().size() - 1);
        XYChart.Data<LocalDateTime, Float> stringFloatDatafirst = (XYChart.Data<LocalDateTime, Float>) seriesHighRaw.getData().get(0);
        String endDate = (String.valueOf(stringFloatData.getXValue())).concat(":00Z");
        String startDate = (String.valueOf(stringFloatDatafirst.getXValue())).concat(":00Z");
        clientDolphin.findPresentationModelById(STATE).findAttributeByPropertyName(STARTDATE).setValue(startDate);
        clientDolphin.findPresentationModelById(STATE).findAttributeByPropertyName(ENDDATE).setValue(endDate);

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

                seriesHighRaw.getData().clear();
                seriesHighRawProviderTwo.getData().clear();
                seriesHighRawProviderThree.getData().clear();
                seriesHighRawProviderFour.getData().clear();
                seriesHighRawNASDAQ.getData().clear();
                seriesAverageLow.getData().clear();
                seriesAverageHigh.getData().clear();
                seriesDiffBar.getData().clear();
                markCount = 0;
                observableAboveAverages.clear();
                listOfItems.clear();
                valuesHighRaw.clear();

                for (int c = size; c >= 0; c--) {

                    Item item = dataList.get(size - c);

                    LocalDateTime itemDate = LocalDateTime.parse(toUtcDate(item.getDate()).substring(0, 19));
                    Float itemValue = Float.valueOf(item.getHigh());

                    if (item.getSpike().equals("spike") && item.getSeries().equals("0")) {
                        aboveAverages.add(itemDate);
                        observableAboveAverages.add(itemDate);
                        markCount++;

                    }
                    try {
                        switch (item.getSeries().toString()){
                            case ("0"):{
                                seriesHighRaw.getData().add(new XYChart.Data(LocalDateTime.parse(toUtcDate(item.getDate()).substring(0, 19)), Float.valueOf(item.getHigh())));
                                seriesAverageLow.getData().add(new XYChart.Data(LocalDateTime.parse(toUtcDate(item.getDate()).substring(0, 19)), Float.valueOf(item.getClose())));
                                seriesAverageHigh.getData().add(new XYChart.Data(LocalDateTime.parse(toUtcDate(item.getDate()).substring(0, 19)), Float.valueOf(item.getOpen())));
                                listOfItems.add(item);

                                valuesHighRaw.add(item.getDate());
                                break;
                            }case ("1"):{
                                seriesHighRawProviderTwo.getData().add(new XYChart.Data(LocalDateTime.parse(toUtcDate(item.getDate()).substring(0, 19)), Float.valueOf(item.getHigh())));
                                listOfItems.add(item);

                                break;
                            }case ("2"):{
                                seriesHighRawProviderThree.getData().add(new XYChart.Data(LocalDateTime.parse(toUtcDate(item.getDate()).substring(0, 19)), Float.valueOf(item.getHigh())));
                                listOfItems.add(item);
                                break;
                            }case ("3"):{
                                seriesHighRawProviderFour.getData().add(new XYChart.Data(LocalDateTime.parse(toUtcDate(item.getDate()).substring(0, 19)), Float.valueOf(item.getHigh())));
                                listOfItems.add(item);
                                break;
                            }case ("4"):{
                                seriesHighRawNASDAQ.getData().add(new XYChart.Data(LocalDateTime.parse(toUtcDate(item.getDate()).substring(0, 19)), Float.valueOf(item.getHigh())));
                                break;
                            }
                        }
                    } catch (Exception e){

                    }
                }
                chartItemSize.setText("Chart item size: " + seriesHighRaw.getData().size());
                clientDolphin.findPresentationModelById(STATE).findAttributeByPropertyName(DISABLED).setValue(false);
                for (n = 0; n < valuesHighRaw.size()-1; n++){
                    Double biggestDiff = 0.0;
                    listOfPrices.clear();
                    listOfItems.forEach(new Consumer<Item>() {
                        @Override
                        public void accept(Item item) {
                            if (item.getDate().toString().equals(valuesHighRaw.get(n)))
                                listOfPrices.add(Double.valueOf(item.getHigh()));
                        }
                    });
                    Double maxDiff, maxNumber = 0.0, minNumber = Double.MAX_VALUE;
                    for (Double listOfPrice : listOfPrices) {
                        minNumber = Math.min(minNumber, listOfPrice);
                        maxNumber = Math.max(maxNumber, listOfPrice);
                    }
                    maxDiff = maxNumber - minNumber;
//                            if (n % 10 == 0 && maxDiff == 0){
//                            }else{
                    seriesDiffBar.getData().add(new XYChart.Data(LocalDateTime.parse(toUtcDate(valuesHighRaw.get(n)).substring(0, 19)), Float.valueOf(maxDiff.toString())));
//                            }
                }
            }
        });

        listOfMarks.getItems().clear();
        listOfMarks.getItems().addAll(observableAboveAverages);
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
            } catch (ParseException ignore) {
            }
        }
        throw new IllegalArgumentException("Invalid date: " + dateStr);
    }

    EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent mouseEvent) {

            if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
                zoomBounds.setX(mouseEvent.getX());
                zoomBounds.setY(-20);
                rectinitX.set(mouseEvent.getX());
                rectinitY.set(0);
            } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED) {
                trackXTargetPosition.set(mouseEvent.getSceneX());
                rectX.set(mouseEvent.getX());
                rectY.set(lineChart.getHeight() + 20);
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
                    valueForDisplayStart = String.valueOf(lineChart.getXAxis().getValueForDisplay(rectinitX.getValue() - 55.0));
                    valueForDisplayEnd = String.valueOf(lineChart.getXAxis().getValueForDisplay(rectX.getValue() - 55.0));

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
                        GraphActions.removePointsAtFront(seriesAverageHigh, dataRemovedFromFrontHighBound, lastLatestValue);
                        GraphActions.removePointsAtFront(seriesAverageLow, dataRemovedFromFrontLowBound, lastLatestValue);
                        GraphActions.removePointsAtFront(seriesDiffBar, dataRemovedFromFrontDelta, lastLatestValue);
                        GraphActions.removePointsAtFront(seriesHighRawNASDAQ, dataRemovedFromFrontNASDAQ, lastLatestValue);

                        GraphActions.removePointsAtBack(seriesHighRaw, dataRemovedFromBack, lastEarliestValue);
                        GraphActions.removePointsAtBack(seriesHighRawProviderTwo, dataRemovedFromBackProv2, lastEarliestValue);
                        GraphActions.removePointsAtBack(seriesHighRawProviderThree, dataRemovedFromBackProv3, lastEarliestValue);
                        GraphActions.removePointsAtBack(seriesHighRawProviderFour, dataRemovedFromBackProv4, lastEarliestValue);
                        GraphActions.removePointsAtBack(seriesAverageHigh, dataRemovedFromBackHighBound, lastEarliestValue);
                        GraphActions.removePointsAtBack(seriesAverageLow, dataRemovedFromBackLowBound, lastEarliestValue);
                        GraphActions.removePointsAtBack(seriesDiffBar, dataRemovedFromBackDelta, lastEarliestValue);
                        GraphActions.removePointsAtBack(seriesHighRawNASDAQ, dataRemovedFromBackNASDAQ, lastEarliestValue);

                    } catch (Exception e) {
                    }

                    clientDolphin.send("Query", new OnFinishedHandler() {
                        @Override
                        public void onFinished(List<ClientPresentationModel> presentationModels) {
                        }
                        @Override
                        public void onFinishedData(List<Map> datas) {
                            List<Item> dataList = new ArrayList<>();
                            int f = 0;
                            for (Map map : datas) {
                                dataList.add((Item) map.get(f));
                                f++;
                            }

                            int size = dataList.size() - 1;

                            seriesHighRaw.getData().clear();
                            seriesHighRawProviderTwo.getData().clear();
                            seriesHighRawProviderThree.getData().clear();
                            seriesHighRawProviderFour.getData().clear();
                            seriesHighRawNASDAQ.getData().clear();
                            seriesAverageLow.getData().clear();
                            seriesAverageHigh.getData().clear();
                            seriesDiffBar.getData().clear();
                            markCount = 0;
                            observableAboveAverages.clear();
                            listOfItems.clear();
                            valuesHighRaw.clear();

                            for (int c = size; c >= 0; c--) {

                                Item item = dataList.get(size - c);

                                LocalDateTime itemDate = LocalDateTime.parse(toUtcDate(item.getDate()).substring(0, 19));
                                Float itemValue = Float.valueOf(item.getHigh());

                                if (item.getSpike().equals("spike") && item.getSeries().equals("0")) {
                                    aboveAverages.add(itemDate);
                                    observableAboveAverages.add(itemDate);
                                    markCount++;

                                }
                                try {
                                    switch (item.getSeries().toString()){
                                        case ("0"):{
                                            seriesHighRaw.getData().add(new XYChart.Data(LocalDateTime.parse(toUtcDate(item.getDate()).substring(0, 19)), Float.valueOf(item.getHigh())));
                                            seriesAverageLow.getData().add(new XYChart.Data(LocalDateTime.parse(toUtcDate(item.getDate()).substring(0, 19)), Float.valueOf(item.getClose())));
                                            seriesAverageHigh.getData().add(new XYChart.Data(LocalDateTime.parse(toUtcDate(item.getDate()).substring(0, 19)), Float.valueOf(item.getOpen())));
                                            listOfItems.add(item);

                                            valuesHighRaw.add(item.getDate());
                                            break;
                                        }case ("1"):{
                                            seriesHighRawProviderTwo.getData().add(new XYChart.Data(LocalDateTime.parse(toUtcDate(item.getDate()).substring(0, 19)), Float.valueOf(item.getHigh())));
                                            listOfItems.add(item);

                                            break;
                                        }case ("2"):{
                                            seriesHighRawProviderThree.getData().add(new XYChart.Data(LocalDateTime.parse(toUtcDate(item.getDate()).substring(0, 19)), Float.valueOf(item.getHigh())));
                                            listOfItems.add(item);
                                            break;
                                        }case ("3"):{
                                            seriesHighRawProviderFour.getData().add(new XYChart.Data(LocalDateTime.parse(toUtcDate(item.getDate()).substring(0, 19)), Float.valueOf(item.getHigh())));
                                            listOfItems.add(item);
                                            break;
                                        }case ("4"):{
                                            seriesHighRawNASDAQ.getData().add(new XYChart.Data(LocalDateTime.parse(toUtcDate(item.getDate()).substring(0, 19)), Float.valueOf(item.getHigh())));
                                            break;
                                        }
                                    }
                                } catch (Exception e){

                                }
                                chartItemSize.setText("Chart item size: " + seriesHighRaw.getData().size());
                            }
                            clientDolphin.findPresentationModelById(STATE).findAttributeByPropertyName(DISABLED).setValue(false);
                            for (n = 0; n < valuesHighRaw.size()-1; n++){
                                Double biggestDiff = 0.0;
                                listOfPrices.clear();
                                listOfItems.forEach(new Consumer<Item>() {
                                    @Override
                                    public void accept(Item item) {
                                        if (item.getDate().toString().equals(valuesHighRaw.get(n)))
                                            listOfPrices.add(Double.valueOf(item.getHigh()));
                                    }
                                });
                                Double maxDiff, maxNumber = 0.0, minNumber = Double.MAX_VALUE;
                                for (Double listOfPrice : listOfPrices) {
                                    minNumber = Math.min(minNumber, listOfPrice);
                                    maxNumber = Math.max(maxNumber, listOfPrice);
                                }
                                maxDiff = maxNumber - minNumber;
//                            if (n % 10 == 0 && maxDiff == 0){
//                            }else{
                                seriesDiffBar.getData().add(new XYChart.Data(LocalDateTime.parse(toUtcDate(valuesHighRaw.get(n)).substring(0, 19)), Float.valueOf(maxDiff.toString())));
//                            }
                            }
                        }
                    });
                    System.out.println("Getting results from: " + endDate.substring(0, 10) + " to " + startDate.substring(0, 10));
                }catch (Exception e) {

                }

                listOfMarks.getItems().clear();
                listOfMarks.getItems().addAll(observableAboveAverages);

                if (seriesHighRaw.getData().size() == 0) {
                    XYChart.Data<LocalDateTime, Float> frontData = dataRemovedFromFront.get(dataRemovedFromFront.size() - 1);
                    dataRemovedFromFront.remove(frontData);
                    chartUpperBound.setText("Chart upper bound: " + frontData.getXValue());
                    String dateFront = String.valueOf(frontData.getXValue());
                    Float valueFront = frontData.getYValue();
                    seriesHighRaw.getData().add(new XYChart.Data(frontData.getXValue(), valueFront));

                    XYChart.Data<LocalDateTime, Float> backData = dataRemovedFromBack.get(dataRemovedFromBack.size() - 1);
                    dataRemovedFromBack.remove(backData);
                    chartLowerBound.setText("Chart lower bound: " + backData.getXValue());
                    String dateBack = String.valueOf(backData.getXValue());
                    Float valueBack = backData.getYValue();

                    seriesHighRaw.getData().add(0, new XYChart.Data(backData.getXValue(), valueBack));
                }

                rectX.set(0);
                rectY.set(0);
            } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_MOVED) {
                trackX.setVisible(true);
                trackXPosition.set(mouseEvent.getSceneX());
                trackYPosition.set(mouseEvent.getSceneY());
            } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_EXITED) {
                trackX.setVisible(false);
                displayAtPosition.setVisible(false);
                displayAtTarget.setVisible(false);
            }
            updateIndicator();

        }
    };

    private void setupCharts() {
        ((DateAxis310) lineChart.getXAxis()).setTickLabelsVisible(true);
        ((DateAxis310) lineChart.getXAxis()).setTickMarkVisible(false);
        lineChart.setLegendVisible(true);
        lineChart.setHorizontalGridLinesVisible(true);
//        lineChart.getData().add(seriesAverageLow);
//        lineChart.getData().add(seriesAverageHigh);
        lineChart.getData().add(seriesHighRawNASDAQ);
        lineChart.getData().add(seriesHighRawProviderTwo);
        lineChart.getData().add(seriesHighRawProviderThree);
        lineChart.getData().add(seriesHighRawProviderFour);
        lineChart.getData().add(seriesHighRaw);
        diffBarChart.getData().addAll(seriesDiffBar);
        lineChart.setMaxWidth(1390);
        lineChart.setMinWidth(1390);
        lineChart.setMaxHeight(550);
        lineChart.setMinHeight(550);
        diffBarChart.setMaxWidth(1378);
        diffBarChart.setAnimated(false);
        lineChart.getCreateSymbols();
        lineChart.setVerticalZeroLineVisible(false);
        lineChart.setVerticalGridLinesVisible(false);
        lineChart.setHorizontalZeroLineVisible(false);
        lineChart.setVerticalZeroLineVisible(false);
        lineChart.setHorizontalZeroLineVisible(false);
        lineChart.setAnimated(false);
        lineChart.setTitle("Price of gold");
        lineChart.setCreateSymbols(true);
        lineChart.setVerticalZeroLineVisible(false);

        lineChartOverview.setCreateSymbols(true);
        lineChartOverview.legendVisibleProperty().setValue(false);
        lineChartOverview.getData().add(seriesTotal);
        lineChartOverview.setVerticalGridLinesVisible(false);
        lineChartOverview.setHorizontalZeroLineVisible(false);
        ((DateAxis310) lineChartOverview.getXAxis()).setTickLabelsVisible(false);
        ((DateAxis310) lineChartOverview.getXAxis()).setTickMarkVisible(false);
        lineChartOverview.setMaxHeight(140);
        lineChartOverview.setMinWidth(1390);
        lineChartOverview.setMaxWidth(1390);
    }

    private void addListeners() {

        clientDolphin.findPresentationModelById(STATE).findAttributeByPropertyName(DISABLED).addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                splitPane.setDisable((Boolean) evt.getNewValue());
            }
        });
        optionsPane.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                final Timeline timeline = new Timeline();
                timeline.setCycleCount(1);
                timeline.setAutoReverse(false);
                final KeyValue kv2 = new KeyValue(optionsPane.layoutYProperty(), -10);
                final KeyFrame kf2 = new KeyFrame(Duration.millis(200), kv2);
                timeline.getKeyFrames().add(kf2);
                timeline.play();

                final Timeline timelineOpacity = new Timeline();
                timelineOpacity.setCycleCount(1);
                timelineOpacity.setAutoReverse(false);
                final KeyValue kv = new KeyValue(optionsLabel.opacityProperty(), 0);
                final KeyFrame kf = new KeyFrame(Duration.millis(500), kv);
                timelineOpacity.getKeyFrames().add(kf);
                timelineOpacity.play();
            }
        });

        optionsPane.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                final Timeline timeline = new Timeline();
                timeline.setCycleCount(1);
                timeline.setAutoReverse(false);
                final KeyValue kv2 = new KeyValue(optionsPane.layoutYProperty(), -115);
                final KeyFrame kf2 = new KeyFrame(Duration.millis(400), kv2);
                timeline.getKeyFrames().add(kf2);
                timeline.play();

                final Timeline timelineOpacity = new Timeline();
                timelineOpacity.setCycleCount(1);
                timelineOpacity.setAutoReverse(false);
                final KeyValue kv = new KeyValue(optionsLabel.opacityProperty(), 1);
                final KeyFrame kf = new KeyFrame(Duration.millis(500), kv);
                timelineOpacity.getKeyFrames().add(kf);
                timelineOpacity.play();

            }
        });

        hookLeft.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                initialLeftHookPosition = mouseEvent.getSceneX();
            }
        });


        hookRight.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                initialRightHookPosition = mouseEvent.getSceneX();
            }
        });

        trackXPosition.addListener(new Listeners(this, lineChart, displayAtPosition));

        trackXTargetPosition.addListener(new XTargetChangeListener());

        hookLeft.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println(leftRect.getWidth() + " <<rectRight.getWidth()");
                System.out.println(hookRight.getLayoutX() + " <<hookRight.getLayoutX()");
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
                lastEarliestValue = String.valueOf(lineChartOverview.getXAxis().getValueForDisplay(mouseEvent.getSceneX() - 40));

                updateIndicator();
                cutLeft(deltaLeft);
                initialLeftHookPosition = mouseEvent.getSceneX();

            }
        });

        hookLeft.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (leftRect.getWidth() >= (hookRight.getLayoutX() - 50)) {
                    leftHookPosition.set(hookRight.getLayoutX() - 60);
                    return;
                }
                if (mouseEvent.getSceneX() < 55) {
                    leftHookPosition.set(0);
                    return;
                }
                refreshGraphFromSolr();
                updateIndicator();
            }
        });

        hookRight.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
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

                XYChart.Data<LocalDateTime, Float> firstDate = (XYChart.Data<LocalDateTime, Float>) seriesHighRaw.getData().get(seriesHighRaw.getData().size() - 1);
                lastLatestValue = String.valueOf(lineChartOverview.getXAxis().getValueForDisplay(mouseEvent.getSceneX() - 45));
                updateIndicator();
                cutRight(deltaRight);

                initialRightHookPosition = mouseEvent.getSceneX();

            }
        });

        hookRight.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                String endDate = "*";
                String startDate = "*";
                if (rightRect.getLayoutX() - 50 < leftRect.getWidth() || mouseEvent.getSceneX() - 55 < leftRect.getWidth()) {
                    rightHookPosition.set(leftRect.getWidth() + 60);
                    return;
                }
                if (mouseEvent.getSceneX() > 1375) {
                    rightHookPosition.setValue(1375);
                    return;
                }
                refreshGraphFromSolr();
                updateIndicator();
            }
        });

        listOfMarks.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<LocalDateTime>() {
            @Override
            public void changed(ObservableValue<? extends LocalDateTime> observableDateValue, LocalDateTime previousSelection, LocalDateTime newSelection) {
                try {
                    lineIndicator.setVisible(true);
                    lineIndicator.setLayoutX(lineChart.getXAxis().getDisplayPosition(newSelection) + 42.0);
                    if (newSelection == null) return;
                    selectedDate = newSelection.toString();
                } catch (Exception e) {

                }

            }
        });
        listOfMarks.getItems().addListener(new ListChangeListener<LocalDateTime>() {
            @Override
            public void onChanged(Change<? extends LocalDateTime> change) {
                while (change.next()) {
                    if (listOfMarks.getItems().size() == 0) {
                        return;
                    }
                }
            }
        });

        blinkButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                if (startStop){
                    t = new Timer();
                    t.schedule(
                            new TimerTask() {
                                @Override
                                public void run() {
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (onOff) {
                                                lineChart.getData().remove(seriesHighRawProviderFour);
                                                lineChart.getData().remove(seriesHighRawProviderTwo);
                                                lineChart.getData().remove(seriesHighRawProviderThree);
                                                onOff = false;
                                            } else {
                                                lineChart.getData().remove(seriesHighRaw);
                                                lineChart.getData().add(seriesHighRawProviderFour);
                                                lineChart.getData().add(seriesHighRawProviderTwo);
                                                lineChart.getData().add(seriesHighRawProviderThree);
                                                lineChart.getData().add(seriesHighRaw);
                                                onOff = true;
                                            }
                                        }
                                    });
                                }
                            }, 0, 500);
                    startStop = false;

                }else{
                    t.cancel();
                    startStop = true;
                }
            }
        });

        showHideButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                t.cancel();
                if (lineChart.getData().contains(seriesHighRawProviderTwo)){
                    try{
                        showHideButton.setText("Show providers");
                        lineChart.getData().remove(seriesHighRawProviderFour);
                        lineChart.getData().remove(seriesHighRawProviderTwo);
                        lineChart.getData().remove(seriesHighRawProviderThree);
                    }catch (Exception e){

                    }
                } else{
                    try{
                        showHideButton.setText("Hide providers");
                        lineChart.getData().remove(seriesHighRaw);
                        lineChart.getData().add(seriesHighRawProviderFour);
                        lineChart.getData().add(seriesHighRawProviderTwo);
                        lineChart.getData().add(seriesHighRawProviderThree);
                        lineChart.getData().add(seriesHighRaw);
                    }catch (Exception e){

                    }
                }
            }
        });

        lockCB.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean aBoolean2) {
                if (aBoolean){
                    lineChart.getYAxis().setAutoRanging(true);
                    lineChart.getXAxis().setAutoRanging(true);
                }else{
                    lineChart.getYAxis().setAutoRanging(false);
                    lineChart.getXAxis().setAutoRanging(false);
                }
            }
        });

        showHideThresh.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (onOffThresh){
                    showHideThresh.setText("Show bounds");
                    lineChart.getData().remove(seriesAverageLow);
                    lineChart.getData().remove(seriesAverageHigh);
                    onOffThresh = false;
                }else{
                    showHideThresh.setText("Hide bounds");
                    lineChart.getData().add(seriesAverageLow);
                    lineChart.getData().add(seriesAverageHigh);
                    onOffThresh = true;
                }
            }
        });

        lineChart.setOnMouseClicked(mouseHandler);
        lineChart.setOnMouseDragged(mouseHandler);
        lineChart.setOnMouseMoved(mouseHandler);
        lineChart.setOnMousePressed(mouseHandler);
        lineChart.setOnMouseReleased(mouseHandler);
        lineChart.setOnMouseEntered(mouseHandler);
        lineChart.setOnMouseExited(mouseHandler);
        lineChartOverview.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                positionXOverviewChart = mouseEvent.getSceneX();
            }
        });

        lineChartOverview.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
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
                    System.out.println((XYChart.Data<LocalDateTime, Float>) seriesHighRaw.getData().get(seriesHighRaw.getData().size() - 1));
                    System.out.println((XYChart.Data<LocalDateTime, Float>) seriesHighRaw.getData().get(0));
                }
                initialRightHookPosition = rightHookPosition.getValue();
                initialLeftHookPosition = leftHookPosition.getValue();
                updateIndicator();
            }
        });

        lineChartOverview.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent MouseEvent) {
                refreshGraphFromSolr();
                updateIndicator();
            }
        });
    }

    private void cutLeft(double deltaLeft) {
        try {
            if (deltaLeft < 0) {
                GraphActions.removePointsAtBack(seriesHighRaw, dataRemovedFromBack, lastEarliestValue);
                GraphActions.removePointsAtBack(seriesHighRawProviderTwo, dataRemovedFromBackProv2, lastEarliestValue);
                GraphActions.removePointsAtBack(seriesHighRawProviderThree, dataRemovedFromBackProv3, lastEarliestValue);
                GraphActions.removePointsAtBack(seriesHighRawProviderFour, dataRemovedFromBackProv4, lastEarliestValue);
                GraphActions.removePointsAtBack(seriesAverageHigh, dataRemovedFromBackHighBound, lastEarliestValue);
                GraphActions.removePointsAtBack(seriesAverageLow, dataRemovedFromBackLowBound, lastEarliestValue);
                GraphActions.removePointsAtBack(seriesDiffBar, dataRemovedFromBackDelta, lastEarliestValue);
                GraphActions.removePointsAtBack(seriesHighRawNASDAQ, dataRemovedFromBackNASDAQ, lastEarliestValue);

            } else if (deltaLeft > 0) {
                GraphActions.addPointsAtBack(seriesHighRaw, dataRemovedFromBack, lastEarliestValue);
                GraphActions.addPointsAtBack(seriesHighRawProviderTwo, dataRemovedFromBackProv2, lastEarliestValue);
                GraphActions.addPointsAtBack(seriesHighRawProviderThree, dataRemovedFromBackProv3, lastEarliestValue);
                GraphActions.addPointsAtBack(seriesHighRawProviderFour, dataRemovedFromBackProv4, lastEarliestValue);
                GraphActions.addPointsAtBack(seriesAverageHigh, dataRemovedFromBackHighBound, lastEarliestValue);
                GraphActions.addPointsAtBack(seriesAverageLow, dataRemovedFromBackLowBound, lastEarliestValue);
                GraphActions.addPointsAtBack(seriesDiffBar, dataRemovedFromBackDelta, lastEarliestValue);
                GraphActions.addPointsAtBack(seriesHighRawNASDAQ, dataRemovedFromBackNASDAQ, lastEarliestValue);

            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void cutRight(double deltaRight) {
        try {
            if (deltaRight > 0) {
              GraphActions.removePointsAtFront(seriesHighRaw, dataRemovedFromFront, lastLatestValue);
              GraphActions.removePointsAtFront(seriesHighRawProviderTwo, dataRemovedFromFrontProv2, lastLatestValue);
              GraphActions.removePointsAtFront(seriesHighRawProviderThree, dataRemovedFromFrontProv3, lastLatestValue);
              GraphActions.removePointsAtFront(seriesHighRawProviderFour, dataRemovedFromFrontProv4, lastLatestValue);
              GraphActions.removePointsAtFront(seriesAverageHigh, dataRemovedFromFrontHighBound, lastLatestValue);
              GraphActions.removePointsAtFront(seriesAverageLow, dataRemovedFromFrontLowBound, lastLatestValue);
                GraphActions.removePointsAtFront(seriesDiffBar, dataRemovedFromFrontDelta, lastLatestValue);
                GraphActions.removePointsAtFront(seriesHighRawNASDAQ, dataRemovedFromFrontNASDAQ, lastLatestValue);
            } else if (deltaRight < 0) {
              GraphActions.addPointsAtFront(seriesHighRaw, dataRemovedFromFront, lastLatestValue);
              GraphActions.addPointsAtFront(seriesHighRawProviderTwo, dataRemovedFromFrontProv2, lastLatestValue);
              GraphActions.addPointsAtFront(seriesHighRawProviderThree, dataRemovedFromFrontProv3, lastLatestValue);
              GraphActions.addPointsAtFront(seriesHighRawProviderFour, dataRemovedFromFrontProv4, lastLatestValue);
              GraphActions.addPointsAtFront(seriesAverageHigh, dataRemovedFromFrontHighBound, lastLatestValue);
              GraphActions.addPointsAtFront(seriesAverageLow, dataRemovedFromFrontLowBound, lastLatestValue);
                GraphActions.addPointsAtFront(seriesDiffBar, dataRemovedFromFrontDelta, lastLatestValue);
                GraphActions.addPointsAtFront(seriesHighRawNASDAQ, dataRemovedFromFrontNASDAQ, lastLatestValue);
            }
        } catch (Exception e) {
        }
    }

    private void updateIndicator() {
        try {

            double newPosition = lineChart.getXAxis().getDisplayPosition(LocalDateTime.parse(selectedDate)) + 42.0;
            if (newPosition < 1390 && newPosition > 45) {
                lineIndicator.setVisible(true);
                lineIndicator.setLayoutX(newPosition);
            } else {
                lineIndicator.setVisible(false);
            }

        } catch (Exception e) {
        }
    }

    private void initComponents() {
        aboveAverages = new ArrayList<LocalDateTime>();
        observableAboveAverages = FXCollections.observableArrayList(aboveAverages);
        valuesHighRaw = new ArrayList<>();
        tickValues = new ArrayList<>();
        dataRemovedFromFront = new ArrayList<XYChart.Data<LocalDateTime, Float>>();
        dataRemovedFromBack = new ArrayList<XYChart.Data<LocalDateTime, Float>>();
        changeOfvalues = new ArrayList<>();
        listOfMarks = new ListView<LocalDateTime>();

        lineChartPane = new Pane();
        lineChartPane.setFocusTraversable(true);

        yLineAxis = new NumberAxis();
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
        displayAtPosition.setEffect(new InnerShadow(2, Color.ORANGE));
        displayAtPosition.setFont(Font.font(null, FontWeight.BOLD, 10));

        displayAtTarget.layoutYProperty().bind(trackYPosition);
        displayAtTarget.setMouseTransparent(true);
        displayAtTarget.setTranslateX(10);
        displayAtTarget.setTranslateY(-10);
        displayAtTarget.setEffect(new InnerShadow(2, Color.ORANGE));
        displayAtTarget.setFont(Font.font(null, FontWeight.BOLD, 10));

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
        buttonControls = new VBox(blinkButton, showHideButton, showHideThresh, lockCB, optionsLabel);
        buttonControls.setTranslateY(3);
        splitPane.setDividerPosition(0, 0.825);

        seriesTotal = new XYChart.Series();
        seriesAverageHigh = new XYChart.Series();
        seriesAverageLow = new XYChart.Series();
        seriesOpenRaw = new XYChart.Series();


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

        miniMapDetail = new Text();
        miniMapDetail.setFill(Color.WHITE);
        miniMapDetail.setEffect(new InnerShadow(2, Color.ORANGE));
        miniMapDetail.setFont(Font.font(null, FontWeight.BOLD, 12));
        miniMapDetail.setVisible(false);
        uniquePositions = new ArrayList<>();

        detail = new Text();
        detail.setCache(true);
        detail.setFill(Color.WHITE);
        detail.setEffect(new InnerShadow(2, Color.ORANGE));
        detail.setFont(Font.font(null, FontWeight.BOLD, 13));
        detail.setVisible(false);

        DateAxis310 xAxis = new DateAxis310();
        ValueAxis yAxis = new NumberAxis();
        diffBarChart = new AreaChart<LocalDateTime, Float>(xAxis,yAxis);

        containingPane = new Pane();
        optionsPane = new Pane();
        optionsPane.setTranslateX(50);
        optionsPane.maxWidth(180);
        buttonControls.setAlignment(Pos.BOTTOM_LEFT);
        buttonControls.setPadding(new Insets(10, 10, 10, 10));
        buttonControls.setMaxWidth(150);
        buttonControls.setMaxHeight(180);
        lineIndicator.setStroke(Color.DODGERBLUE);
//        yAxis.setForceZeroInRange(false);
        separator.prefWidthProperty().bind(miniMapPane.widthProperty());
        listOfMarks.prefWidthProperty().bind(simplePane.widthProperty());
        listOfMarks.prefHeightProperty().bind(simplePane.heightProperty());
        placeHolder = new Text();
        placeHolder.setFill(Color.WHITE);
        placeHolder.setEffect(new InnerShadow(2, Color.ORANGE));
        placeHolder.setFont(Font.font(null, FontWeight.BOLD, 13));
        placeHolder.setText("No Spikes");
        listOfMarks.setPlaceholder(placeHolder);
        seriesList = new ArrayList<XYChart.Series>();
        seriesList.add(seriesHighRaw);
        seriesList.add(seriesHighRawProviderTwo);
        seriesList.add(seriesHighRawProviderThree);
        seriesList.add(seriesHighRawProviderFour);

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
            String displayValueAtLocation = (String.valueOf(lineChart.getXAxis().getValueForDisplay((Double) number2)));

            displayAtTarget.setText(displayValueAtLocation.substring(0, 10));
        }
    }

    private static void registerApplicationActions(DefaultInMemoryConfig config) {
        config.getServerDolphin().register(new ApplicationDirector());
    }

    public List getSeriesName(Bounds bounds){
        List seriesName = new ArrayList<>();
        for (XYChart.Series<LocalDateTime, Number> stringNumberSeries : seriesList) {

            for (XYChart.Data<LocalDateTime, Number> stringNumberData : stringNumberSeries.getData()) {
                    if (stringNumberData.getNode().getBoundsInParent().equals(bounds)){
                        seriesName.add(stringNumberSeries.getName());
                    }
            }
        }
        return seriesName;
    }
    public List getSeriesNameFromX(Double posX){
        List seriesName = new ArrayList<>();
        for (XYChart.Series<LocalDateTime, Number> stringNumberSeries : seriesList) {

            for (XYChart.Data<LocalDateTime, Number> stringNumberData : stringNumberSeries.getData()) {
                Bounds boundsInParent = stringNumberData.getNode().getBoundsInParent();
                double posXother = boundsInParent.getMinX() + (boundsInParent.getMaxX() - boundsInParent.getMinX()) / 2.0;

                if (posXother == (posX)){
                    seriesName.add(stringNumberSeries.getName());
                }
            }
        }
        return seriesName;
    }

}
