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
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.Signature;
/**
 * Data of transaction sent to Klaytn.
 */
@Schema(description = "Data of transaction sent to Klaytn.")

public class TransactionResult {
  @SerializedName("from")
  private String from = null;

  @SerializedName("gas")
  private Long gas = null;

  @SerializedName("gasPrice")
  private String gasPrice = null;

  @SerializedName("input")
  private String input = null;

  @SerializedName("nonce")
  private Long nonce = null;

  @SerializedName("rlp")
  private String rlp = null;

  @SerializedName("signatures")
  private List<Signature> signatures = null;

  @SerializedName("status")
  private String status = null;

  @SerializedName("transactionHash")
  private String transactionHash = null;

  @SerializedName("typeInt")
  private Long typeInt = null;

  @SerializedName("value")
  private String value = null;

  @SerializedName("to")
  private String to = null;

  @SerializedName("code")
  private Long code = null;

  @SerializedName("message")
  private String message = null;

  @SerializedName("requestId")
  private String requestId = null;

  @SerializedName("transactionId")
  private String transactionId = null;

  @SerializedName("accountKey")
  private String accountKey = null;

  public TransactionResult from(String from) {
    this.from = from;
    return this;
  }

   /**
   * Klaytn account address that sent the transaction.
   * @return from
  **/
  @Schema(example = "0x60d0902c428d0e197f97a756011fd4893c1e57b0", required = true, description = "Klaytn account address that sent the transaction.")
  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public TransactionResult gas(Long gas) {
    this.gas = gas;
    return this;
  }

   /**
   * Maximum gas fee for sending the transaction.
   * @return gas
  **/
  @Schema(example = "1000000", required = true, description = "Maximum gas fee for sending the transaction.")
  public Long getGas() {
    return gas;
  }

  public void setGas(Long gas) {
    this.gas = gas;
  }

  public TransactionResult gasPrice(String gasPrice) {
    this.gasPrice = gasPrice;
    return this;
  }

   /**
   * Cost of gas fee for sending the transaction.
   * @return gasPrice
  **/
  @Schema(example = "0x5d21dba00", required = true, description = "Cost of gas fee for sending the transaction.")
  public String getGasPrice() {
    return gasPrice;
  }

  public void setGasPrice(String gasPrice) {
    this.gasPrice = gasPrice;
  }

  public TransactionResult input(String input) {
    this.input = input;
    return this;
  }

   /**
   * Data that is sent along with the transaction to Klaytn
   * @return input
  **/
  @Schema(example = "0x", description = "Data that is sent along with the transaction to Klaytn")
  public String getInput() {
    return input;
  }

  public void setInput(String input) {
    this.input = input;
  }

  public TransactionResult nonce(Long nonce) {
    this.nonce = nonce;
    return this;
  }

   /**
   * Number of transactions that the current sender has sent in the past.
   * @return nonce
  **/
  @Schema(example = "1376", required = true, description = "Number of transactions that the current sender has sent in the past.")
  public Long getNonce() {
    return nonce;
  }

  public void setNonce(Long nonce) {
    this.nonce = nonce;
  }

  public TransactionResult rlp(String rlp) {
    this.rlp = rlp;
    return this;
  }

   /**
   * RLP serialization value for the transaction.
   * @return rlp
  **/
  @Schema(example = "0xf8558205608505d21dba00830f42408080808207f6a0ddb4fe7dcd0b8adc8a35a4f184e5432c1f84938be50117a177366703e365e150a01dcf2e755836674a4f9de71fb14e40cbd3ab73a373a7f186a895d75251b6f4ae", required = true, description = "RLP serialization value for the transaction.")
  public String getRlp() {
    return rlp;
  }

  public void setRlp(String rlp) {
    this.rlp = rlp;
  }

  public TransactionResult signatures(List<Signature> signatures) {
    this.signatures = signatures;
    return this;
  }

  public TransactionResult addSignaturesItem(Signature signaturesItem) {
    if (this.signatures == null) {
      this.signatures = new ArrayList<Signature>();
    }
    this.signatures.add(signaturesItem);
    return this;
  }

   /**
   * Get signatures
   * @return signatures
  **/
  @Schema(description = "")
  public List<Signature> getSignatures() {
    return signatures;
  }

  public void setSignatures(List<Signature> signatures) {
    this.signatures = signatures;
  }

  public TransactionResult status(String status) {
    this.status = status;
    return this;
  }

   /**
   * Status after the transaction has been sent (\&quot;Submitted\&quot; or \&quot;Pending\&quot;)
   * @return status
  **/
  @Schema(example = "Submitted", description = "Status after the transaction has been sent (\"Submitted\" or \"Pending\")")
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public TransactionResult transactionHash(String transactionHash) {
    this.transactionHash = transactionHash;
    return this;
  }

   /**
   * Hash value for the transaction.
   * @return transactionHash
  **/
  @Schema(example = "0xd0fee21dc7f40d562019cefc3f162961f20ef07da0c24791455530f0009938de", description = "Hash value for the transaction.")
  public String getTransactionHash() {
    return transactionHash;
  }

