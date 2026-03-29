package com.iceyetiwins.fireflyInABottle;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;

public final class ModItems {
    public static Item register(Item item, ResourceKey<Item> registryKey) {
        return Registry.register(BuiltInRegistries.ITEM, registryKey.identifier(), item);
    }

    public static final ResourceKey<Item> FIREFLY_BOTTLE_KEY = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("firefly-in-a-bottle", "firefly_bottle"));

    public static final Item FIREFLY_BOTTLE = register(
            new FireflyBottle(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON).component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true).craftRemainder(Items.GLASS_BOTTLE).setId(FIREFLY_BOTTLE_KEY)),
            FIREFLY_BOTTLE_KEY
    );

    public static void initialize(){
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS)
                .register((itemGroup) -> itemGroup.accept(ModItems.FIREFLY_BOTTLE));
    }
}
