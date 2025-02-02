package com.iceyetiwins.fireflyInABottle;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public final class ModItems {
    public static Item register(Item item, RegistryKey<Item> registryKey) {
        return Registry.register(Registries.ITEM, registryKey.getValue(), item);
    }

    public static final RegistryKey<Item> FIREFLY_BOTTLE_KEY = RegistryKey.of(RegistryKeys.ITEM, Identifier.of("firefly-in-a-bottle", "firefly_bottle"));

    public static final Item FIREFLY_BOTTLE = register(
            new FireflyBottle(new Item.Settings().maxCount(1).rarity(Rarity.UNCOMMON).component(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true).recipeRemainder(Items.GLASS_BOTTLE).registryKey(FIREFLY_BOTTLE_KEY)),
            FIREFLY_BOTTLE_KEY
    );

    public static void initialize(){
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS)
                .register((itemGroup) -> itemGroup.add(ModItems.FIREFLY_BOTTLE));
    }
}
