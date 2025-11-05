package com.rebby.airship_civ_configure.mixin;

import com.rebby.airship_civ_configure.Config;
import org.joml.Vector3dc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.valkyrienskies.clockwork.content.forces.PropellerController;

@Mixin(PropellerController.class)
public class MixinPropellerController {
    @Inject(
            method = "airPressure",
            at = @At("RETURN"),
            remap = false,
            cancellable = true
    )
    private void airPressureRate(Vector3dc pos, CallbackInfoReturnable<Double> cir){
        double ln_per_block = Math.log(Config.getServer().CW_PROP_THRUST_DECREASE.get()) / 100;
        cir.setReturnValue(Config.getServer().CW_PROP_THRUST_BASE.get() * Math.exp(pos.y() * ln_per_block));
    }
}