  public void setTransactionHash(String transactionHash) {
    this.transactionHash = transactionHash;
  }

  public TransactionResult typeInt(Long typeInt) {
    this.typeInt = typeInt;
    return this;
  }

   /**
   * Integer that represents the transaction type
   * @return typeInt
  **/
  @Schema(example = "0", required = true, description = "Integer that represents the transaction type")
  public Long getTypeInt() {
    return typeInt;
  }

  public void setTypeInt(Long typeInt) {
    this.typeInt = typeInt;
  }

  public TransactionResult value(String value) {
    this.value = value;
    return this;
  }

   /**
   * KLAY converted into PEB
   * @return value
  **/
  @Schema(example = "0x0", description = "KLAY converted into PEB")
  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public TransactionResult to(String to) {
    this.to = to;
    return this;
  }

   /**
   * KLAY receiver&#x27;s Klaytn account address
   * @return to
  **/
  @Schema(example = "0x5053e2f0dde795c423a2b7f551c873f6ef6f4daa", description = "KLAY receiver's Klaytn account address")
  public String getTo() {
    return to;
  }

  public void setTo(String to) {
    this.to = to;
  }

  public TransactionResult code(Long code) {
    this.code = code;
    return this;
  }

   /**
   * Get code
   * @return code
  **/
  @Schema(example = "1061608", description = "")
  public Long getCode() {
    return code;
  }

  public void setCode(Long code) {
    this.code = code;
  }

  public TransactionResult message(String message) {
    this.message = message;
    return this;
  }

   /**
   * Get message
   * @return message
  **/
  @Schema(example = "cannot be empty or zero value; to", description = "")
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public TransactionResult requestId(String requestId) {
    this.requestId = requestId;
    return this;
  }

   /**
   * Get requestId
   * @return requestId
  **/
  @Schema(example = "0cbbd3d6-88f7-9e09-b97a-e2961848f926", description = "")
  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

  public TransactionResult transactionId(String transactionId) {
    this.transactionId = transactionId;
    return this;
  }

   /**
   * Multisig transaction ID
   * @return transactionId
  **/
  @Schema(example = "0x3b67ef4ce5c97249443da16e6b188220e2efc91c703154f8b810ffeca0171202", description = "Multisig transaction ID")
  public String getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }

  public TransactionResult accountKey(String accountKey) {
    this.accountKey = accountKey;
    return this;
  }

   /**
   * Newly updated account key.
   * @return accountKey
  **/
  @Schema(example = "0x02a10204037e014e98d15905d404c06742f78df441c962e4ec58f39b0300b99b411c86", description = "Newly updated account key.")
  public String getAccountKey() {
    return accountKey;
  }

  public void setAccountKey(String accountKey) {
    this.accountKey = accountKey;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TransactionResult transactionResult = (TransactionResult) o;
    return Objects.equals(this.from, transactionResult.from) &&
        Objects.equals(this.gas, transactionResult.gas) &&
        Objects.equals(this.gasPrice, transactionResult.gasPrice) &&
        Objects.equals(this.input, transactionResult.input) &&
        Objects.equals(this.nonce, transactionResult.nonce) &&
        Objects.equals(this.rlp, transactionResult.rlp) &&
        Objects.equals(this.signatures, transactionResult.signatures) &&
        Objects.equals(this.status, transactionResult.status) &&
        Objects.equals(this.transactionHash, transactionResult.transactionHash) &&
        Objects.equals(this.typeInt, transactionResult.typeInt) &&
        Objects.equals(this.value, transactionResult.value) &&
        Objects.equals(this.to, transactionResult.to) &&
        Objects.equals(this.code, transactionResult.code) &&
        Objects.equals(this.message, transactionResult.message) &&
        Objects.equals(this.requestId, transactionResult.requestId) &&
        Objects.equals(this.transactionId, transactionResult.transactionId) &&
        Objects.equals(this.accountKey, transactionResult.accountKey);
  }

  @Override
  public int hashCode() {
    return Objects.hash(from, gas, gasPrice, input, nonce, rlp, signatures, status, transactionHash, typeInt, value, to, code, message, requestId, transactionId, accountKey);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TransactionResult {\n");
    
    sb.append("    from: ").append(toIndentedString(from)).append("\n");
    sb.append("    gas: ").append(toIndentedString(gas)).append("\n");
    sb.append("    gasPrice: ").append(toIndentedString(gasPrice)).append("\n");
    sb.append("    input: ").append(toIndentedString(input)).append("\n");
    sb.append("    nonce: ").append(toIndentedString(nonce)).append("\n");
    sb.append("    rlp: ").append(toIndentedString(rlp)).append("\n");
    sb.append("    signatures: ").append(toIndentedString(signatures)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    transactionHash: ").append(toIndentedString(transactionHash)).append("\n");
    sb.append("    typeInt: ").append(toIndentedString(typeInt)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
    sb.append("    to: ").append(toIndentedString(to)).append("\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    requestId: ").append(toIndentedString(requestId)).append("\n");
    sb.append("    transactionId: ").append(toIndentedString(transactionId)).append("\n");
    sb.append("    accountKey: ").append(toIndentedString(accountKey)).append("\n");
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
