package com.rebby.airship_civ_configure.mixin;

import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.valkyrienskies.core.api.ships.Ship;

@Mixin(value = LivingEntity.class, priority = 1500)
public abstract class MixinDismountEntities extends Entity {

    public MixinDismountEntities(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @TargetHandler(
            mixin = "org.valkyrienskies.mod.mixin.feature.dismount_dead_entities.MixinLivingEntity",
            name = "preDismountVehicle"
    )
    @Inject(
            method = "@MixinSquared:Handler",
            at = @At(value = "INVOKE_ASSIGN", target = "Lorg/valkyrienskies/mod/common/VSGameUtilsKt;getShipManagingPos(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/Position;)Lorg/valkyrienskies/core/api/ships/Ship;"),
            cancellable = true,
            remap = false
    )
    private void dismountOnNullShip(Entity entity, CallbackInfo ci0, CallbackInfo ci1, @Local(name = "ship") final Ship ship){
        if(ship == null) {
            final Vec3 vec3 = new Vec3(this.getX(), this.getY(), this.getZ());
            this.dismountTo(vec3.x, vec3.y, vec3.z);
            ci0.cancel();
            ci1.cancel();
        }
    }
}
