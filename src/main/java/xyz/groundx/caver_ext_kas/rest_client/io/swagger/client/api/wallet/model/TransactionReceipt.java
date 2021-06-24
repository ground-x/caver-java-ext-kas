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
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.EventLog;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.FeePayerSignaturesObj;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.Signature;
/**
 * Transaction receipt
 */
@Schema(description = "Transaction receipt")

public class TransactionReceipt {
  @SerializedName("blockHash")
  private String blockHash = null;

  @SerializedName("blockNumber")
  private String blockNumber = null;

  @SerializedName("codeFormat")
  private String codeFormat = null;

  @SerializedName("contractAddress")
  private String contractAddress = null;

  @SerializedName("feePayer")
  private String feePayer = null;

  @SerializedName("feePayerSignatures")
  private List<FeePayerSignaturesObj> feePayerSignatures = null;

  @SerializedName("from")
  private String from = null;

  @SerializedName("gas")
  private String gas = null;

  @SerializedName("gasPrice")
  private String gasPrice = null;

  @SerializedName("gasUsed")
  private String gasUsed = null;

  @SerializedName("hash")
  private String hash = null;

  @SerializedName("humanReadable")
  private Boolean humanReadable = null;

  @SerializedName("input")
  private String input = null;

  @SerializedName("logs")
  private List<EventLog> logs = null;

  @SerializedName("logsBloom")
  private String logsBloom = null;

  @SerializedName("nonce")
  private String nonce = null;

  @SerializedName("senderTxHash")
  private String senderTxHash = null;

  @SerializedName("signatures")
  private List<Signature> signatures = null;

  @SerializedName("status")
  private String status = null;

  @SerializedName("transactionHash")
  private String transactionHash = null;

  @SerializedName("transactionIndex")
  private String transactionIndex = null;

  @SerializedName("type")
  private String type = null;

  @SerializedName("typeInt")
  private Long typeInt = null;

  @SerializedName("value")
  private String value = null;

  @SerializedName("to")
  private String to = null;

  public TransactionReceipt blockHash(String blockHash) {
    this.blockHash = blockHash;
    return this;
  }

   /**
   * Hash value of the block with the transaction.
   * @return blockHash
  **/
  @Schema(example = "0x205c2537286c8ac7889961e39c438f868385238c202deaa4398f85a3c140810c", description = "Hash value of the block with the transaction.")
  public String getBlockHash() {
    return blockHash;
  }

  public void setBlockHash(String blockHash) {
    this.blockHash = blockHash;
  }

  public TransactionReceipt blockNumber(String blockNumber) {
    this.blockNumber = blockNumber;
    return this;
  }

   /**
   * Number of the block with the transaction.
   * @return blockNumber
  **/
  @Schema(example = "0x2e5a", description = "Number of the block with the transaction.")
  public String getBlockNumber() {
    return blockNumber;
  }

  public void setBlockNumber(String blockNumber) {
    this.blockNumber = blockNumber;
  }

  public TransactionReceipt codeFormat(String codeFormat) {
    this.codeFormat = codeFormat;
    return this;
  }

   /**
   * Code format of the smart contract
   * @return codeFormat
  **/
  @Schema(example = "0x0", description = "Code format of the smart contract")
  public String getCodeFormat() {
    return codeFormat;
  }

  public void setCodeFormat(String codeFormat) {
    this.codeFormat = codeFormat;
  }

  public TransactionReceipt contractAddress(String contractAddress) {
    this.contractAddress = contractAddress;
    return this;
  }

   /**
   * Contract address. Has &#x60;null&#x60; if it&#x27;s not contract deployment.
   * @return contractAddress
  **/
  @Schema(example = "0x0a868e321c0c689c2093001fdf7ecdb9d3ed79e1", description = "Contract address. Has `null` if it's not contract deployment.")
  public String getContractAddress() {
    return contractAddress;
  }

  public void setContractAddress(String contractAddress) {
    this.contractAddress = contractAddress;
  }

  public TransactionReceipt feePayer(String feePayer) {
    this.feePayer = feePayer;
    return this;
  }

   /**
   * Account address to pay transaction fee on behalf.
   * @return feePayer
  **/
  @Schema(example = "0x85b98485444c89880cd9c48807cef727c296f2da", description = "Account address to pay transaction fee on behalf.")
  public String getFeePayer() {
    return feePayer;
  }

  public void setFeePayer(String feePayer) {
    this.feePayer = feePayer;
  }

