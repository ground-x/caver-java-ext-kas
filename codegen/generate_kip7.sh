#!/bin/bash

swagger-codegen generate \
-i ./kip7.yaml \
-l java \
-o ./kip7 \
-c ./config_kip7.json;