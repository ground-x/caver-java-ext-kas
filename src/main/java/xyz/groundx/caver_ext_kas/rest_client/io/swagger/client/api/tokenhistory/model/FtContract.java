/*
 * Token History API
 * # Introduction  Token History API allows users to search for information and transfer records on KLAY, FT (KIP-7, Labeled ERC-20), and NFT (KIP-17, Labeled ERC-721) tokens. You can use Token History API to check the records of a specific EOA transferring KLAY, retrieve NFT information, or other purposes.  For more details on Token History API, refer to our [tutorial](https://klaytn.com).  For any questions regarding this document or KAS, visit [the developer forum](https://forum.klaytn.com/).  # Authentication  <!-- ReDoc-Inject: <security-definitions> -->
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
 * FtContract
 */


public class FtContract {
  @SerializedName("address")
  private String address = null;

  @SerializedName("decimals")
  private Long decimals = null;

  @SerializedName("name")
  private String name = null;

  @SerializedName("symbol")
  private String symbol = null;

  @SerializedName("status")
  private String status = null;

  public FtContract address(String address) {
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

  public FtContract decimals(Long decimals) {
    this.decimals = decimals;
    return this;
  }

   /**
   * Number of token digits
   * @return decimals
  **/
  @Schema(example = "8", required = true, description = "Number of token digits")
  public Long getDecimals() {
    return decimals;
  }

  public void setDecimals(Long decimals) {
    this.decimals = decimals;
  }

  public FtContract name(String name) {
    this.name = name;
    return this;
  }

   /**
   * Token name
   * @return name
  **/
  @Schema(example = "Test Coin", required = true, description = "Token name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public FtContract symbol(String symbol) {
    this.symbol = symbol;
    return this;
  }

   /**
   * Token symbol
   * @return symbol
  **/
  @Schema(example = "TSTC", required = true, description = "Token symbol")
  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public FtContract status(String status) {
    this.status = status;
    return this;
  }

   /**
   * Contract labeling status (completed, processing, failed, cancelled)
   * @return status
  **/
  @Schema(example = "completed", required = true, description = "Contract labeling status (completed, processing, failed, cancelled)")
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FtContract ftContract = (FtContract) o;
    return Objects.equals(this.address, ftContract.address) &&
        Objects.equals(this.decimals, ftContract.decimals) &&
        Objects.equals(this.name, ftContract.name) &&
        Objects.equals(this.symbol, ftContract.symbol) &&
        Objects.equals(this.status, ftContract.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(address, decimals, name, symbol, status);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FtContract {\n");
    
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    decimals: ").append(toIndentedString(decimals)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    symbol: ").append(toIndentedString(symbol)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
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
