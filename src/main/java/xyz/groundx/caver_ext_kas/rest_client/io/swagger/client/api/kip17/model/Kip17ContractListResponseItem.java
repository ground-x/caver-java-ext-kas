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
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.kip17.model.Kip17FeePayerOptions;
/**
 * Kip17ContractListResponseItem
 */


public class Kip17ContractListResponseItem {
  @SerializedName("address")
  private String address = null;

  @SerializedName("alias")
  private String alias = null;

  @SerializedName("name")
  private String name = null;

  @SerializedName("options")
  private Kip17FeePayerOptions options = null;

  @SerializedName("symbol")
  private String symbol = null;

  public Kip17ContractListResponseItem address(String address) {
    this.address = address;
    return this;
  }

   /**
   * The contract address.
   * @return address
  **/
  @Schema(example = "297295353402086868010758942177261478453962615799", required = true, description = "The contract address.")
  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public Kip17ContractListResponseItem alias(String alias) {
    this.alias = alias;
    return this;
  }

   /**
   * The contract alias.
   * @return alias
  **/
  @Schema(example = "test", required = true, description = "The contract alias.")
  public String getAlias() {
    return alias;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }

  public Kip17ContractListResponseItem name(String name) {
    this.name = name;
    return this;
  }

   /**
   * The token name.
   * @return name
  **/
  @Schema(example = "TEST NFT", required = true, description = "The token name.")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Kip17ContractListResponseItem options(Kip17FeePayerOptions options) {
    this.options = options;
    return this;
  }

   /**
   * Get options
   * @return options
  **/
  @Schema(description = "")
  public Kip17FeePayerOptions getOptions() {
    return options;
  }

  public void setOptions(Kip17FeePayerOptions options) {
    this.options = options;
  }

  public Kip17ContractListResponseItem symbol(String symbol) {
    this.symbol = symbol;
    return this;
  }

   /**
   * The token symbol.
   * @return symbol
  **/
  @Schema(example = "TEST", required = true, description = "The token symbol.")
  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Kip17ContractListResponseItem kip17ContractListResponseItem = (Kip17ContractListResponseItem) o;
    return Objects.equals(this.address, kip17ContractListResponseItem.address) &&
        Objects.equals(this.alias, kip17ContractListResponseItem.alias) &&
        Objects.equals(this.name, kip17ContractListResponseItem.name) &&
        Objects.equals(this.options, kip17ContractListResponseItem.options) &&
        Objects.equals(this.symbol, kip17ContractListResponseItem.symbol);
  }

  @Override
  public int hashCode() {
    return Objects.hash(address, alias, name, options, symbol);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Kip17ContractListResponseItem {\n");
    
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    alias: ").append(toIndentedString(alias)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    options: ").append(toIndentedString(options)).append("\n");
    sb.append("    symbol: ").append(toIndentedString(symbol)).append("\n");
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
