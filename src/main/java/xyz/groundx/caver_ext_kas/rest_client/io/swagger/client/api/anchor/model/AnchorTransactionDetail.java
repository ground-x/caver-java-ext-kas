/*
 * Anchor API
 * # Introduction This document describes KAS (Klaytn API Service) Anchor API. Anchor API provides features sending metadata available to verify data reliability to ensure the reliability of service chain data to Klaytn main chain.  For more details on using the Anchor API, please refer to [Tutorial](https://docs.klaytnapi.com/tutorial/anchor-api).    # Error Codes  ## 400: Bad Request   | Code | Messages |   | --- | --- |   | 1071010 | data don't exist 1071615 | its value is out of range; size 1072100 | same payload ID or payload was already anchored 1072101 | all configured accounts have insufficient funds |  
 *
 * OpenAPI spec version: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.anchor.model;

import java.util.Objects;
import java.util.Arrays;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.IOException;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.anchor.model.AnchorBlockPayload;
/**
 * Anchoring transaction information
 */
@Schema(description = "Anchoring transaction information")

public class AnchorTransactionDetail {
  @SerializedName("payload")
  private AnchorBlockPayload payload = null;

  @SerializedName("transactionHash")
  private String transactionHash = null;

  public AnchorTransactionDetail payload(AnchorBlockPayload payload) {
    this.payload = payload;
    return this;
  }

   /**
   * Get payload
   * @return payload
  **/
  @Schema(required = true, description = "")
  public AnchorBlockPayload getPayload() {
    return payload;
  }

  public void setPayload(AnchorBlockPayload payload) {
    this.payload = payload;
  }

  public AnchorTransactionDetail transactionHash(String transactionHash) {
    this.transactionHash = transactionHash;
    return this;
  }

   /**
   * Transaction hash of anchoring transactions
   * @return transactionHash
  **/
  @Schema(example = "0x4b8ab38728123ead97e4e8a304cb99f6dc1c78470e86020207ffa949005eb831", required = true, description = "Transaction hash of anchoring transactions")
  public String getTransactionHash() {
    return transactionHash;
  }

  public void setTransactionHash(String transactionHash) {
    this.transactionHash = transactionHash;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AnchorTransactionDetail anchorTransactionDetail = (AnchorTransactionDetail) o;
    return Objects.equals(this.payload, anchorTransactionDetail.payload) &&
        Objects.equals(this.transactionHash, anchorTransactionDetail.transactionHash);
  }

  @Override
  public int hashCode() {
    return Objects.hash(payload, transactionHash);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AnchorTransactionDetail {\n");
    
    sb.append("    payload: ").append(toIndentedString(payload)).append("\n");
    sb.append("    transactionHash: ").append(toIndentedString(transactionHash)).append("\n");
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
