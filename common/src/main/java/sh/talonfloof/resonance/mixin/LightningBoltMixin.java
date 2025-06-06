package sh.talonfloof.resonance.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import sh.talonfloof.resonance.Constants;
import sh.talonfloof.resonance.config.ResonanceConfig;

import java.util.Objects;

import static sh.talonfloof.resonance.CommonClass.config;

// Some code used here is derived from https://github.com/netcatgirl/ImmersiveThunder/blob/1.21.4/common/src/main/java/com/netcatgirl/immersivethunder/mixin/ImmersiveThunderMixin.java
@Mixin(LightningBolt.class)
public class LightningBoltMixin {
    @Unique
    private static SoundEvent THUNDER_CLOSE = SoundEvent.createVariableRangeEvent(Constants.path("ambient.thunder.close"));
    @Unique
    private static SoundEvent THUNDER_MEDIUM = SoundEvent.createVariableRangeEvent(Constants.path("ambient.thunder.medium"));
    @Unique
    private static SoundEvent THUNDER_FAR = SoundEvent.createVariableRangeEvent(Constants.path("ambient.thunder.far"));

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playLocalSound(DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFZ)V"))
    private void playSound(Level level, double x, double y, double z, SoundEvent sound, SoundSource source, float volume, float pitch, boolean useDistance) {
        if(!config.weather.newThunderSounds) {
            level.playLocalSound(x,y,z,sound,source,volume,pitch,useDistance);
            return;
        }
        LightningBolt lightningBolt = (LightningBolt) (Object) this;
        LocalPlayer player = Minecraft.getInstance().player;

        double distanceToEntity = Objects.requireNonNull(player).distanceTo(lightningBolt);

        if (distanceToEntity <= config.weather.closeThunderDistance) {
            playThunderSound(level, lightningBolt, THUNDER_CLOSE, 5000.0f, false);
        } else if (distanceToEntity <= config.weather.mediumThunderDistance) {
            playThunderSound(level, lightningBolt, THUNDER_MEDIUM, 10000.0f, false);
        } else {
            playThunderSound(level, lightningBolt, THUNDER_FAR, 10000.0f, false);
        }
    }

    @Unique
    private static void playThunderSound(Level level, LightningBolt lightningBolt, SoundEvent soundEvent, float volume, boolean useDistance) {
        level.playLocalSound(lightningBolt.getX(), lightningBolt.getY(), lightningBolt.getZ(), soundEvent, SoundSource.WEATHER, volume, 0.8f, useDistance);
    }
}
