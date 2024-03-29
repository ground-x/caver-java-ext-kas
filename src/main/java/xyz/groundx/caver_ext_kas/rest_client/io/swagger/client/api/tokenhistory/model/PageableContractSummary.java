/*
 * Token History API
 * # Introduction  Token History API allows you to query the transaction history of KLAY, FTs (KIP-7 and Labelled ERC-20), NFTs (KIP-17 and Labelled ERC-721), and MTs (KIP-37 and Labelled ERC-1155). You can track KLAY's transaction history or retrieve NFT-related data of a certain EOA.   For more details on using Token History API, please refer to the [Tutorial](https://docs.klaytnapi.com/tutorial).   For any inquiries on this document or KAS in general, please visit [Developer Forum](https://forum.klaytn.com/).  
 *
 * OpenAPI spec version: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.tokenhistory.model;

import java.util.Objects;
import java.util.Arrays;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.IOException;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.tokenhistory.model.ContractSummaryArray;
/**
 * PageableContractSummary
 */


public class PageableContractSummary {
  @SerializedName("items")
  private ContractSummaryArray items = null;

  @SerializedName("cursor")
  private String cursor = null;

  public PageableContractSummary items(ContractSummaryArray items) {
    this.items = items;
    return this;
  }

   /**
   * Get items
   * @return items
  **/
  @Schema(required = true, description = "")
  public ContractSummaryArray getItems() {
    return items;
  }

  public void setItems(ContractSummaryArray items) {
    this.items = items;
  }

  public PageableContractSummary cursor(String cursor) {
    this.cursor = cursor;
    return this;
  }

   /**
   * Next page cursor
   * @return cursor
  **/
  @Schema(example = "z2o87adeLbW4Aqm53gpq6VbGZg3JmE5vodrwD9XKmY5vMl4Gkw9PZO1NoBpV8LR83y0Edb3Aar7eKQqzJWDg6X2xOe1P27l4kzY0xQa8LNABMWv0VJQ6MpNlr9O1xBDE", required = true, description = "Next page cursor")
  public String getCursor() {
    return cursor;
  }

  public void setCursor(String cursor) {
    this.cursor = cursor;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PageableContractSummary pageableContractSummary = (PageableContractSummary) o;
    return Objects.equals(this.items, pageableContractSummary.items) &&
        Objects.equals(this.cursor, pageableContractSummary.cursor);
  }

  @Override
  public int hashCode() {
    return Objects.hash(items, cursor);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PageableContractSummary {\n");
    
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
    sb.append("    cursor: ").append(toIndentedString(cursor)).append("\n");
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