  public TransactionReceipt feePayerSignatures(List<FeePayerSignaturesObj> feePayerSignatures) {
    this.feePayerSignatures = feePayerSignatures;
    return this;
  }

  public TransactionReceipt addFeePayerSignaturesItem(FeePayerSignaturesObj feePayerSignaturesItem) {
    if (this.feePayerSignatures == null) {
      this.feePayerSignatures = new ArrayList<FeePayerSignaturesObj>();
    }
    this.feePayerSignatures.add(feePayerSignaturesItem);
    return this;
  }

   /**
   * Get feePayerSignatures
   * @return feePayerSignatures
  **/
  @Schema(description = "")
  public List<FeePayerSignaturesObj> getFeePayerSignatures() {
    return feePayerSignatures;
  }

  public void setFeePayerSignatures(List<FeePayerSignaturesObj> feePayerSignatures) {
    this.feePayerSignatures = feePayerSignatures;
  }

  public TransactionReceipt from(String from) {
    this.from = from;
    return this;
  }

   /**
   * Klaytn account address that sent the transaction
   * @return from
  **/
  @Schema(example = "0x60b5c6b28a8a0339a43a0acd5f65eede5f68cf0e", description = "Klaytn account address that sent the transaction")
  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public TransactionReceipt gas(String gas) {
    this.gas = gas;
    return this;
  }

   /**
   * Maximum gas fee set to be used for sending the transaction.
   * @return gas
  **/
  @Schema(example = "0x895440", description = "Maximum gas fee set to be used for sending the transaction.")
  public String getGas() {
    return gas;
  }

  public void setGas(String gas) {
    this.gas = gas;
  }

  public TransactionReceipt gasPrice(String gasPrice) {
    this.gasPrice = gasPrice;
    return this;
  }

   /**
   * Maximum gas fee set to be used for sending the transaction.
   * @return gasPrice
  **/
  @Schema(example = "0x5d21dba00", description = "Maximum gas fee set to be used for sending the transaction.")
  public String getGasPrice() {
    return gasPrice;
  }

  public void setGasPrice(String gasPrice) {
    this.gasPrice = gasPrice;
  }

  public TransactionReceipt gasUsed(String gasUsed) {
    this.gasUsed = gasUsed;
    return this;
  }

   /**
   * Gas fee used for sending the transaction.
   * @return gasUsed
  **/
  @Schema(example = "0x25de7", description = "Gas fee used for sending the transaction.")
  public String getGasUsed() {
    return gasUsed;
  }

  public void setGasUsed(String gasUsed) {
    this.gasUsed = gasUsed;
  }

  public TransactionReceipt hash(String hash) {
    this.hash = hash;
    return this;
  }

   /**
   * Transaction data hash.
   * @return hash
  **/
  @Schema(example = "0x3d13d31bd4b1e6ca37c4e73cfcb0ee4374a06290e84c6b01a41143c2cc2fa322", description = "Transaction data hash.")
  public String getHash() {
    return hash;
  }

  public void setHash(String hash) {
    this.hash = hash;
  }

  public TransactionReceipt humanReadable(Boolean humanReadable) {
    this.humanReadable = humanReadable;
    return this;
  }

   /**
   * Shows whether the account address is&#x60;humanReadable&#x60;.
   * @return humanReadable
  **/
  @Schema(example = "false", description = "Shows whether the account address is`humanReadable`.")
  public Boolean isHumanReadable() {
    return humanReadable;
  }

  public void setHumanReadable(Boolean humanReadable) {
    this.humanReadable = humanReadable;
  }

  public TransactionReceipt input(String input) {
    this.input = input;
    return this;
  }

   /**
   * Data that is sent along with the transaction and is used for execution.
   * @return input
  **/
  @Schema(example = "0x60806040526000805534801561001457600080fd5b50610116806100246000396000f3006080604052600436106053576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806306661abd14605857806342cbb15c146080578063d14e62b81460a8575b600080fd5b348015606357600080fd5b50606a60d2565b6040518082815260200191505060405180910390f35b348015608b57600080fd5b50609260d8565b6040518082815260200191505060405180910390f35b34801560b357600080fd5b5060d06004803603810190808035906020019092919050505060e0565b005b60005481565b600043905090565b80600081905550505600a165627a7a7230582064856de85a2706463526593b08dd790054536042ef66d3204018e6790a2208d10029", description = "Data that is sent along with the transaction and is used for execution.")
  public String getInput() {
    return input;
  }

  public void setInput(String input) {
    this.input = input;
  }

  public TransactionReceipt logs(List<EventLog> logs) {
    this.logs = logs;
    return this;
  }

