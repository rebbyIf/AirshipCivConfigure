package com.rebby.airship_civ_configure.mixin;

import com.bawnorton.mixinsquared.TargetHandler;
import com.mojang.logging.LogUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
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
import java.util.function.Supplier;

@Mixin(value = ServerLevel.class, priority = 3000)
public abstract class MixinServerLevel extends Level {

    protected MixinServerLevel(WritableLevelData pLevelData, ResourceKey<Level> pDimension, RegistryAccess pRegistryAccess, Holder<DimensionType> pDimensionTypeRegistration, Supplier<ProfilerFiller> pProfiler, boolean pIsClientSide, boolean pIsDebug, long pBiomeZoomSeed, int pMaxChainedNeighborUpdates) {
        super(pLevelData, pDimension, pRegistryAccess, pDimensionTypeRegistration, pProfiler, pIsClientSide, pIsDebug, pBiomeZoomSeed, pMaxChainedNeighborUpdates);
    }

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
                    ship.getActiveChunksSet().forEach(
                            (chunkX, chunkZ) -> {
                                LevelChunk chunk = this.getChunk(chunkX, chunkZ);
                                chunk.clearAllBlockEntities();
                                for(LevelChunkSection section : chunk.getSections()){
                                    for(int i = 0; i < 16; i++) {
                                        for(int j = 0; j < 16; j++) {
                                            for(int k = 0; k < 16; k++) {
                                                section.setBlockState(i, j, k, Blocks.AIR.defaultBlockState());
                                            }
                                        }
                                    }
                                }
                            }
                    );
                    VSGameUtilsKt.getShipObjectWorld(ServerLevel.class.cast(this)).deleteShip((ServerShip) ship);
                }
        );
    }
}
