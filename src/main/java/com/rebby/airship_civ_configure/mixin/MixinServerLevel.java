package com.rebby.airship_civ_configure.mixin;

import com.bawnorton.mixinsquared.TargetHandler;
import com.mojang.logging.LogUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.valkyrienskies.core.api.ships.ServerShip;
import org.valkyrienskies.core.api.ships.Ship;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

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

    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    private void removeVoidShips(BooleanSupplier pHasTimeLeft, CallbackInfo ci){
        List<Ship> voidedShips = new ArrayList<>();
        VSGameUtilsKt.getAllShips(ServerLevel.class .cast(this)).forEach(
                ship -> {
                    if (ship.getTransform().getPositionInWorld().y() < -128) {
                        voidedShips.add(ship);
                    }
                }
        );
        voidedShips.forEach(
                ship -> {
                    LogUtils.getLogger().info("Removing ship {} from void", ship.getSlug());
                    VSGameUtilsKt.getShipObjectWorld(ServerLevel.class.cast(this)).deleteShip((ServerShip) ship);
                }
        );
    }
}
