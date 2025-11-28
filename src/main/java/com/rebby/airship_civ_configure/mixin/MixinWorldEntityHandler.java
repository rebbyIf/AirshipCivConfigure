package com.rebby.airship_civ_configure.mixin;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.valkyrienskies.mod.common.VSGameUtilsKt;
import org.valkyrienskies.mod.common.entity.handling.WorldEntityHandler;

@Mixin(WorldEntityHandler.class)
public class MixinWorldEntityHandler {
    @Inject(
            method = "positionSetFromVehicle",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private void positionCancel(Entity self, Entity vehicle, double x, double y, double z, CallbackInfo ci){
        if(VSGameUtilsKt.getShipManagingPos(self.level(), x, y, z) == null) ci.cancel();
    }
}