  public TransactionReceipt addLogsItem(EventLog logsItem) {
    if (this.logs == null) {
      this.logs = new ArrayList<EventLog>();
    }
    this.logs.add(logsItem);
    return this;
  }

   /**
   * Get logs
   * @return logs
  **/
  @Schema(description = "")
  public List<EventLog> getLogs() {
    return logs;
  }

  public void setLogs(List<EventLog> logs) {
    this.logs = logs;
  }

  public TransactionReceipt logsBloom(String logsBloom) {
    this.logsBloom = logsBloom;
    return this;
  }

   /**
   * Bloom filter to search the log more quickly.
   * @return logsBloom
  **/
  @Schema(example = "0x00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000", description = "Bloom filter to search the log more quickly.")
  public String getLogsBloom() {
    return logsBloom;
  }

  public void setLogsBloom(String logsBloom) {
    this.logsBloom = logsBloom;
  }

  public TransactionReceipt nonce(String nonce) {
    this.nonce = nonce;
    return this;
  }

   /**
   * Number of transactions sent by the sender of the current transaction.
   * @return nonce
  **/
  @Schema(example = "0x0", description = "Number of transactions sent by the sender of the current transaction.")
  public String getNonce() {
    return nonce;
  }

  public void setNonce(String nonce) {
    this.nonce = nonce;
  }

  public TransactionReceipt senderTxHash(String senderTxHash) {
    this.senderTxHash = senderTxHash;
    return this;
  }

   /**
   * Transaction hash value without Fee Delegation account address and signature
   * @return senderTxHash
  **/
  @Schema(example = "0x01bd21f13617fc725b80975a68ff1bc2002aec66277e536e5643a8791518c3df", description = "Transaction hash value without Fee Delegation account address and signature")
  public String getSenderTxHash() {
    return senderTxHash;
  }

  public void setSenderTxHash(String senderTxHash) {
    this.senderTxHash = senderTxHash;
  }

  public TransactionReceipt signatures(List<Signature> signatures) {
    this.signatures = signatures;
    return this;
  }

