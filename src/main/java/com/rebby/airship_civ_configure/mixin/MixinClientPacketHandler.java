package com.rebby.airship_civ_configure.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.valkyrienskies.core.api.ships.ClientShip;
import org.valkyrienskies.mod.common.VSGameUtilsKt;
import org.valkyrienskies.mod.common.entity.handling.WorldEntityHandler;

@Mixin(ClientPacketListener.class)
public abstract class MixinClientPacketHandler {
    @WrapOperation(
            method = "handleTakeItemEntity",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/multiplayer/ClientLevel;getEntity(I)Lnet/minecraft/world/entity/Entity;")
    )
    private Entity setPositionBackToWorld(ClientLevel level, int i, Operation<Entity> getEntity) {
        Entity entity = getEntity.call(level, i);
        if(VSGameUtilsKt.getShipManaging(entity) instanceof ClientShip ship) {
            WorldEntityHandler.INSTANCE.moveEntityFromShipyardToWorld(entity, ship);
        }
        return entity;
    }
}
