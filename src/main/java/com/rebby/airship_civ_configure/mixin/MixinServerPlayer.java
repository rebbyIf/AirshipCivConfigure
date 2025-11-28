package com.rebby.airship_civ_configure.mixin;

import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.valkyrienskies.core.api.ships.Ship;
import org.valkyrienskies.eureka.EurekaMod;

@Mixin(value = ServerPlayer.class, priority = 1500)
public abstract class MixinServerPlayer extends Player {

    public MixinServerPlayer(Level pLevel, BlockPos pPos, float pYRot, GameProfile pGameProfile) {
        super(pLevel, pPos, pYRot, pGameProfile);
    }

    @Inject(
            method = "restoreFrom",
            at = @At(value = "RETURN")
    )
    private void emergencyKit(ServerPlayer pThat, boolean pKeepEverything, CallbackInfo ci){
        addItem(new ItemStack(ForgeRegistries.ITEMS.getValue(ResourceLocation.fromNamespaceAndPath(EurekaMod.MOD_ID, "oak_ship_helm")), 1));
        addItem(new ItemStack(ForgeRegistries.ITEMS.getValue(ResourceLocation.fromNamespaceAndPath(EurekaMod.MOD_ID, "balloon")), 2));
    }

    @TargetHandler(
            mixin = "org.valkyrienskies.mod.mixin.server.command.level.MixinServerPlayer",
            name = "beforeDismountTo"
    )
    @Redirect(
            method = "@MixinSquared:Handler",
            at = @At(value = "INVOKE", target = "Lorg/valkyrienskies/mod/common/VSGameUtilsKt;toWorldCoordinates(Lorg/valkyrienskies/core/api/ships/Ship;DDD)Lorg/joml/Vector3d;"),
            remap = false
    )
    private Vector3d teleportAddSpeed(Ship ship, double x, double y, double z){
        Vector3d prevPos = ship.getPrevTickTransform().getShipToWorld().transformPosition(x, y, z, new Vector3d());
        Vector3d curPos = ship.getTransform().getShipToWorld().transformPosition(x, y, z, new Vector3d());
        return curPos.mul(2).sub(prevPos);
    }
}
