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
 * BurnFromKip7TokenRequest
 */


public class BurnFromKip7TokenRequest {
  @SerializedName("owner")
  private String owner = null;

  @SerializedName("spender")
  private String spender = null;

  @SerializedName("amount")
  private String amount = null;

  public BurnFromKip7TokenRequest owner(String owner) {
    this.owner = owner;
    return this;
  }

   /**
   * Klaytn account address of the owner of the tokens to be burned
   * @return owner
  **/
  @Schema(example = "0x1eb15b3ccb2add3bfe132a4f6ad21ca62cf94e6d", required = true, description = "Klaytn account address of the owner of the tokens to be burned")
  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public BurnFromKip7TokenRequest spender(String spender) {
    this.spender = spender;
    return this;
  }

   /**
   * Klaytn account address on which the tokens will be burned
   * @return spender
  **/
  @Schema(example = "not-eoa", required = true, description = "Klaytn account address on which the tokens will be burned")
  public String getSpender() {
    return spender;
  }

  public void setSpender(String spender) {
    this.spender = spender;
  }

  public BurnFromKip7TokenRequest amount(String amount) {
    this.amount = amount;
    return this;
  }

   /**
   * The amount of tokens to burn (in hexadecimal)
   * @return amount
  **/
  @Schema(example = "0x100", required = true, description = "The amount of tokens to burn (in hexadecimal)")
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
    BurnFromKip7TokenRequest burnFromKip7TokenRequest = (BurnFromKip7TokenRequest) o;
    return Objects.equals(this.owner, burnFromKip7TokenRequest.owner) &&
        Objects.equals(this.spender, burnFromKip7TokenRequest.spender) &&
        Objects.equals(this.amount, burnFromKip7TokenRequest.amount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(owner, spender, amount);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BurnFromKip7TokenRequest {\n");
    
    sb.append("    owner: ").append(toIndentedString(owner)).append("\n");
    sb.append("    spender: ").append(toIndentedString(spender)).append("\n");
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
