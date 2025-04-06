package sh.talonfloof.resonance.ambiance;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import sh.talonfloof.resonance.CommonClass;
import sh.talonfloof.resonance.Constants;
import sh.talonfloof.resonance.config.ResonanceConfig;

import java.util.Iterator;

import static sh.talonfloof.resonance.CommonClass.config;
import static sh.talonfloof.resonance.Constants.dayTime;

public class AmbientGrassBlockSoundsPlayer {
    private static final int SURROUNDING_BLOCKS_PLAY_SOUND_THRESHOLD = 3;
    private static final int SURROUNDING_BLOCKS_DISTANCE_CHECK = 8;
    public static final SoundEvent NIGHT_IDLE = SoundEvent.createVariableRangeEvent(Constants.path("ambient.grass.night_idle"));
    public static final SoundEvent JUNGLE_NIGHT_IDLE = SoundEvent.createVariableRangeEvent(Constants.path("ambient.jungle.night_idle"));
    public static final SoundEvent JUNGLE_IDLE = SoundEvent.createVariableRangeEvent(Constants.path("ambient.jungle.idle"));
    public static final SoundEvent NIGHT_WOLF = SoundEvent.createVariableRangeEvent(Constants.path("ambient.forest.night_wolf"));

    public static void playAmbientBlockSounds(BlockState state, Level level, BlockPos pos, RandomSource rsource) {
        if (state.is(Blocks.GRASS_BLOCK)) {
            if(level.getRainLevel(0) > 0)
                return;
            var biome = level.getBiome(pos);
            if(dayTime(level) > 13000) {
                if(isInAmbientSoundBiome(biome) && shouldPlayAmbientSound(level, pos) && CommonClass.timeSinceNightIdle >= 8*20) {
                    level.playLocalSound((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), NIGHT_IDLE, SoundSource.AMBIENT, 1.0F, 1.0F, false);
                    CommonClass.timeSinceNightIdle = 0;
                }
                if (Minecraft.getInstance().player.getPosition(0).distanceTo(pos.getCenter()) <= 8 && biome.is(BiomeTags.IS_JUNGLE) && shouldPlayAmbientSound(level, pos) && CommonClass.timeSinceJungle >= (9*20)+10) {
                    level.playLocalSound((double) pos.getX(), (double) pos.getY(), (double) pos.getZ(), JUNGLE_NIGHT_IDLE, SoundSource.AMBIENT, 1.0F, 1.0F, false);
                    CommonClass.timeSinceJungle = 0;
                }
                if(rsource.nextInt(config.ambiance.forestWolfChance) == 0 && biome.is(BiomeTags.IS_FOREST) && shouldPlayAmbientSound(level, pos) && CommonClass.timeSinceAddition >= 40) {
                    level.playLocalSound((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), NIGHT_WOLF, SoundSource.AMBIENT, 1.0F, 1.0F, false);
                    CommonClass.timeSinceAddition = 0;
                }
            } else {
                if (Minecraft.getInstance().player.getPosition(0).distanceTo(pos.getCenter()) <= 8 && biome.is(BiomeTags.IS_JUNGLE) && shouldPlayAmbientSound(level, pos) && CommonClass.timeSinceJungle >= (9*20)+10) {
                    level.playLocalSound((double) pos.getX(), (double) pos.getY(), (double) pos.getZ(), JUNGLE_IDLE, SoundSource.AMBIENT, 0.5F, 1.0F, false);
                    CommonClass.timeSinceJungle = 0;
                }
            }
        }
    }

    private static boolean isInAmbientSoundBiome(Holder<Biome> biome) {
        return !biome.is(BiomeTags.IS_JUNGLE) && !biome.is(Constants.IS_SWAMP) && (biome.is(BiomeTags.IS_FOREST) || biome.is(Constants.IS_PLAINS) || biome.is(BiomeTags.IS_TAIGA) || biome.is(BiomeTags.IS_SAVANNA));
    }

    private static boolean shouldPlayAmbientSound(Level level, BlockPos pos) {
        int i = 0;
        Iterator var3 = Direction.Plane.HORIZONTAL.iterator();

        while(var3.hasNext()) {
            Direction direction = (Direction)var3.next();
            BlockPos blockpos = pos.relative(direction, SURROUNDING_BLOCKS_DISTANCE_CHECK);
            BlockState blockstate = level.getBlockState(blockpos.atY(level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, blockpos) - 1));
            if (blockstate.is(Blocks.GRASS_BLOCK)) {
                ++i;
                if (i >= SURROUNDING_BLOCKS_PLAY_SOUND_THRESHOLD) {
                    return true;
                }
            }
        }

        return false;
    }
}
