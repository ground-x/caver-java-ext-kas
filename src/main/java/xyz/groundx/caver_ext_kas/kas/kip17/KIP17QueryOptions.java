package xyz.groundx.caver_ext_kas.kas.kip17;

/**
 * Representing a query parameters where using KIP17 REST API.
 */
public class KIP17QueryOptions {

    /**
     * Maximum number of data to query.
     */
    Long size;

    /**
     * Information of the last retrieved cursor.
     */
    String cursor;

    /**
     * Creates a KIP17QueryOptions instance
     */
    public KIP17QueryOptions() {
    }

    /**
     * Creates a KIP17QueryOptions instance
     * @param size Maximum number of data to query.
     * @param cursor Information of the last retrieved cursor.
     */
    public KIP17QueryOptions(Long size, String cursor) {
        this.size = size;
        this.cursor = cursor;
    }

    /**
     * Getter function for size.
     * @return Long
     */
    public Long getSize() {
        return size;
    }

    /**
     * Setter function for size
     * @param size Maximum number of data to query.
     */
    public void setSize(Long size) {
        this.size = size;
    }

    /**
     * Getter function for cursor.
     * @return String
     */
    public String getCursor() {
        return cursor;
    }

    /**
     * Setter function for cursor
     * @param cursor Information of the last retrieved cursor.
     */
    public void setCursor(String cursor) {
        this.cursor = cursor;
    }
}
