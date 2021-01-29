/*
 * Wallet API
 * # Introduction Wallet API is used to create and manage Klaytn accounts and transfer transactions. If you create a Klaytn account with Wallet API, you do not need to manage private keys separately. Wallet API provides a secure wallet to keep your Klaytn account’s private keys for BApp. For more details on Wallet API, refer to our [tutorial](https://docs.klaytnapi.com/v/ko/tutorial).  Wallet API features an “Account” section for creating and managing Klaytn accounts and a “Transaction” section for transferring transactions. Wallet API creates, deletes, and monitors Klaytn accounts; updates multisig accounts; and manages the privates keys of all accounts registered to KAS.  In addition, Wallet API creates transactions and transfers them to Klaytn. They include transactions that are sent through the multisig accounts. A transaction will be automatically transferred to Klaytn if the threshold is met for the number of signatures. For more details on multisignatures, refer to [the followings](https://docs.klaytnapi.com/v/ko/tutorial).  Transactions include basic and fee delegation transactions. In particular, fee delegation transactions include global and user fee delegation transactions. In the global fee delegation transaction, Ground X’s KAS account first pays the transaction fee and charges the users later. Meanwhile, in the user fee delegation transaction, a user creates an account to pay for transaction fees when sending transactions.  Wallet API has the following functions and limitations.  | Version | Item | Description | | :--- | :--- | :--- | | 2.0 | Limitations | Support for Cypress (mainnet) and Baobab (testnet) (Service Chain not supported) | |  |  | Account management for external management keys not supported | |  |  | Multisignatures of RLP-encoded transactions not supported | |  | Account management  | Account creation, search, and deletion | |  |  | Multisignature account updates | |  | Transaction management | [Basic](https://ko.docs.klaytn.com/klaytn/design/transactions/basic) Transaction Creation and Transfer | |  |  | [FeeDelegatedWithRatio](https://ko.docs.klaytn.com/klaytn/design/transactions/partial-fee-delegation) Transaction Creation and Transfer | |  |  | RLP-encoded transaction \\([Legacy](https://ko.docs.klaytn.com/klaytn/design/transactions/basic#txtypelegacytransaction), [Basic](https://ko.docs.klaytn.com/klaytn/design/transactions/basic), [FeeDelegatedWithRatio](https://ko.docs.klaytn.com/klaytn/design/transactions/partial-fee-delegation) Transaction Creation and Transfer \\) | |  |  | Multisignature transaction management and transfer | |  | Administrator | Resource pool management (creation, pool search, deletion, and account search) |    # Error Codes  ## 400: Bad Request   | Code | Messages |   | --- | --- |   | 1061010 | data don't exist 1061510 | account has been already deleted or disabled 1061511 | account has been already deleted or enabled 1061512 | account is invalid to sign the transaction; 0xc9bFDDabf2c38396b097C8faBE9151955413995D</br>account is invalid to sign the transaction; 0x35Cc4921B17Dfa67a58B93c9F8918f823e58b77e 1061515 | the requested account must be a legacy account; if the account is multisig account, use `PUT /v2/tx/{fd|fd-user}/account` API for multisig transaction and /v2/multisig/_**_/_** APIs 1061607 | it has to start with '0x' and allows [0-9a-fA-F]; input</br>it has to start with '0x' and allows [0-9a-fA-F]; tx_id 1061608 | cannot be empty or zero value; to</br>cannot be empty or zero value; input 1061609 | it just allow Klaytn address form; to 1061615 | its value is out of range; size 1061616 | feeration must be between 1 and 99; feeRatio 1061903 | failed to decode account keys 1061905 | failed to get feepayer 1061912 | rlp value and request value are not same; feeRatio</br>rlp value and request value are not same; feePayer 1061914 | already submitted transaction. Confirm transaction hash; 0xb9612ec6ec39bfd3f2841daa7ab062fc94cf33f23503606c979b2f81e50b2cb1 1061917 | AccountKeyLegacy type is not supported in AccountKeyRoleBased type 1061918 | it just allow (Partial)FeeDelegation transaction type 1061919 | PartialFeeDelegation transaction must set fee ratio to non-zero value 1061920 | FeeDelegation transaction cannot set fee ratio, use PartialFeeDelegation transaction type 1061921 | it just allow Basic transaction type 1065000 | failed to retrieve a transaction from klaytn node 1065001 | failed to send a raw transaction to klaytn node; -32000::insufficient funds of the sender for value </br>failed to send a raw transaction to klaytn node; -32000::not a program account (e.g., an account having code and storage)</br>failed to send a raw transaction to klaytn node; -32000::nonce too low</br>failed to send a raw transaction to klaytn node; -32000::insufficient funds of the fee payer for gas * price 1065100 | failed to get an account from AMS</br>failed to get an account from AMS; account key corrupted. can not use this account 1065102 | account key corrupted. can not use this account |  
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
 * Current status of the multisig transaction
 */
