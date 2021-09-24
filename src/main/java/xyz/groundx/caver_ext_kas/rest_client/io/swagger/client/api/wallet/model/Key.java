/*
 * Wallet API
 * # Introduction Wallet API is an API for creating and managing Klaytn accounts as well as sending transactions. If you create your Klaytn account using Wallet API, you don't have to manage your private key yourself. Wallet API provides a wallet for safe storage of your Klaytn account private keys that you would need to use BApps. For more details on how to use Wallet API, please refer to this [tutorial](https://docs.klaytnapi.com/v/en/tutorial).  Wallet API can be divided into the Account part, which creates and manages Klaytn accounts, and the Transaction part, which sends different kinds of transactions.  Wallet API creates, deletes and monitors Klaytn accounts and updates the accounts to multisig, and manages all private keys for all accounts registered on KAS.  Wallet API can also create transaction to send it to Klaytn. These include transactions sent from multisig accounts. In case of muiltisig accounts, a transaction will automatically be sent to Klaytn once \\(Threshold\\) is met. For more detail, please refer to this [tutorial](https://docs.klaytnapi.com/v/en/tutorial).  There are mainly two types of transactions: basic transactions and fee delegation transactions. Fee delegation transactions include Global Fee Delegation transaction and user fee deletation transaction. With the Global Fee Delegation transaction scheme, the transaction fee will initially be paid by GroundX and then be charged to you at a later date. With the User Fee Delegation transaction scheme, you create an account that pays the transaction fees on behalf of the users when a transaction.  The functionalities and limits of Wallet API are shown below:  | Version | Item | Description | | :--- | :--- | :--- | | 2.0 | Limits | Supports Cypress(Mainnet), Baobab(Testnet) \\ Doesn't support (Service Chain \\) | |  |  | Doesn't support account management for external custodial keys | |  |  | Doesn't support multisig for RLP encoded transactions | |  | Account management | Create, retrieve and delete account | |  |  | Multisig account update | |  | Managing transaction | [Basic](https://ko.docs.klaytn.com/klaytn/design/transactions/basic) creating and sending transaction | |  |  | [FeeDelegatedWithRatio](https://ko.docs.klaytn.com/klaytn/design/transactions/partial-fee-delegation) creating and sending transaction | |  |  | RLP encoded transaction\\([Legacy](https://ko.docs.klaytn.com/klaytn/design/transactions/basic#txtypelegacytransaction), [Basic](https://ko.docs.klaytn.com/klaytn/design/transactions/basic), [FeeDelegatedWithRatio](https://ko.docs.klaytn.com/klaytn/design/transactions/partial-fee-delegation)\\) creating and sending | |  |  | Managing and sending multisig transactions | |  | Administrator | Manage resource pool\\(create, query pool, delete, retrieve account \\) | 
 *
 * OpenAPI spec version: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model;

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
 * Data of the created key
 */
@Schema(description = "Data of the created key")

public class Key {
  @SerializedName("blob")
  private String blob = null;

  @SerializedName("keyId")
  private String keyId = null;

  @SerializedName("krn")
  private String krn = null;

  @SerializedName("publicKey")
  private String publicKey = null;

  public Key blob(String blob) {
    this.blob = blob;
    return this;
  }

   /**
   * Encrypted secret key
   * @return blob
  **/
  @Schema(example = "1326947698341238653465192148882061144816719931924207170300852420613343474463740563253191977185878155127389677490290684575379517919102183714135370860619041132905194497590984078674603838673078517091529702166665084801525629035198736128165064868021672221483546799881081289271490989736822790287811066728105917909073036405943149031661430372221009708776684261642794807842883843713522732079408137424506259636108636750854542858715295220582044770021317137374041241812627338908341662120268872911858682129848239289725088983278599088774443449721697117626592695225065394026717505300259094444292106185150636509805691302021476038040927462859609560146488187250371193461455918407169938485117422486881015148357257630429323852745424655630539585909428533347787178280382953616933681546761890240435867628255333939904945705372098338060332457774393062082599392781048917131338141099239427016573341346982599932608684664018549818983102036623169508784406147395465603082924417560027497530456994951206983864049597699084004625681741343518914706686915034113683062226879353152185098860046080862874790709840048209465147113980492001288806081475351105990483715644858039482947441112052200043575487008033179841598952536175068329968458266790476262670464", required = true, description = "Encrypted secret key")
  public String getBlob() {
    return blob;
  }

  public void setBlob(String blob) {
    this.blob = blob;
  }

  public Key keyId(String keyId) {
    this.keyId = keyId;
    return this;
  }

   /**
   * Key ID
   * @return keyId
  **/
  @Schema(example = "krn:1001:wallet:676de94a-9ca9-45e2-a67b-ed72178cdbcc:key-pool:default:0xce662e6eab8bf2b135664042150cf56e198e43a01d815a30227cd3f498c632c2", required = true, description = "Key ID")
  public String getKeyId() {
    return keyId;
  }

  public void setKeyId(String keyId) {
    this.keyId = keyId;
  }

  public Key krn(String krn) {
    this.krn = krn;
    return this;
  }

   /**
   * Resource name of the key.
   * @return krn
  **/
  @Schema(example = "krn:1001:wallet:676de94a-9ca9-45e2-a67b-ed72178cdbcc:key-pool:default", required = true, description = "Resource name of the key.")
  public String getKrn() {
    return krn;
  }

  public void setKrn(String krn) {
    this.krn = krn;
  }

  public Key publicKey(String publicKey) {
    this.publicKey = publicKey;
    return this;
  }

   /**
   * Public key
   * @return publicKey
  **/
  @Schema(example = "63874692089288067218106611993827512122919310994454259472929700082569394593849752921720359434362211669991539715693369963170521269036913689003385145622321242", required = true, description = "Public key")
  public String getPublicKey() {
    return publicKey;
  }

  public void setPublicKey(String publicKey) {
    this.publicKey = publicKey;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Key key = (Key) o;
    return Objects.equals(this.blob, key.blob) &&
        Objects.equals(this.keyId, key.keyId) &&
        Objects.equals(this.krn, key.krn) &&
        Objects.equals(this.publicKey, key.publicKey);
  }

  @Override
  public int hashCode() {
    return Objects.hash(blob, keyId, krn, publicKey);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Key {\n");
    
    sb.append("    blob: ").append(toIndentedString(blob)).append("\n");
    sb.append("    keyId: ").append(toIndentedString(keyId)).append("\n");
    sb.append("    krn: ").append(toIndentedString(krn)).append("\n");
    sb.append("    publicKey: ").append(toIndentedString(publicKey)).append("\n");
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
