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

package xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.tokenhistory.model.adapter;

import com.google.gson.*;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.tokenhistory.model.FTTokenSummary;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.tokenhistory.model.MTTokenSummary;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.tokenhistory.model.NFTTokenSummary;

import java.lang.reflect.Type;

public class TokenSummaryAdapter implements JsonDeserializer {
    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        String kind = jsonObject.getAsJsonPrimitive("kind").getAsString();

        if(kind.equals("ft")) {
            return context.deserialize(json, FTTokenSummary.class);
        } else if(kind.equals("nft")) {
            return context.deserialize(json, NFTTokenSummary.class);
        } else if (kind.equals("mt")) {
            return context.deserialize(json, MTTokenSummary.class);
        }

        return null;

    }
}
