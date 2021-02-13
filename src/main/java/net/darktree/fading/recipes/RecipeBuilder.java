package net.darktree.fading.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

public class RecipeBuilder {

    public static JsonObject makeShapeless( Item result, Item... items ) {
        JsonObject recipe = new JsonObject();
        JsonArray ingredients = new JsonArray();

        recipe.addProperty("type", "minecraft:crafting_shapeless");

        for( Item item : items ) {
            ingredients.add( itemAsKey(item) );
        }

        recipe.add("ingredients", ingredients);
        recipe.add("result", itemAsResult(result));
        return recipe;
    }

    private static JsonObject itemAsKey(Item item) {
        JsonObject key = new JsonObject();
        key.addProperty("item", Registry.ITEM.getId(item).toString());
        return key;
    }

    private static JsonObject itemAsResult( Item item ) {
        JsonObject result = itemAsKey(item);
        result.addProperty("count", 1);
        return result;
    }

}
