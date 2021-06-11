package xyz.groundx.caver_ext_kas.kas.kip7;

public class KIP7QueryOptions {
    Integer size;

    String cursor;

    String status;

    public KIP7QueryOptions() {
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
