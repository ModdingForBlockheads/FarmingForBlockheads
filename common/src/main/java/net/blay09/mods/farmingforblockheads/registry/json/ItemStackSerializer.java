package net.blay09.mods.farmingforblockheads.registry.json;

import com.google.gson.*;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.lang.reflect.Type;

public class ItemStackSerializer implements JsonDeserializer<ItemStack>, JsonSerializer<ItemStack> {
    @Override
    public ItemStack deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonPrimitive()) {
            Item item = GsonHelper.convertToItem(json, "item");
            return new ItemStack(item);
        } else {
            JsonObject jsonObject = json.getAsJsonObject();
            Item item = GsonHelper.getAsItem(jsonObject, "item");
            int count = GsonHelper.getAsInt(jsonObject, "count", 1);
            ItemStack itemStack = new ItemStack(item, count);

            // Allow JSON to be specified as either String or Object. String JSON can be used to support Mojang's special JSON format with type identifiers.
            String jsonString = null;
            if (GsonHelper.isStringValue(jsonObject, "nbt")) {
                jsonString = GsonHelper.getAsString(jsonObject, "nbt");
            } else {
                JsonObject nbtJson = GsonHelper.getAsJsonObject(jsonObject, "nbt", new JsonObject());
                if (nbtJson.size() > 0) {
                    jsonString = nbtJson.toString();
                }
            }
            if (jsonString != null) {
                try {
                    CompoundTag tagFromJson = TagParser.parseTag(jsonString);
                    itemStack.setTag(tagFromJson);
                } catch (CommandSyntaxException e) {
                    FarmingForBlockheads.logger.error("Failed to parse nbt data for item stack {}x {}: ", item, count, e);
                }
            }

            return itemStack;
        }
    }

    @Override
    public JsonElement serialize(ItemStack itemStack, Type type, JsonSerializationContext context) {
        throw new UnsupportedOperationException("Serialization of ItemStack to JSON is not implemented");
    }
}
