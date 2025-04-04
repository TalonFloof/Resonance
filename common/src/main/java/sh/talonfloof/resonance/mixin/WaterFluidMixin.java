package sh.talonfloof.resonance.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.WaterFluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sh.talonfloof.resonance.ambiance.AmbientWaterBlockSoundsPlayer;

@Mixin(WaterFluid.class)
public class WaterFluidMixin {
    @Inject(method = "animateTick", at = @At("TAIL"))
    public void resonance$waterAmbiance(Level level, BlockPos pos, FluidState state, RandomSource rsource, CallbackInfo ci) {
        AmbientWaterBlockSoundsPlayer.playAmbientBlockSounds(state,level,pos,rsource);
    }
}
