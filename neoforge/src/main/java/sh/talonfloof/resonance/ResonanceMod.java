package sh.talonfloof.resonance;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.data.internal.NeoForgeBiomeTagsProvider;

@Mod(Constants.MOD_ID)
public class ResonanceMod {

    public ResonanceMod(IEventBus eventBus) {
        CommonClass.init();
    }
}