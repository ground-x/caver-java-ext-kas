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
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.EventLog;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.FeePayerSignaturesObj;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.wallet.model.Signature;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * Transaction receipt
 */
@Schema(description = "Transaction receipt")

public class TransactionReceipt {
  @SerializedName("blockHash")
  private String blockHash = null;

  @SerializedName("blockNumber")
  private String blockNumber = null;

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

  @SerializedName("to")
  private String to = null;

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

  @SerializedName("contractAddress")
  private String contractAddress = null;

  @SerializedName("codeFormat")
  private String codeFormat = null;

  @SerializedName("feePayer")
  private String feePayer = null;

  @SerializedName("feePayerSignatures")
  private List<FeePayerSignaturesObj> feePayerSignatures = null;

  @SerializedName("humanReadable")
  private Boolean humanReadable = null;

  public TransactionReceipt blockHash(String blockHash) {
    this.blockHash = blockHash;
    return this;
  }

   /**
   * Hash value of the block where the transaction is located
   * @return blockHash
  **/
  @Schema(example = "0x276e6efcc01b27c992b0663cc843baebc9dbb167cf0cd7e74808c21c97a74182", description = "Hash value of the block where the transaction is located")
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
   * No. of the block where the transaction is located
   * @return blockNumber
  **/
  @Schema(example = "0x24bb088", description = "No. of the block where the transaction is located")
  public String getBlockNumber() {
    return blockNumber;
  }

  public void setBlockNumber(String blockNumber) {
    this.blockNumber = blockNumber;
  }

  public TransactionReceipt from(String from) {
    this.from = from;
    return this;
  }

   /**
   * Klaytn account address that sent a transaction
   * @return from
  **/
  @Schema(example = "0x3e3733b256c93f9d759e33c9939258068bd5957d", description = "Klaytn account address that sent a transaction")
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
   * Max. transaction fee (gas) for sending the transaction
   * @return gas
  **/
  @Schema(example = "0xf4240", description = "Max. transaction fee (gas) for sending the transaction")
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
   * Transaction fee (gas) for sending the transaction
   * @return gasPrice
  **/
  @Schema(example = "0x5d21dba00", description = "Transaction fee (gas) for sending the transaction")
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
   * Transaction fee (gas) for sending the transaction
   * @return gasUsed
  **/
  @Schema(example = "0x55478", description = "Transaction fee (gas) for sending the transaction")
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
   * Transaction data hash
   * @return hash
  **/
  @Schema(example = "0x6a3bb7c14981f04e54261a542f0acaf27433befa9619443139f288b3b07c6b05", description = "Transaction data hash")
  public String getHash() {
    return hash;
  }

  public void setHash(String hash) {
    this.hash = hash;
  }

  public TransactionReceipt input(String input) {
    this.input = input;
    return this;
  }

   /**
   * Data attached to and used for executing the outgoing transaction
   * @return input
  **/
  @Schema(example = "0x4867ba1500000000000000000000000000000000000000000000000000000000000000c000000000000000000000000001021e96a79de1b663753935ac856c2cfc51ce8c000000000000000000000000270f21fbf544e5f87b4988c521315a87ce24acf200000000000000000000000000000000000000000000000000000000000000009dbb36061e9a8cb752a9e8abd17e459d7577eaf614f351dfac0b3b3a2d4fca7400000000000000000000000000000000000000000000000000000000000001400000000000000000000000000000000000000000000000000000000000000041efc087a54f954b416b6d051775770336f26d65442306145f8148ee2d2181012d5d49e2d3bd51b22456bb69f5adda7e21c93750aa0c4bfd8a99e6bd17584d10a41b0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000a401db711901000000000000000000000000000000000000000000000000000000000000000000000000000000000000001c8c7395e3e64e69471ed11debbff2a0ffb89d5b00000000000000000000000000000000000000000000000000000000000000600000000000000000000000000000000000000000000000000000000000000020efefefefefefef0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000", description = "Data attached to and used for executing the outgoing transaction")
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
   * Bloom filter for finding related logs quickly
   * @return logsBloom
  **/
  @Schema(example = "0x00000000000000000000000000000000000000000040000000000000200000000000000000000000000001000000000000000000000200000000000000000000000000000000000000000000000000000000000000000004000000000000000000000000000000000000000000000000000000000000000000000000000000000001000000000000000000000000000000000000000000002000000000000000000000000000000000000000000000000000000000000000000800000000000000000000000000000000000000800000000000000000000000000000000000080000000000000000000000000000000000000000000800000000000800000000", description = "Bloom filter for finding related logs quickly")
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
   * No. of current user’s previous transactions
   * @return nonce
  **/
  @Schema(example = "0x26dc3", description = "No. of current user’s previous transactions")
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
   * Hash value of a transaction with no fee delegation account and signature value
   * @return senderTxHash
  **/
  @Schema(example = "0x6a3bb7c14981f04e54261a542f0acaf27433befa9619443139f288b3b07c6b05", description = "Hash value of a transaction with no fee delegation account and signature value")
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
   * Transaction status. The status is “Pending” if the transaction is still in txpool, “Committed” if the transaction is successful, and “CommitError” if the transaction failed.
   * @return status
  **/
  @Schema(example = "Committed", description = "Transaction status. The status is “Pending” if the transaction is still in txpool, “Committed” if the transaction is successful, and “CommitError” if the transaction failed.")
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public TransactionReceipt to(String to) {
    this.to = to;
    return this;
  }

