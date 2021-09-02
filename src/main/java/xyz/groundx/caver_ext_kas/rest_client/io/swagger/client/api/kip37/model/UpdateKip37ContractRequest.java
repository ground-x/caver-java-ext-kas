/*
 * KIP-37 API
 * ## Introduction The KIP-37 API helps Blockchain app (BApp) developers to easily deploy smart contracts and send tokens of the [KIP-37 Multi Token Standard](https://kips.klaytn.com/KIPs/kip-37).  You can use the default contract managing account (`deployer`) and `alias`.    You can also manage the contracts and tokens created on the klaytn network using the caver SDK, using contract address and the [Wallet API](https://refs.klaytnapi.com/ko/wallet/latest) account.    ## Error Codes  ### 400: Bad Request  | Code    | Message                                      | |---------|----------------------------------------------| | 1160050 | incorrect request                            | | 1160251 | its value is out of range                    | | 1164000 | invalid alias format                         | | 1164001 | invalid address                              | | 1164002 | invalid hex format                           | | 1164004 | account not found in wallet account-pool     | | 1164005 | batch items mismatch                         | | 1164006 | too many batch items                         | | 1164007 | invalid krn                                  | | 1164008 | no contract code                             | | 1164009 | insufficient balance                         | | 1164011 | fee payer not found in wallet feepayer-pool  |   ### 403: Forbidden  | Code    | Message                          | |---------|----------------------------------| | 1164300 | insufficient account permissions |   ### 404: Not Found  | Code    | Message            | |---------|--------------------| | 1164400 | contract not found | | 1164401 | token not found    |  ### 409: Conflict  | Code    | Message                   | |---------|---------------------------| | 1164900 | duplicate alias           | | 1164901 | contract already paused   | | 1164902 | contract already unpaused | | 1164903 | token already exist       | | 1164904 | contract already paused   | | 1164905 | token already unpaused    | | 1164906 | already approved          | | 1164907 | already not approved      | | 1164908 | duplicate contract        | | 1164909 | contract being created    |   ### 503: Service Unavailable  | Code    | Message                   | |---------|---------------------------| | 1165100 | internal server error     |   ## Fee Payer Options  KAS KIP-37 supports four scenarios for paying transactin fees:      **1. Using only KAS Global FeePayer Account**   Sends all transactions using the KAS global FeePayer Account.       ``` {     \"options\": {       \"enableGlobalFeePayer\": true     }     } ```    <br />    **2. Using User FeePayer account**   Sends all transactions using the KAS User FeePayer Account.      ``` {   \"options\": {     \"enableGlobalFeePayer\": false,     \"userFeePayer\": {       \"krn\": \"krn:1001:wallet:20bab367-141b-439a-8b4c-ae8788b86316:feepayer-pool:default\",       \"address\": \"0xd6905b98E4Ba43a24E842d2b66c1410173791cab\"     }   } } ```    <br />  **3. Using both KAS Global FeePayer Account + User FeePayer Account**   Uses User FeePayer Account as default. When the balance runs out, KAS Global FeePayer Account will be used.     ``` {   \"options\": {     \"enableGlobalFeePayer\": true,     \"userFeePayer\": {       \"krn\": \"krn:1001:wallet:20bab367-141b-439a-8b4c-ae8788b86316:feepayer-pool:default\",       \"address\": \"0xd6905b98E4Ba43a24E842d2b66c1410173791cab\"     }   } } ```    <br />  **4. Not using FeePayer Account**   Sends a transaction via normal means where the sender pays the transaction fee.       ``` {   \"options\": {     \"enableGlobalFeePayer\": false   } } ``` 
 *
 * OpenAPI spec version: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.kip37.model;

import java.util.Objects;
import java.util.Arrays;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.IOException;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.kip37.model.Kip37FeePayerOption;
/**
 * UpdateKip37ContractRequest
 */


public class UpdateKip37ContractRequest {
  @SerializedName("options")
  private Kip37FeePayerOption options = null;

  public UpdateKip37ContractRequest options(Kip37FeePayerOption options) {
    this.options = options;
    return this;
  }

   /**
   * Get options
   * @return options
  **/
  @Schema(description = "")
  public Kip37FeePayerOption getOptions() {
    return options;
  }

  public void setOptions(Kip37FeePayerOption options) {
    this.options = options;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UpdateKip37ContractRequest updateKip37ContractRequest = (UpdateKip37ContractRequest) o;
    return Objects.equals(this.options, updateKip37ContractRequest.options);
  }

  @Override
  public int hashCode() {
    return Objects.hash(options);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UpdateKip37ContractRequest {\n");
    
    sb.append("    options: ").append(toIndentedString(options)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}
