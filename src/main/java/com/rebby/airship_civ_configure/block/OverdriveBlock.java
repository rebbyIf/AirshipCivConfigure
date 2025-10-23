package com.rebby.airship_civ_configure.block;

import com.rebby.airship_civ_configure.block.entity.ModBlockEntities;
import com.rebby.airship_civ_configure.block.entity.OverdriveBlockEntity;
import com.simibubi.create.content.kinetics.RotationPropagator;
import com.simibubi.create.content.kinetics.base.AbstractEncasedShaftBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.content.kinetics.transmission.GearshiftBlock;
import com.simibubi.create.content.kinetics.transmission.SplitShaftBlockEntity;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.ticks.TickPriority;
import org.jetbrains.annotations.NotNull;

public class OverdriveBlock extends AbstractEncasedShaftBlock implements IBE<SplitShaftBlockEntity> {
    public OverdriveBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Class<SplitShaftBlockEntity> getBlockEntityClass() {
        return SplitShaftBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends SplitShaftBlockEntity> getBlockEntityType() {
        return ModBlockEntities.OVERDRIVE_BLOCK_ENTITY.get();
    }


    @Override
    public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource random) {
        BlockEntity be = worldIn.getBlockEntity(pos);
        if (!(be instanceof KineticBlockEntity kte))
            return;
        RotationPropagator.handleAdded(worldIn, pos, kte);
    }
}
