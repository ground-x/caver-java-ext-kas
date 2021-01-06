#!/bin/bash

swagger-codegen generate \
-i ./anchor.yaml \
-l java \
-o ./anchor \
-c ./config_anchor.json;