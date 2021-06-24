/*
 * Wallet API
 * # Introduction Wallet API is an API for creating and managing Klaytn accounts as well as sending transactions. If you create your Klaytn account using Wallet API, you don't have to manage your private key yourself. Wallet API provides a wallet for safe storage of your Klaytn account private keys that you would need to use BApps. For more details on how to use Wallet API, please refer to this [tutorial](https://docs.klaytnapi.com/v/en/tutorial). Wallet API can be divided into the Account part, which creates and manages Klaytn accounts, and the Transaction part, which sends different kinds of transactions. Wallet API creates, deletes and monitors Klaytn accounts and updates the accounts to multisig, and manages all private keys for all accounts registered on KAS. Wallet API can also create transaction to send it to Klaytn. These include transactions sent from multisig accounts. In case of muiltisig accounts, a transaction will automatically be sent to Klaytn once \\(Threshold\\) is met. For more detail, please refer to this [tutorial](https://docs.klaytnapi.com/v/en/tutorial). There are mainly two types of transactions: basic transactions and fee delegation transactions. Fee delegation transactions include Global Fee Delegation transaction and user fee deletation transaction. With the Global Fee Delegation transaction scheme, the transaction fee will initially be paid by GroundX and then be charged to you at a later date. With the User Fee Delegation transaction scheme, you create an account that pays the transaction fees on behalf of the users when a transaction. The functionalities and limits of Wallet API are shown below: | Version | Item | Description | | :--- | :--- | :--- | | 2.0 | Limits | Supports Cypress(Mainnet), Baobab(Testnet) \\ Doesn't support (Service Chain \\) | |  |  | Doesn't support account management for external custodial keys | |  |  | Doesn't support multisig for RLP encoded transactions | |  | Account management | Create, retrieve and delete account | |  |  | Multisig account update | |  | Managing transaction | [Basic](https://ko.docs.klaytn.com/klaytn/design/transactions/basic) creating and sending transaction | |  |  | [FeeDelegatedWithRatio](https://ko.docs.klaytn.com/klaytn/design/transactions/partial-fee-delegation) creating and sending transaction | |  |  | RLP encoded transaction\\([Legacy](https://ko.docs.klaytn.com/klaytn/design/transactions/basic#txtypelegacytransaction), [Basic](https://ko.docs.klaytn.com/klaytn/design/transactions/basic), [FeeDelegatedWithRatio](https://ko.docs.klaytn.com/klaytn/design/transactions/partial-fee-delegation)\\) creating and sending | |  |  | Managing and sending multisig transactions | |  | Administrator | Manage resource pool\\(create, query pool, delete, retrieve account \\) | # Error Codes ## 400: Bad Request  | Code | Messages |   | --- | --- |   | 1061010 | data don't exist</br>data don't exist; krn:1001:wallet:68ec0e4b-0f61-4e6f-ae35-be865ab23187:account-pool:default:0x9b2f4d85d7f7abb14db229b5a81f1bdca0aa24c8ff0c4c100b3f25098b7a6152 1061510 | account has been already deleted or disabled 1061511 | account has been already deleted or enabled 1061512 | account is invalid to sign the transaction; 0x18925BDD724614bF13Bd5d53a74adFd228903796</br>account is invalid to sign the transaction; 0x6d06e7cA9F26d6D30B3b4Dff6084E74C51908fef 1061515 | the requested account must be a legacy account; if the account is multisig account, use `PUT /v2/tx/{fd|fd-user}/account` API for multisig transaction and /v2/multisig/_**_/_** APIs 1061607 | it has to start with '0x' and allows [0-9a-fA-F]; input</br>it has to start with '0x' and allows [0-9a-fA-F]; transaction-id 1061608 | cannot be empty or zero value; to</br>cannot be empty or zero value; keyId</br>cannot be empty or zero value; address 1061609 | it just allow Klaytn address form; to 1061615 | its value is out of range; size 1061616 | fee ratio must be between 1 and 99; feeRatio 1061903 | failed to decode account keys; runtime error: slice bounds out of range [:64] with length 4 1061905 | failed to get feepayer 1061912 | rlp value and request value are not same; feeRatio</br>rlp value and request value are not same; feePayer 1061914 | already submitted transaction. Confirm transaction hash; 0x6f2e9235a48a86c3a7912b4237f83e760609c7ca609bbccbf648c8617a3a980c</br>already submitted transaction. Confirm transaction hash; 0xfb1fae863da42bcefdde3d572404bf5fcb89c1809e9253d5fff7c07a4bb5210f 1061917 | AccountKeyLegacy type is not supported in AccountKeyRoleBased type 1061918 | it just allow (Partial)FeeDelegation transaction type 1061919 | PartialFeeDelegation transaction must set fee ratio to non-zero value 1061920 | FeeDelegation transaction cannot set fee ratio, use PartialFeeDelegation transaction type 1061921 | it just allow Basic transaction type 1065000 | failed to retrieve a transaction from klaytn node 1065001 | failed to send a raw transaction to klaytn node; -32000::insufficient funds of the sender for value </br>failed to send a raw transaction to klaytn node; -32000::not a program account (e.g., an account having code and storage)</br>failed to send a raw transaction to klaytn node; -32000::nonce too low</br>failed to send a raw transaction to klaytn node; -32000::insufficient funds of the fee payer for gas * price 1065100 | failed to get an account</br>failed to get an account; data don't exist</br>failed to get an account; account key corrupted. can not use this account 1065102 | account key corrupted. can not use this account |  
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
  @Schema(example = "0x06000000c0010000c0010000959d4612f8e393190f17021598705780823fa77269ef917744ae2bb673fc9c347880317ae1cac179746d9432606ebfcfb409da113af41a69d15d96e69c7c6a6e8154f59f141fcd494c45040c842a255c60791c433ca1bed3e4d91abfb40b32cd5743e3b6855a7d1e2fc6950810c137638b8a5810d2b6d6bfbd98d67a608a9df10be111e7c8fba3c0e923a216d7faccec01102cb9eec491f34c5204c555c9f132bdcfa031b2054afe0dcd3d5191546e222d0f6268a86f3249e4658f01ed020078196068c4f3bde490ad3563a3ace46d1c1984e34535ecd8eb66d829ab7321dc2b32866c9bd1d1ebb7dc9950484f93764cf5ec1b0594a5913f8294a24ecf0ca631be8a5199d7eb2d29cfc86bf7f532821b05a622ec06bc1e55db1cdf9521083516a7eb0d27e113477357242cea492a5c4943b367609c96a8fdec21f564264a027dbf6e13d6589c0ff34a83620f1b6d0b8c37612b6ae24490a655a86578638ffc19281ddcc4c5b7cb9a808374586e6f3ee90ec390d30b5f83cab0583a9e3e5639e3c97920bd1eaf0ac2856c48afb887784f60e42381642440479b14967116a319ab4fdae18f2bc135f5c2b60fa6f105d0a9d2184b135951537b5427c8f3f1eab6473ff2f9d8cc15e306085c4bd2f207d5e1824324c7697a53ce99e97d44b4435683ccc09de54594ea44de000080", required = true, description = "Encrypted secret key")
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
  @Schema(example = "0x04c394fdc6b7e29d733c6004316a283d9d83771ccbfa4e3983a8c5b71aad7933d943aa2760f46b750d76af5692d4d43f3a48ea4c6c6d829cdb40edd274cb18d85a", required = true, description = "Public key")
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
