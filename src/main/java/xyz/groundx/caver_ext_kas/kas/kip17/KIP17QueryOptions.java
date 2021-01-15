package xyz.groundx.caver_ext_kas.kas.kip17;

public class KIP17QueryOptions {

    Long size;
    String cursor;

    public KIP17QueryOptions() {
    }

    public KIP17QueryOptions(Long size, String cursor) {
        this.size = size;
        this.cursor = cursor;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }
}
