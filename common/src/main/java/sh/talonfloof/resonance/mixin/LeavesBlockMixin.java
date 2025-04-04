package sh.talonfloof.resonance.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sh.talonfloof.resonance.ambiance.AmbientLeavesBlockSoundsPlayer;

@Mixin(LeavesBlock.class)
public class LeavesBlockMixin {
    @Inject(method = "animateTick", at = @At("TAIL"))
    public void resonance$forestAmbiance(BlockState state, Level level, BlockPos pos, RandomSource rsource, CallbackInfo ci) {
        AmbientLeavesBlockSoundsPlayer.playAmbientBlockSounds(state,level,pos,rsource);
    }
}
