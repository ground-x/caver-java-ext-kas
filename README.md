# caver-java-ext-kas

caver-java-ext-kas is [caver-java](https://github.com/klaytn/caver-java)'s extension library for using KAS(Klaytn API service).

## Table of contents
  * [Installation](#installation)
  * [Getting Started](#getting-started)
    * [Activate API](#activate-api)
    * [Use Node API](#use-node-api)
    * [Use Token History API](#use-token-history-api)
    * [Use Wallet API](#use-wallet-api)
    * [Use Anchor API](#use-anchor-api)

## Installation

#### maven
```groovy
TBD
```
#### gradle
```groovy
TBD
```
caver-java-ext-kas requires at minimum Java 8+.

## Getting Started

### Initialize API

For now, you can use Node API, Token History API, Wallet API and Anchor API provided by KAS through this library. 
To use KAS API, the following items are required.
  - Access key, secret access key issued by KAS console.
  - Base URL to use the API provided by KAS.
  - The Klaytn network chain id to be used.

You can activate KAS API by writing code as below.

```java
CaverExtKAS caver = new CaverExtKAS();
caver.initNodeAPI("base Node API url", chain ID, accessKey, secretAccessKey);
caver.initWalletAPI("base Wallet API url", chain ID, accessKey, secretAccessKey);
caver.initTokenHistoryAPI("base Token History API url", chain ID, accessKey, secretAccessKey);
caver.initAnchorAPI("base Anchor API url", chain ID, accessKey, secretAccessKey);
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
        PageableFtContractDetails details = caver.kas.getTokenHistoryAPI().getFTContractList();
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
        PageableNftContractDetails details = caver.kas.getTokenHistoryAPI().getNFTContractList(options);
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
        Account account = kas.getWalletAPI().createAccount();
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
        Operators res = caver.kas.getAnchorAPI().getOperators();
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