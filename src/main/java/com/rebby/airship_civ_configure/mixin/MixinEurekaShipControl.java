package com.rebby.airship_civ_configure.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import org.joml.Vector3d;
import org.joml.Vector3dc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.valkyrienskies.core.api.ships.PhysShip;
import org.valkyrienskies.core.api.ships.ServerShip;
import org.valkyrienskies.core.impl.game.ships.PhysShipImpl;
import org.valkyrienskies.eureka.EurekaConfig;
import org.valkyrienskies.eureka.ship.EurekaShipControl;
import org.valkyrienskies.mod.common.VSGameUtilsKt;
import org.valkyrienskies.mod.common.ValkyrienSkiesMod;


@Mixin(value = EurekaShipControl.class)
public abstract class MixinEurekaShipControl {
    @Shadow(remap = false)
    private ServerShip ship;

    @WrapMethod(
            method = "getCanDisassemble",
            remap = false
    )
    private boolean wrapCanDisassemble(Operation<Boolean> original) {
        if (!original.call()) return false;
        ServerLevel level = VSGameUtilsKt.getLevelFromDimensionId(ValkyrienSkiesMod.getCurrentServer(), ship.getChunkClaimDimension());
        int minShipY = Mth.floor(ship.getWorldAABB().minY());
        int maxShipY = Mth.ceil(ship.getWorldAABB().maxY());
        return level != null && !(level.isOutsideBuildHeight(minShipY) || level.isOutsideBuildHeight(maxShipY));
    }

    @ModifyVariable(
            method = "getPlayerForwardVel",
            at = @At(value = "STORE"),
            name = "velOrthogonalToPlayerUp",
            remap = false
    )
    private Vector3d noDecelerationAtControl(Vector3d orig) {
        return new Vector3d();
    }

    @WrapOperation(
            method = "applyForces",
            at = @At(value = "INVOKE", target = "Lorg/valkyrienskies/eureka/ship/StabilizeKt;stabilize(Lorg/valkyrienskies/core/impl/game/ships/PhysShipImpl;Lorg/joml/Vector3dc;Lorg/joml/Vector3dc;Lorg/valkyrienskies/core/api/ships/PhysShip;ZZ)V"),
            remap = false
    )
    private void disableLinearStabilizer(PhysShipImpl physShipImpl, Vector3dc omega, Vector3dc vel, PhysShip physShip, boolean linear, boolean yaw, Operation<Void> original){
        original.call(physShipImpl, omega, vel, physShip, false, yaw);
    }
}
