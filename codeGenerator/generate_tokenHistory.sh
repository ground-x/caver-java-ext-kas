#!/bin/bash

# ./generate_wallet.sh -> This case `master` branch will be used as a default
# ./generate_wallet.sh {branch name}

./generate_client_code.sh tokenhistory v2 tokenhistory $1

# apply a json adapter.
cd ../src/main/java/xyz/groundx/caver_ext_kas/rest_client/io/swagger/client/api/tokenhistory/model

sed -i 's/package xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.tokenhistory.model;/package xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.tokenhistory.model;\n\nimport com.google.gson.annotations.JsonAdapter;\nimport xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.tokenhistory.adapter.ContractSummaryArrayAdapter;/' ./AnyOfContractSummaryArrayItems.java
sed -i 's/package xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.tokenhistory.model;/package xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.tokenhistory.model;\n\nimport com.google.gson.annotations.JsonAdapter;\nimport xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.tokenhistory.adapter.TransferArrayItemAdapter;/' ./AnyOfTransferArrayItems.java
sed -i 's/package xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.tokenhistory.model;/package xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.tokenhistory.model;\n\nimport com.google.gson.annotations.JsonAdapter;\nimport xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.tokenhistory.adapter.TokenSummaryArrayAdapter;/' ./AnyOfTokenSummaryArrayItems.java

sed -i 's/public interface AnyOfContractSummaryArrayItems {/@JsonAdapter(ContractSummaryArrayAdapter.class)\npublic interface AnyOfContractSummaryArrayItems {/' ./AnyOfContractSummaryArrayItems.java
sed -i 's/public interface AnyOfTokenSummaryArrayItems {/@JsonAdapter(TokenSummaryArrayAdapter.class)\npublic interface AnyOfTokenSummaryArrayItems {/' ./AnyOfTokenSummaryArrayItems.java
sed -i 's/public interface AnyOfTransferArrayItems {/@JsonAdapter(TransferArrayItemAdapter.class)\npublic interface AnyOfTransferArrayItems {/' ./AnyOfTransferArrayItems.java