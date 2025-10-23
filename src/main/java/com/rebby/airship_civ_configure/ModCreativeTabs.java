package com.rebby.airship_civ_configure;

import com.rebby.airship_civ_configure.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AirshipCivConfigure.MODID);

    public static final RegistryObject<CreativeModeTab> AIRSHIP_CIV_CONFIGURE_TAB =
            TABS.register("airship_civ_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.airship_civ_tab"))
                    .icon(() -> new ItemStack(ModItems.STEEL_SHEET.get()))
                    .displayItems((displayParams, output) -> {
                        output.accept(ModItems.STEEL_SHEET.get());
                    })
                    .build());
}
