package xyz.groundx.caver_ext_kas.kas.kip7;

import java.security.InvalidParameterException;

/**
 * Representing a query parameters where using KIP-7 REST API.
 */
public class KIP7QueryOptions {

    /**
     * Enum for "status" query option.
     */
    public enum STATUS_TYPE {
        ALL("all"), INIT("init"), SUBMITTED("submitted"), DEPLOYED("deployed");

        String status;

        STATUS_TYPE(String type) {
            this.status = type;
        }

        /**
         * Check if there is an enum mapped to the given status string.
         * @param kind The kind string to find enum defined in STATUS_TYPE
         * @return boolean
         */
        public static boolean isExist(String kind) {
            for(STATUS_TYPE statusType : STATUS_TYPE.values()) {
                if(statusType.getStatus().equals(kind)) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Gets all type options
         * @return String.
         */
        public static String getAllStatus() {
            String result = "";

            for(int i=0; i < STATUS_TYPE.values().length; i++) {
                STATUS_TYPE statusType = STATUS_TYPE.values()[i];
                result += "'" + statusType.getStatus() + "'";

                if(i != (STATUS_TYPE.values().length-1)) {
                    result += ", ";
                }
            }

            return result;
        }

        /**
         * Getter function for status
         * @return String
         */
        public String getStatus() {
            return status;
        }
    }

    /**
     * The maximum number of items to return.
     */
    Integer size;

    /**
     * Offset for the first item. You can query data after the given offset using the cursor value returned in the response.
     */
    String cursor;

    /**
     * The deploy status ("all", "init", "submitted", "deployed") for the contract you wish to query. <br>
     * You can only choose one deploy status.
     */
    String status;

    /**
     * Creates an KIP7QueryOptions instance.
     */
    public KIP7QueryOptions() {
    }

    /**
     * Getter function for size
     * @return Integer
     */
    public Integer getSize() {
        return size;
    }

    /**
     * Getter function for size.
     * @param size The maximum number of items to return.
     */
    public void setSize(Integer size) {
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
     * @param cursor Offset for the first item. You can query data after the given offset using the cursor value returned in the response.
     */
    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    /**
     * Getter function for status.
     * @return String
     */
    public String getStatus() {
        return status;
    }

    /**
     * Setter function for status
     * @param status The deploy status ("all", "init", "submitted", "deployed") for the contract you wish to query.
     */
    public void setStatus(String status) {
        if(!STATUS_TYPE.isExist(status)) {
            throw new InvalidParameterException("The type parameter have one of the following: [" + STATUS_TYPE.getAllStatus() + "]");
        }

        this.status = status;
    }

    /**
     * Setter function for status
     * @param status The STATUS_TYPE enum value.
     */
    public void setStatus(STATUS_TYPE status) {
        this.status = status.getStatus();
    }
}
