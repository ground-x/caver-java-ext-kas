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
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.kip7.model.Kip7FeePayerOptions;
/**
 * Kip7ContractListResponseItem
 */


public class Kip7ContractListResponseItem {
  @SerializedName("address")
  private String address = null;

  @SerializedName("alias")
  private String alias = null;

  @SerializedName("decimals")
  private Long decimals = null;

  @SerializedName("name")
  private String name = null;

  @SerializedName("status")
  private String status = null;

  @SerializedName("symbol")
  private String symbol = null;

  @SerializedName("totalSupply")
  private String totalSupply = null;

  @SerializedName("options")
  private Kip7FeePayerOptions options = null;

  public Kip7ContractListResponseItem address(String address) {
    this.address = address;
    return this;
  }

   /**
   * Contract address
   * @return address
  **/
  @Schema(example = "88215745102653612937125749890058681434164137913", required = true, description = "Contract address")
  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public Kip7ContractListResponseItem alias(String alias) {
    this.alias = alias;
    return this;
  }

   /**
   * Contract alias
   * @return alias
  **/
  @Schema(example = "mycontract", required = true, description = "Contract alias")
  public String getAlias() {
    return alias;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }

  public Kip7ContractListResponseItem decimals(Long decimals) {
    this.decimals = decimals;
    return this;
  }

   /**
   * The number of digits that come after the decimal place when displaying token values on-screen. The default value is &#x60;0&#x60;.
   * @return decimals
  **/
  @Schema(example = "8", required = true, description = "The number of digits that come after the decimal place when displaying token values on-screen. The default value is `0`.")
  public Long getDecimals() {
    return decimals;
  }

  public void setDecimals(Long decimals) {
    this.decimals = decimals;
  }

  public Kip7ContractListResponseItem name(String name) {
    this.name = name;
    return this;
  }

   /**
   * Contract name
   * @return name
  **/
  @Schema(example = "MyKIP7", required = true, description = "Contract name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Kip7ContractListResponseItem status(String status) {
    this.status = status;
    return this;
  }

   /**
   * Contract deployment status[&#x60;init&#x60;,&#x60;submitted&#x60;,&#x60;deployed&#x60;]
   * @return status
  **/
  @Schema(example = "deployed", required = true, description = "Contract deployment status[`init`,`submitted`,`deployed`]")
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Kip7ContractListResponseItem symbol(String symbol) {
    this.symbol = symbol;
    return this;
  }

   /**
   * Contract symbol
   * @return symbol
  **/
  @Schema(example = "MSK7", required = true, description = "Contract symbol")
  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public Kip7ContractListResponseItem totalSupply(String totalSupply) {
    this.totalSupply = totalSupply;
    return this;
  }

   /**
   * Total supply (in hex.)
   * @return totalSupply
  **/
  @Schema(example = "1280", required = true, description = "Total supply (in hex.)")
  public String getTotalSupply() {
    return totalSupply;
  }

  public void setTotalSupply(String totalSupply) {
    this.totalSupply = totalSupply;
  }

  public Kip7ContractListResponseItem options(Kip7FeePayerOptions options) {
    this.options = options;
    return this;
  }

   /**
   * Get options
   * @return options
  **/
  @Schema(description = "")
  public Kip7FeePayerOptions getOptions() {
    return options;
  }

  public void setOptions(Kip7FeePayerOptions options) {
    this.options = options;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Kip7ContractListResponseItem kip7ContractListResponseItem = (Kip7ContractListResponseItem) o;
    return Objects.equals(this.address, kip7ContractListResponseItem.address) &&
        Objects.equals(this.alias, kip7ContractListResponseItem.alias) &&
        Objects.equals(this.decimals, kip7ContractListResponseItem.decimals) &&
        Objects.equals(this.name, kip7ContractListResponseItem.name) &&
        Objects.equals(this.status, kip7ContractListResponseItem.status) &&
        Objects.equals(this.symbol, kip7ContractListResponseItem.symbol) &&
        Objects.equals(this.totalSupply, kip7ContractListResponseItem.totalSupply) &&
        Objects.equals(this.options, kip7ContractListResponseItem.options);
  }

  @Override
  public int hashCode() {
    return Objects.hash(address, alias, decimals, name, status, symbol, totalSupply, options);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Kip7ContractListResponseItem {\n");
    
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    alias: ").append(toIndentedString(alias)).append("\n");
    sb.append("    decimals: ").append(toIndentedString(decimals)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    symbol: ").append(toIndentedString(symbol)).append("\n");
    sb.append("    totalSupply: ").append(toIndentedString(totalSupply)).append("\n");
    sb.append("    options: ").append(toIndentedString(options)).append("\n");
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
