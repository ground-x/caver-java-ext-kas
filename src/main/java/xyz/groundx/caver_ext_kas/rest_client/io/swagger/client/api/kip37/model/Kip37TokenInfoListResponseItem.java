/*
 * KIP-37 API
 * ## Introduction The KIP-37 API helps Blockchain app (BApp) developers to easily deploy smart contracts and send tokens of the [KIP-37 Multi Token Standard](https://kips.klaytn.com/KIPs/kip-37).  You can use the default contract managing account (`deployer`) and `alias`.    You can also manage the contracts and tokens created on the klaytn network using the caver SDK, using contract address and the [Wallet API](https://refs.klaytnapi.com/ko/wallet/latest) account.    ## Error Codes  ### 400: Bad Request  | Code    | Message                                      | |---------|----------------------------------------------| | 1160050 | incorrect request                            | | 1160251 | its value is out of range                    | | 1164000 | invalid alias format                         | | 1164001 | invalid address                              | | 1164002 | invalid hex format                           | | 1164004 | account not found in wallet account-pool     | | 1164005 | batch items mismatch                         | | 1164006 | too many batch items                         | | 1164007 | invalid krn                                  | | 1164008 | no contract code                             | | 1164009 | insufficient balance                         | | 1164011 | fee payer not found in wallet feepayer-pool  |   ### 403: Forbidden  | Code    | Message                          | |---------|----------------------------------| | 1164300 | insufficient account permissions |   ### 404: Not Found  | Code    | Message            | |---------|--------------------| | 1164400 | contract not found | | 1164401 | token not found    |  ### 409: Conflict  | Code    | Message                   | |---------|---------------------------| | 1164900 | duplicate alias           | | 1164901 | contract already paused   | | 1164902 | contract already unpaused | | 1164903 | token already exist       | | 1164904 | contract already paused   | | 1164905 | token already unpaused    | | 1164906 | already approved          | | 1164907 | already not approved      | | 1164908 | duplicate contract        | | 1164909 | contract being created    |   ### 503: Service Unavailable  | Code    | Message                   | |---------|---------------------------| | 1165100 | internal server error     |   ## Fee Payer Options  KAS KIP-37 supports four scenarios for paying transactin fees:      **1. Using only KAS Global FeePayer Account**   Sends all transactions using the KAS global FeePayer Account.       ``` {     \"options\": {       \"enableGlobalFeePayer\": true     }     } ```    <br />    **2. Using User FeePayer account**   Sends all transactions using the KAS User FeePayer Account.      ``` {   \"options\": {     \"enableGlobalFeePayer\": false,     \"userFeePayer\": {       \"krn\": \"krn:1001:wallet:20bab367-141b-439a-8b4c-ae8788b86316:feepayer-pool:default\",       \"address\": \"0xd6905b98E4Ba43a24E842d2b66c1410173791cab\"     }   } } ```    <br />  **3. Using both KAS Global FeePayer Account + User FeePayer Account**   Uses User FeePayer Account as default. When the balance runs out, KAS Global FeePayer Account will be used.     ``` {   \"options\": {     \"enableGlobalFeePayer\": true,     \"userFeePayer\": {       \"krn\": \"krn:1001:wallet:20bab367-141b-439a-8b4c-ae8788b86316:feepayer-pool:default\",       \"address\": \"0xd6905b98E4Ba43a24E842d2b66c1410173791cab\"     }   } } ```    <br />  **4. Not using FeePayer Account**   Sends a transaction via normal means where the sender pays the transaction fee.       ``` {   \"options\": {     \"enableGlobalFeePayer\": false   } } ``` 
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
/**
 * Kip37TokenInfoListResponseItem
 */


public class Kip37TokenInfoListResponseItem {
  @SerializedName("tokenId")
  private String tokenId = null;

  @SerializedName("totalSupply")
  private String totalSupply = null;

  @SerializedName("tokenUri")
  private String tokenUri = null;

  public Kip37TokenInfoListResponseItem tokenId(String tokenId) {
    this.tokenId = tokenId;
    return this;
  }

   /**
   * Token ID
   * @return tokenId
  **/
  @Schema(required = true, description = "Token ID")
  public String getTokenId() {
    return tokenId;
  }

  public void setTokenId(String tokenId) {
    this.tokenId = tokenId;
  }

  public Kip37TokenInfoListResponseItem totalSupply(String totalSupply) {
    this.totalSupply = totalSupply;
    return this;
  }

   /**
   * Total token supply (in hex.)
   * @return totalSupply
  **/
  @Schema(required = true, description = "Total token supply (in hex.)")
  public String getTotalSupply() {
    return totalSupply;
  }

  public void setTotalSupply(String totalSupply) {
    this.totalSupply = totalSupply;
  }

  public Kip37TokenInfoListResponseItem tokenUri(String tokenUri) {
    this.tokenUri = tokenUri;
    return this;
  }

   /**
   * Token URI
   * @return tokenUri
  **/
  @Schema(required = true, description = "Token URI")
  public String getTokenUri() {
    return tokenUri;
  }

  public void setTokenUri(String tokenUri) {
    this.tokenUri = tokenUri;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Kip37TokenInfoListResponseItem kip37TokenInfoListResponseItem = (Kip37TokenInfoListResponseItem) o;
    return Objects.equals(this.tokenId, kip37TokenInfoListResponseItem.tokenId) &&
        Objects.equals(this.totalSupply, kip37TokenInfoListResponseItem.totalSupply) &&
        Objects.equals(this.tokenUri, kip37TokenInfoListResponseItem.tokenUri);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tokenId, totalSupply, tokenUri);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Kip37TokenInfoListResponseItem {\n");
    
    sb.append("    tokenId: ").append(toIndentedString(tokenId)).append("\n");
    sb.append("    totalSupply: ").append(toIndentedString(totalSupply)).append("\n");
    sb.append("    tokenUri: ").append(toIndentedString(tokenUri)).append("\n");
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
