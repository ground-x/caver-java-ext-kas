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
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.Signature;
/**
 * Fee delegation transaction information sent to Klaytn
 */
@Schema(description = "Fee delegation transaction information sent to Klaytn")

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

  @SerializedName("transactionId")
  private String transactionId = null;

  @SerializedName("feeRatio")
  private Long feeRatio = null;

  @SerializedName("accountKey")
  private String accountKey = null;

  public FDTransactionResult feePayer(String feePayer) {
    this.feePayer = feePayer;
    return this;
  }

   /**
   * Account address for fee delegation of transaction
   * @return feePayer
  **/
  @Schema(example = "0x85b98485444c89880cd9c48807cef727c296f2da", required = true, description = "Account address for fee delegation of transaction")
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
   * Klaytn account address sending a transaction
   * @return from
  **/
  @Schema(example = "0x569a3da2e37b4c08e342820d580122e5283bafbc", required = true, description = "Klaytn account address sending a transaction")
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
   * Max. transaction fee (gas) for sending the transaction
   * @return gas
  **/
  @Schema(example = "1000000", required = true, description = "Max. transaction fee (gas) for sending the transaction")
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
   * Gas price for sending the transaction
   * @return gasPrice
  **/
  @Schema(example = "0x5d21dba00", required = true, description = "Gas price for sending the transaction")
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
   * Data attached to and used for executing the outgoing transaction
   * @return input
  **/
  @Schema(example = "0x6d656d6f", description = "Data attached to and used for executing the outgoing transaction")
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
   * No. of current user’s previous transactions
   * @return nonce
  **/
  @Schema(example = "0", required = true, description = "No. of current user’s previous transactions")
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
   * RLP serialization value of the transaction
   * @return rlp
  **/
  @Schema(example = "0x11f8e2808505d21dba00830f4240942f87ba64de5526f7880f21481effbf950f70005c1294569a3da2e37b4c08e342820d580122e5283bafbc846d656d6ff847f8458207f5a048a6618e31293c2b05c093b6fe5c5f2513b844b793b3560934169f0fb7fa4fb5a02aaa4645c711bf0cc25dc3a3878462b13276143b30fed875455b62e14ab9a0a29485b98485444c89880cd9c48807cef727c296f2daf847f8458207f6a0307c6e2df0ba301a3a535c4cede427ce3d43a8a0ec2aaad17fcde9f753555a86a026e4d176ebd1e66b32b5bfa383edd60884e87fd5e788729116916c9313d30976", required = true, description = "RLP serialization value of the transaction")
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
   * Status of the transaction(“Submitted” or “Pending”)
   * @return status
  **/
  @Schema(example = "Submitted", description = "Status of the transaction(“Submitted” or “Pending”)")
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
   * Klaytn account address to receive KLAY
   * @return to
  **/
  @Schema(example = "0x2f87ba64de5526f7880f21481effbf950f70005c", description = "Klaytn account address to receive KLAY")
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
   * Transaction hash value
   * @return transactionHash
  **/
  @Schema(example = "0x0b3b7f02640692af371060c311576bc42c3c48122e4f68ca032e5076a3f983d0", description = "Transaction hash value")
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
   * Numeric value of transaction type
   * @return typeInt
  **/
  @Schema(example = "17", required = true, description = "Numeric value of transaction type")
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
   * KLAY converted into PEB unit
   * @return value
  **/
  @Schema(example = "0x12", description = "KLAY converted into PEB unit")
  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public FDTransactionResult transactionId(String transactionId) {
    this.transactionId = transactionId;
    return this;
  }

   /**
   * Multisig transaction ID
   * @return transactionId
  **/
  @Schema(example = "0x0416bf52b804211220aca957250d6bc2e2c6ab8688e68dc9096ae035d009c334", description = "Multisig transaction ID")
  public String getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }

  public FDTransactionResult feeRatio(Long feeRatio) {
    this.feeRatio = feeRatio;
    return this;
  }

   /**
   * Ratio to be paid by the Proxy fee payer for the entire transaction fee
   * @return feeRatio
  **/
  @Schema(example = "10", description = "Ratio to be paid by the Proxy fee payer for the entire transaction fee")
  public Long getFeeRatio() {
    return feeRatio;
  }

  public void setFeeRatio(Long feeRatio) {
    this.feeRatio = feeRatio;
  }

  public FDTransactionResult accountKey(String accountKey) {
    this.accountKey = accountKey;
    return this;
  }

   /**
   * Updated account key
   * @return accountKey
  **/
  @Schema(example = "0x04f84b04f848e303a102c7460ef57f8b648220667785dc6c87f132d57b8e5d8d3373e37c4c9798460eafe301a102ffb48935aa7460b0595e1fe6749c402eaa59d6fd78dd64217470191580f73211", description = "Updated account key")
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
        Objects.equals(this.transactionId, fdTransactionResult.transactionId) &&
        Objects.equals(this.feeRatio, fdTransactionResult.feeRatio) &&
        Objects.equals(this.accountKey, fdTransactionResult.accountKey);
  }

  @Override
  public int hashCode() {
    return Objects.hash(feePayer, from, gas, gasPrice, input, nonce, rlp, signatures, status, to, transactionHash, typeInt, value, transactionId, feeRatio, accountKey);
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
    sb.append("    transactionId: ").append(toIndentedString(transactionId)).append("\n");
    sb.append("    feeRatio: ").append(toIndentedString(feeRatio)).append("\n");
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
