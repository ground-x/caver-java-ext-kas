#!/bin/bash

swagger-codegen generate \
-i ./tokenHistory.yaml \
-l java \
-o ./tokenHistory \
-c ./config_tokenHistory.json;