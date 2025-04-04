package sh.talonfloof.resonance;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class ResonanceMod implements ModInitializer {
    
    @Override
    public void onInitialize() {
        CommonClass.init();
    }
}
