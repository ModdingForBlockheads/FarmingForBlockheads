package net.blay09.mods.farmingforblockheads.registry.json;

import com.google.gson.*;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.farmingforblockheads.registry.market.MarketRegistryData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.lang.reflect.Type;

public class MarketRegistryDataSerializer implements JsonDeserializer<MarketRegistryData>, JsonSerializer<MarketRegistryData> {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(ResourceLocation.class, new ResourceLocation.Serializer())
            .registerTypeAdapter(ItemStack.class, new ItemStackSerializer())
            .create();

    @Override
    public MarketRegistryData deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        if (jsonObject.has("modId")) {
            String modId = jsonObject.get("modId").getAsString();
            if (!modId.equals("minecraft") && !Balm.isModLoaded(modId)) {
                MarketRegistryData result = new MarketRegistryData();
                result.setModId(modId);
                return result;
            }
        }

        return gson.fromJson(jsonObject, MarketRegistryData.class);
    }

    @Override
    public JsonElement serialize(MarketRegistryData itemStack, Type type, JsonSerializationContext context) {
        return new JsonObject();
    }
}
