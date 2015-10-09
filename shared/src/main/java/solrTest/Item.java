package solrTest;

public class Item {
    private String id;
    private String date;
    private String open;
    private String high;
    private String low;
    private String close;
    private String volume;
    private String adj_close;
    private String spike;
    private String series;

    public void setSeries(String series) { this.series = series;}

    public void setId(String id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public void setAdj_close(String adj_close) {
        this.adj_close = adj_close;
    }

    public void setSpike(String spike) {
        this.spike = spike;
    }

    public String getId() {

        return id;
    }

    public String getDate() {
        return date;
    }

    public String getSeries(){ return series;}

    public String getOpen() {
        return open;
    }

    public String getHigh() {
        return high;
    }

    public String getLow() {
        return low;
    }

    public String getClose() {
        return close;
    }

    public String getVolume() {
        return volume;
    }

    public String getAdj_close() {
        return adj_close;
    }

    public String getSpike(){ return  spike; }

}
