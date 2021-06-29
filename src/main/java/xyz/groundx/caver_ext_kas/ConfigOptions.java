/*
 * Copyright 2021 The caver-java-ext-kas Authors
 *
 * Licensed under the Apache License, Version 2.0 (the “License”);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an “AS IS” BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
