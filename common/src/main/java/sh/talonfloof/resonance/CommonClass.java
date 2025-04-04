package sh.talonfloof.resonance;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.BiomeAmbientSoundsHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BiomeTags;
import sh.talonfloof.resonance.ambiance.AmbientWaterBlockSoundsPlayer;
import sh.talonfloof.resonance.config.ResonanceConfig;
import sh.talonfloof.resonance.platform.Services;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Items;

import static sh.talonfloof.resonance.ambiance.AmbientWaterBlockSoundsPlayer.OCEAN_IDLE;

public class CommonClass {
    public static void init() {
        Constants.LOG.info("Resonance on {}", Services.PLATFORM.getPlatformName());
        ResonanceConfig.HANDLER.load();
    }

    public static BiomeAmbientSoundsHandler.LoopSoundInstance OCEAN_LOOP = null;

    public static long timeSinceRiverTick = 0;
    public static long timeSinceAddition = 0;

    public static void onClientTick() {
        Minecraft mc = Minecraft.getInstance();
        if(mc.level != null) {
            var biome = mc.level.getBiome(new BlockPos(mc.player.getBlockX(),mc.player.getBlockY(),mc.player.getBlockZ()));
            if(mc.player.getBlockY() >= 50 && (biome.is(BiomeTags.IS_BEACH) || biome.is(BiomeTags.IS_OCEAN) || biome.is(BiomeTags.IS_DEEP_OCEAN))) {
                if(OCEAN_LOOP == null || OCEAN_LOOP.isStopped()) {
                    OCEAN_LOOP = new BiomeAmbientSoundsHandler.LoopSoundInstance(OCEAN_IDLE);
                    OCEAN_LOOP.fadeIn();
                    OCEAN_LOOP.fade = 0;
                    OCEAN_LOOP.volume = 0.01F;
                    Minecraft.getInstance().getSoundManager().play(OCEAN_LOOP);
                }
                if (OCEAN_LOOP != null && OCEAN_LOOP.fade >= 0) {
                    OCEAN_LOOP.fadeIn();
                }
            } else {
                if (OCEAN_LOOP != null && OCEAN_LOOP.fadeDirection != -1) {
                    OCEAN_LOOP.fadeOut();
                }
            }
            if(AmbientWaterBlockSoundsPlayer.RIVER_LOOP != null) {
                timeSinceRiverTick++;
                if (timeSinceRiverTick >= 20) {
                    AmbientWaterBlockSoundsPlayer.RIVER_LOOP.fadeOut();
                }
            }
            timeSinceAddition++;
        }
    }
}