@Schema(description = "Current status of the multisig transaction")

public class MultisigTransactionStatus {
  @SerializedName("signedWeight")
  private Long signedWeight = null;

  @SerializedName("status")
  private String status = null;

  @SerializedName("threshold")
  private Long threshold = null;

  @SerializedName("transactionHash")
  private String transactionHash = null;

  @SerializedName("transactionId")
  private String transactionId = null;

  @SerializedName("weight")
  private Long weight = null;

  @SerializedName("reminders")
  private List<String> reminders = null;

  public MultisigTransactionStatus signedWeight(Long signedWeight) {
    this.signedWeight = signedWeight;
    return this;
  }

   /**
   * Sum of weight after this signer signed
   * @return signedWeight
  **/
  @Schema(example = "4", required = true, description = "Sum of weight after this signer signed")
  public Long getSignedWeight() {
    return signedWeight;
  }

  public void setSignedWeight(Long signedWeight) {
    this.signedWeight = signedWeight;
  }

  public MultisigTransactionStatus status(String status) {
    this.status = status;
    return this;
  }

   /**
   * Current transaction status
   * @return status
  **/
  @Schema(example = "Submitted", required = true, description = "Current transaction status")
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public MultisigTransactionStatus threshold(Long threshold) {
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

  public MultisigTransactionStatus transactionHash(String transactionHash) {
    this.transactionHash = transactionHash;
    return this;
  }

   /**
   * Transaction hash value
   * @return transactionHash
  **/
  @Schema(example = "0x81f2ff422eae9b40d65b7a6f1e3c5ca598f83d1473ccc87e53d51b875fe75c82", description = "Transaction hash value")
  public String getTransactionHash() {
    return transactionHash;
  }

  public void setTransactionHash(String transactionHash) {
    this.transactionHash = transactionHash;
  }

  public MultisigTransactionStatus transactionId(String transactionId) {
    this.transactionId = transactionId;
    return this;
  }

   /**
   * ID for multisig transactions
   * @return transactionId
  **/
  @Schema(example = "0xfa275dba88d197a85504ef70e0dd2640acc708817e9eb3933d7850acfb048649", required = true, description = "ID for multisig transactions")
  public String getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }

  public MultisigTransactionStatus weight(Long weight) {
    this.weight = weight;
    return this;
  }

   /**
   * Weight of this signer
   * @return weight
  **/
  @Schema(example = "3", required = true, description = "Weight of this signer")
  public Long getWeight() {
    return weight;
  }

  public void setWeight(Long weight) {
    this.weight = weight;
  }

  public MultisigTransactionStatus reminders(List<String> reminders) {
    this.reminders = reminders;
    return this;
  }

  public MultisigTransactionStatus addRemindersItem(String remindersItem) {
    if (this.reminders == null) {
      this.reminders = new ArrayList<String>();
    }
    this.reminders.add(remindersItem);
    return this;
  }

   /**
   * Get reminders
   * @return reminders
  **/
  @Schema(description = "")
  public List<String> getReminders() {
    return reminders;
  }

  public void setReminders(List<String> reminders) {
    this.reminders = reminders;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MultisigTransactionStatus multisigTransactionStatus = (MultisigTransactionStatus) o;
    return Objects.equals(this.signedWeight, multisigTransactionStatus.signedWeight) &&
        Objects.equals(this.status, multisigTransactionStatus.status) &&
        Objects.equals(this.threshold, multisigTransactionStatus.threshold) &&
        Objects.equals(this.transactionHash, multisigTransactionStatus.transactionHash) &&
        Objects.equals(this.transactionId, multisigTransactionStatus.transactionId) &&
        Objects.equals(this.weight, multisigTransactionStatus.weight) &&
        Objects.equals(this.reminders, multisigTransactionStatus.reminders);
  }

  @Override
  public int hashCode() {
    return Objects.hash(signedWeight, status, threshold, transactionHash, transactionId, weight, reminders);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MultisigTransactionStatus {\n");
    
    sb.append("    signedWeight: ").append(toIndentedString(signedWeight)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    threshold: ").append(toIndentedString(threshold)).append("\n");
    sb.append("    transactionHash: ").append(toIndentedString(transactionHash)).append("\n");
    sb.append("    transactionId: ").append(toIndentedString(transactionId)).append("\n");
    sb.append("    weight: ").append(toIndentedString(weight)).append("\n");
    sb.append("    reminders: ").append(toIndentedString(reminders)).append("\n");
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
