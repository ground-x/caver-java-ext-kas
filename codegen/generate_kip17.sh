#!/bin/bash

swagger-codegen generate \
-i ./kip17.yaml \
-l java \
-o ./kip17 \
-c ./config_kip17.json;