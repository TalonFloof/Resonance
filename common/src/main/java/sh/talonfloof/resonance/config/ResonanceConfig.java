package sh.talonfloof.resonance.config;

import me.fzzyhmstrs.fzzy_config.annotations.Comment;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.util.Walkable;
import sh.talonfloof.resonance.Constants;

public class ResonanceConfig extends Config {
    public static class AmbianceSection extends ConfigSection implements Walkable {
        @Comment("The chance of hearing leaves blow with the wind (1 in x)")
        public int leavesIdleChance = 4000;
        @Comment("The chance of hearing additional sounds from trees (1 in x)")
        public int leavesAdditionsChance = 10000;
        @Comment("The chance of hearing robins chirp in the early morning (1 in x)")
        public int leavesDawnAdditionsChance = 20000;
        @Comment("The chance of hearing wolves howl at night (1 in x)")
        public int forestWolfChance = 100000;
        @Comment("Sets if the distant insect chirp loop sound should be played at night")
        public boolean enableNightIdle = true;
        @Comment("Sets if the river loop sound should be played")
        public boolean enableRiverSounds = true;
        @Comment("The chance of hearing ambient swamp noises (1 in x)")
        public int swampIdleChance = 6000;
        @Comment("Sets if the ocean loop sound should be played")
        public boolean enableOceanSounds = true;
        @Comment("Sets if the jungle loop sounds should be played")
        public boolean enableJungleIdle = true;
        @Comment("Sets if the plains and savanna ambient loop sound should be played")
        public boolean enablePlainsSavannaIdle = true;
        @Comment("The chance of hearing ambient village noises (1 in x)")
        public int villageAdditionsChance = 500;
    }

    public static class WeatherSection extends ConfigSection implements Walkable {
        @Comment("Enables the new, distance-based thunder sounds")
        public boolean newThunderSounds = true;
        @Comment("The maximum distance (in blocks) that the close thunder sound set will play at")
        public double closeThunderDistance = 90;
        @Comment("The maximum distance (in blocks) that the medium thunder sound set will play at")
        public double mediumThunderDistance = 140;
    }

    public AmbianceSection ambiance = new AmbianceSection();
    public WeatherSection weather = new WeatherSection();

    public ResonanceConfig() {
        super(Constants.path("client"));
    }
}
