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
import java.util.ArrayList;
import java.util.List;
/**
 * BurnKip37TokenRequest
 */


public class BurnKip37TokenRequest {
  @SerializedName("from")
  private String from = null;

  @SerializedName("ids")
  private List<String> ids = new ArrayList<String>();

  @SerializedName("amounts")
  private List<String> amounts = new ArrayList<String>();

  public BurnKip37TokenRequest from(String from) {
    this.from = from;
    return this;
  }

   /**
   * The owner of the token or the Klaytn account address authorized to burn. The default value is the address that deployed the contract.
   * @return from
  **/
  @Schema(description = "The owner of the token or the Klaytn account address authorized to burn. The default value is the address that deployed the contract.")
  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public BurnKip37TokenRequest ids(List<String> ids) {
    this.ids = ids;
    return this;
  }

  public BurnKip37TokenRequest addIdsItem(String idsItem) {
    this.ids.add(idsItem);
    return this;
  }

   /**
   * ID of the token to burn.
   * @return ids
  **/
  @Schema(required = true, description = "ID of the token to burn.")
  public List<String> getIds() {
    return ids;
  }

  public void setIds(List<String> ids) {
    this.ids = ids;
  }

  public BurnKip37TokenRequest amounts(List<String> amounts) {
    this.amounts = amounts;
    return this;
  }

  public BurnKip37TokenRequest addAmountsItem(String amountsItem) {
    this.amounts.add(amountsItem);
    return this;
  }

   /**
   * Number of the token to burn (in hex.)
   * @return amounts
  **/
  @Schema(required = true, description = "Number of the token to burn (in hex.)")
  public List<String> getAmounts() {
    return amounts;
  }

  public void setAmounts(List<String> amounts) {
    this.amounts = amounts;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BurnKip37TokenRequest burnKip37TokenRequest = (BurnKip37TokenRequest) o;
    return Objects.equals(this.from, burnKip37TokenRequest.from) &&
        Objects.equals(this.ids, burnKip37TokenRequest.ids) &&
        Objects.equals(this.amounts, burnKip37TokenRequest.amounts);
  }

  @Override
  public int hashCode() {
    return Objects.hash(from, ids, amounts);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BurnKip37TokenRequest {\n");
    
    sb.append("    from: ").append(toIndentedString(from)).append("\n");
    sb.append("    ids: ").append(toIndentedString(ids)).append("\n");
    sb.append("    amounts: ").append(toIndentedString(amounts)).append("\n");
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