   /**
   * Klaytn account address to receive KLAY or contract address
   * @return to
  **/
  @Schema(example = "0x01021e96a79de1b663753935ac856c2cfc51ce8c", description = "Klaytn account address to receive KLAY or contract address")
  public String getTo() {
    return to;
  }

  public void setTo(String to) {
    this.to = to;
  }

  public TransactionReceipt transactionHash(String transactionHash) {
    this.transactionHash = transactionHash;
    return this;
  }

   /**
   * Transaction hash value
   * @return transactionHash
  **/
  @Schema(example = "0x6a3bb7c14981f04e54261a542f0acaf27433befa9619443139f288b3b07c6b05", description = "Transaction hash value")
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
   * Order of transactions in the block where they are located
   * @return transactionIndex
  **/
  @Schema(example = "0x0", description = "Order of transactions in the block where they are located")
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
   * Character value of transaction type
   * @return type
  **/
  @Schema(example = "TxTypeSmartContractExecution", description = "Character value of transaction type")
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
   * Numeric value of transaction type
   * @return typeInt
  **/
  @Schema(example = "48", description = "Numeric value of transaction type")
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
   * KLAY converted into PEB unit
   * @return value
  **/
  @Schema(example = "0x0", description = "KLAY converted into PEB unit")
  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public TransactionReceipt contractAddress(String contractAddress) {
    this.contractAddress = contractAddress;
    return this;
  }

   /**
   * Contract address. It has a null value if it is not for a contract release.
   * @return contractAddress
  **/
  @Schema(required = true, description = "Contract address. It has a null value if it is not for a contract release.")
  public String getContractAddress() {
    return contractAddress;
  }

  public void setContractAddress(String contractAddress) {
    this.contractAddress = contractAddress;
  }

  public TransactionReceipt codeFormat(String codeFormat) {
    this.codeFormat = codeFormat;
    return this;
  }

   /**
   * Code format of smart contract
   * @return codeFormat
  **/
  @Schema(example = "0x0", description = "Code format of smart contract")
  public String getCodeFormat() {
    return codeFormat;
  }

  public void setCodeFormat(String codeFormat) {
    this.codeFormat = codeFormat;
  }

  public TransactionReceipt feePayer(String feePayer) {
    this.feePayer = feePayer;
    return this;
  }

   /**
   * Account address for fee delegation of transaction fee
   * @return feePayer
  **/
  @Schema(example = "0x85b98485444c89880cd9c48807cef727c296f2da", description = "Account address for fee delegation of transaction fee")
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

  public TransactionReceipt humanReadable(Boolean humanReadable) {
    this.humanReadable = humanReadable;
    return this;
  }

   /**
   * Wheter to be &#x60;humanReadable&#x60; of the account
   * @return humanReadable
  **/
  @Schema(example = "false", description = "Wheter to be `humanReadable` of the account")
  public Boolean isHumanReadable() {
    return humanReadable;
  }

