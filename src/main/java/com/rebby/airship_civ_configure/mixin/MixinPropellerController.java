package com.rebby.airship_civ_configure.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import org.joml.Vector3dc;
import org.spongepowered.asm.mixin.Mixin;
import org.valkyrienskies.clockwork.content.forces.PropellerController;

@Mixin(PropellerController.class)
public class MixinPropellerController {
    @WrapMethod(
            method = "airPressure"
    )
    private double airPressureConstant(Vector3dc pos, Operation<Double> original){
        return 1.0F;
    }
}
