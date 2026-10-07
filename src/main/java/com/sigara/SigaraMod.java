package com.sigara;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class SigaraMod implements ModInitializer {
    public static final String MOD_ID = "sigara";

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        ModItems.init();

        CreativeModeTab tab = FabricItemGroup.builder()
                .title(Component.translatable("itemGroup.sigara"))
                .icon(() -> new ItemStack(ModItems.ITEMS.get("pack_classic")))
                .displayItems((params, output) -> ModItems.ITEMS.values().forEach(i -> output.accept(i)))
                .build();
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
                ResourceKey.create(Registries.CREATIVE_MODE_TAB, id("sigara")), tab);
    }
}
