/*
 * Copyright 2021 The caver-java-ext-kas Authors
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

package xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.tokenhistory.adapter;

import com.google.gson.*;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.tokenhistory.model.FtTransfer;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.tokenhistory.model.KlayTransfer;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.tokenhistory.model.NftTransfer;

import java.lang.reflect.Type;


public class TransferArrayItemAdapter implements JsonDeserializer {
    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        String type = jsonObject.getAsJsonPrimitive("transferType").getAsString();

        if(type.equals("ft")) {
            return context.deserialize(json, FtTransfer.class);
        } else if(type.equals("nft")) {
            return context.deserialize(json, NftTransfer.class);
        } else if (type.equals("klay")) {
            return context.deserialize(json, KlayTransfer.class);
        }

        return null;
    }
}

