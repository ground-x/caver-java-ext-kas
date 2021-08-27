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
 * Kip37TokenListResponseItem
 */


public class Kip37TokenListResponseItem {
  @SerializedName("tokenId")
  private String tokenId = null;

  @SerializedName("owner")
  private String owner = null;

  @SerializedName("tokenAddress")
  private String tokenAddress = null;

  @SerializedName("totalSupply")
  private String totalSupply = null;

  @SerializedName("tokenUri")
  private String tokenUri = null;

  @SerializedName("replacedTokenUri")
  private String replacedTokenUri = null;

  @SerializedName("balance")
  private String balance = null;

  @SerializedName("transactionHash")
  private String transactionHash = null;

  @SerializedName("transferFrom")
  private String transferFrom = null;

  @SerializedName("transferTo")
  private String transferTo = null;

  @SerializedName("updatedAt")
  private String updatedAt = null;

  public Kip37TokenListResponseItem tokenId(String tokenId) {
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

  public Kip37TokenListResponseItem owner(String owner) {
    this.owner = owner;
    return this;
  }

   /**
   * Klaytn account address of the token owner
   * @return owner
  **/
  @Schema(required = true, description = "Klaytn account address of the token owner")
  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public Kip37TokenListResponseItem tokenAddress(String tokenAddress) {
    this.tokenAddress = tokenAddress;
    return this;
  }

   /**
   * Contract address
   * @return tokenAddress
  **/
  @Schema(required = true, description = "Contract address")
  public String getTokenAddress() {
    return tokenAddress;
  }

  public void setTokenAddress(String tokenAddress) {
    this.tokenAddress = tokenAddress;
  }

  public Kip37TokenListResponseItem totalSupply(String totalSupply) {
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

  public Kip37TokenListResponseItem tokenUri(String tokenUri) {
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

  public Kip37TokenListResponseItem replacedTokenUri(String replacedTokenUri) {
    this.replacedTokenUri = replacedTokenUri;
    return this;
  }

   /**
   * Changed token URI. The token ID is used as the URI path.
   * @return replacedTokenUri
  **/
  @Schema(required = true, description = "Changed token URI. The token ID is used as the URI path.")
  public String getReplacedTokenUri() {
    return replacedTokenUri;
  }

  public void setReplacedTokenUri(String replacedTokenUri) {
    this.replacedTokenUri = replacedTokenUri;
  }

  public Kip37TokenListResponseItem balance(String balance) {
    this.balance = balance;
    return this;
  }

   /**
   * Balance (in hex.)
   * @return balance
  **/
  @Schema(required = true, description = "Balance (in hex.)")
  public String getBalance() {
    return balance;
  }

  public void setBalance(String balance) {
    this.balance = balance;
  }

  public Kip37TokenListResponseItem transactionHash(String transactionHash) {
    this.transactionHash = transactionHash;
    return this;
  }

   /**
   * Latest transaction hash (32-byte)
   * @return transactionHash
  **/
  @Schema(required = true, description = "Latest transaction hash (32-byte)")
  public String getTransactionHash() {
    return transactionHash;
  }

  public void setTransactionHash(String transactionHash) {
    this.transactionHash = transactionHash;
  }

  public Kip37TokenListResponseItem transferFrom(String transferFrom) {
    this.transferFrom = transferFrom;
    return this;
  }

   /**
   * The Klaytn account address of the sender of the lastest TransferSingle or TransferBatch
   * @return transferFrom
  **/
  @Schema(required = true, description = "The Klaytn account address of the sender of the lastest TransferSingle or TransferBatch")
  public String getTransferFrom() {
    return transferFrom;
  }

  public void setTransferFrom(String transferFrom) {
    this.transferFrom = transferFrom;
  }

  public Kip37TokenListResponseItem transferTo(String transferTo) {
    this.transferTo = transferTo;
    return this;
  }

   /**
   * The Klaytn account address of the receiver of the lastest TransferSingle or TransferBatch
   * @return transferTo
  **/
  @Schema(required = true, description = "The Klaytn account address of the receiver of the lastest TransferSingle or TransferBatch")
  public String getTransferTo() {
    return transferTo;
  }

  public void setTransferTo(String transferTo) {
    this.transferTo = transferTo;
  }

  public Kip37TokenListResponseItem updatedAt(String updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

   /**
   * Timestamp of the latest change in token data
   * @return updatedAt
  **/
  @Schema(required = true, description = "Timestamp of the latest change in token data")
  public String getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(String updatedAt) {
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
    Kip37TokenListResponseItem kip37TokenListResponseItem = (Kip37TokenListResponseItem) o;
    return Objects.equals(this.tokenId, kip37TokenListResponseItem.tokenId) &&
        Objects.equals(this.owner, kip37TokenListResponseItem.owner) &&
        Objects.equals(this.tokenAddress, kip37TokenListResponseItem.tokenAddress) &&
        Objects.equals(this.totalSupply, kip37TokenListResponseItem.totalSupply) &&
        Objects.equals(this.tokenUri, kip37TokenListResponseItem.tokenUri) &&
        Objects.equals(this.replacedTokenUri, kip37TokenListResponseItem.replacedTokenUri) &&
        Objects.equals(this.balance, kip37TokenListResponseItem.balance) &&
        Objects.equals(this.transactionHash, kip37TokenListResponseItem.transactionHash) &&
        Objects.equals(this.transferFrom, kip37TokenListResponseItem.transferFrom) &&
        Objects.equals(this.transferTo, kip37TokenListResponseItem.transferTo) &&
        Objects.equals(this.updatedAt, kip37TokenListResponseItem.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tokenId, owner, tokenAddress, totalSupply, tokenUri, replacedTokenUri, balance, transactionHash, transferFrom, transferTo, updatedAt);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Kip37TokenListResponseItem {\n");
    
    sb.append("    tokenId: ").append(toIndentedString(tokenId)).append("\n");
    sb.append("    owner: ").append(toIndentedString(owner)).append("\n");
    sb.append("    tokenAddress: ").append(toIndentedString(tokenAddress)).append("\n");
    sb.append("    totalSupply: ").append(toIndentedString(totalSupply)).append("\n");
    sb.append("    tokenUri: ").append(toIndentedString(tokenUri)).append("\n");
    sb.append("    replacedTokenUri: ").append(toIndentedString(replacedTokenUri)).append("\n");
    sb.append("    balance: ").append(toIndentedString(balance)).append("\n");
    sb.append("    transactionHash: ").append(toIndentedString(transactionHash)).append("\n");
    sb.append("    transferFrom: ").append(toIndentedString(transferFrom)).append("\n");
    sb.append("    transferTo: ").append(toIndentedString(transferTo)).append("\n");
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
