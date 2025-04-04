package sh.talonfloof.resonance.ambiance;

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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import sh.talonfloof.resonance.CommonClass;
import sh.talonfloof.resonance.Constants;
import sh.talonfloof.resonance.config.ResonanceConfig;

import java.util.Iterator;

import static sh.talonfloof.resonance.Constants.dayTime;

public class AmbientLeavesBlockSoundsPlayer {
    public static final SoundEvent PLAINS_TREE_ADDITIONS = SoundEvent.createVariableRangeEvent(Constants.path("ambient.plains.tree_additions"));
    public static final SoundEvent FOREST_IDLE = SoundEvent.createVariableRangeEvent(Constants.path("ambient.forest.idle"));
    public static final SoundEvent FOREST_NIGHT_ADDITIONS = SoundEvent.createVariableRangeEvent(Constants.path("ambient.forest.night_additions"));
    public static final SoundEvent FOREST_ADDITIONS = SoundEvent.createVariableRangeEvent(Constants.path("ambient.forest.additions"));
    public static final SoundEvent FOREST_DAWN = SoundEvent.createVariableRangeEvent(Constants.path("ambient.forest.dawn"));

    public static void playAmbientBlockSounds(BlockState state, Level level, BlockPos pos, RandomSource rsource) {
        if (state.is(BlockTags.LEAVES) && pos.getY() > 63) {
            if(level.getRainLevel(0) > 0)
                return;
            if (rsource.nextInt(ResonanceConfig.getInstance().leavesIdleChance) == 0 && dayTime(level) < 13000) {
                var b = level.getBiome(pos);
                if(b.is(BiomeTags.IS_JUNGLE) || b.is(Constants.IS_SWAMP))
                    return;
                level.playLocalSound((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), FOREST_IDLE, SoundSource.AMBIENT, 1.0F, 1.0F, false);
            }

            if (rsource.nextInt(ResonanceConfig.getInstance().leavesAdditionsChance) == 0 && CommonClass.timeSinceAddition >= 40) {
                var b = level.getBiome(pos);
                if(isInAmbientSoundBiome(b)) {
                    if(dayTime(level) < 12000) {
                        level.playPlayerSound(FOREST_ADDITIONS, SoundSource.AMBIENT, 1.0F, 1.0F);
                        CommonClass.timeSinceAddition = 0;
                    } else if(dayTime(level) < 22500) {
                        level.playPlayerSound(FOREST_NIGHT_ADDITIONS, SoundSource.AMBIENT, 1.0F, 1.0F);
                        CommonClass.timeSinceAddition = 0;
                    }
                } else if(dayTime(level) < 12000 && b.is(Constants.IS_PLAINS)) {
                    level.playPlayerSound(PLAINS_TREE_ADDITIONS, SoundSource.AMBIENT, 1.0F, 1.0F);
                    CommonClass.timeSinceAddition = 0;
                }
            }
            if (rsource.nextInt(ResonanceConfig.getInstance().leavesDawnAdditionsChance) == 0 && isInAmbientSoundBiome(level.getBiome(pos)) && dayTime(level) > 20000 && CommonClass.timeSinceAddition >= 40) {
                level.playPlayerSound(FOREST_DAWN, SoundSource.AMBIENT, 1.0F, 1.0F);
                CommonClass.timeSinceAddition = 0;
            }
        }

    }

    private static boolean isInAmbientSoundBiome(Holder<Biome> biome) {
        return biome.is(BiomeTags.IS_FOREST);
    }
}
