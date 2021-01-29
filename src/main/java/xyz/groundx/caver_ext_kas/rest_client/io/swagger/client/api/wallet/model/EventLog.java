/*
 * Wallet API
 * # Introduction Wallet API is used to create and manage Klaytn accounts and transfer transactions. If you create a Klaytn account with Wallet API, you do not need to manage private keys separately. Wallet API provides a secure wallet to keep your Klaytn account’s private keys for BApp. For more details on Wallet API, refer to our [tutorial](https://docs.klaytnapi.com/v/ko/tutorial).  Wallet API features an “Account” section for creating and managing Klaytn accounts and a “Transaction” section for transferring transactions. Wallet API creates, deletes, and monitors Klaytn accounts; updates multisig accounts; and manages the privates keys of all accounts registered to KAS.  In addition, Wallet API creates transactions and transfers them to Klaytn. They include transactions that are sent through the multisig accounts. A transaction will be automatically transferred to Klaytn if the threshold is met for the number of signatures. For more details on multisignatures, refer to [the followings](https://docs.klaytnapi.com/v/ko/tutorial).  Transactions include basic and fee delegation transactions. In particular, fee delegation transactions include global and user fee delegation transactions. In the global fee delegation transaction, Ground X’s KAS account first pays the transaction fee and charges the users later. Meanwhile, in the user fee delegation transaction, a user creates an account to pay for transaction fees when sending transactions.  Wallet API has the following functions and limitations.  | Version | Item | Description | | :--- | :--- | :--- | | 2.0 | Limitations | Support for Cypress (mainnet) and Baobab (testnet) (Service Chain not supported) | |  |  | Account management for external management keys not supported | |  |  | Multisignatures of RLP-encoded transactions not supported | |  | Account management  | Account creation, search, and deletion | |  |  | Multisignature account updates | |  | Transaction management | [Basic](https://ko.docs.klaytn.com/klaytn/design/transactions/basic) Transaction Creation and Transfer | |  |  | [FeeDelegatedWithRatio](https://ko.docs.klaytn.com/klaytn/design/transactions/partial-fee-delegation) Transaction Creation and Transfer | |  |  | RLP-encoded transaction \\([Legacy](https://ko.docs.klaytn.com/klaytn/design/transactions/basic#txtypelegacytransaction), [Basic](https://ko.docs.klaytn.com/klaytn/design/transactions/basic), [FeeDelegatedWithRatio](https://ko.docs.klaytn.com/klaytn/design/transactions/partial-fee-delegation) Transaction Creation and Transfer \\) | |  |  | Multisignature transaction management and transfer | |  | Administrator | Resource pool management (creation, pool search, deletion, and account search) |    # Error Codes  ## 400: Bad Request   | Code | Messages |   | --- | --- |   | 1061010 | data don't exist</br>data don't exist; krn:1001:wallet:68ec0e4b-0f61-4e6f-ae35-be865ab23187:account-pool:default:0x9b2f4d85d7f7abb14db229b5a81f1bdca0aa24c8ff0c4c100b3f25098b7a6152 1061510 | account has been already deleted or disabled 1061511 | account has been already deleted or enabled 1061512 | account is invalid to sign the transaction; 0xc9bFDDabf2c38396b097C8faBE9151955413995D</br>account is invalid to sign the transaction; 0x35Cc4921B17Dfa67a58B93c9F8918f823e58b77e 1061515 | the requested account must be a legacy account; if the account is multisig account, use `PUT /v2/tx/{fd|fd-user}/account` API for multisig transaction and /v2/multisig/_**_/_** APIs 1061607 | it has to start with '0x' and allows [0-9a-fA-F]; input</br>it has to start with '0x' and allows [0-9a-fA-F]; tx_id 1061608 | cannot be empty or zero value; to</br>cannot be empty or zero value; input</br>cannot be empty or zero value; address</br>cannot be empty or zero value; keyId 1061609 | it just allow Klaytn address form; to 1061615 | its value is out of range; size 1061616 | feeration must be between 1 and 99; feeRatio 1061903 | failed to decode account keys 1061905 | failed to get feepayer 1061912 | rlp value and request value are not same; feeRatio</br>rlp value and request value are not same; feePayer 1061914 | already submitted transaction. Confirm transaction hash; 0xb9612ec6ec39bfd3f2841daa7ab062fc94cf33f23503606c979b2f81e50b2cb1 1061917 | AccountKeyLegacy type is not supported in AccountKeyRoleBased type 1061918 | it just allow (Partial)FeeDelegation transaction type 1061919 | PartialFeeDelegation transaction must set fee ratio to non-zero value 1061920 | FeeDelegation transaction cannot set fee ratio, use PartialFeeDelegation transaction type 1061921 | it just allow Basic transaction type 1065000 | failed to retrieve a transaction from klaytn node 1065001 | failed to send a raw transaction to klaytn node; -32000::insufficient funds of the sender for value </br>failed to send a raw transaction to klaytn node; -32000::not a program account (e.g., an account having code and storage)</br>failed to send a raw transaction to klaytn node; -32000::nonce too low</br>failed to send a raw transaction to klaytn node; -32000::insufficient funds of the fee payer for gas * price 1065100 | failed to get an account from AMS</br>failed to get an account from AMS; account key corrupted. can not use this account 1065102 | account key corrupted. can not use this account |   # Authentication  <!-- ReDoc-Inject: <security-definitions> -->
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
/**
 * Logs emitted by the transaction
 */
@Schema(description = "Logs emitted by the transaction")

public class EventLog {
  @SerializedName("address")
  private String address = null;

