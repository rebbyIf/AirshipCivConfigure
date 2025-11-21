package com.rebby.airship_civ_configure.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.logging.LogUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.valkyrienskies.core.api.ships.Ship;
import org.valkyrienskies.mod.common.VSGameUtilsKt;
import org.valkyrienskies.mod.common.entity.handling.DefaultShipyardEntityHandler;
import org.valkyrienskies.mod.common.entity.handling.VSEntityManager;
import org.valkyrienskies.mod.common.entity.handling.WorldEntityHandler;
import org.valkyrienskies.mod.common.util.VectorConversionsMCKt;
import org.valkyrienskies.mod.mixin.accessors.entity.EntityAccessor;

@Mixin(Projectile.class)
public abstract class MixinProjectile extends Entity implements TraceableEntity {

    public MixinProjectile(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * If the projectile hit the ship, sends it to the shipyard.
     * This makes arrows/tridents/fishing hooks stuck on a ship render at correct position.
     */
    @WrapMethod(
            method = "onHit"
    )
    private void sendToShipyard(HitResult pResult, Operation<Void> original) {
        Ship ship;
        if (VSGameUtilsKt.getShipManaging(this) == null && pResult instanceof BlockHitResult blockHitResult && (ship = VSGameUtilsKt.getShipManagingPos(level(), blockHitResult.getBlockPos())) != null) {
            Vector3d newHitLocation = ship.getWorldToShip().transformPosition(VectorConversionsMCKt.toJOML(blockHitResult.getLocation()));
            BlockHitResult newResult = new BlockHitResult(VectorConversionsMCKt.toMinecraft(newHitLocation), blockHitResult.getDirection(), blockHitResult.getBlockPos(), blockHitResult.isInside());
            VSEntityManager.INSTANCE.pair(this.getType(), DefaultShipyardEntityHandler.INSTANCE);
            moveEntityFromWorldToShipyard(this, ship);
            original.call(newResult);
        } else {
            original.call(pResult);
        }
    }

    /**
     * If the projectile is no longer touching any block on shipyard, return it to the world.
     */
    @Inject(
        method = "tick",
        at = @At("HEAD")
    )
    private void returnFromShipyard(CallbackInfo ci) {
        final Ship ship;
        if((ship = VSGameUtilsKt.getShipManaging(this)) == null) return;
        Iterable<VoxelShape> result = level().getBlockCollisions(this, this.getBoundingBox().inflate(0.1));
        if(!result.iterator().hasNext()) {
            WorldEntityHandler.INSTANCE.moveEntityFromShipyardToWorld(this, ship);
        }
    }

    @Unique
    private static void moveEntityFromWorldToShipyard(Entity entity, Ship ship) {
        Vector3d shipyardPos = ship.getWorldToShip().transformPosition(entity.getX(), entity.getY(), entity.getZ(), new Vector3d());
        Vector3d relativePos = VectorConversionsMCKt.toJOML(entity.position()).sub(ship.getTransform().getPositionInWorld());
        Vector3d shipPosVelocity = new Vector3d(ship.getVelocity())
                .add(new Vector3d(ship.getOmega()).cross(relativePos))
                .mul(0.05);
        Vector3d relativeDeltaOnShip = VectorConversionsMCKt.toJOML(entity.getDeltaMovement()).sub(shipPosVelocity);
        ship.getWorldToShip().transformDirection(relativeDeltaOnShip);
        entity.setPos(VectorConversionsMCKt.toMinecraft(shipyardPos));
        entity.setDeltaMovement(VectorConversionsMCKt.toMinecraft(relativeDeltaOnShip));

        entity.xo = shipyardPos.x;
        entity.yo = shipyardPos.y;
        entity.zo = shipyardPos.z;


        final Vector3d direction;
        final double yaw;
        final double pitch;

        if (entity instanceof AbstractArrow) {
            direction = VectorConversionsMCKt.toJOML(entity.getDeltaMovement());
            yaw = Math.atan2(direction.x, direction.z);
            pitch = Math.atan2(direction.y, Math.sqrt((direction.x * direction.x) + (direction.z * direction.z)));
        } else {
            direction = ship.getWorldToShip().transformDirection(VectorConversionsMCKt.toJOML(entity.getLookAngle()));
            yaw = Math.atan2(-direction.x, direction.z);
            pitch = Math.atan2(-direction.y, Math.sqrt((direction.x * direction.x) + (direction.z * direction.z)));
        }

        entity.setYRot((float) (yaw * (180 / Math.PI)));
        entity.setXRot((float) (pitch * (180 / Math.PI)));
        entity.yRotO = entity.getYRot();
        entity.xRotO = entity.getXRot();

        if (entity instanceof AbstractHurtingProjectile projectile) {
            Vector3d power = new Vector3d(projectile.xPower, projectile.yPower, projectile.zPower);
            ship.getWorldToShip().transformDirection(power);

            projectile.xPower = power.x;
            projectile.yPower = power.y;
            projectile.zPower = power.z;

            ProjectileUtil.rotateTowardsMovement(projectile, 1.0f);
        }
    }
}
