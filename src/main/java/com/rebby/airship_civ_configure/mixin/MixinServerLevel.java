package com.rebby.airship_civ_configure.mixin;

import com.bawnorton.mixinsquared.TargetHandler;
import com.mojang.logging.LogUtils;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ServerLevel.class, priority = 3000)
public class MixinServerLevel {

    @TargetHandler(
            mixin = "org.valkyrienskies.mod.mixin.feature.shipyard_entities.MixinServerLevel",
            name = "preAddEntity"
    )
    @Inject(
            method = "@MixinSquared:Handler",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    private void cancelPreAddEntity(Entity entity, CallbackInfoReturnable<Boolean> cir, CallbackInfo ci){
        ci.cancel();
    }
}
