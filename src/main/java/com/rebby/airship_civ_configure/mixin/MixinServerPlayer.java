package com.rebby.airship_civ_configure.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.valkyrienskies.eureka.EurekaMod;

@Mixin(ServerPlayer.class)
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
}