  public TransactionReceipt addSignaturesItem(Signature signaturesItem) {
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

  public TransactionReceipt status(String status) {
    this.status = status;
    return this;
  }

   /**
   * Status of the transaction. If the transaction is still in the txpool, it returns &#x60;Pending&#x60;. A successful transaction returns &#x60;Committed&#x60;, and failed transaction returns &#x60;CommitError&#x60;.
   * @return status
  **/
  @Schema(example = "Committed", description = "Status of the transaction. If the transaction is still in the txpool, it returns `Pending`. A successful transaction returns `Committed`, and failed transaction returns `CommitError`.")
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public TransactionReceipt transactionHash(String transactionHash) {
    this.transactionHash = transactionHash;
    return this;
  }

   /**
   * Transaction hash
   * @return transactionHash
  **/
  @Schema(example = "0x3d13d31bd4b1e6ca37c4e73cfcb0ee4374a06290e84c6b01a41143c2cc2fa322", description = "Transaction hash")
  public String getTransactionHash() {
    return transactionHash;
  }

  public void setTransactionHash(String transactionHash) {
    this.transactionHash = transactionHash;
  }

  public TransactionReceipt transactionIndex(String transactionIndex) {
    this.transactionIndex = transactionIndex;
    return this;
  }

   /**
   * Order of the transaction within the block that contains it
   * @return transactionIndex
  **/
  @Schema(example = "0x0", description = "Order of the transaction within the block that contains it")
  public String getTransactionIndex() {
    return transactionIndex;
  }

  public void setTransactionIndex(String transactionIndex) {
    this.transactionIndex = transactionIndex;
  }

  public TransactionReceipt type(String type) {
    this.type = type;
    return this;
  }

   /**
   * Characters that represents the transaction type
   * @return type
  **/
  @Schema(example = "TxTypeFeeDelegatedSmartContractDeploy", description = "Characters that represents the transaction type")
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public TransactionReceipt typeInt(Long typeInt) {
    this.typeInt = typeInt;
    return this;
  }

   /**
   * Integer that represents the transaction type
   * @return typeInt
  **/
  @Schema(example = "41", description = "Integer that represents the transaction type")
  public Long getTypeInt() {
    return typeInt;
  }

  public void setTypeInt(Long typeInt) {
    this.typeInt = typeInt;
  }

  public TransactionReceipt value(String value) {
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

  public TransactionReceipt to(String to) {
    this.to = to;
    return this;
  }

   /**
   * Klaytn account address or Contract address to receive KLAY
   * @return to
  **/
  @Schema(example = "0x01021e96a79de1b663753935ac856c2cfc51ce8c", required = true, description = "Klaytn account address or Contract address to receive KLAY")
  public String getTo() {
    return to;
  }

  public void setTo(String to) {
    this.to = to;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TransactionReceipt transactionReceipt = (TransactionReceipt) o;
    return Objects.equals(this.blockHash, transactionReceipt.blockHash) &&
        Objects.equals(this.blockNumber, transactionReceipt.blockNumber) &&
        Objects.equals(this.codeFormat, transactionReceipt.codeFormat) &&
        Objects.equals(this.contractAddress, transactionReceipt.contractAddress) &&
        Objects.equals(this.feePayer, transactionReceipt.feePayer) &&
        Objects.equals(this.feePayerSignatures, transactionReceipt.feePayerSignatures) &&
        Objects.equals(this.from, transactionReceipt.from) &&
        Objects.equals(this.gas, transactionReceipt.gas) &&
        Objects.equals(this.gasPrice, transactionReceipt.gasPrice) &&
        Objects.equals(this.gasUsed, transactionReceipt.gasUsed) &&
        Objects.equals(this.hash, transactionReceipt.hash) &&
        Objects.equals(this.humanReadable, transactionReceipt.humanReadable) &&
        Objects.equals(this.input, transactionReceipt.input) &&
        Objects.equals(this.logs, transactionReceipt.logs) &&
        Objects.equals(this.logsBloom, transactionReceipt.logsBloom) &&
        Objects.equals(this.nonce, transactionReceipt.nonce) &&
        Objects.equals(this.senderTxHash, transactionReceipt.senderTxHash) &&
        Objects.equals(this.signatures, transactionReceipt.signatures) &&
        Objects.equals(this.status, transactionReceipt.status) &&
        Objects.equals(this.transactionHash, transactionReceipt.transactionHash) &&
        Objects.equals(this.transactionIndex, transactionReceipt.transactionIndex) &&
        Objects.equals(this.type, transactionReceipt.type) &&
        Objects.equals(this.typeInt, transactionReceipt.typeInt) &&
        Objects.equals(this.value, transactionReceipt.value) &&
        Objects.equals(this.to, transactionReceipt.to);
  }

  @Override
  public int hashCode() {
    return Objects.hash(blockHash, blockNumber, codeFormat, contractAddress, feePayer, feePayerSignatures, from, gas, gasPrice, gasUsed, hash, humanReadable, input, logs, logsBloom, nonce, senderTxHash, signatures, status, transactionHash, transactionIndex, type, typeInt, value, to);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TransactionReceipt {\n");
    
    sb.append("    blockHash: ").append(toIndentedString(blockHash)).append("\n");
    sb.append("    blockNumber: ").append(toIndentedString(blockNumber)).append("\n");
    sb.append("    codeFormat: ").append(toIndentedString(codeFormat)).append("\n");
    sb.append("    contractAddress: ").append(toIndentedString(contractAddress)).append("\n");
    sb.append("    feePayer: ").append(toIndentedString(feePayer)).append("\n");
    sb.append("    feePayerSignatures: ").append(toIndentedString(feePayerSignatures)).append("\n");
    sb.append("    from: ").append(toIndentedString(from)).append("\n");
    sb.append("    gas: ").append(toIndentedString(gas)).append("\n");
    sb.append("    gasPrice: ").append(toIndentedString(gasPrice)).append("\n");
    sb.append("    gasUsed: ").append(toIndentedString(gasUsed)).append("\n");
    sb.append("    hash: ").append(toIndentedString(hash)).append("\n");
    sb.append("    humanReadable: ").append(toIndentedString(humanReadable)).append("\n");
    sb.append("    input: ").append(toIndentedString(input)).append("\n");
    sb.append("    logs: ").append(toIndentedString(logs)).append("\n");
    sb.append("    logsBloom: ").append(toIndentedString(logsBloom)).append("\n");
    sb.append("    nonce: ").append(toIndentedString(nonce)).append("\n");
    sb.append("    senderTxHash: ").append(toIndentedString(senderTxHash)).append("\n");
    sb.append("    signatures: ").append(toIndentedString(signatures)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    transactionHash: ").append(toIndentedString(transactionHash)).append("\n");
    sb.append("    transactionIndex: ").append(toIndentedString(transactionIndex)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    typeInt: ").append(toIndentedString(typeInt)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
    sb.append("    to: ").append(toIndentedString(to)).append("\n");
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
