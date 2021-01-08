package net.darktree.fading.util;

import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Map;
import java.util.Random;

public class Utils {

    private static Map<Item, Integer> fuelTimeMap = null;

    private static void createFuelMap() {
        fuelTimeMap = AbstractFurnaceBlockEntity.createFuelTimeMap();
    }

    public static boolean isFuel(ItemStack stack) {
        if( fuelTimeMap == null ) { createFuelMap(); }
        return fuelTimeMap.containsKey(stack.getItem());
    }

    public static int strangeInt( Random random, int max ) {
        if( max > 0 ) {
            return random.nextInt( max );
        }else{
            return max;
        }
    }

}
