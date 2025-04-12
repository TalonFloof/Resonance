package sh.talonfloof.resonance;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientWorldEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

public class ResonanceModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CommonClass.init();
        ClientTickEvents.START_CLIENT_TICK.register((x) -> CommonClass.onClientTick());
    }
}
