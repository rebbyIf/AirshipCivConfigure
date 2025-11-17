package com.rebby.airship_civ_configure.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.rebby.airship_civ_configure.Config;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import org.joml.Vector3d;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.valkyrienskies.core.api.ships.ServerShip;
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

    @WrapOperation(
            method = "getPlayerForwardVel",
            remap = false,
            at = @At(value = "INVOKE", target = "Lorg/joml/Vector3d;normalize()Lorg/joml/Vector3d;")
    )
    private Vector3d unNormalize(Vector3d instance, Operation<Vector3d> original) {
        return instance.mul(Config.getServer().EUREKA_IMPULSE_SPEED_RATE.get());
    }

    @Redirect(
            method = "applyForces",
            at = @At(value = "FIELD", target = "Lorg/valkyrienskies/eureka/ship/EurekaShipControl;isCruising:Z",
                    opcode = Opcodes.GETFIELD, ordinal = 2),
            remap = false
    )
    private boolean maintainCruise(EurekaShipControl instance) {
        return true;
    }
}
