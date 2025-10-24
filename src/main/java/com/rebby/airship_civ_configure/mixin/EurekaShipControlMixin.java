package com.rebby.airship_civ_configure.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.joml.Vector3dc;
import org.spongepowered.asm.mixin.injection.At;
import org.valkyrienskies.core.api.ships.PhysShip;
import org.valkyrienskies.eureka.ship.EurekaShipControl;

@org.spongepowered.asm.mixin.Mixin(EurekaShipControl.class)
public class EurekaShipControlMixin {

    @WrapOperation(
            method="applyForces",
            at = @At(value = "INVOKE", target = "Lorg/valkyrienskies/eureka/ship/StabilizeKt;stabilize(Lorg/valkyrienskies/core/api/ships/PhysShip;Lorg/joml/Vector3dc;Lorg/joml/Vector3dc;Lorg/valkyrienskies/core/api/ships/PhysShip;ZZ)V")
    )
    public void bypassStabilization(PhysShip stabilizationRotationAxisNormalized, Vector3dc idealVelocity, Vector3dc s, PhysShip shipUp, boolean worldUp, boolean angleBetween, Operation<Void> original){

    }
}
