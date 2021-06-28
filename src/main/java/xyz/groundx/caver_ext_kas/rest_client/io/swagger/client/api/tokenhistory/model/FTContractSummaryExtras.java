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
/**
 * FTContractSummaryExtras
 */


public class FTContractSummaryExtras {
  @SerializedName("name")
  private String name = null;

  @SerializedName("decimals")
  private Long decimals = null;

  @SerializedName("symbol")
  private String symbol = null;

  @SerializedName("totalSupply")
  private String totalSupply = null;

  @SerializedName("formattedValue")
  private String formattedValue = null;

  public FTContractSummaryExtras name(String name) {
    this.name = name;
    return this;
  }

   /**
   * Token name
   * @return name
  **/
  @Schema(example = "KAS FCoin", description = "Token name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public FTContractSummaryExtras decimals(Long decimals) {
    this.decimals = decimals;
    return this;
  }

   /**
   * Token digits
   * @return decimals
  **/
  @Schema(example = "8", description = "Token digits")
  public Long getDecimals() {
    return decimals;
  }

  public void setDecimals(Long decimals) {
    this.decimals = decimals;
  }

  public FTContractSummaryExtras symbol(String symbol) {
    this.symbol = symbol;
    return this;
  }

   /**
   * Token symbols
   * @return symbol
  **/
  @Schema(example = "KFC", description = "Token symbols")
  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public FTContractSummaryExtras totalSupply(String totalSupply) {
    this.totalSupply = totalSupply;
    return this;
  }

   /**
   * Total issued amount (in hexadecimal)
   * @return totalSupply
  **/
  @Schema(example = "0xde0b6b3a7640000", description = "Total issued amount (in hexadecimal)")
  public String getTotalSupply() {
    return totalSupply;
  }

  public void setTotalSupply(String totalSupply) {
    this.totalSupply = totalSupply;
  }

  public FTContractSummaryExtras formattedValue(String formattedValue) {
    this.formattedValue = formattedValue;
    return this;
  }

   /**
   * Formatted value with contracts &#x60;decimals&#x60;
   * @return formattedValue
  **/
  @Schema(example = "0.00000000000000035", description = "Formatted value with contracts `decimals`")
  public String getFormattedValue() {
    return formattedValue;
  }

  public void setFormattedValue(String formattedValue) {
    this.formattedValue = formattedValue;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FTContractSummaryExtras ftContractSummaryExtras = (FTContractSummaryExtras) o;
    return Objects.equals(this.name, ftContractSummaryExtras.name) &&
        Objects.equals(this.decimals, ftContractSummaryExtras.decimals) &&
        Objects.equals(this.symbol, ftContractSummaryExtras.symbol) &&
        Objects.equals(this.totalSupply, ftContractSummaryExtras.totalSupply) &&
        Objects.equals(this.formattedValue, ftContractSummaryExtras.formattedValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, decimals, symbol, totalSupply, formattedValue);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FTContractSummaryExtras {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    decimals: ").append(toIndentedString(decimals)).append("\n");
    sb.append("    symbol: ").append(toIndentedString(symbol)).append("\n");
    sb.append("    totalSupply: ").append(toIndentedString(totalSupply)).append("\n");
    sb.append("    formattedValue: ").append(toIndentedString(formattedValue)).append("\n");
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
