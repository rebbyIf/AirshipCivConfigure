package com.rebby.airship_civ_configure.block.entity;

import com.rebby.airship_civ_configure.AirshipCivConfigure;
import com.rebby.airship_civ_configure.block.ModBlocks;
import com.simibubi.create.content.kinetics.transmission.SplitShaftInstance;
import com.simibubi.create.content.kinetics.transmission.SplitShaftRenderer;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

public class ModBlockEntities {

    public static final BlockEntityEntry<OverdriveBlockEntity> OVERDRIVE_BLOCK_ENTITY =
            AirshipCivConfigure.AIRSHIP_CIV_CONFIGURE_REGISTRATE.blockEntity("overdrive_block", OverdriveBlockEntity::new)
                    .instance(() -> SplitShaftInstance::new, false)
                    .validBlocks(ModBlocks.OVERDRIVE)
                    .renderer(() -> SplitShaftRenderer::new)
                    .register();

    // Loads this class
    public static void register(){}
}
