/*
 * Token History API
 * # Introduction  Token History API allows users to search for information and transfer records on KLAY, FT (KIP-7, Labeled ERC-20), and NFT (KIP-17, Labeled ERC-721) tokens. You can use Token History API to check the records of a specific EOA transferring KLAY, retrieve NFT information, or other purposes.  For more details on Token History API, refer to our [tutorial](https://klaytn.com).  For any questions regarding this document or KAS, visit [the developer forum](https://forum.klaytn.com/).  
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
 * AccountFT
 */


public class AccountFT {
  @SerializedName("address")
  private String address = null;

  @SerializedName("owner")
  private String owner = null;

  @SerializedName("balance")
  private String balance = null;

  @SerializedName("formattedValue")
  private String formattedValue = null;

  @SerializedName("decimals")
  private Long decimals = null;

  @SerializedName("name")
  private String name = null;

  @SerializedName("symbol")
  private String symbol = null;

  @SerializedName("totalSupply")
  private String totalSupply = null;

  @SerializedName("updatedAt")
  private Long updatedAt = null;

  public AccountFT address(String address) {
    this.address = address;
    return this;
  }

   /**
   * Contract address (20-byte)
   * @return address
  **/
  @Schema(example = "0x5e47b195eeb11d72f5e1d27aebb6d341f1a9bedb", required = true, description = "Contract address (20-byte)")
  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public AccountFT owner(String owner) {
    this.owner = owner;
    return this;
  }

   /**
   * EOA address (20-byte)
   * @return owner
  **/
  @Schema(example = "0x00ebd049eff96861d9fb3843fd0de79b08a3cc17", required = true, description = "EOA address (20-byte)")
  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public AccountFT balance(String balance) {
    this.balance = balance;
    return this;
  }

   /**
   * Token balance (hex)
   * @return balance
  **/
  @Schema(example = "0x15e", required = true, description = "Token balance (hex)")
  public String getBalance() {
    return balance;
  }

  public void setBalance(String balance) {
    this.balance = balance;
  }

  public AccountFT formattedValue(String formattedValue) {
    this.formattedValue = formattedValue;
    return this;
  }

   /**
   * Converted value using the decimals
   * @return formattedValue
  **/
  @Schema(example = "0.00000000000000035", required = true, description = "Converted value using the decimals")
  public String getFormattedValue() {
    return formattedValue;
  }

  public void setFormattedValue(String formattedValue) {
    this.formattedValue = formattedValue;
  }

  public AccountFT decimals(Long decimals) {
    this.decimals = decimals;
    return this;
  }

   /**
   * FT decimals
   * @return decimals
  **/
  @Schema(example = "8", required = true, description = "FT decimals")
  public Long getDecimals() {
    return decimals;
  }

  public void setDecimals(Long decimals) {
    this.decimals = decimals;
  }

  public AccountFT name(String name) {
    this.name = name;
    return this;
  }

   /**
   * FT name
   * @return name
  **/
  @Schema(example = "KASFT", required = true, description = "FT name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public AccountFT symbol(String symbol) {
    this.symbol = symbol;
    return this;
  }

   /**
   * FT symbol
   * @return symbol
  **/
  @Schema(example = "KFT", required = true, description = "FT symbol")
  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public AccountFT totalSupply(String totalSupply) {
    this.totalSupply = totalSupply;
    return this;
  }

   /**
   * FT total supply (hex)
   * @return totalSupply
  **/
  @Schema(example = "0x174876e800", required = true, description = "FT total supply (hex)")
  public String getTotalSupply() {
    return totalSupply;
  }

  public void setTotalSupply(String totalSupply) {
    this.totalSupply = totalSupply;
  }

  public AccountFT updatedAt(Long updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

   /**
   * UNIX timestamp of when the token information last changed
   * @return updatedAt
  **/
  @Schema(example = "1592180992", required = true, description = "UNIX timestamp of when the token information last changed")
  public Long getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Long updatedAt) {
    this.updatedAt = updatedAt;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AccountFT accountFT = (AccountFT) o;
    return Objects.equals(this.address, accountFT.address) &&
        Objects.equals(this.owner, accountFT.owner) &&
        Objects.equals(this.balance, accountFT.balance) &&
        Objects.equals(this.formattedValue, accountFT.formattedValue) &&
        Objects.equals(this.decimals, accountFT.decimals) &&
        Objects.equals(this.name, accountFT.name) &&
        Objects.equals(this.symbol, accountFT.symbol) &&
        Objects.equals(this.totalSupply, accountFT.totalSupply) &&
        Objects.equals(this.updatedAt, accountFT.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(address, owner, balance, formattedValue, decimals, name, symbol, totalSupply, updatedAt);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AccountFT {\n");
    
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    owner: ").append(toIndentedString(owner)).append("\n");
    sb.append("    balance: ").append(toIndentedString(balance)).append("\n");
    sb.append("    formattedValue: ").append(toIndentedString(formattedValue)).append("\n");
    sb.append("    decimals: ").append(toIndentedString(decimals)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    symbol: ").append(toIndentedString(symbol)).append("\n");
    sb.append("    totalSupply: ").append(toIndentedString(totalSupply)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
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
