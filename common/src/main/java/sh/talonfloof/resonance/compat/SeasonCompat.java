package sh.talonfloof.resonance.compat;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import sereneseasons.api.season.SeasonHelper;
import sh.talonfloof.resonance.CommonClass;

public class SeasonCompat {
    public enum Season {
        SPRING,
        SUMMER,
        AUTUMN,
        WINTER
    }

    public static boolean inTropicalBiome(Player player) {
        if(!CommonClass.hasSereneSeasons)
            return false;
        return SeasonHelper.usesTropicalSeasons(player.level().getBiome(player.blockPosition()));
    }

    public static Season getCurrentSeason(Level level) {
        if(CommonClass.hasSereneSeasons) {
            var state = SeasonHelper.getSeasonState(level);
            switch(state.getSeason()) {
                case SPRING -> {
                    return Season.SPRING;
                }
                case SUMMER -> {
                    return Season.SUMMER;
                }
                case AUTUMN -> {
                    return Season.AUTUMN;
                }
                case WINTER -> {
                    return Season.WINTER;
                }
            }
        }
        return Season.SPRING;
    }
}
