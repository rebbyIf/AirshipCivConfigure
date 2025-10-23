package com.rebby.airship_civ_configure.block.entity;

import com.simibubi.create.content.kinetics.transmission.SplitShaftBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class OverdriveBlockEntity extends SplitShaftBlockEntity {
    public OverdriveBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public float getRotationSpeedModifier(Direction face) {
        if (hasSource()) {
            if (face != getSourceFacing()) {
                return 2;
            }
        }

        return 1;
    }
}
