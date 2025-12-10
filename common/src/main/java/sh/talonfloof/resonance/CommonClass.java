package sh.talonfloof.resonance;

import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;
import me.fzzyhmstrs.fzzy_config.util.platform.Registrar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.BiomeAmbientSoundsHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.level.block.entity.BellBlockEntity;
import net.minecraft.world.phys.AABB;
import sh.talonfloof.resonance.ambiance.AmbientWaterBlockSoundsPlayer;
import sh.talonfloof.resonance.compat.SeasonCompat;
import sh.talonfloof.resonance.config.ResonanceConfig;
import sh.talonfloof.resonance.platform.Services;

import static sh.talonfloof.resonance.Constants.LOG;
import static sh.talonfloof.resonance.Constants.dayTime;
import static sh.talonfloof.resonance.ambiance.AmbientWaterBlockSoundsPlayer.OCEAN_IDLE;

public class CommonClass {
    public static ResonanceConfig config = ConfigApiJava.registerAndLoadConfig(ResonanceConfig::new, RegisterType.CLIENT);
    public static boolean hasSereneSeasons = false;

    public static void init() {
        Constants.LOG.info("Resonance on {}", Services.PLATFORM.getPlatformName());
        if(Services.PLATFORM.isModLoaded("sereneseasons"))
            hasSereneSeasons = true;
        if(hasSereneSeasons) {
            Constants.LOG.info("Enabled Serene Seasons compatibility with Resonance!");
        }
    }

    public static BiomeAmbientSoundsHandler.LoopSoundInstance WINTER_LOOP = null;
    public static BiomeAmbientSoundsHandler.LoopSoundInstance OCEAN_LOOP = null;
    public static BiomeAmbientSoundsHandler.LoopSoundInstance DESERT_LOOP = null;
    public static final SoundEvent DESERT_IDLE = SoundEvent.createVariableRangeEvent(Constants.path("ambient.desert.idle"));
    public static final SoundEvent WINTER_IDLE = SoundEvent.createVariableRangeEvent(Constants.path("ambient.winter"));
    public static final SoundEvent SWAMP_IDLE = SoundEvent.createVariableRangeEvent(Constants.path("ambient.swamp.idle"));
    public static final SoundEvent SWAMP_NIGHT_IDLE = SoundEvent.createVariableRangeEvent(Constants.path("ambient.swamp.night_idle"));
    public static final SoundEvent VILLAGE_ADDITIONS = SoundEvent.createVariableRangeEvent(Constants.path("ambient.village.additions"));
    public static final SoundEvent VILLAGE_ROOSTER = SoundEvent.createVariableRangeEvent(Constants.path("ambient.village.rooster"));

    public static long timeSinceRiverTick = 0;
    public static long timeSinceAddition = 0;
    public static long timeSinceSwamp = 0;
    public static long timeSinceJungle = 0;
    public static long timeSinceNightIdle = 0;
    public static long timeSincePlains = 0;
    public static long villageScanInterval = 19;
    public static boolean isInVillage = false;
    public static boolean isOutside = false;
    public static int previousDay = 0;

