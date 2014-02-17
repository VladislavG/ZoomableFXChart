package solrTest;

import javafx.scene.chart.XYChart;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by vladislav on 11.02.14.
 */
public class GraphActions {
    static void removePointsAtBack(XYChart.Series series, List pointsList, String lastPoint){
        Date currentDate = new Date();
        Date filterDate = new Date();
        if (series.getData().size() == 0)return;
        try{
            filterDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse(lastPoint);
            currentDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(String.valueOf(((XYChart.Data<LocalDateTime, Float>) series.getData().get(0)).getXValue()));
        }catch (ParseException e){
            try {
                filterDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(lastPoint);
            } catch (ParseException e1) {
                try {
                    filterDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(lastPoint);
                } catch (ParseException e2) {
                    System.out.println("1");
                }
            }
        }
        System.out.println("currentDate" + currentDate + " filterDate: " + filterDate + " series: " + series);

        while (filterDate.getTime() > currentDate.getTime()) {
            XYChart.Data<LocalDateTime, Float> data = (XYChart.Data<LocalDateTime, Float>) series.getData().get(0);
            pointsList.add(data);

            series.getData().remove(0);
            try {
                currentDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(String.valueOf(data.getXValue()));
            } catch (ParseException e1) {
                try {
                    currentDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(String.valueOf(data.getXValue()));
                } catch (ParseException e2) {
                    System.out.println("2");
                }
            }
        }
    }

    static void addPointsAtBack(XYChart.Series series, List pointsList, String lastPoint){
        Date currentDate = new Date();
        Date filterDate = new Date();
        if (pointsList.size() == 0)return;
        try{
            filterDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse(lastPoint);
            currentDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(String.valueOf(((XYChart.Data<LocalDateTime, Float>) pointsList.get(pointsList.size() - 1)).getXValue()));
        }catch (ParseException e){
            try {
                filterDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(lastPoint);
            } catch (ParseException e1) {
                try {
                    filterDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(lastPoint);
                } catch (ParseException e2) {
                    System.out.println("3");
                }
            }
        }
        while (filterDate.getTime() < currentDate.getTime()) {

            XYChart.Data<LocalDateTime, Float> backData = (XYChart.Data< LocalDateTime, Float>) pointsList.get(pointsList.size() - 1);
            pointsList.remove(backData);
            String dateBack = String.valueOf(backData.getXValue());
            Float valueBack = backData.getYValue();

            series.getData().add(0, new XYChart.Data(backData.getXValue(), valueBack));
            try {
                currentDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(String.valueOf(backData.getXValue()));
            } catch (ParseException e1) {
                try {
                    currentDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(String.valueOf(backData.getXValue()));
                } catch (ParseException e2) {
                    System.out.println("4");
                }
            }
        }
    }

    static void removePointsAtFront(XYChart.Series series, List pointsList, String lastPoint){
        Date currentDate = new Date();
        Date filterDate = new Date();
        if (series.getData().size() == 0)return;

        try{
            filterDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse(lastPoint);
            currentDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(String.valueOf(((XYChart.Data<LocalDateTime, Float>) series.getData().get(series.getData().size() - 1)).getXValue()));
        }catch (ParseException e){
            try {
                filterDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(lastPoint);
            } catch (ParseException e1) {
                try {
                    filterDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(lastPoint);
                } catch (ParseException e2) {
                    System.out.println("5");
                }
            }
        }

        while (filterDate.getTime() < currentDate.getTime()) {
            XYChart.Data<LocalDateTime, Float> data = (XYChart.Data<LocalDateTime, Float>) series.getData().get(series.getData().size() - 1);
            pointsList.add(data);
            series.getData().remove(data);
            try {
                currentDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(String.valueOf(data.getXValue()));
            } catch (ParseException e1) {
                try {
                    currentDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(String.valueOf(data.getXValue()));
                } catch (ParseException e2) {
                    System.out.println("6");
                }
            }
        }
    }

    static void addPointsAtFront(XYChart.Series series, List pointsList, String lastPoint){
        Date currentDate = new Date();
        Date filterDate = new Date();
        if (pointsList.size() == 0)return;

        try{
            filterDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse(lastPoint);
            currentDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(String.valueOf(((XYChart.Data<LocalDateTime, Float>) series.getData().get(series.getData().size() - 1)).getXValue()));
        }catch (ParseException e){
            try {
                filterDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(lastPoint);
            } catch (ParseException e1) {
                try {
                    filterDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(lastPoint);
                } catch (ParseException e2) {
                    System.out.println("7");
                }
            }
        }

        while (filterDate.getTime() > currentDate.getTime()) {

            XYChart.Data<LocalDateTime, Float> frontData = (XYChart.Data<LocalDateTime, Float>)pointsList.get(pointsList.size() - 1);
            pointsList.remove(frontData);
            String dateFront = String.valueOf(frontData.getXValue());
            Float valueFront = frontData.getYValue();
            series.getData().add(new XYChart.Data(frontData.getXValue(), valueFront));
            try {
                currentDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(String.valueOf(frontData.getXValue()));
            } catch (ParseException e1) {
                try {
                    currentDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(String.valueOf(frontData.getXValue()));
                } catch (ParseException e2) {
                    System.out.println("8");
                }
            }
        }
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
}
