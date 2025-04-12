package sh.talonfloof.resonance;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public class ResonanceNeoForgeEventBus {
    @SubscribeEvent
    public static void onClientTickStart(ClientTickEvent.Pre event) {
        CommonClass.onClientTick();
    }
}
