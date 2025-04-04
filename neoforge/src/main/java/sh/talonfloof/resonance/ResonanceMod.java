package sh.talonfloof.resonance;


import dev.isxander.yacl3.api.YetAnotherConfigLib;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import sh.talonfloof.resonance.config.ResonanceConfig;

@Mod(Constants.MOD_ID)
public class ResonanceMod {

    public ResonanceMod(IEventBus eventBus) {
        CommonClass.init();
        ModLoadingContext.get().registerExtensionPoint(
                IConfigScreenFactory.class,
                () -> (client, parent) -> ResonanceConfig.HANDLER.generateGui().generateScreen(parent)
        );
    }
}