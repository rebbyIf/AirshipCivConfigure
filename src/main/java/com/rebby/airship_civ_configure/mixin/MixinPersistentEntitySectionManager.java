package com.rebby.airship_civ_configure.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.valkyrienskies.core.api.ships.LoadedShip;
import org.valkyrienskies.mod.common.VSGameUtilsKt;
import org.valkyrienskies.mod.common.entity.handling.VSEntityManager;
import org.valkyrienskies.mod.common.util.VectorConversionsMCKt;

@Mixin(PersistentEntitySectionManager.class)
public class MixinPersistentEntitySectionManager {

    @Inject(
            method = "addEntity",
            at = @At(value = "HEAD")
    )
    <T extends EntityAccess> void preAddEntity(T entityAccess, boolean bl, CallbackInfoReturnable<Boolean> cir) {
        if (entityAccess instanceof final Entity entity) {
            final LoadedShip ship =
                    VSGameUtilsKt.getShipObjectManagingPos(entity.level(), VectorConversionsMCKt.toJOML(entity.position()));
            if (ship != null) {
                VSEntityManager.INSTANCE.getHandler(entity).freshEntityInShipyard(entity, ship);
            }
        }
    }
}
