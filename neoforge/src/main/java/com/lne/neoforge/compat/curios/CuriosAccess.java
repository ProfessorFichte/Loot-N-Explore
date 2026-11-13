package com.lne.neoforge.compat.curios;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.ArrayList;
import java.util.List;

public final class CuriosAccess {
    private CuriosAccess() {
    }

    public static List<ItemStack> equipped(LivingEntity entity) {
        var stacks = new ArrayList<ItemStack>();
        CuriosApi.getCuriosInventory(entity).ifPresent(handler -> {
            var equipped = handler.getEquippedCurios();
            for (int slot = 0; slot < equipped.getSlots(); slot++) {
                stacks.add(equipped.getStackInSlot(slot));
            }
        });
        return stacks;
    }
}
