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
import java.util.ArrayList;
import java.util.List;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.MultisigKey;
/**
 * Data on multisig accounts
 */
@Schema(description = "Data on multisig accounts")

public class MultisigAccount {
  @SerializedName("address")
  private String address = null;

  @SerializedName("krn")
  private String krn = null;

  @SerializedName("multiSigKeys")
  private List<MultisigKey> multiSigKeys = null;

  @SerializedName("threshold")
  private Long threshold = null;

  @SerializedName("transactionHash")
  private String transactionHash = null;

  @SerializedName("updatedAt")
  private Long updatedAt = null;

  public MultisigAccount address(String address) {
    this.address = address;
    return this;
  }

   /**
   * Klaytn account address
   * @return address
  **/
  @Schema(example = "959314666733178453511848909171916369896053837332", required = true, description = "Klaytn account address")
  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public MultisigAccount krn(String krn) {
    this.krn = krn;
    return this;
  }

   /**
   * KAS KRN
   * @return krn
  **/
  @Schema(example = "krn:1001:wallet:68ec0e4b-0f61-4e6f-ae35-be865ab23187:account-pool:default", required = true, description = "KAS KRN")
  public String getKrn() {
    return krn;
  }

  public void setKrn(String krn) {
    this.krn = krn;
  }

  public MultisigAccount multiSigKeys(List<MultisigKey> multiSigKeys) {
    this.multiSigKeys = multiSigKeys;
    return this;
  }

  public MultisigAccount addMultiSigKeysItem(MultisigKey multiSigKeysItem) {
    if (this.multiSigKeys == null) {
      this.multiSigKeys = new ArrayList<MultisigKey>();
    }
    this.multiSigKeys.add(multiSigKeysItem);
    return this;
  }

   /**
   * Get multiSigKeys
   * @return multiSigKeys
  **/
  @Schema(description = "")
  public List<MultisigKey> getMultiSigKeys() {
    return multiSigKeys;
  }

  public void setMultiSigKeys(List<MultisigKey> multiSigKeys) {
    this.multiSigKeys = multiSigKeys;
  }

  public MultisigAccount threshold(Long threshold) {
    this.threshold = threshold;
    return this;
  }

   /**
   * Threshold of total weighted values required to create a multisig transaction.
   * @return threshold
  **/
  @Schema(example = "4", required = true, description = "Threshold of total weighted values required to create a multisig transaction.")
  public Long getThreshold() {
    return threshold;
  }

  public void setThreshold(Long threshold) {
    this.threshold = threshold;
  }

  public MultisigAccount transactionHash(String transactionHash) {
    this.transactionHash = transactionHash;
    return this;
  }

   /**
   * Transaction hash for account data update
   * @return transactionHash
  **/
  @Schema(example = "92915688272539675909459150629851792233983727632469896273587916884256256312618", required = true, description = "Transaction hash for account data update")
  public String getTransactionHash() {
    return transactionHash;
  }

  public void setTransactionHash(String transactionHash) {
    this.transactionHash = transactionHash;
  }

  public MultisigAccount updatedAt(Long updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

   /**
   * The time of the latest update of account data
   * @return updatedAt
  **/
  @Schema(example = "1599187391", required = true, description = "The time of the latest update of account data")
  public Long getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Long updatedAt) {
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
    MultisigAccount multisigAccount = (MultisigAccount) o;
    return Objects.equals(this.address, multisigAccount.address) &&
        Objects.equals(this.krn, multisigAccount.krn) &&
        Objects.equals(this.multiSigKeys, multisigAccount.multiSigKeys) &&
        Objects.equals(this.threshold, multisigAccount.threshold) &&
        Objects.equals(this.transactionHash, multisigAccount.transactionHash) &&
        Objects.equals(this.updatedAt, multisigAccount.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(address, krn, multiSigKeys, threshold, transactionHash, updatedAt);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MultisigAccount {\n");
    
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    krn: ").append(toIndentedString(krn)).append("\n");
    sb.append("    multiSigKeys: ").append(toIndentedString(multiSigKeys)).append("\n");
    sb.append("    threshold: ").append(toIndentedString(threshold)).append("\n");
    sb.append("    transactionHash: ").append(toIndentedString(transactionHash)).append("\n");
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
