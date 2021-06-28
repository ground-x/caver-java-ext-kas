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
import java.util.ArrayList;
import java.util.List;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.MultisigAddress;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.TxData;
/**
 * Pending transaction
 */
@Schema(description = "Pending transaction")

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
  @Schema(example = "0x68311224a5d693E93B64E5B9b9935bAFa0a1B916", required = true, description = "Multisig account or signer account")
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
   * Klaytn Chain ID
   * @return chainId
  **/
  @Schema(example = "1001", required = true, description = "Klaytn Chain ID")
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
   * Transaction request time
   * @return createdAt
  **/
  @Schema(example = "1616131131", required = true, description = "Transaction request time")
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
   * Current status of transaction.
   * @return status
  **/
  @Schema(example = "2", required = true, description = "Current status of transaction.")
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
   * Threshold required for sending the transaction.
   * @return threshold
  **/
  @Schema(example = "4", required = true, description = "Threshold required for sending the transaction.")
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
   * ID for sign transactions.
   * @return transactionId
  **/
  @Schema(example = "0xdaa6296ca623514bca7f109bfe1193203f4cc3922ecc6e250c33adcc3e38ab42", required = true, description = "ID for sign transactions.")
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
   * Transaction types (TX)
   * @return type
  **/
  @Schema(example = "TX", description = "Transaction types (TX)")
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
   * The time of the latest update of transaction
   * @return updatedAt
  **/
  @Schema(example = "1616131131", required = true, description = "The time of the latest update of transaction")
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
