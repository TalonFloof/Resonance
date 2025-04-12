package sh.talonfloof.resonance.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sh.talonfloof.resonance.CommonClass;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {
    @Inject(method = "handleLogin", at=@At("TAIL"))
    public void resonance$clientPlayLoginEvent(ClientboundLoginPacket packet, CallbackInfo ci) {
        CommonClass.onClientJoin(Minecraft.getInstance().level);
    }
}