  @SerializedName("blockHash")
  private String blockHash = null;

  @SerializedName("blockNumber")
  private String blockNumber = null;

  @SerializedName("data")
  private String data = null;

  @SerializedName("logIndex")
  private String logIndex = null;

  @SerializedName("removed")
  private Boolean removed = null;

  @SerializedName("topics")
  private List<String> topics = null;

  @SerializedName("transactionHash")
  private String transactionHash = null;

  @SerializedName("transactionIndex")
  private String transactionIndex = null;

  public EventLog address(String address) {
    this.address = address;
    return this;
  }

   /**
   * Klaytn account address
   * @return address
  **/
  @Schema(example = "0xdcd62c57182e780e23d2313c4782709da85b9d6c", required = true, description = "Klaytn account address")
  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public EventLog blockHash(String blockHash) {
    this.blockHash = blockHash;
    return this;
  }

   /**
   * Block hash including the transaction
   * @return blockHash
  **/
  @Schema(example = "0xb714b09a463d32cf309a6e9e0f4d1a1738be5373c584cefeeb781343c208e36d", required = true, description = "Block hash including the transaction")
  public String getBlockHash() {
    return blockHash;
  }

  public void setBlockHash(String blockHash) {
    this.blockHash = blockHash;
  }

  public EventLog blockNumber(String blockNumber) {
    this.blockNumber = blockNumber;
    return this;
  }

   /**
   * Block No. including the transaction
   * @return blockNumber
  **/
  @Schema(example = "0x28aead8", required = true, description = "Block No. including the transaction")
  public String getBlockNumber() {
    return blockNumber;
  }

  public void setBlockNumber(String blockNumber) {
    this.blockNumber = blockNumber;
  }

  public EventLog data(String data) {
    this.data = data;
    return this;
  }

   /**
   * Transaction data
   * @return data
  **/
  @Schema(example = "0x0000000000000000000000000000000000000000000000008ac7230489e80000", required = true, description = "Transaction data")
  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public EventLog logIndex(String logIndex) {
    this.logIndex = logIndex;
    return this;
  }

   /**
   * Index of log
   * @return logIndex
  **/
  @Schema(example = "0x2", required = true, description = "Index of log")
  public String getLogIndex() {
    return logIndex;
  }

  public void setLogIndex(String logIndex) {
    this.logIndex = logIndex;
  }

  public EventLog removed(Boolean removed) {
    this.removed = removed;
    return this;
  }

   /**
   * Whether to remove log
   * @return removed
  **/
  @Schema(example = "false", required = true, description = "Whether to remove log")
  public Boolean isRemoved() {
    return removed;
  }

  public void setRemoved(Boolean removed) {
    this.removed = removed;
  }

  public EventLog topics(List<String> topics) {
    this.topics = topics;
    return this;
  }

  public EventLog addTopicsItem(String topicsItem) {
    if (this.topics == null) {
      this.topics = new ArrayList<String>();
    }
    this.topics.add(topicsItem);
    return this;
  }

   /**
   * Get topics
   * @return topics
  **/
  @Schema(description = "")
  public List<String> getTopics() {
    return topics;
  }

  public void setTopics(List<String> topics) {
    this.topics = topics;
  }

  public EventLog transactionHash(String transactionHash) {
    this.transactionHash = transactionHash;
    return this;
  }

   /**
   * Transaction hash value
   * @return transactionHash
  **/
  @Schema(example = "0x8becaceca227568ef2afb94d85a4ee34c0b5b67a45517a6655c6eb5faee66d29", required = true, description = "Transaction hash value")
  public String getTransactionHash() {
    return transactionHash;
  }

  public void setTransactionHash(String transactionHash) {
    this.transactionHash = transactionHash;
  }

  public EventLog transactionIndex(String transactionIndex) {
    this.transactionIndex = transactionIndex;
    return this;
  }

   /**
   * Index of the transaction
   * @return transactionIndex
  **/
  @Schema(example = "0x3", required = true, description = "Index of the transaction")
  public String getTransactionIndex() {
    return transactionIndex;
  }

  public void setTransactionIndex(String transactionIndex) {
    this.transactionIndex = transactionIndex;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EventLog eventLog = (EventLog) o;
    return Objects.equals(this.address, eventLog.address) &&
        Objects.equals(this.blockHash, eventLog.blockHash) &&
        Objects.equals(this.blockNumber, eventLog.blockNumber) &&
        Objects.equals(this.data, eventLog.data) &&
        Objects.equals(this.logIndex, eventLog.logIndex) &&
        Objects.equals(this.removed, eventLog.removed) &&
        Objects.equals(this.topics, eventLog.topics) &&
        Objects.equals(this.transactionHash, eventLog.transactionHash) &&
        Objects.equals(this.transactionIndex, eventLog.transactionIndex);
  }

  @Override
  public int hashCode() {
    return Objects.hash(address, blockHash, blockNumber, data, logIndex, removed, topics, transactionHash, transactionIndex);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EventLog {\n");
    
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    blockHash: ").append(toIndentedString(blockHash)).append("\n");
    sb.append("    blockNumber: ").append(toIndentedString(blockNumber)).append("\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
    sb.append("    logIndex: ").append(toIndentedString(logIndex)).append("\n");
    sb.append("    removed: ").append(toIndentedString(removed)).append("\n");
    sb.append("    topics: ").append(toIndentedString(topics)).append("\n");
    sb.append("    transactionHash: ").append(toIndentedString(transactionHash)).append("\n");
    sb.append("    transactionIndex: ").append(toIndentedString(transactionIndex)).append("\n");
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
