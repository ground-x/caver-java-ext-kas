/*
 * KAS Token History API v0.7.0
 * # Types Token History API는 다음과 같은 타입을 사용합니다.   ## KlayTransfer | Field Name         | Description                     | Example           | Type   | |--------------------|---------------------------------|-------------------|--------| | `feePayer`         | 수수료 대납 계정 주소 (20-byte) | \"0x7e57...f602\"   | string | | `feeRatio`         | 수수료 대납 비율                | 50                | number | | `fee`              | 가스비                          | \"0x5e63748a64800\" | string | | `from`             | 보낸 사람 EOA 주소 (20-byte)    | \"0x2828...6e22\"   | string | | `status`           | status                          | 1                 | number | | `timestamp`        | 트랜잭션 발생 시간 (timestamp)  | 1592180992        | number | | `to`               | 받은 사람 EOA 주소 (20-byte)    | \"0xce8e...cbf4\"   | string | | `transactionHash`  | 트랜잭션 해시                   | \"0x618e...d8c1\"   | string | | `transactionIndex` | 트랜잭션 인덱스                 | 1                 | number | | `transferType`     | 거래내역 유형                   | \"klay\"            | string | | `typeInt`          | 트랜잭션 유형                   | 10                | number | | `value`            | KLAY 전송량 (16진수)            | \"0xa\"             | string |    ## FtTransfer | Field Name       | Description                  | Example                | Type          | |------------------|------------------------------|------------------------|---------------| | `contract`       | 연관 컨트랙트                | *omitted*              | `FtContract`  | | `formattedValue` | `decimal`을 적용한 변환값    | \"0.000000000000000002\" | string        | | `from`           | 보낸 사람 EOA 주소 (20-byte) | \"0x2828...6e22\"        | string        | | `to`             | 받은 사람 EOA 주소 (20-byte) | \"0xce8e...cbf4\"        | string        | | `transaction`    | 거래내역                     | *omitted*              | `Transaction` | | `transferType`   | 거래내역 유형                | \"ft\"                   | string        | | `value`          | 전송한 토큰 개수 (16진수)    | \"0x2\"                  | string        |  ## NftTransfer | Field Name     | Description                  | Example         | Type          | |----------------|------------------------------|-----------------|---------------| | `contract`     | 연관 컨트랙트                | *omitted*       | `NftContract` | | `from`         | 보낸 사람 EOA 주소 (20-byte) | \"0x2828...6e22\" | string        | | `to`           | 받은 사람 EOA 주소 (20-byte) | \"0xce8e...cbf4\" | string        | | `tokenId`      | 토큰 식별자 (16진수)         | \"0x1\"           | string        | | `transaction`  | 거래내역                     | *omitted*       | `Transaction` | | `transferType` | 거래내역 유형                | \"nft\"           | string        |   ## Transaction | Field Name        | Description                               | Example           | Type   | |-------------------|-------------------------------------------|-------------------|--------| | `feePayer`        | 수수료 대납 계정 주소 (20-byte)           | \"0x7e57...f602\"   | string | | `feeRatio`        | 수수료 대납 비율                          | 50                | number | | `fee`             | 가스비                                    | \"0x5e63748a64800\" | string | | `from`            | 트랜잭션을 전송한 사람 EOA 주소 (20-byte) | \"0x2828...6e22\"   | string | | `timestamp`       | 트랜잭션 발생 시간 (timestamp)            | 1592180992        | number | | `transactionHash` | 트랜잭션 해시 (32-byte)                   | \"0xa762...01bf\"   | string | | `typeInt`         | 트랜잭션 유형                             | 10                | number | | `value`           | KLAY 전송량 (16진수)                      | \"0xa\"             | string |     ## FtContract | Field Name | Description             | Example                                      | Type   | |------------|-------------------------|----------------------------------------------|--------| | `address`  | 컨트랙트 주소 (20-byte) | \"0xc7565d24af561fe783aa73747ca0eda1f09f1118\" | string | | `decimals` | 토큰 자릿수             | 10                                           | number | | `name`     | 컨트랙트 이름           | \"Test Contract\"                              | string | | `symbol`   | 컨트랙트 심볼           | \"TSTC\"                                       | string |        ## NftContract | Field Name | Description             | Example                                      | Type   | |------------|-------------------------|----------------------------------------------|--------| | `address`  | 컨트랙트 주소 (20-byte) | \"0xc7565d24af561fe783aa73747ca0eda1f09f1118\" | string | | `name`     | 컨트랙트 이름           | \"Test Contract\"                              | string | | `symbol`   | 컨트랙트 심볼           | \"TSTC\"                                       | string |  ## FtContractDetail | Field Name    | Description                                                                                                        | Example         | Type   | |---------------|--------------------------------------------------------------------------------------------------------------------|-----------------|--------| | `address`     | 컨트랙트 주소 (20-byte)                                                                                            | \"0xdc8c...9dd8\" | string | | `createdAt`   | 라벨링된 시점 (timestamp)                                                                                          | 1592180992      | number | | `decimals`    | 토큰 자릿수                                                                                                        | 8               | number | | `deletedAt`   | 라벨링이 삭제된 시점 (timestamp); 삭제되지 않았을 경우 0                                                           | 0               | number | | `link`        | 컨트랙트 고유 정보, 컨트랙트를 대표하는 이미지의 URL `icon`과<br /> 컨트랙트를 대표하는 웹사이트 URL `website` 값을 가짐 | *omitted*       | object | | `name`        | 컨트랙트 이름                                                                                                      | \"Test Coin\"     | string | | `symbol`      | 컨트랙트 심볼                                                                                                      | \"TSTC\"          | string | | `totalSupply` | 총 발행량 (16진수로 표기)                                                                                          | \"0x174876e800\"  | string | | `type`        | 컨트랙트 유형 (kip, erc)                                                                                           | \"kip\"           | string | | `updatedAt`   | 라벨링 정보가 마지막으로 변경된 시점 (timestamp)                                                                   | 1592180992      | number |  ## NftContractDetail | Field Name    | Description                                              | Example         | Type   | |---------------|----------------------------------------------------------|-----------------|--------| | `address`     | 컨트랙트 주소 (20-byte)                                  | \"0x04a9...446f\" | string | | `name`        | 컨트랙트 이름                                            | \"Test Coin\"     | string | | `symbol`      | 컨트랙트 심볼                                            | \"TSTC\"          | string | | `totalSupply` | 총 발행량 (16진수로 표기)                                | \"0x174876e800\"  | string | | `type`        | 컨트랙트 유형 (kip, erc)                                 | \"erc\"           | string | | `createdAt`   | 라벨링된 시점 (timestamp)                                | 1592180992      | number | | `updatedAt`   | 라벨링 정보가 마지막으로 변경된 시점 (timestamp)         | 1592180992      | number | | `deletedAt`   | 라벨링이 삭제된 시점 (timestamp); 삭제되지 않았을 경우 0 | 0               | number |   ## NFT | Field Name        | Description                                    | Example                           | Type   | |-------------------|------------------------------------------------|-----------------------------------|--------| | `owner`           | 소유자 EOA 주소 (20-byte)                      | \"0x3926...1f5a\"                   | string | | `previousOwner`   | 직전 소유자 EOA 주소 (20-byte)                 | \"0x09d9...776f\"                   | string | | `tokenId`         | 토큰 ID (16진수)                               | \"0x1\"                             | string | | `tokenUri`        | 토큰 고유의 URL                                | \"https://link.to/your/token.json\" | string | | `transactionHash` | 마지막 트랜잭션 해시 (32-byte)                 | \"0x13f3...30b0\"                   | string | | `createdAt`       | 토큰이 생성된 시점 (timestamp)                 | 1592180992                        | number | | `updatedAt`       | 토큰 정보가 마지막으로 변경된 시점 (timestamp) | 1592180992                        | number |    ## NftOwnershipChange | Field Name  | Description                      | Example         | Type   | |-------------|----------------------------------|-----------------|--------| | `from`      | 직전 소유자 EOA 주소 (20-byte)   | \"0x09d9...776f\" | string | | `timestamp` | 소유권이 변경된 시점 (timestamp) | 1592180992      | number | | `to`        | 소유자 EOA 주소 (20-byte)        | \"0x3926...1f5a\" | string |   
 *
 * OpenAPI spec version: 0.7.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package io.swagger.client.api.tokenhistory.v2.model;

import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

/**
 * KlayTransfer
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2020-08-21T06:29:48.263Z")



public class KlayTransfer {
  @SerializedName("feePayer")
  private String feePayer = null;

  @SerializedName("feeRatio")
  private Integer feeRatio = null;

  @SerializedName("fee")
  private String fee = null;

  @SerializedName("from")
  private String from = null;

  @SerializedName("to")
  private String to = null;

  @SerializedName("transactionHash")
  private String transactionHash = null;

  @SerializedName("transactionIndex")
  private Integer transactionIndex = null;

  @SerializedName("transferType")
  private String transferType = null;

  @SerializedName("typeInt")
  private Integer typeInt = null;

  @SerializedName("value")
  private String value = null;

  public KlayTransfer feePayer(String feePayer) {
    this.feePayer = feePayer;
    return this;
  }

   /**
   * Fee payer address
   * @return feePayer
  **/
  @ApiModelProperty(example = "0xd0ea3e0eabaea095ea3ba231c043dbf8c0feb40a", required = true, value = "Fee payer address")
  public String getFeePayer() {
    return feePayer;
  }

  public void setFeePayer(String feePayer) {
    this.feePayer = feePayer;
  }

  public KlayTransfer feeRatio(Integer feeRatio) {
    this.feeRatio = feeRatio;
    return this;
  }

   /**
   * Proportion that the fee payer pays for
   * @return feeRatio
  **/
  @ApiModelProperty(example = "100", required = true, value = "Proportion that the fee payer pays for")
  public Integer getFeeRatio() {
    return feeRatio;
  }

  public void setFeeRatio(Integer feeRatio) {
    this.feeRatio = feeRatio;
  }

  public KlayTransfer fee(String fee) {
    this.fee = fee;
    return this;
  }

   /**
   * Gas fee in hex digits
   * @return fee
  **/
  @ApiModelProperty(example = "0xa455", required = true, value = "Gas fee in hex digits")
  public String getFee() {
    return fee;
  }

  public void setFee(String fee) {
    this.fee = fee;
  }

  public KlayTransfer from(String from) {
    this.from = from;
    return this;
  }

   /**
   * Sender address
   * @return from
  **/
  @ApiModelProperty(example = "0x5e47b195eeb11d72f5e1d27aebb6d341f1a9bedb", required = true, value = "Sender address")
  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public KlayTransfer to(String to) {
    this.to = to;
    return this;
  }

   /**
   * Recipient address
   * @return to
  **/
  @ApiModelProperty(example = "0xb4bf60383c64d47f2e667f2fe8f7ed0c9380f770", required = true, value = "Recipient address")
  public String getTo() {
    return to;
  }

  public void setTo(String to) {
    this.to = to;
  }

  public KlayTransfer transactionHash(String transactionHash) {
    this.transactionHash = transactionHash;
    return this;
  }

   /**
   * Transaction hash
   * @return transactionHash
  **/
  @ApiModelProperty(example = "0xc4a32f41e829f50c4e8774be68864f522120a2047db2143b59b3919ebd9234f8", required = true, value = "Transaction hash")
  public String getTransactionHash() {
    return transactionHash;
  }

  public void setTransactionHash(String transactionHash) {
    this.transactionHash = transactionHash;
  }

  public KlayTransfer transactionIndex(Integer transactionIndex) {
    this.transactionIndex = transactionIndex;
    return this;
  }

   /**
   * Transaction index
   * @return transactionIndex
  **/
  @ApiModelProperty(example = "1", required = true, value = "Transaction index")
  public Integer getTransactionIndex() {
    return transactionIndex;
  }

  public void setTransactionIndex(Integer transactionIndex) {
    this.transactionIndex = transactionIndex;
  }

  public KlayTransfer transferType(String transferType) {
    this.transferType = transferType;
    return this;
  }

   /**
   * Token type transferred
   * @return transferType
  **/
  @ApiModelProperty(example = "klay", required = true, value = "Token type transferred")
  public String getTransferType() {
    return transferType;
  }

  public void setTransferType(String transferType) {
    this.transferType = transferType;
  }

  public KlayTransfer typeInt(Integer typeInt) {
    this.typeInt = typeInt;
    return this;
  }

   /**
   * Transaction type
   * @return typeInt
  **/
  @ApiModelProperty(example = "50", required = true, value = "Transaction type")
  public Integer getTypeInt() {
    return typeInt;
  }

  public void setTypeInt(Integer typeInt) {
    this.typeInt = typeInt;
  }

  public KlayTransfer value(String value) {
    this.value = value;
    return this;
  }

   /**
   * Amount of KLAY transferred in hex digits
   * @return value
  **/
  @ApiModelProperty(example = "0xa", required = true, value = "Amount of KLAY transferred in hex digits")
  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    KlayTransfer klayTransfer = (KlayTransfer) o;
    return Objects.equals(this.feePayer, klayTransfer.feePayer) &&
        Objects.equals(this.feeRatio, klayTransfer.feeRatio) &&
        Objects.equals(this.fee, klayTransfer.fee) &&
        Objects.equals(this.from, klayTransfer.from) &&
        Objects.equals(this.to, klayTransfer.to) &&
        Objects.equals(this.transactionHash, klayTransfer.transactionHash) &&
        Objects.equals(this.transactionIndex, klayTransfer.transactionIndex) &&
        Objects.equals(this.transferType, klayTransfer.transferType) &&
        Objects.equals(this.typeInt, klayTransfer.typeInt) &&
        Objects.equals(this.value, klayTransfer.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(feePayer, feeRatio, fee, from, to, transactionHash, transactionIndex, transferType, typeInt, value);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class KlayTransfer {\n");
    
    sb.append("    feePayer: ").append(toIndentedString(feePayer)).append("\n");
    sb.append("    feeRatio: ").append(toIndentedString(feeRatio)).append("\n");
    sb.append("    fee: ").append(toIndentedString(fee)).append("\n");
    sb.append("    from: ").append(toIndentedString(from)).append("\n");
    sb.append("    to: ").append(toIndentedString(to)).append("\n");
    sb.append("    transactionHash: ").append(toIndentedString(transactionHash)).append("\n");
    sb.append("    transactionIndex: ").append(toIndentedString(transactionIndex)).append("\n");
    sb.append("    transferType: ").append(toIndentedString(transferType)).append("\n");
    sb.append("    typeInt: ").append(toIndentedString(typeInt)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
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

