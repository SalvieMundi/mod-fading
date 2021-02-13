package net.darktree.fading.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.darktree.fading.Fading;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

import java.util.Map;

public class Recipes {

    public static JsonObject FLINT_AND_FLINT = null;
    public static JsonObject FLINT_AND_GOLD = null;
    public static JsonObject FLINT_AND_DIAMOND = null;

    public static void init() {
        FLINT_AND_FLINT = RecipeBuilder.makeShapeless( Fading.FLINT_AND_FLINT, Items.FLINT, Items.FLINT );
        FLINT_AND_GOLD = RecipeBuilder.makeShapeless( Fading.FLINT_AND_GOLD, Items.FLINT, Items.GOLD_INGOT );
        FLINT_AND_DIAMOND = RecipeBuilder.makeShapeless( Fading.FLINT_AND_DIAMOND, Items.FLINT, Items.DIAMOND );
    }

    public static void inject( Map<Identifier, JsonElement> map ) {
        map.put( new Identifier(Fading.NAMESPACE, "flint_and_flint"), FLINT_AND_FLINT );
        map.put( new Identifier(Fading.NAMESPACE, "flint_and_gold"), FLINT_AND_GOLD );
        map.put( new Identifier(Fading.NAMESPACE, "flint_and_diamond"), FLINT_AND_DIAMOND );
    }

}
