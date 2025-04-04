package sh.talonfloof.resonance.ambiance;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.BiomeAmbientSoundsHandler;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import sh.talonfloof.resonance.CommonClass;
import sh.talonfloof.resonance.Constants;
import sh.talonfloof.resonance.config.ResonanceConfig;

import static sh.talonfloof.resonance.Constants.dayTime;

public class AmbientWaterBlockSoundsPlayer {
    public static final SoundEvent RIVER = SoundEvent.createVariableRangeEvent(Constants.path("ambient.river"));
    public static final SoundEvent SWAMP_IDLE = SoundEvent.createVariableRangeEvent(Constants.path("ambient.swamp.idle"));
    public static final SoundEvent OCEAN_IDLE = SoundEvent.createVariableRangeEvent(Constants.path("ambient.ocean"));

    public static BiomeAmbientSoundsHandler.LoopSoundInstance RIVER_LOOP = null;

    public static void playAmbientBlockSounds(FluidState state, Level level, BlockPos pos, RandomSource rsource) {
        if (state.is(Fluids.WATER) && pos.getY() >= 60) {
            if(level.getRainLevel(0) > 0)
                return;
            if(rsource.nextInt(ResonanceConfig.getInstance().swampIdleChance) == 0 && level.getBiome(pos).is(Constants.IS_SWAMP)) {
                level.playLocalSound((double) pos.getX(), (double) pos.getY(), (double) pos.getZ(), SWAMP_IDLE, SoundSource.AMBIENT, 1.0F, 1.0F, false);
            }
            if(level.getBiome(pos).is(BiomeTags.IS_RIVER)) {
                if(Minecraft.getInstance().player.getPosition(0).distanceTo(pos.getCenter()) <= 8) {
                    if (RIVER_LOOP == null || (RIVER_LOOP.fade < 0 && RIVER_LOOP.fadeDirection == -1)) {
                        RIVER_LOOP = new BiomeAmbientSoundsHandler.LoopSoundInstance(RIVER);
                        RIVER_LOOP.fadeIn();
                        RIVER_LOOP.fade = 0;
                        RIVER_LOOP.volume = 0.01F;
                        Minecraft.getInstance().getSoundManager().play(RIVER_LOOP);
                    }
                    if (RIVER_LOOP != null && RIVER_LOOP.fade >= 0) {
                        CommonClass.timeSinceRiverTick = 0;
                        RIVER_LOOP.fadeIn();
                    }
                }
            }
        }
    }
}
