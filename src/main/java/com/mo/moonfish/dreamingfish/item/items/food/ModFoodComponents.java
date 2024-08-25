package com.mo.moonfish.dreamingfish.item.items.food;

import net.minecraft.item.FoodComponent;

public class ModFoodComponents {
    public static final FoodComponent FISH_SOUP = createStew(6).build();

    private static FoodComponent.Builder createStew(int hunger) {
        return new FoodComponent.Builder().hunger(hunger).saturationModifier(0.6F);
    }
}