    public static void onClientTick() {
        Minecraft mc = Minecraft.getInstance();
        if(mc.isPaused())
            return;
        if(mc.level != null) {
            var pos = new BlockPos(mc.player.getBlockX(),mc.player.getBlockY(),mc.player.getBlockZ());
            villageScanInterval++;
            if(villageScanInterval % 20 == 0) {
                boolean prevInVillage = isInVillage;
                isInVillage = false;
                villageScanInterval = 0;
                // Also go ahead and check if we're outside
                isOutside = Constants.isOutside(mc.player);
                if (mc.level.dimensionType().hasSkyLight()) {
                    var playerEyes = mc.player.getEyePosition();
                    AABB box = AABB.unitCubeFromLowerCorner(playerEyes).inflate(64);
                    var villagerEntities = mc.level.getEntitiesOfClass(Villager.class, box);
                    if(!villagerEntities.isEmpty()) {
                        for (int x = -4; x <= 4; x++) {
                            for (int z = -4; z <= 4; z++) {
                                var c = mc.level.getChunk(SectionPos.blockToSectionCoord(mc.player.getBlockX()) + x, SectionPos.blockToSectionCoord(mc.player.getBlockZ()) + z);
                                if (c.getBlockEntities().values().stream().anyMatch((e) -> e instanceof BellBlockEntity)) {
                                    isInVillage = true;
                                    break;
                                }
                            }
                            if (isInVillage)
                                break;
                        }
                    }
                }
                if(!prevInVillage && isInVillage && mc.level.getDayTime() / 24000 != previousDay) // Prevent the rooster sound from playing if the day changed, and we previously weren't in a village
                    previousDay = (int)(mc.level.getDayTime() / 24000);
            }
            if(isInVillage) {
                if(mc.level.getRainLevel(0) == 0) {
                    if (mc.level.getDayTime() / 24000 != previousDay) {
                        mc.player.playSound(VILLAGE_ROOSTER, 10000000.0F, 1.0F);
                        previousDay = (int) (mc.level.getDayTime() / 24000);
                    }
                    if (mc.level.getRandom().nextInt(config.ambiance.villageAdditionsChance) == 0 && dayTime(mc.level) < 12000) {
                        mc.player.playSound(VILLAGE_ADDITIONS, 10000000.0F, 1.0F);
                    }
                } else {
                    if (mc.level.getDayTime() / 24000 != previousDay) { // Prevent a delayed rooster sound due to rain
                        previousDay = (int) (mc.level.getDayTime() / 24000);
                    }
                }
            }
            var biome = mc.level.getBiome(pos);
            if(config.ambiance.enableSwampSounds && isOutside && Constants.isSwamp(biome) && mc.level.getRainLevel(0) <= 0 && CommonClass.timeSinceSwamp >= 8*20) {
                if(dayTime(mc.level) < 13000 && SeasonCompat.getCurrentSeason(mc.level) != SeasonCompat.Season.WINTER) {
                    mc.level.playLocalSound((double) pos.getX(), (double) pos.getY(), (double) pos.getZ(), SWAMP_IDLE, SoundSource.AMBIENT, 0.5F, 1.0F, false);
                } else {
                    mc.level.playLocalSound((double) pos.getX(), (double) pos.getY(), (double) pos.getZ(), SWAMP_NIGHT_IDLE, SoundSource.AMBIENT, 1.0F, 1.0F, false);
                }
                CommonClass.timeSinceSwamp = 0;
            }
            if(config.ambiance.enableDesertIdle && isOutside && Constants.isDesert(biome) && dayTime(mc.level) < 13000 && mc.level.getRainLevel(0) <= 0) {
                if(DESERT_LOOP == null || DESERT_LOOP.isStopped()) {
                    DESERT_LOOP = new BiomeAmbientSoundsHandler.LoopSoundInstance(DESERT_IDLE);
                    DESERT_LOOP.fadeIn();
                    DESERT_LOOP.fade = 0;
                    DESERT_LOOP.volume = 0.01F;
                    Minecraft.getInstance().getSoundManager().play(DESERT_LOOP);
                }
                if (DESERT_LOOP != null && DESERT_LOOP.fade >= 0) {
                    DESERT_LOOP.fadeIn();
                }
            } else {
                if (DESERT_LOOP != null && DESERT_LOOP.fadeDirection != -1) {
                    DESERT_LOOP.fadeOut();
                }
            }
            if(config.ambiance.enableOceanSounds & mc.player.getBlockY() >= 50 && (Constants.isBeach(biome) || Constants.isOcean(biome)) && mc.level.getRainLevel(0) <= 0) {
                if(OCEAN_LOOP == null || OCEAN_LOOP.isStopped()) {
                    OCEAN_LOOP = new BiomeAmbientSoundsHandler.LoopSoundInstance(OCEAN_IDLE);
                    OCEAN_LOOP.fadeIn();
                    OCEAN_LOOP.fade = 0;
                    OCEAN_LOOP.volume = 0.01F;
                    Minecraft.getInstance().getSoundManager().play(OCEAN_LOOP);
                }
                if (OCEAN_LOOP != null && OCEAN_LOOP.fade >= 0) {
                    OCEAN_LOOP.fadeIn();
                }
            } else {
                if (OCEAN_LOOP != null && OCEAN_LOOP.fadeDirection != -1) {
                    OCEAN_LOOP.fadeOut();
                }
            }
            if(AmbientWaterBlockSoundsPlayer.RIVER_LOOP != null) {
                timeSinceRiverTick++;
                if (timeSinceRiverTick >= 20) {
                    AmbientWaterBlockSoundsPlayer.RIVER_LOOP.fadeOut();
                }
            }
            if((Constants.isSnowy(biome) || SeasonCompat.getCurrentSeason(mc.level) == SeasonCompat.Season.WINTER) && isOutside && !SeasonCompat.inTropicalBiome(mc.player)) {
                if(WINTER_LOOP == null || WINTER_LOOP.isStopped()) {
                    WINTER_LOOP = new BiomeAmbientSoundsHandler.LoopSoundInstance(WINTER_IDLE);
                    WINTER_LOOP.fadeIn();
                    WINTER_LOOP.fade = 0;
                    WINTER_LOOP.volume = 0.01F;
                    Minecraft.getInstance().getSoundManager().play(WINTER_LOOP);
                }
            } else {
                if (WINTER_LOOP != null && WINTER_LOOP.fadeDirection != -1) {
                    WINTER_LOOP.fadeOut();
                }
            }
            timeSinceSwamp++;
            timeSincePlains++;
            timeSinceNightIdle++;
            timeSinceJungle++;
            timeSinceAddition++;
        }
    }

    public static void onClientJoin(ClientLevel level) {
        isInVillage = false;
        timeSinceJungle = (9*20)+10;
        timeSinceNightIdle = 8*20;
        timeSincePlains = 9*20;
        timeSinceSwamp = 8*20;
    }
}