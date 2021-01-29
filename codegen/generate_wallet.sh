#!/bin/bash

swagger-codegen generate \
-i ./wallet.yaml \
-l java \
-o ./wallet \
-c ./config_wallet.json;