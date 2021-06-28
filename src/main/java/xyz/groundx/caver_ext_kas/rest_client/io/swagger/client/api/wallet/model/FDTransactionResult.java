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
 * Data on Fee Delegation transaction sent to Klaytn
 */
@Schema(description = "Data on Fee Delegation transaction sent to Klaytn")

public class FDTransactionResult {
  @SerializedName("feePayer")
  private String feePayer = null;

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

  @SerializedName("to")
  private String to = null;

  @SerializedName("transactionHash")
  private String transactionHash = null;

  @SerializedName("typeInt")
  private Long typeInt = null;

  @SerializedName("value")
  private String value = null;

  @SerializedName("feeRatio")
  private Long feeRatio = null;

  @SerializedName("transactionId")
  private String transactionId = null;

  @SerializedName("accountKey")
  private String accountKey = null;

  public FDTransactionResult feePayer(String feePayer) {
    this.feePayer = feePayer;
    return this;
  }

   /**
   * Account address for Fee Delegation
   * @return feePayer
  **/
  @Schema(example = "0x85b98485444c89880cd9c48807cef727c296f2da", required = true, description = "Account address for Fee Delegation")
  public String getFeePayer() {
    return feePayer;
  }

  public void setFeePayer(String feePayer) {
    this.feePayer = feePayer;
  }

  public FDTransactionResult from(String from) {
    this.from = from;
    return this;
  }

   /**
   * Klaytn account address that sent the transaction
   * @return from
  **/
  @Schema(example = "0x569a3da2e37b4c08e342820d580122e5283bafbc", required = true, description = "Klaytn account address that sent the transaction")
  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public FDTransactionResult gas(Long gas) {
    this.gas = gas;
    return this;
  }

   /**
   * Maximum gas fee to be used for sending the transaction
   * @return gas
  **/
  @Schema(example = "1000000", required = true, description = "Maximum gas fee to be used for sending the transaction")
  public Long getGas() {
    return gas;
  }

  public void setGas(Long gas) {
    this.gas = gas;
  }

  public FDTransactionResult gasPrice(String gasPrice) {
    this.gasPrice = gasPrice;
    return this;
  }

   /**
   * Gas fee used for sending the transaction
   * @return gasPrice
  **/
  @Schema(example = "0x5d21dba00", required = true, description = "Gas fee used for sending the transaction")
  public String getGasPrice() {
    return gasPrice;
  }

  public void setGasPrice(String gasPrice) {
    this.gasPrice = gasPrice;
  }

  public FDTransactionResult input(String input) {
    this.input = input;
    return this;
  }

   /**
   * Data that&#x27;s sent along with the transaction to Klaytn
   * @return input
  **/
  @Schema(example = "0x6d656d6f", description = "Data that's sent along with the transaction to Klaytn")
  public String getInput() {
    return input;
  }

  public void setInput(String input) {
    this.input = input;
  }

  public FDTransactionResult nonce(Long nonce) {
    this.nonce = nonce;
    return this;
  }

   /**
   * Number of total transactions from the sender of the current transaction
   * @return nonce
  **/
  @Schema(example = "0", required = true, description = "Number of total transactions from the sender of the current transaction")
  public Long getNonce() {
    return nonce;
  }

  public void setNonce(Long nonce) {
    this.nonce = nonce;
  }

  public FDTransactionResult rlp(String rlp) {
    this.rlp = rlp;
    return this;
  }

   /**
   * RLP serialization of the transaction
   * @return rlp
  **/
  @Schema(example = "0x11f8e2808505d21dba00830f4240942f87ba64de5526f7880f21481effbf950f70005c1294569a3da2e37b4c08e342820d580122e5283bafbc846d656d6ff847f8458207f5a048a6618e31293c2b05c093b6fe5c5f2513b844b793b3560934169f0fb7fa4fb5a02aaa4645c711bf0cc25dc3a3878462b13276143b30fed875455b62e14ab9a0a29485b98485444c89880cd9c48807cef727c296f2daf847f8458207f6a0307c6e2df0ba301a3a535c4cede427ce3d43a8a0ec2aaad17fcde9f753555a86a026e4d176ebd1e66b32b5bfa383edd60884e87fd5e788729116916c9313d30976", required = true, description = "RLP serialization of the transaction")
  public String getRlp() {
    return rlp;
  }

  public void setRlp(String rlp) {
    this.rlp = rlp;
  }

  public FDTransactionResult signatures(List<Signature> signatures) {
    this.signatures = signatures;
    return this;
  }

