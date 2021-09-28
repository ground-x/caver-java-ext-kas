/*
 * KIP-7 API
 * # Introduction The KIP-17 API helps BApp (Blockchain Application) developers to manage contracts and tokens created in accordance with the [KIP-7](https://docs.klaytnapi.com/v/en/api#kip-7-api) standard, which is Klaytn's technical speficication for Fungible Tokens.  The functionality of the multiple endpoints enables you to do the following actions: - deploy smart contracts - manage the entire life cycle of an FT from minting, to sending and burning - get contract or token data - authorize a third party to execute token transfers - send tokens on behalf of the owner   For more details on KAS, please refer to [KAS Docs](https://docs.klaytnapi.com/). If you have any questions or comments, please leave them in the [Klaytn Developers Forum](http://forum.klaytn.com).    **alias**  When a method of the KIP-17 API requires a contract address, you can use the contract **alias**. You can give the contract an alias when deploying, and use it in place of the complicated address.  **deployer**  When you create a contract, you will be assigned one `deployer` address per Credential, which is the account address used for managing contracts. In KIP-7 API, this address is used in many different token-related operations. You can find the `deployer` address with [KIP7Deployer](#operation/GetDefaultDeployer).  Even with contracts created using SDKs like \"caver\", you can still use the contract address and [Wallet API](https://refs.klaytnapi.com/en/wallet/latest) account to manage your contracts and tokens.  # Fee Payer Options  KAS KIP-17 supports four ways to pay the transaction fees.<br />  **1. Only using KAS Global FeePayer Account** <br /> Sends all transactions using KAS Global FeePayer Account. ``` {     \"options\": {       \"enableGlobalFeePayer\": true     } } ```  <br />  **2. Using User FeePayer Account** <br /> Sends all transactions using User FeePayer Account. ``` {   \"options\": {     \"enableGlobalFeePayer\": false,     \"userFeePayer\": {       \"krn\": \"krn:1001:wallet:20bab367-141b-439a-8b4c-ae8788b86316:feepayer-pool:default\",       \"address\": \"0xd6905b98E4Ba43a24E842d2b66c1410173791cab\"     }   } } ```  <br />  **3. Using both KAS Global FeePayer Account + User FeePayer Account** <br /> Sends transactions using User FeePayer Account by default, and switches to the KAS Global FeePayer Account when balances are insufficient. ``` {   \"options\": {     \"enableGlobalFeePayer\": true,     \"userFeePayer\": {       \"krn\": \"krn:1001:wallet:20bab367-141b-439a-8b4c-ae8788b86316:feepayer-pool:default\",       \"address\": \"0xd6905b98E4Ba43a24E842d2b66c1410173791cab\"     }   } } ```  <br />  **4. Not using FeePayer Account** <br /> Sends transactions the default way, paying the transaction fee from the user's account. ``` {   \"options\": {     \"enableGlobalFeePayer\": false   } } ```  # Error Code This section contains the errors that might occur when using the KIP-17 API. KAS uses HTTP status codes. More details can be found in this [link](https://developer.mozilla.org/en/docs/Web/HTTP/Status). 
 *
 * OpenAPI spec version: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.kip7.model;

import java.util.Objects;
import java.util.Arrays;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.IOException;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.kip7.model.Kip7FeePayerOptionResponse;
/**
 * Kip7DeployResponse
 */


public class Kip7DeployResponse {
  @SerializedName("status")
  private String status = null;

  @SerializedName("transactionHash")
  private String transactionHash = null;

  @SerializedName("options")
  private Kip7FeePayerOptionResponse options = null;

  public Kip7DeployResponse status(String status) {
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

  public Kip7DeployResponse transactionHash(String transactionHash) {
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

  public Kip7DeployResponse options(Kip7FeePayerOptionResponse options) {
    this.options = options;
    return this;
  }

   /**
   * Get options
   * @return options
  **/
  @Schema(description = "")
  public Kip7FeePayerOptionResponse getOptions() {
    return options;
  }

  public void setOptions(Kip7FeePayerOptionResponse options) {
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
    Kip7DeployResponse kip7DeployResponse = (Kip7DeployResponse) o;
    return Objects.equals(this.status, kip7DeployResponse.status) &&
        Objects.equals(this.transactionHash, kip7DeployResponse.transactionHash) &&
        Objects.equals(this.options, kip7DeployResponse.options);
  }

  @Override
  public int hashCode() {
    return Objects.hash(status, transactionHash, options);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Kip7DeployResponse {\n");
    
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    transactionHash: ").append(toIndentedString(transactionHash)).append("\n");
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
