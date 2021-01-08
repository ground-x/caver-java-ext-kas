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
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.MultisigAddress;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.TxData;
/**
 * List of Pending Transactions
 */
@Schema(description = "List of Pending Transactions")

public class PendedTransaction {
  @SerializedName("address")
  private String address = null;

  @SerializedName("chainId")
  private Long chainId = null;

  @SerializedName("createdAt")
  private Long createdAt = null;

  @SerializedName("multiSigKeys")
  private List<MultisigAddress> multiSigKeys = null;

  @SerializedName("status")
  private Long status = null;

  @SerializedName("threshold")
  private Long threshold = null;

  @SerializedName("transactionId")
  private String transactionId = null;

  @SerializedName("txData")
  private TxData txData = null;

  @SerializedName("type")
  private String type = null;

  @SerializedName("updatedAt")
  private Long updatedAt = null;

  public PendedTransaction address(String address) {
    this.address = address;
    return this;
  }

   /**
   * Multisig account or signer account
   * @return address
  **/
  @Schema(example = "0xc6C9356887b7F7887918Bf577417E5D8De253295", required = true, description = "Multisig account or signer account")
  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public PendedTransaction chainId(Long chainId) {
    this.chainId = chainId;
    return this;
  }

   /**
   * Klaytn chain ID
   * @return chainId
  **/
  @Schema(example = "1001", required = true, description = "Klaytn chain ID")
  public Long getChainId() {
    return chainId;
  }

  public void setChainId(Long chainId) {
    this.chainId = chainId;
  }

  public PendedTransaction createdAt(Long createdAt) {
    this.createdAt = createdAt;
    return this;
  }

   /**
   * Transaction request timestamp
   * @return createdAt
  **/
  @Schema(example = "1599144020", required = true, description = "Transaction request timestamp")
  public Long getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Long createdAt) {
    this.createdAt = createdAt;
  }

  public PendedTransaction multiSigKeys(List<MultisigAddress> multiSigKeys) {
    this.multiSigKeys = multiSigKeys;
    return this;
  }

  public PendedTransaction addMultiSigKeysItem(MultisigAddress multiSigKeysItem) {
    if (this.multiSigKeys == null) {
      this.multiSigKeys = new ArrayList<MultisigAddress>();
    }
    this.multiSigKeys.add(multiSigKeysItem);
    return this;
  }

   /**
   * Get multiSigKeys
   * @return multiSigKeys
  **/
  @Schema(description = "")
  public List<MultisigAddress> getMultiSigKeys() {
    return multiSigKeys;
  }

  public void setMultiSigKeys(List<MultisigAddress> multiSigKeys) {
    this.multiSigKeys = multiSigKeys;
  }

  public PendedTransaction status(Long status) {
    this.status = status;
    return this;
  }

   /**
   * Current transaction status
   * @return status
  **/
  @Schema(example = "2", required = true, description = "Current transaction status")
  public Long getStatus() {
    return status;
  }

  public void setStatus(Long status) {
    this.status = status;
  }

  public PendedTransaction threshold(Long threshold) {
    this.threshold = threshold;
    return this;
  }

   /**
   * Total weight required for sending the transaction
   * @return threshold
  **/
  @Schema(example = "4", required = true, description = "Total weight required for sending the transaction")
  public Long getThreshold() {
    return threshold;
  }

  public void setThreshold(Long threshold) {
    this.threshold = threshold;
  }

  public PendedTransaction transactionId(String transactionId) {
    this.transactionId = transactionId;
    return this;
  }

   /**
   * ID for multisig transactions
   * @return transactionId
  **/
  @Schema(example = "0x65111d4fba621a1bfa3bd97c219b3e0454471cf3c07827396f1202946df51ee2", required = true, description = "ID for multisig transactions")
  public String getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }

  public PendedTransaction txData(TxData txData) {
    this.txData = txData;
    return this;
  }

   /**
   * Get txData
   * @return txData
  **/
  @Schema(required = true, description = "")
  public TxData getTxData() {
    return txData;
  }

  public void setTxData(TxData txData) {
    this.txData = txData;
  }

  public PendedTransaction type(String type) {
    this.type = type;
    return this;
  }

   /**
   * Transaction type (TX)
   * @return type
  **/
  @Schema(example = "TX", description = "Transaction type (TX)")
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public PendedTransaction updatedAt(Long updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

   /**
   * Time when the transaction was last updated
   * @return updatedAt
  **/
  @Schema(example = "1599144020", required = true, description = "Time when the transaction was last updated")
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
    PendedTransaction pendedTransaction = (PendedTransaction) o;
    return Objects.equals(this.address, pendedTransaction.address) &&
        Objects.equals(this.chainId, pendedTransaction.chainId) &&
        Objects.equals(this.createdAt, pendedTransaction.createdAt) &&
        Objects.equals(this.multiSigKeys, pendedTransaction.multiSigKeys) &&
        Objects.equals(this.status, pendedTransaction.status) &&
        Objects.equals(this.threshold, pendedTransaction.threshold) &&
        Objects.equals(this.transactionId, pendedTransaction.transactionId) &&
        Objects.equals(this.txData, pendedTransaction.txData) &&
        Objects.equals(this.type, pendedTransaction.type) &&
        Objects.equals(this.updatedAt, pendedTransaction.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(address, chainId, createdAt, multiSigKeys, status, threshold, transactionId, txData, type, updatedAt);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PendedTransaction {\n");
    
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    chainId: ").append(toIndentedString(chainId)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    multiSigKeys: ").append(toIndentedString(multiSigKeys)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    threshold: ").append(toIndentedString(threshold)).append("\n");
    sb.append("    transactionId: ").append(toIndentedString(transactionId)).append("\n");
    sb.append("    txData: ").append(toIndentedString(txData)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
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
