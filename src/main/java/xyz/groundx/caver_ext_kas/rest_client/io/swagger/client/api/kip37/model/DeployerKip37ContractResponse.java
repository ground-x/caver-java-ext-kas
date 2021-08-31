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
 * DeployerKip37ContractResponse
 */


public class DeployerKip37ContractResponse {
  @SerializedName("status")
  private String status = null;

  @SerializedName("transactionHash")
  private String transactionHash = null;

  @SerializedName("uri")
  private String uri = null;

  @SerializedName("options")
  private Kip37FeePayerOption options = null;

  public DeployerKip37ContractResponse status(String status) {
    this.status = status;
    return this;
  }

   /**
   * Transaction status (&#x60;Submitted&#x60;, &#x60;Pending&#x60;)
   * @return status
  **/
  @Schema(required = true, description = "Transaction status (`Submitted`, `Pending`)")
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public DeployerKip37ContractResponse transactionHash(String transactionHash) {
    this.transactionHash = transactionHash;
    return this;
  }

   /**
   * Transaction hash
   * @return transactionHash
  **/
  @Schema(required = true, description = "Transaction hash")
  public String getTransactionHash() {
    return transactionHash;
  }

  public void setTransactionHash(String transactionHash) {
    this.transactionHash = transactionHash;
  }

  public DeployerKip37ContractResponse uri(String uri) {
    this.uri = uri;
    return this;
  }

   /**
   * Contract URI
   * @return uri
  **/
  @Schema(required = true, description = "Contract URI")
  public String getUri() {
    return uri;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }

  public DeployerKip37ContractResponse options(Kip37FeePayerOption options) {
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
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeployerKip37ContractResponse deployerKip37ContractResponse = (DeployerKip37ContractResponse) o;
    return Objects.equals(this.status, deployerKip37ContractResponse.status) &&
        Objects.equals(this.transactionHash, deployerKip37ContractResponse.transactionHash) &&
        Objects.equals(this.uri, deployerKip37ContractResponse.uri) &&
        Objects.equals(this.options, deployerKip37ContractResponse.options);
  }

  @Override
  public int hashCode() {
    return Objects.hash(status, transactionHash, uri, options);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DeployerKip37ContractResponse {\n");
    
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    transactionHash: ").append(toIndentedString(transactionHash)).append("\n");
    sb.append("    uri: ").append(toIndentedString(uri)).append("\n");
    sb.append("    options: ").append(toIndentedString(options)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}
