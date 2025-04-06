package sh.talonfloof.resonance.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.SpreadingSnowyDirtBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import sh.talonfloof.resonance.ambiance.AmbientGrassBlockSoundsPlayer;

@Mixin(GrassBlock.class)
public abstract class GrassBlockMixin extends SpreadingSnowyDirtBlock {
    protected GrassBlockMixin(Properties p_56817_) {
        super(p_56817_);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rsource) {
        AmbientGrassBlockSoundsPlayer.playAmbientBlockSounds(state,level,pos,rsource);
    }
}