  public void setHumanReadable(Boolean humanReadable) {
    this.humanReadable = humanReadable;
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
        Objects.equals(this.from, transactionReceipt.from) &&
        Objects.equals(this.gas, transactionReceipt.gas) &&
        Objects.equals(this.gasPrice, transactionReceipt.gasPrice) &&
        Objects.equals(this.gasUsed, transactionReceipt.gasUsed) &&
        Objects.equals(this.hash, transactionReceipt.hash) &&
        Objects.equals(this.input, transactionReceipt.input) &&
        Objects.equals(this.logs, transactionReceipt.logs) &&
        Objects.equals(this.logsBloom, transactionReceipt.logsBloom) &&
        Objects.equals(this.nonce, transactionReceipt.nonce) &&
        Objects.equals(this.senderTxHash, transactionReceipt.senderTxHash) &&
        Objects.equals(this.signatures, transactionReceipt.signatures) &&
        Objects.equals(this.status, transactionReceipt.status) &&
        Objects.equals(this.to, transactionReceipt.to) &&
        Objects.equals(this.transactionHash, transactionReceipt.transactionHash) &&
        Objects.equals(this.transactionIndex, transactionReceipt.transactionIndex) &&
        Objects.equals(this.type, transactionReceipt.type) &&
        Objects.equals(this.typeInt, transactionReceipt.typeInt) &&
        Objects.equals(this.value, transactionReceipt.value) &&
        Objects.equals(this.contractAddress, transactionReceipt.contractAddress) &&
        Objects.equals(this.codeFormat, transactionReceipt.codeFormat) &&
        Objects.equals(this.feePayer, transactionReceipt.feePayer) &&
        Objects.equals(this.feePayerSignatures, transactionReceipt.feePayerSignatures) &&
        Objects.equals(this.humanReadable, transactionReceipt.humanReadable);
  }

  @Override
  public int hashCode() {
    return Objects.hash(blockHash, blockNumber, from, gas, gasPrice, gasUsed, hash, input, logs, logsBloom, nonce, senderTxHash, signatures, status, to, transactionHash, transactionIndex, type, typeInt, value, contractAddress, codeFormat, feePayer, feePayerSignatures, humanReadable);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TransactionReceipt {\n");
    
    sb.append("    blockHash: ").append(toIndentedString(blockHash)).append("\n");
    sb.append("    blockNumber: ").append(toIndentedString(blockNumber)).append("\n");
    sb.append("    from: ").append(toIndentedString(from)).append("\n");
    sb.append("    gas: ").append(toIndentedString(gas)).append("\n");
    sb.append("    gasPrice: ").append(toIndentedString(gasPrice)).append("\n");
    sb.append("    gasUsed: ").append(toIndentedString(gasUsed)).append("\n");
    sb.append("    hash: ").append(toIndentedString(hash)).append("\n");
    sb.append("    input: ").append(toIndentedString(input)).append("\n");
    sb.append("    logs: ").append(toIndentedString(logs)).append("\n");
    sb.append("    logsBloom: ").append(toIndentedString(logsBloom)).append("\n");
    sb.append("    nonce: ").append(toIndentedString(nonce)).append("\n");
    sb.append("    senderTxHash: ").append(toIndentedString(senderTxHash)).append("\n");
    sb.append("    signatures: ").append(toIndentedString(signatures)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    to: ").append(toIndentedString(to)).append("\n");
    sb.append("    transactionHash: ").append(toIndentedString(transactionHash)).append("\n");
    sb.append("    transactionIndex: ").append(toIndentedString(transactionIndex)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    typeInt: ").append(toIndentedString(typeInt)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
    sb.append("    contractAddress: ").append(toIndentedString(contractAddress)).append("\n");
    sb.append("    codeFormat: ").append(toIndentedString(codeFormat)).append("\n");
    sb.append("    feePayer: ").append(toIndentedString(feePayer)).append("\n");
    sb.append("    feePayerSignatures: ").append(toIndentedString(feePayerSignatures)).append("\n");
    sb.append("    humanReadable: ").append(toIndentedString(humanReadable)).append("\n");
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
