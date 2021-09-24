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
 * Fee Delegation KLAY transfer transaction request schema
 */
@Schema(description = "Fee Delegation KLAY transfer transaction request schema")

public class FDValueTransferTransactionRequest {
  @SerializedName("from")
  private String from = null;

  @SerializedName("value")
  private String value = null;

  @SerializedName("to")
  private String to = null;

  @SerializedName("memo")
  private String memo = null;

  @SerializedName("nonce")
  private Long nonce = null;

  @SerializedName("gas")
  private Long gas = 100000l;

  @SerializedName("submit")
  private Boolean submit = null;

  @SerializedName("feeRatio")
  private Long feeRatio = null;

  public FDValueTransferTransactionRequest from(String from) {
    this.from = from;
    return this;
  }

   /**
   * Klaytn transaction address to send transaction
   * @return from
  **/
  @Schema(example = "598502809624869583754624016892076982032272448885", required = true, description = "Klaytn transaction address to send transaction")
  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public FDValueTransferTransactionRequest value(String value) {
    this.value = value;
    return this;
  }

   /**
   * KLAY converted into PEB
   * @return value
  **/
  @Schema(example = "0", required = true, description = "KLAY converted into PEB")
  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public FDValueTransferTransactionRequest to(String to) {
    this.to = to;
    return this;
  }

   /**
   * KLAY receiver&#x27;s Klaytn account address
   * @return to
  **/
  @Schema(example = "458589992856735687761939783085233253254964399530", required = true, description = "KLAY receiver's Klaytn account address")
  public String getTo() {
    return to;
  }

  public void setTo(String to) {
    this.to = to;
  }

  public FDValueTransferTransactionRequest memo(String memo) {
    this.memo = memo;
    return this;
  }

   /**
   * Memo to be attached to the transaction.
   * @return memo
  **/
  @Schema(example = "291", description = "Memo to be attached to the transaction.")
  public String getMemo() {
    return memo;
  }

  public void setMemo(String memo) {
    this.memo = memo;
  }

  public FDValueTransferTransactionRequest nonce(Long nonce) {
    this.nonce = nonce;
    return this;
  }

   /**
   * Unique identifier for the transactions being sent (By entering 0, the nonce will be automatically determined)
   * @return nonce
  **/
  @Schema(example = "0", description = "Unique identifier for the transactions being sent (By entering 0, the nonce will be automatically determined)")
  public Long getNonce() {
    return nonce;
  }

  public void setNonce(Long nonce) {
    this.nonce = nonce;
  }

  public FDValueTransferTransactionRequest gas(Long gas) {
    this.gas = gas;
    return this;
  }

   /**
   * Maximum gas fee to be used for sending the transaction (By entering 0, it will be set to default value))
   * @return gas
  **/
  @Schema(example = "0", description = "Maximum gas fee to be used for sending the transaction (By entering 0, it will be set to default value))")
  public Long getGas() {
    return gas;
  }

  public void setGas(Long gas) {
    this.gas = gas;
  }

  public FDValueTransferTransactionRequest submit(Boolean submit) {
    this.submit = submit;
    return this;
  }

   /**
   * Shows whether to send the transaction to Klaytn
   * @return submit
  **/
  @Schema(example = "true", description = "Shows whether to send the transaction to Klaytn")
  public Boolean isSubmit() {
    return submit;
  }

  public void setSubmit(Boolean submit) {
    this.submit = submit;
  }

  public FDValueTransferTransactionRequest feeRatio(Long feeRatio) {
    this.feeRatio = feeRatio;
    return this;
  }

   /**
   * The ratio of the gas fee to be delegated. When it&#x27;s empty or 0, the entire fee will be delegated.
   * maximum: 99
   * @return feeRatio
  **/
  @Schema(example = "0", description = "The ratio of the gas fee to be delegated. When it's empty or 0, the entire fee will be delegated.")
  public Long getFeeRatio() {
    return feeRatio;
  }

  public void setFeeRatio(Long feeRatio) {
    this.feeRatio = feeRatio;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FDValueTransferTransactionRequest fdValueTransferTransactionRequest = (FDValueTransferTransactionRequest) o;
    return Objects.equals(this.from, fdValueTransferTransactionRequest.from) &&
        Objects.equals(this.value, fdValueTransferTransactionRequest.value) &&
        Objects.equals(this.to, fdValueTransferTransactionRequest.to) &&
        Objects.equals(this.memo, fdValueTransferTransactionRequest.memo) &&
        Objects.equals(this.nonce, fdValueTransferTransactionRequest.nonce) &&
        Objects.equals(this.gas, fdValueTransferTransactionRequest.gas) &&
        Objects.equals(this.submit, fdValueTransferTransactionRequest.submit) &&
        Objects.equals(this.feeRatio, fdValueTransferTransactionRequest.feeRatio);
  }

  @Override
  public int hashCode() {
    return Objects.hash(from, value, to, memo, nonce, gas, submit, feeRatio);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FDValueTransferTransactionRequest {\n");
    
    sb.append("    from: ").append(toIndentedString(from)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
    sb.append("    to: ").append(toIndentedString(to)).append("\n");
    sb.append("    memo: ").append(toIndentedString(memo)).append("\n");
    sb.append("    nonce: ").append(toIndentedString(nonce)).append("\n");
    sb.append("    gas: ").append(toIndentedString(gas)).append("\n");
    sb.append("    submit: ").append(toIndentedString(submit)).append("\n");
    sb.append("    feeRatio: ").append(toIndentedString(feeRatio)).append("\n");
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
