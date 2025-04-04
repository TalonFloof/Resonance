package sh.talonfloof.resonance.config;

import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.autogen.AutoGen;
import dev.isxander.yacl3.config.v2.api.autogen.DoubleField;
import dev.isxander.yacl3.config.v2.api.autogen.IntField;
import dev.isxander.yacl3.config.v2.api.autogen.TickBox;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import dev.isxander.yacl3.platform.YACLPlatform;
import sh.talonfloof.resonance.Constants;

public class ResonanceConfig {
    public static ConfigClassHandler<ResonanceConfig> HANDLER = ConfigClassHandler.createBuilder(ResonanceConfig.class)
            .id(Constants.path("config"))
                    .serializer(config -> GsonConfigSerializerBuilder.create(config)
                            .setPath(YACLPlatform.getConfigDir().resolve("resonance.json5"))
                            .setJson5(true)
                            .build())
                    .build();

    @AutoGen(category = "Ambiance")
    @IntField
    @SerialEntry
    public int leavesIdleChance = 4000;
    @AutoGen(category = "Ambiance")
    @IntField
    @SerialEntry
    public int leavesAdditionsChance = 10000;
    @AutoGen(category = "Ambiance")
    @IntField
    @SerialEntry
    public int leavesDawnAdditionsChance = 20000;
    @AutoGen(category = "Ambiance")
    @IntField
    @SerialEntry
    public int forestWolfChance = 100000;
    @AutoGen(category = "Ambiance")
    @IntField
    @SerialEntry
    public int nightIdleChance = 4000;
    @AutoGen(category = "Ambiance")
    @TickBox
    @SerialEntry
    public boolean enableRiverSounds = true;
    @AutoGen(category = "Ambiance")
    @IntField
    @SerialEntry
    public int swampIdleChance = 6000;

    @AutoGen(category = "Weather")
    @TickBox
    @SerialEntry
    public boolean newThunderSounds = true;
    @AutoGen(category = "Weather")
    @DoubleField
    @SerialEntry
    public double closeThunderDistance = 90;
    @AutoGen(category = "Weather")
    @DoubleField
    @SerialEntry
    public double mediumThunderDistance = 140;

    public static ResonanceConfig getInstance() {
        return ResonanceConfig.HANDLER.instance();
    }
}
