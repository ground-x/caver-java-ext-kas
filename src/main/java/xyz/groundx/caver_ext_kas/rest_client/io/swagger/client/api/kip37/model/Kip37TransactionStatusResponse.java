/*
 * KIP-37 API
 * ## Introduction The KIP-37 API helps Blockchain app (BApp) developers to easily deploy smart contracts and send tokens of the [KIP-37 Multi Token Standard](https://kips.klaytn.com/KIPs/kip-37).  You can use the default contract managing account (`deployer`) and `alias`.    You can also manage the contracts and tokens created on the klaytn network using the caver SDK, using contract address and the [Wallet API](https://refs.klaytnapi.com/ko/wallet/latest) account.    ## Fee Payer Options  KAS KIP-37 supports four scenarios for paying transactin fees:      **1. Using only KAS Global FeePayer Account**   Sends all transactions using the KAS global FeePayer Account.       ``` {     \"options\": {       \"enableGlobalFeePayer\": true     }     } ```    <br />    **2. Using User FeePayer account**   Sends all transactions using the KAS User FeePayer Account.      ``` {   \"options\": {     \"enableGlobalFeePayer\": false,     \"userFeePayer\": {       \"krn\": \"krn:1001:wallet:20bab367-141b-439a-8b4c-ae8788b86316:feepayer-pool:default\",       \"address\": \"0xd6905b98E4Ba43a24E842d2b66c1410173791cab\"     }   } } ```    <br />  **3. Using both KAS Global FeePayer Account + User FeePayer Account**   Uses User FeePayer Account as default. When the balance runs out, KAS Global FeePayer Account will be used.     ``` {   \"options\": {     \"enableGlobalFeePayer\": true,     \"userFeePayer\": {       \"krn\": \"krn:1001:wallet:20bab367-141b-439a-8b4c-ae8788b86316:feepayer-pool:default\",       \"address\": \"0xd6905b98E4Ba43a24E842d2b66c1410173791cab\"     }   } } ```    <br />  **4. Not using FeePayer Account**   Sends a transaction via normal means where the sender pays the transaction fee.       ``` {   \"options\": {     \"enableGlobalFeePayer\": false   } } ``` 
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
/**
 * Kip37TransactionStatusResponse
 */


public class Kip37TransactionStatusResponse {
  @SerializedName("status")
  private String status = null;

  @SerializedName("transactionHash")
  private String transactionHash = null;

  @SerializedName("transactionId")
  private String transactionId = null;

  public Kip37TransactionStatusResponse status(String status) {
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

  public Kip37TransactionStatusResponse transactionHash(String transactionHash) {
    this.transactionHash = transactionHash;
    return this;
  }

   /**
   * Transaction hash
   * @return transactionHash
  **/
  @Schema(description = "Transaction hash")
  public String getTransactionHash() {
    return transactionHash;
  }

  public void setTransactionHash(String transactionHash) {
    this.transactionHash = transactionHash;
  }

  public Kip37TransactionStatusResponse transactionId(String transactionId) {
    this.transactionId = transactionId;
    return this;
  }

   /**
   * Multisig Transaction ID
   * @return transactionId
  **/
  @Schema(description = "Multisig Transaction ID")
  public String getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Kip37TransactionStatusResponse kip37TransactionStatusResponse = (Kip37TransactionStatusResponse) o;
    return Objects.equals(this.status, kip37TransactionStatusResponse.status) &&
        Objects.equals(this.transactionHash, kip37TransactionStatusResponse.transactionHash) &&
        Objects.equals(this.transactionId, kip37TransactionStatusResponse.transactionId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(status, transactionHash, transactionId);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Kip37TransactionStatusResponse {\n");
    
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    transactionHash: ").append(toIndentedString(transactionHash)).append("\n");
    sb.append("    transactionId: ").append(toIndentedString(transactionId)).append("\n");
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
