package com.rebby.airship_civ_configure.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.simibubi.create.content.contraptions.bearing.IBearingBlockEntity;
import com.simibubi.create.content.kinetics.KineticNetwork;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.valkyrienskies.clockwork.content.contraptions.propeller.PropellerBearingBlockEntity;

import java.util.List;


@Mixin(PropellerBearingBlockEntity.class)
public abstract class MixinPropellerBearingBlockEntity extends KineticBlockEntity implements IBearingBlockEntity {

    @Unique
    float airciv$stress = 0.0f;

    @Shadow
    private List<BlockPos> sailPositions;

    public MixinPropellerBearingBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Inject(
            method = "assemble",
            at = @At("RETURN"),
            remap = false
    )
    private void updateStress(CallbackInfo ci) {
        KineticNetwork network1 = getOrCreateNetwork();
        if (network1 != null) network1.updateStressFor(this, getModifiedStress());
    }

    @Inject(
            method = "disassemble",
            at = @At("RETURN"),
            remap = false
    )
    private void removeStress(CallbackInfo ci) {
        KineticNetwork network1 = getOrCreateNetwork();
        if (network1 != null) network1.updateStressFor(this, 0.0f);
    }

    @WrapMethod(
            method = "read"
    )
    private void readTag(CompoundTag compound, boolean clientPacket, Operation<Void> original) {
        original.call(compound, clientPacket);
        airciv$stress = compound.getFloat("airCivPropStress");
    }

    @WrapMethod(
            method = "write"
    )
    private void writeTag(CompoundTag compound, boolean clientPacket, Operation<Void> original) {
        original.call(compound, clientPacket);
        compound.putFloat("airCivPropStress", airciv$stress);
    }

    @Inject(
            method = "calculateStressApplied",
            at = @At(value = "RETURN", ordinal = 0),
            cancellable = true,
            remap = false
    )
    private void wrapStressApplied(CallbackInfoReturnable<Float> cir){
        float stress = getModifiedStress();
        this.lastStressApplied = stress;
        cir.setReturnValue(stress);
    }

    @Unique
    public float getModifiedStress() {
        if (!level.isClientSide) {
            airciv$stress = 0.0f;
            Vec3 axis = Vec3.atLowerCornerOf(this.getBlockState().getValue(BlockStateProperties.FACING).getNormal());
            for (BlockPos pos : sailPositions) {
                Vec3 posDouble = Vec3.atLowerCornerOf(pos);
                double distSqr = posDouble.lengthSqr();
                double distAxisSqr = Mth.square(axis.dot(posDouble));
                airciv$stress += (float) Math.sqrt(distSqr - distAxisSqr);
            }
        }
        return airciv$stress * 2;
    }
}
