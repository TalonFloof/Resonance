package sh.talonfloof.resonance.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.SpreadingSnowyBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import sh.talonfloof.resonance.ambiance.AmbientGrassBlockSoundsPlayer;

@Mixin(GrassBlock.class)
public abstract class GrassBlockMixin extends SpreadingSnowyBlock {
    protected GrassBlockMixin(BlockBehaviour.Properties p_56817_, ResourceKey<Block> baseBlock) {
        super(p_56817_, baseBlock);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rsource) {
        AmbientGrassBlockSoundsPlayer.playAmbientBlockSounds(state,level,pos,rsource);
    }
}
