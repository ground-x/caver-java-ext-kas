# caver-java-ext-kas

caver-java-ext-kas is [caver-java](https://github.com/klaytn/caver-java)'s extension library for using KAS(Klaytn API service).

## Table of contents
  * [Installation](#installation)
  * [Getting Started](#getting-started)
    * [Initialize API](#initialize-api)
    * [Use Node API](#use-node-api)
    * [Use Token History API](#use-token-history-api)
    * [Use Wallet API](#use-wallet-api)
    * [Use Anchor API](#use-anchor-api)
  * [Test](#test)

## Installation

### Installation

#### add a Repository

To install caver-java-ext-kas, you need to install caver-java.
To install the latest version of caver-java, you should add a jitpack repository for IPFS feature.

**maven**
```groovy
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

**gradle**
```groovy
allprojects {
    repositories {
        ... //mavenCentral() or jcenter()
        maven { url 'https://jitpack.io' }
    }
}
```

#### add a dependency

**maven**
```groovy
<dependency>
  <groupId>xyz.groundx.caver</groupId>
  <artifactId>caver-java-ext-kas</artifactId>
  <version>X.X.X</version>
  <type>pom</type>
</dependency>
```

```groovy
<dependency>
  <groupId>xyz.groundx.caver</groupId>
  <artifactId>caver-java-ext-kas</artifactId>
  <version>X.X.X-android</version>
  <type>pom</type>
</dependency>
```

**gradle**
```groovy
implementation 'xyz.groundx.caver:caver-java-ext-kas:X.X.X'
```

```groovy
implementation 'xyz.groundx.caver:caver-java-ext-kas:X.X.X-android'
```
You can find latest caver-java-ext-kas version at [release page](https://github.com/ground-x/caver-java-ext-kas/releases).

caver-java-ext-kas requires at minimum Java 8+.

## Getting Started

### Initialize API

For now, you can use Node API, Token History API, Wallet API and Anchor API provided by KAS through this library. 
To use KAS API, the following items are required.
  - Access key, secret access key issued by KAS console.
  - Base URL to use the API provided by KAS.
  - The Klaytn network chain id to be used.

You can activate KAS API by writing code as below.

`initKASAPI()` function can initialize all API provided by KAS.

```java
CaverExtKAS caver = new CaverExtKAS();
caver.initKASAPI(chain_id, accessKey, secretAccessKey);
``` 

If you want to initialize API each other, you can use `initialize***API()` instead. 

```java
CaverExtKAS caver = new CaverExtKAS();
caver.initNodeAPI(chain ID, accessKey, secretAccessKey);
caver.initWalletAPI(chain ID, accessKey, secretAccessKey);
caver.initTokenHistoryAPI(chain ID, accessKey, secretAccessKey);
caver.initAnchorAPI(chain ID, accessKey, secretAccessKey);
```

### Use Node API
You can now use Node API through `com.klaytn.caver.rpc.Klay` class in caver-java library. You can send a Node API request to the KAS as shown below and check the results.

```java
public void getBlockNumber() {
    try {
        Quantity response = caver.rpc.klay.getBlockNumber().send();
    } catch(Exception e) {
        // Handle error.
    }
```

### Use Token History API
You can use Token History API through caver-java-ext-kas. You can send a Token History API request to the KAS as shwon below.

```java 
public void getFTContractList() {
    try {
        PageableFtContractDetails details = caver.kas.tokenHistory.getFTContractList();
        assertNotNull(details);
    } catch (ApiException e) {
        //Handle error
    }
}
```

You can find query options required each Token History API in KAS Docs. 
For defining query options, you can use `TokenHistoryQueryOptions` class.

```java
TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
options.setCaFileter(..);
options.setKind(..);
options.setRange(..);
options.setSize(..);
options.setSize(..);
options.setStatus(..);
options.setType(..);
```

```java
public void getNFTContractList() {
    try {
        TokenHistoryQueryOptions options = new TokenHistoryQueryOptions();
        options.setStatus("processing");
        PageableNftContractDetails details = caver.kas.tokenHistory.getNFTContractList(options);
        assertNotNull(details);
    } catch (ApiException e) {
        //handle error
    }
}
```

### Use Wallet API

You can use Wallet API through caver-java-ext-kas. You can send a Wallet API request to the KAS as shwon below.

```java
public void createAccount() {
    try {
        Account account = caver.kas.wallet.createAccount();
        assertNotNull(account);
    } catch (ApiException e) {
        //handle error
    }
}
```

You can find query options required each Wallet API in KAS Docs. 
For defining query options, you can use `WalletQueryOptions` class.

```java
WalletQueryOptions options = new WalletQueryOptions();
options.setSize();
options.setCursor();
options.setFromTimestamp();
options.setToTimesatamp();
```


### Use Anchor API

You can use Anchor API through caver-java-ext-kas. You can send a Anchor API request to the KAS as shwon below.

```java
public void getOperators() {
    try {
        Operators res = caver.kas.anchor.getOperators();
        assertNotNull(res);
    } catch(ApiException e) {
        //handle error
    }
}
```

You can find query options required each Anchor API in KAS Docs. 
For defining query options, you can use `AnchorQueryOptions` class.

```java
AnchorQueryOptions options = new AnchorQueryOptions();
options.setSize();
options.setCursor();
options.setFromTimestamp();
options.setToTimesatamp();
```

### Introduced KASWallet
KASWallet allows you to handle transaction instance in caver-java by using KAS Wallet API. 
  - Generate and manage accounts by using KAS Wallet API.
  - Sign a transaction instance in caver-java by using KAS Wallet API.

KASWallet can be used as a member called `wallet` of the `CaverExtKAS` class.
The `CaverExtKAS` class can provide the same usability as the 'wallet' of `Caver` class in caver-java through KASWallet.
Also, `Contract`, `KIP7`, `KIP17` classes in caver-java can be used the same as the existing caver-java.

Here we introduced a simple example using Contract, KIP7 and KIP17 respectively. Please refer to Contract, KIP7 and KIP17 of [Klaytn Docs](https://docs.klaytn.com/bapp/sdk/caver-java/getting-started#smart-contract) for detailed usage.

#### Use Contract class with KASWallet
```java
final String ABI = "[{\"constant\":true,\"inputs\":[{\"name\":\"key\",\"type\":\"string\"}],\"name\":\"get\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},...]";
final String BINARY = "Smart contract binary Data";
String accessKey = "your access key";
String secretAccessKey = "your secret access key";
CaverExtKAS caver = new CaverExtKAS(ChainId.BAOBAB_TESTNET, accessKey, secretAccessKey);

Contract contract = new Contract(caver, abi);

//Deploy Contract
String account = "0x{address}";
BigInteger gas = BigInteger.valueOf(10000000);
SendOptions sendOptions = new SendOptions(account, gas);
contract.deploy(sendOptions, BINARY);

//Execute contract's "set" function.
SendOptions sendOptions = new SendOptions(account, BigInteger.valueOf(5000000));
TransactionReceipt.TransactionReceiptData receiptData = contract.send(sendOptions, "set", "key", "value");
```

#### Use KIP7 class with KASWallet
```java
String accessKey = "your access key";
String secretAccessKey = "your secret access key";
CaverExtKAS caver = new CaverExtKAS(ChainId.BAOBAB_TESTNET, accessKey, secretAccessKey);

String from = "0x{from address}";
String to = "0x{to address}";

//deploy KIP7 contract
BigInteger initialSupply = BigInteger.TEN.multiply(BigInteger.TEN.pow(18)); // 10 * 10^18
KIP7 kip7 = KIP7.deploy(caver, from, "KAS", "SDK", 18, iniinitialSupply);

//execute KIP7's transfer function.
BigInteger transferAmount = BigInteger.ONE.multiply(BigInteger.TEN.pow(18));
SendOptions sendOptions = new SendOptions(from, (String)null);
TransactionReceipt.TransactionReceiptData receiptData = kip7.transfer(to, amount, sendOptions);
```

#### Use KIP17 class with KASWallet
```java
String accessKey = "your access key";
String secretAccessKey = "your secret access key";
CaverExtKAS caver = new CaverExtKAS(ChainId.BAOBAB_TESTNET, accessKey, secretAccessKey);

//deploy KIP17 contract.
String from = "0x{from address}";
KIP17 kip17 = KIP17.deploy(caver, from, name, symbol);

//execute KIP17's mint function.
SendOptions sendOptions = new SendOptions(from, (String)null);
TransactionReceipt.TransactionReceiptData receiptData = kip17.mint(kip17, from, BigInteger.ZERO);
```

#### Handling KASAPIException
When an error occurs while using KAS Wallet API in `KASWallet` class, it throws `KASAPIException`(extends RuntimeException).
KASAPIException has HTTP Error code and message and also it contains response body.

Below is an example code that handles the error that occurred while executing the KAS Wallet API.

```java
try {
    String unKnownAddress = "0x785ba1146cc1bed97b9c8d73e9293cc3b6bc3691";
    Account account = caver.wallet.getAccount(unKnownAddress);
} catch (KASAPIException e) {
    System.out.println(e.getcode()); // 400
    System.out.println(e.getMessage()); // "Bad Request"
    System.out.println(e.getResponseBody().getCode()); // 1061010
    System.out.println(e.getResponseBody().getMessage()); // "data don't exist"
}
```

## Test
Before testing, you need to either modify the data in "Config.java" or use ".env" to set the data required for testing.
If you use ".env", you must set variable below. 

```groovy
ACCESS_KEY= //KAS service access key
SECRET_ACCESS_KEY= //KAS service secret access key
SENDER_PRV_KEY= // A account's private key that has klay 
FEE_PAYER_ADDR= // UserFeePayer address set in console(Wallet)
OPERATOR= // Operator address set in console(Anchor)
PRESET= // Preset set in console(Token History)
```
