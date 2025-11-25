package com.rebby.airship_civ_configure.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import de.maxhenkel.gravestone.corelib.death.Death;
import de.maxhenkel.gravestone.events.DeathEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

import java.util.List;

@Mixin(DeathEvents.class)
public class MixinGraveStoneDeathEvents {
    @WrapOperation(
            method = "playerDeath",
            at = @At(value = "INVOKE", target = "Lde/maxhenkel/gravestone/GraveUtils;getGraveStoneLocation(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/core/BlockPos;"),
            remap = false
    )
    private BlockPos toShip(Level world, BlockPos pos, Operation<BlockPos> original, @Local Death death){
        BlockPos result = original.call(world, pos);
        List<Vector3d> possiblePos = VSGameUtilsKt.transformToNearbyShipsAndWorld(world, death.getPosX(), death.getPosY(), death.getPosZ(), 1);
        for(Vector3d temp : possiblePos) {
            BlockPos base = BlockPos.containing(temp.x, temp.y, temp.z);
            if(VSGameUtilsKt.isBlockInShipyard(world, base)) return base;
        }
        return result;
    }
}
