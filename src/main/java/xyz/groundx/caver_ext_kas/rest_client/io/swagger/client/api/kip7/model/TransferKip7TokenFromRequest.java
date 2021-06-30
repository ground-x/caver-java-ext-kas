/*
 * KIP-7 API
 * # Introduction KIP-7 API is a RESTful API for managing KIP-7 contracts and tokens that follow the [KIP-7 Fungible Token Standard](https://kips.klaytn.com/KIPs/kip-7).   You can deploy contracts and send tokens using the default contract managing account (`deployer`) and an `alias`. And by using SDK like caver,  you can manage your contracts and tokens using [Wallet API](https://refs.klaytnapi.com/en/wallet/latest) for contracts created on the Klaytn Network.     # Error Codes  ## 400: Bad Request   | Code | Messages |   | --- | --- |  | 1130050 | incorrect request; spender 1130107 | incorrect bookmark 1134410 | invalid address; to</br>invalid address; owner</br>invalid address; address 1134411 | invalid amount; amount |  ## 404: Not Found   | Code | Messages |  | --- | --- |  | 1134504 | contract not found 1134506 | deployer not found |   ## 409: Conflict   | Code | Messages |   | --- | --- |   | 1134900 | duplicate alias 1134902 | contract already paused 1134903 | contract already unpaused |  
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
/**
 * TransferKip7TokenFromRequest
 */


public class TransferKip7TokenFromRequest {
  @SerializedName("spender")
  private String spender = null;

  @SerializedName("owner")
  private String owner = null;

  @SerializedName("to")
  private String to = null;

  @SerializedName("amount")
  private String amount = null;

  public TransferKip7TokenFromRequest spender(String spender) {
    this.spender = spender;
    return this;
  }

   /**
   * Klaytn account address to send tokens
   * @return spender
  **/
  @Schema(example = "0xd6905b98E4Ba43a24E842d2b66c1410173791cab", required = true, description = "Klaytn account address to send tokens")
  public String getSpender() {
    return spender;
  }

  public void setSpender(String spender) {
    this.spender = spender;
  }

  public TransferKip7TokenFromRequest owner(String owner) {
    this.owner = owner;
    return this;
  }

   /**
   * Klaytn account address of the owner delegating the token transfer
   * @return owner
  **/
  @Schema(example = "0x1eb15b3ccb2add3bfe132a4f6ad21ca62cf94e6d", required = true, description = "Klaytn account address of the owner delegating the token transfer")
  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public TransferKip7TokenFromRequest to(String to) {
    this.to = to;
    return this;
  }

   /**
   * Klaytn account address to receive tokens
   * @return to
  **/
  @Schema(example = "not-eoa", required = true, description = "Klaytn account address to receive tokens")
  public String getTo() {
    return to;
  }

  public void setTo(String to) {
    this.to = to;
  }

  public TransferKip7TokenFromRequest amount(String amount) {
    this.amount = amount;
    return this;
  }

   /**
   * Transfer amount (in hexadecimal)
   * @return amount
  **/
  @Schema(example = "0x50", required = true, description = "Transfer amount (in hexadecimal)")
  public String getAmount() {
    return amount;
  }

  public void setAmount(String amount) {
    this.amount = amount;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TransferKip7TokenFromRequest transferKip7TokenFromRequest = (TransferKip7TokenFromRequest) o;
    return Objects.equals(this.spender, transferKip7TokenFromRequest.spender) &&
        Objects.equals(this.owner, transferKip7TokenFromRequest.owner) &&
        Objects.equals(this.to, transferKip7TokenFromRequest.to) &&
        Objects.equals(this.amount, transferKip7TokenFromRequest.amount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(spender, owner, to, amount);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TransferKip7TokenFromRequest {\n");
    
    sb.append("    spender: ").append(toIndentedString(spender)).append("\n");
    sb.append("    owner: ").append(toIndentedString(owner)).append("\n");
    sb.append("    to: ").append(toIndentedString(to)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
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