  public FDTransactionResult addSignaturesItem(Signature signaturesItem) {
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

  public FDTransactionResult status(String status) {
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

  public FDTransactionResult to(String to) {
    this.to = to;
    return this;
  }

   /**
   * KLAY receiver&#x27;s Klaytn account address
   * @return to
  **/
  @Schema(example = "0x2f87ba64de5526f7880f21481effbf950f70005c", description = "KLAY receiver's Klaytn account address")
  public String getTo() {
    return to;
  }

  public void setTo(String to) {
    this.to = to;
  }

  public FDTransactionResult transactionHash(String transactionHash) {
    this.transactionHash = transactionHash;
    return this;
  }

   /**
   * Hash value of the transaction
   * @return transactionHash
  **/
  @Schema(example = "0x0b3b7f02640692af371060c311576bc42c3c48122e4f68ca032e5076a3f983d0", description = "Hash value of the transaction")
  public String getTransactionHash() {
    return transactionHash;
  }

  public void setTransactionHash(String transactionHash) {
    this.transactionHash = transactionHash;
  }

  public FDTransactionResult typeInt(Long typeInt) {
    this.typeInt = typeInt;
    return this;
  }

   /**
   * Integer that represents the transaction type
   * @return typeInt
  **/
  @Schema(example = "17", required = true, description = "Integer that represents the transaction type")
  public Long getTypeInt() {
    return typeInt;
  }

  public void setTypeInt(Long typeInt) {
    this.typeInt = typeInt;
  }

  public FDTransactionResult value(String value) {
    this.value = value;
    return this;
  }

   /**
   * KLAY converted into PEB
   * @return value
  **/
  @Schema(example = "0x12", description = "KLAY converted into PEB")
  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public FDTransactionResult feeRatio(Long feeRatio) {
    this.feeRatio = feeRatio;
    return this;
  }

   /**
   * Fee ratio between the sender&#x27;s account and fee payer account
   * @return feeRatio
  **/
  @Schema(example = "30", description = "Fee ratio between the sender's account and fee payer account")
  public Long getFeeRatio() {
    return feeRatio;
  }

  public void setFeeRatio(Long feeRatio) {
    this.feeRatio = feeRatio;
  }

  public FDTransactionResult transactionId(String transactionId) {
    this.transactionId = transactionId;
    return this;
  }

   /**
   * Multisig transaction ID
   * @return transactionId
  **/
  @Schema(example = "0x42a647cc4ab21494394351aa7e39ca5d7b291ded88a588558b6c098b07b5dfae", description = "Multisig transaction ID")
  public String getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }

  public FDTransactionResult accountKey(String accountKey) {
    this.accountKey = accountKey;
    return this;
  }

   /**
   * Newly updated account key
   * @return accountKey
  **/
  @Schema(example = "0x01c0", description = "Newly updated account key")
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
    FDTransactionResult fdTransactionResult = (FDTransactionResult) o;
    return Objects.equals(this.feePayer, fdTransactionResult.feePayer) &&
        Objects.equals(this.from, fdTransactionResult.from) &&
        Objects.equals(this.gas, fdTransactionResult.gas) &&
        Objects.equals(this.gasPrice, fdTransactionResult.gasPrice) &&
        Objects.equals(this.input, fdTransactionResult.input) &&
        Objects.equals(this.nonce, fdTransactionResult.nonce) &&
        Objects.equals(this.rlp, fdTransactionResult.rlp) &&
        Objects.equals(this.signatures, fdTransactionResult.signatures) &&
        Objects.equals(this.status, fdTransactionResult.status) &&
        Objects.equals(this.to, fdTransactionResult.to) &&
        Objects.equals(this.transactionHash, fdTransactionResult.transactionHash) &&
        Objects.equals(this.typeInt, fdTransactionResult.typeInt) &&
        Objects.equals(this.value, fdTransactionResult.value) &&
        Objects.equals(this.feeRatio, fdTransactionResult.feeRatio) &&
        Objects.equals(this.transactionId, fdTransactionResult.transactionId) &&
        Objects.equals(this.accountKey, fdTransactionResult.accountKey);
  }

  @Override
  public int hashCode() {
    return Objects.hash(feePayer, from, gas, gasPrice, input, nonce, rlp, signatures, status, to, transactionHash, typeInt, value, feeRatio, transactionId, accountKey);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FDTransactionResult {\n");
    
    sb.append("    feePayer: ").append(toIndentedString(feePayer)).append("\n");
    sb.append("    from: ").append(toIndentedString(from)).append("\n");
    sb.append("    gas: ").append(toIndentedString(gas)).append("\n");
    sb.append("    gasPrice: ").append(toIndentedString(gasPrice)).append("\n");
    sb.append("    input: ").append(toIndentedString(input)).append("\n");
    sb.append("    nonce: ").append(toIndentedString(nonce)).append("\n");
    sb.append("    rlp: ").append(toIndentedString(rlp)).append("\n");
    sb.append("    signatures: ").append(toIndentedString(signatures)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    to: ").append(toIndentedString(to)).append("\n");
    sb.append("    transactionHash: ").append(toIndentedString(transactionHash)).append("\n");
    sb.append("    typeInt: ").append(toIndentedString(typeInt)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
    sb.append("    feeRatio: ").append(toIndentedString(feeRatio)).append("\n");
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
