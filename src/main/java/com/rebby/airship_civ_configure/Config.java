package com.rebby.airship_civ_configure;

import com.simibubi.create.foundation.config.ConfigBase;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.apache.commons.lang3.tuple.Pair;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

public class Config{

    private static final Map<ModConfig.Type, ConfigBase> CONFIGS = new EnumMap<>(ModConfig.Type.class);

    private static ServerConfig server;

    public static ServerConfig getServer() {
        return server;
    }

    private static <T extends ConfigBase> T register(Supplier<T> factory, ModConfig.Type side) {
        Pair<T, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(builder -> {
            T config = factory.get();
            config.registerAll(builder);
            return config;
        });

        T config = specPair.getLeft();
        config.specification = specPair.getRight();
        CONFIGS.put(side, config);
        return config;
    }

    public static void register(ModLoadingContext context) {
        server = register(ServerConfig::new, ModConfig.Type.SERVER);

        for (Map.Entry<ModConfig.Type, ConfigBase> pair : CONFIGS.entrySet())
            context.registerConfig(pair.getKey(), pair.getValue().specification);
    }

    @SubscribeEvent
    public static void onLoad(ModConfigEvent.Loading event) {
        for (ConfigBase config : CONFIGS.values())
            if (config.specification == event.getConfig()
                    .getSpec())
                config.onLoad();
    }

    @SubscribeEvent
    public static void onReload(ModConfigEvent.Reloading event) {
        for (ConfigBase config : CONFIGS.values())
            if (config.specification == event.getConfig()
                    .getSpec())
                config.onReload();
    }

    public static class ServerConfig extends ConfigBase {
        public final ConfigBase.ConfigFloat EUREKA_IMPULSE_SPEED_RATE;
        public final ConfigBase.ConfigFloat CW_PROP_STRESS_IMPACT;
        public final ConfigBase.ConfigFloat CW_PROP_THRUST_BASE;
        public final ConfigBase.ConfigFloat CW_PROP_THRUST_DECREASE;

        public ServerConfig() {
            EUREKA_IMPULSE_SPEED_RATE = this.f(1.25f, 1.0f, 3.0f, "eurekaImpulseSpeedRate", comments.eureka_impulse_speed_rate);
            CW_PROP_STRESS_IMPACT = this.f(2.0f, 0.5f, "cwPropStressImpact", comments.cw_prop_stress_impact);
            CW_PROP_THRUST_BASE = this.f(1.0f, 0.0f, "cwPropThrustBase", comments.cw_prop_thrust_base);
            CW_PROP_THRUST_DECREASE = this.f(0.8f, 0.0f, 1.0f, "cwPropThrustDecrease", comments.cw_prop_thrust_decrease);
        }

        public static class comments {
            static final String eureka_impulse_speed_rate = "Impulse speed for eureka ships.";
            static final String cw_prop_stress_impact = "Multiplier for stress impact of propeller bearing of clockwork propeller bearings.";
            static final String cw_prop_thrust_base = "Thrust modifier for propeller at y level 0.";
            static final String cw_prop_thrust_decrease = "How much thrust decreases if the propeller is 100 blocks higher.\n"
                    + "this is calculated exponentially, so if the decrease is 0.5 and you're 200 blocks higher the propeller will be 0.25 times efficient.";
        }

        @Override
        public String getName() {
            return "server";
        }
    }
}
