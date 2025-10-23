package com.rebby.airship_civ_configure.block;

import com.rebby.airship_civ_configure.AirshipCivConfigure;
import com.rebby.airship_civ_configure.ModCreativeTabs;
import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.material.MapColor;

import static com.rebby.airship_civ_configure.ModCreativeTabs.AIRSHIP_CIV_CONFIGURE_TAB;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;

public class ModBlocks {

    static {
        AirshipCivConfigure.AIRSHIP_CIV_CONFIGURE_REGISTRATE.setCreativeTab(ModCreativeTabs.AIRSHIP_CIV_CONFIGURE_TAB);
    }

    public static final BlockEntry<OverdriveBlock> OVERDRIVE = AirshipCivConfigure.AIRSHIP_CIV_CONFIGURE_REGISTRATE
            .block("overdrive", OverdriveBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.noOcclusion().mapColor(MapColor.PODZOL))
            .transform(BlockStressDefaults.setNoImpact())
            .transform(axeOrPickaxe())
            .blockstate((c, p) -> BlockStateGen.axisBlock(c, p, $ -> AssetLookup.partialBaseModel(c, p)))
            .item()
            .transform(customItemModel())
            .register();

    // Loads this class
    public static void register(){}

}
