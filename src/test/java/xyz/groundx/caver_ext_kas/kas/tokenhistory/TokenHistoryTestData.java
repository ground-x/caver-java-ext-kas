/*
 * Copyright 2020 The caver-java-ext-kas Authors
 *
 * Licensed under the Apache License, Version 2.0 (the “License”);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an “AS IS” BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xyz.groundx.caver_ext_kas.kas.tokenhistory;

public class TokenHistoryTestData {
    public String ftAddress = "";
    public String nftAddress = "";
    public String mtAddress = "";
    public String tokenId = "";
    public String transactionHash = "";
    public String fromRange = "";
    public String toRange = "";
    public String account = "";

    public static TokenHistoryTestData loadDevData() {
        String ftAddress = "0xa904b57f567e07348186a3687cafc3b9d05d8608";
        String nftAddress = "0x8386ea0a21dcf47d489bf1505a84942babadb3b0";
        String mtAddress = "0xDbE9c814B10882bBEE42cbc75CE16A1620F5F042";
        String tokenId = "0x0";
        String transactionHash = "0xd7ca606d531ee9afc5aed7b43d9476be3776ca06e03e0db8f21c121436962fbb";
        String fromRange = "1611804103";
        String toRange = "1611904103";
        String account = "0x76c6b1f34562ed7a843786e1d7f57d0d7948a6f1";

        return new TokenHistoryTestData(ftAddress, nftAddress, mtAddress, tokenId, transactionHash, fromRange, toRange, account);
    }

    public static TokenHistoryTestData loadQAData() {
        String ftAddress = "0x854363D6E06E24809d867515e2bA7A82E0EF3aB2";
        String nftAddress = "0x6B152ee52fA2CF29D60389C5a0DC1a4932868CC3";
        String mtAddress = "0x9AE6E0807359869b1f5b3c73130C146017d562ce";
        String tokenId = "0x0";
        String transactionHash = "0xf257aab67d3b562217d2ebd87391091cbb4f710ac680201be29dc06af98c7965";
        String fromRange = "1613004103";
        String toRange = "1613616169";
        String account = "0x89a8e75d92ce84076d33f68e4909c4156847dc69";

        return new TokenHistoryTestData(ftAddress, nftAddress, mtAddress, tokenId, transactionHash, fromRange, toRange, account);
    }

    public static TokenHistoryTestData loadProdData() {
        String ftAddress = "0x4792f1e64d0f656e61516805b7d2cd99f9359043";
        String nftAddress = "0x18d9add7bf4097cc57dd6962ece441e391146682";
        String mtAddress = "0x6679A0006575989065832e17FE4F1e4eD4923390";
        String tokenId = "0x0";
        String transactionHash = "0x8b86a549dd73895fd72ea5b3430bc043f6d410d74a67004c673c0fc1b9a56534";
        String fromRange = "1611803332";
        String toRange = "1611903332";
        String account = "0x89a8e75d92ce84076d33f68e4909c4156847dc69";


        return new TokenHistoryTestData(ftAddress, nftAddress, mtAddress, tokenId, transactionHash, fromRange, toRange, account);
    }

    public TokenHistoryTestData(String ftAddress, String nftAddress, String mtAddress, String tokenId, String transactionHash, String fromRange, String toRange, String account) {
        this.ftAddress = ftAddress;
        this.nftAddress = nftAddress;
        this.mtAddress = mtAddress;
        this.tokenId = tokenId;
        this.transactionHash = transactionHash;
        this.fromRange = fromRange;
        this.toRange = toRange;
        this.account = account;
    }

    public String getFtAddress() {
        return ftAddress;
    }

    public String getNftAddress() {
        return nftAddress;
    }

    public String getMtAddress() {
        return mtAddress;
    }

    public String getTokenId() {
        return tokenId;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public String getAccount() {
        return account;
    }

    public String getFromRange() {
        return fromRange;
    }

    public String getToRange() {
        return toRange;
    }
}
