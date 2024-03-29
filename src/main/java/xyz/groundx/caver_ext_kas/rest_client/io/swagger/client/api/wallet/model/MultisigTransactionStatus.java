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
/**
 * Current status of multisig transaction
 */
@Schema(description = "Current status of multisig transaction")

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
   * The threshold of the signatures that the transaction received
   * @return signedWeight
  **/
  @Schema(example = "4", required = true, description = "The threshold of the signatures that the transaction received")
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
   * Current status of the transaction
   * @return status
  **/
  @Schema(example = "Submitted", required = true, description = "Current status of the transaction")
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
   * Threshold required to send this transaction.
   * @return threshold
  **/
  @Schema(example = "4", required = true, description = "Threshold required to send this transaction.")
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
   * Hash value of the transaction
   * @return transactionHash
  **/
  @Schema(example = "19548504702668825318046347611389508417932250026983132325495191128646072277648", description = "Hash value of the transaction")
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
   * ID for multisig transaction
   * @return transactionId
  **/
  @Schema(example = "10286140475633631630575752406078627178715539610517233680909155282838290432890", required = true, description = "ID for multisig transaction")
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
   * Weighted value of the signer
   * @return weight
  **/
  @Schema(example = "1", required = true, description = "Weighted value of the signer")
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
