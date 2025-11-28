package com.rebby.airship_civ_configure.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.valkyrienskies.core.api.ships.Ship;
import org.valkyrienskies.mod.mixin.feature.dismount_dead_entities.MixinLivingEntity;

@SuppressWarnings("ReferenceToMixin")
@Mixin(value = MixinLivingEntity.class, priority = 5000)
public abstract class MixinDismountEntities extends Entity {
    @Shadow
    @Final
    private static Logger VS$LOGGER;

    public MixinDismountEntities(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(
            method = "preDismountVehicle",
            at = @At(value = "INVOKE_ASSIGN", target = "Lorg/valkyrienskies/mod/common/VSGameUtilsKt;getShipManagingPos(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/Position;)Lorg/valkyrienskies/core/api/ships/Ship;"),
            cancellable = true,
            remap = false
    )
    private void dismountOnNullShip(Entity entity, CallbackInfo ci0, CallbackInfo ci1, @Local(name = "ship") final Ship ship){
        if(ship == null) {
            VS$LOGGER.debug("Modifying strange dismount");
            final Vec3 vec3 = new Vec3(this.getX(), this.getY(), this.getZ());
            this.dismountTo(vec3.x, vec3.y, vec3.z);
            ci0.cancel();
            ci1.cancel();
        }
    }
}
