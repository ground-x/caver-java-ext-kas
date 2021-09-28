/*
 * KIP-17 API
 * # Introduction The KIP-17 API helps BApp (Blockchain Application) developers to manage contracts and tokens created in accordance with the [KIP-17](https://docs.klaytnapi.com/v/en/api#kip-17-api) standard, which is Klaytn's technical speficication for Non-Fungible Tokens.  The functionality of the multiple endpoints enables you to do the following actions: - deploy smart contracts - manage the entire life cycle of an NFT from minting, to sending and burning - get contract or token data - authorize a third party to execute token transfers - view token ownership history  For more details on KAS, please refer to [KAS Docs](https://docs.klaytnapi.com/). If you have any questions or comments, please leave them in the [Klaytn Developers Forum](http://forum.klaytn.com).    **alias**  When a method of the KIP-17 API requires a contract address, you can use the contract **alias**. You can give the contract an alias when deploying, and use it in place of the complicated address.  # Fee Payer Options KAS KIP-17 supports four ways to pay the transaction fees.<br />  **1. Only using KAS Global FeePayer Account** <br /> Sends all transactions using KAS Global FeePayer Account. ``` {     \"options\": {       \"enableGlobalFeePayer\": true     } } ``` <br />  **2. Using User FeePayer Account** <br /> Sends all transactions using User FeePayer Account. ``` {   \"options\": {     \"enableGlobalFeePayer\": false,     \"userFeePayer\": {       \"krn\": \"krn:1001:wallet:20bab367-141b-439a-8b4c-ae8788b86316:feepayer-pool:default\",       \"address\": \"0xd6905b98E4Ba43a24E842d2b66c1410173791cab\"     }   } } ``` <br />  **3. Using both KAS Global FeePayer Account + User FeePayer Account** <br /> Sends transactions using User FeePayer Account by default, and switches to the KAS Global FeePayer Account when balances are insufficient. ``` {   \"options\": {     \"enableGlobalFeePayer\": true,     \"userFeePayer\": {       \"krn\": \"krn:1001:wallet:20bab367-141b-439a-8b4c-ae8788b86316:feepayer-pool:default\",       \"address\": \"0xd6905b98E4Ba43a24E842d2b66c1410173791cab\"     }   } } ``` <br />  **4. Not using FeePayer Account** <br /> Sends transactions the default way, paying the transaction fee from the user's account. ``` {   \"options\": {     \"enableGlobalFeePayer\": false   } } ``` <br />  # Error Code This section contains the errors that might occur when using the KIP-17 API. KAS uses HTTP status codes. More details can be found in this [link](https://developer.mozilla.org/en/docs/Web/HTTP/Status).
 *
 * OpenAPI spec version: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.kip17.model;

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
 * GetKip17TokenHistoryResponseItem
 */


public class GetKip17TokenHistoryResponseItem {
  @SerializedName("from")
  private String from = null;

  @SerializedName("timestamp")
  private Long timestamp = null;

  @SerializedName("to")
  private String to = null;

  public GetKip17TokenHistoryResponseItem from(String from) {
    this.from = from;
    return this;
  }

   /**
   * The Klaytn account address of the sender.
   * @return from
  **/
  @Schema(example = "1067449575765241055731183944616801253628430668484", required = true, description = "The Klaytn account address of the sender.")
  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public GetKip17TokenHistoryResponseItem timestamp(Long timestamp) {
    this.timestamp = timestamp;
    return this;
  }

   /**
   * The UNIX timestamp of when the ownership changed.
   * @return timestamp
  **/
  @Schema(example = "1607391306", required = true, description = "The UNIX timestamp of when the ownership changed.")
  public Long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Long timestamp) {
    this.timestamp = timestamp;
  }

  public GetKip17TokenHistoryResponseItem to(String to) {
    this.to = to;
    return this;
  }

   /**
   * The Klaytn account address of the recipient.
   * @return to
  **/
  @Schema(example = "996123408997742546708948201890252767073899297426", required = true, description = "The Klaytn account address of the recipient.")
  public String getTo() {
    return to;
  }

  public void setTo(String to) {
    this.to = to;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetKip17TokenHistoryResponseItem getKip17TokenHistoryResponseItem = (GetKip17TokenHistoryResponseItem) o;
    return Objects.equals(this.from, getKip17TokenHistoryResponseItem.from) &&
        Objects.equals(this.timestamp, getKip17TokenHistoryResponseItem.timestamp) &&
        Objects.equals(this.to, getKip17TokenHistoryResponseItem.to);
  }

  @Override
  public int hashCode() {
    return Objects.hash(from, timestamp, to);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetKip17TokenHistoryResponseItem {\n");
    
    sb.append("    from: ").append(toIndentedString(from)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    to: ").append(toIndentedString(to)).append("\n");
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
