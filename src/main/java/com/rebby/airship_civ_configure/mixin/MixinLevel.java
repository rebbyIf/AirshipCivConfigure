package com.rebby.airship_civ_configure.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

/**
 * Biome on ship will follow the biome the ship is in, not the shipyard.
 */
@Mixin(Level.class)
public abstract class MixinLevel implements LevelReader {
    @Shadow
    public abstract BiomeManager getBiomeManager();

    @Override
    public Holder<Biome> getBiome(BlockPos pPos) {
        return getBiomeManager().getBiome(BlockPos.containing(VSGameUtilsKt.toWorldCoordinates(Level.class.cast(this), pPos.getCenter())));
    }
}
