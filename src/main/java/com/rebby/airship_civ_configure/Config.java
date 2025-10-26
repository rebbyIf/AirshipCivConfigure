package com.rebby.airship_civ_configure;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.stream.Collectors;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = AirshipCivConfigure.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.DoubleValue EUREKA_IMPULSE_SPEED_RATE = BUILDER
            .comment("Impulse speed for eureka ships.")
            .defineInRange("eurekaImpulseSpeedRate", 1.25, 1.0, 3.0);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static double eurekaImpulseSpeedRate;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        eurekaImpulseSpeedRate = EUREKA_IMPULSE_SPEED_RATE.get();

    }
}
