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

import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;
/**
 * PageableTokenSummary
 */


public class PageableTokenSummary {
  @SerializedName("items")
  private List<AnyOfPageableTokenSummaryItems> items = new ArrayList<AnyOfPageableTokenSummaryItems>();

  @SerializedName("cursor")
  private String cursor = null;

  public PageableTokenSummary items(List<AnyOfPageableTokenSummaryItems> items) {
    this.items = items;
    return this;
  }

  public PageableTokenSummary addItemsItem(AnyOfPageableTokenSummaryItems itemsItem) {
    this.items.add(itemsItem);
    return this;
  }

   /**
   * Get items
   * @return items
  **/
  @Schema(required = true, description = "")
  public List<AnyOfPageableTokenSummaryItems> getItems() {
    return items;
  }

  public void setItems(List<AnyOfPageableTokenSummaryItems> items) {
    this.items = items;
  }

  public PageableTokenSummary cursor(String cursor) {
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
    PageableTokenSummary pageableTokenSummary = (PageableTokenSummary) o;
    return Objects.equals(this.items, pageableTokenSummary.items) &&
        Objects.equals(this.cursor, pageableTokenSummary.cursor);
  }

  @Override
  public int hashCode() {
    return Objects.hash(items, cursor);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PageableTokenSummary {\n");
    
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
