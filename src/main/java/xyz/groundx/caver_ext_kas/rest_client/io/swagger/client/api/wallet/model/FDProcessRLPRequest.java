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
 * RLP Fee Delegation transaction request schema
 */
@Schema(description = "RLP Fee Delegation transaction request schema")

public class FDProcessRLPRequest {
  @SerializedName("rlp")
  private String rlp = null;

  @SerializedName("submit")
  private Boolean submit = null;

  @SerializedName("feeRatio")
  private Long feeRatio = null;

  public FDProcessRLPRequest rlp(String rlp) {
    this.rlp = rlp;
    return this;
  }

   /**
   * The Klaytn RLP formats SigRLP or TxHashRLP are accepted as RLP values and only (partial) fee delegation transaction type is allowed.
   * @return rlp
  **/
  @Schema(example = "3555775530180462144262874045285080385370799680061830405427251022536315012269437422034374993063363749301742829272037747644239373485472413527444702067525461969341399272696727992089574644794322321180116327861197303036706490360864473007240629099715093535386713837632516014363102287578039545675062105146793695262293102216573565529608532205222903395261337039873747035896626237362069578385680533152328511336466814096878076481705061972667213454900403580370736470601448426491508233930895755649869405162339251883008775165032123394239040705099716216279402849800838850950981610368235770451912324298036637295955929962482481888191347801633665207860165797597798831519174320276290046107532585800539861694122284697058136492270376156223710454841873933260740276640178212258527217081721693027611243767534321676277060036746843485408399065357740002708000609457236348955491018515453427879081623554506129807136433389945001993592811830189424095074622922985371614243848723931601896292370835976953286803111134307816439219191510955347801300158362176963619324450759962513927697331939202785771534315838313402567190182235326985174084227831310785899670881996541347342978753817173810545435491276918518420567836513660002590106877275897078985714421417586266886157606655819550183329449267631954783889", required = true, description = "The Klaytn RLP formats SigRLP or TxHashRLP are accepted as RLP values and only (partial) fee delegation transaction type is allowed.")
  public String getRlp() {
    return rlp;
  }

  public void setRlp(String rlp) {
    this.rlp = rlp;
  }

  public FDProcessRLPRequest submit(Boolean submit) {
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

  public FDProcessRLPRequest feeRatio(Long feeRatio) {
    this.feeRatio = feeRatio;
    return this;
  }

   /**
   * The ratio of the gas fee to be delegated. When it&#x27;s empty or 0, the entire fee will be delegated.
   * maximum: 99
   * @return feeRatio
  **/
  @Schema(example = "20", description = "The ratio of the gas fee to be delegated. When it's empty or 0, the entire fee will be delegated.")
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
    FDProcessRLPRequest fdProcessRLPRequest = (FDProcessRLPRequest) o;
    return Objects.equals(this.rlp, fdProcessRLPRequest.rlp) &&
        Objects.equals(this.submit, fdProcessRLPRequest.submit) &&
        Objects.equals(this.feeRatio, fdProcessRLPRequest.feeRatio);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rlp, submit, feeRatio);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FDProcessRLPRequest {\n");
    
    sb.append("    rlp: ").append(toIndentedString(rlp)).append("\n");
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
