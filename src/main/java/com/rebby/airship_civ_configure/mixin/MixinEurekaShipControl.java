package com.rebby.airship_civ_configure.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.valkyrienskies.core.api.ships.ServerShip;
import org.valkyrienskies.eureka.ship.EurekaShipControl;
import org.valkyrienskies.mod.common.VSGameUtilsKt;
import org.valkyrienskies.mod.common.ValkyrienSkiesMod;


@Mixin(value = EurekaShipControl.class)
public abstract class MixinEurekaShipControl {
    @Shadow
    private ServerShip ship;

    @WrapMethod(
            method = "getCanDisassemble"
    )
    private boolean wrapCanDisassemble(Operation<Boolean> original) {
        if (!original.call()) return false;
        ServerLevel level = VSGameUtilsKt.getLevelFromDimensionId(ValkyrienSkiesMod.getCurrentServer(), ship.getChunkClaimDimension());
        int minShipY = Mth.floor(ship.getWorldAABB().minY());
        int maxShipY = Mth.ceil(ship.getWorldAABB().maxY());
        return level != null && !(level.isOutsideBuildHeight(minShipY) || level.isOutsideBuildHeight(maxShipY));
    }
}
