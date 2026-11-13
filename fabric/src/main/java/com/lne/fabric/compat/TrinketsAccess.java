package com.lne.fabric.compat;

import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public final class TrinketsAccess {
    private TrinketsAccess() {
    }

    public static List<ItemStack> equipped(LivingEntity entity) {
        var stacks = new ArrayList<ItemStack>();
        TrinketsApi.getTrinketComponent(entity).ifPresent(component ->
                component.getAllEquipped().forEach(pair -> stacks.add(pair.getRight())));
        return stacks;
    }
}
