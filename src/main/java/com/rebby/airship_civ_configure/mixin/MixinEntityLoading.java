package com.rebby.airship_civ_configure.mixin;

import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Entity.class, priority = 1500)
public abstract class MixinEntityLoading {

    @Unique
    private boolean isLoading = false;

    @WrapMethod(
            method = "load"
    )
    private void loadWithNoSetPosVS(CompoundTag pCompound, Operation<Void> original){
        isLoading = true;
        original.call(pCompound);
        isLoading = false;
    }

    @TargetHandler(
            mixin = "org.valkyrienskies.mod.mixin.feature.shipyard_entities.MixinEntity",
            name = "handlePosSet"
    )
    @Inject(
            method = "@MixinSquared:Handler",
            at = @At("HEAD"),
            cancellable = true
    )
    private void handlePosSet(final double x, final double y, final double z, final CallbackInfo ci0, final CallbackInfo ci1) {
        if(isLoading) ci1.cancel();
    }
}
