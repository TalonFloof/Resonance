package sh.talonfloof.resonance;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {

	public static final String MOD_ID = "resonance";
	public static final String MOD_NAME = "Resonance";
	public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

	public static ResourceLocation path(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID,path);
	}

	public static final TagKey<Biome> IS_PLAINS = TagKey.create(Registries.BIOME,ResourceLocation.parse("c:is_plains"));
	public static final TagKey<Biome> IS_SWAMP = TagKey.create(Registries.BIOME,ResourceLocation.parse("c:is_swamp"));

	public static long dayTime(Level l) {
		return l.getDayTime() % 24000;
	}
}