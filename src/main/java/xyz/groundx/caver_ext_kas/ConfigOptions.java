package xyz.groundx.caver_ext_kas;

/**
 * The class represented that provides various options for initializing CaverExtKAS class.
 */
public class ConfigOptions {

    /**
     * The Node API's option to choose provider whether Http or Websocket.
     */
    boolean useNodeAPIWithHttp;

    /**
     * Creates a CaverExtKAS instance.
     */
    public ConfigOptions() {

    }

    /**
     * Getter for useNodeAPIWithHttp.
     * @return boolean
     */
    public boolean getUseNodeAPIWithHttp() {
        return useNodeAPIWithHttp;
    }

    /**
     * Setter for useNodeAPIWithHttp.
     * @param useNodeAPIWithHttp The Node API's option to choose provider whether Http or Websocket.
     */
    public void setUseNodeAPIWithHttp(boolean useNodeAPIWithHttp) {
        this.useNodeAPIWithHttp = useNodeAPIWithHttp;
    }
}
