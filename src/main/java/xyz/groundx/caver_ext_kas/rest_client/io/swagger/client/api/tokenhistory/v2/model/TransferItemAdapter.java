package xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.tokenhistory.v2.model;

import com.google.gson.*;

import java.lang.reflect.Type;


public class TransferItemAdapter implements JsonDeserializer {
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

