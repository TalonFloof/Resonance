package sh.talonfloof.resonance;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {

	public static final String MOD_ID = "resonance";
	public static final String MOD_NAME = "Resonance";
	public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

	public static Identifier path(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID,path);
	}

	public static final TagKey<Biome> IS_PLAINS = TagKey.create(Registries.BIOME,Identifier.parse("c:is_plains"));
	public static boolean isPlains(Holder<Biome> b) { return b.is(IS_PLAINS) || b.is(Biomes.PLAINS) || b.is(Biomes.SUNFLOWER_PLAINS); }
	public static final TagKey<Biome> IS_FOREST = TagKey.create(Registries.BIOME,Identifier.parse("c:is_forest"));
	public static boolean isForest(Holder<Biome> b) { return b.is(IS_FOREST) || b.is(BiomeTags.IS_FOREST); }
	public static final TagKey<Biome> IS_SWAMP = TagKey.create(Registries.BIOME,Identifier.parse("c:is_swamp"));
	public static boolean isSwamp(Holder<Biome> b) { return b.is(IS_SWAMP) || b.is(Biomes.SWAMP) || b.is(Biomes.MANGROVE_SWAMP); }
	public static final TagKey<Biome> IS_SNOWY = TagKey.create(Registries.BIOME,Identifier.parse("c:is_snowy"));
	public static boolean isSnowy(Holder<Biome> b) { return b.is(IS_SNOWY) || b.is(Biomes.SNOWY_PLAINS) || b.is(Biomes.SNOWY_TAIGA) || b.is(Biomes.SNOWY_SLOPES) || b.is(Biomes.GROVE); }
	public static final TagKey<Biome> IS_DESERT = TagKey.create(Registries.BIOME,Identifier.parse("c:is_desert"));
	public static boolean isDesert(Holder<Biome> b) { return b.is(IS_DESERT) || b.is(Biomes.DESERT); }
	public static final TagKey<Biome> IS_JUNGLE = TagKey.create(Registries.BIOME,Identifier.parse("c:is_jungle"));
	public static boolean isJungle(Holder<Biome> b) { return b.is(IS_JUNGLE) || b.is(BiomeTags.IS_JUNGLE); }
	public static final TagKey<Biome> IS_SAVANNA = TagKey.create(Registries.BIOME,Identifier.parse("c:is_savanna"));
	public static boolean isSavanna(Holder<Biome> b) { return b.is(IS_SAVANNA) || b.is(BiomeTags.IS_SAVANNA); }
	public static final TagKey<Biome> IS_TAIGA = TagKey.create(Registries.BIOME,Identifier.parse("c:is_taiga"));
	public static boolean isTaiga(Holder<Biome> b) { return b.is(IS_TAIGA) || b.is(BiomeTags.IS_TAIGA); }
	public static final TagKey<Biome> IS_BEACH = TagKey.create(Registries.BIOME,Identifier.parse("c:is_beach"));
	public static boolean isBeach(Holder<Biome> b) { return b.is(IS_BEACH) || b.is(BiomeTags.IS_BEACH) || b.is(Biomes.STONY_SHORE); }
	public static final TagKey<Biome> IS_OCEAN = TagKey.create(Registries.BIOME,Identifier.parse("c:is_ocean"));
	public static boolean isOcean(Holder<Biome> b) { return b.is(IS_OCEAN) || b.is(BiomeTags.IS_OCEAN) || b.is(BiomeTags.IS_DEEP_OCEAN); }
	public static final TagKey<Biome> IS_RIVER = TagKey.create(Registries.BIOME,Identifier.parse("c:is_river"));
	public static boolean isRiver(Holder<Biome> b) { return b.is(IS_RIVER) || b.is(BiomeTags.IS_RIVER); }

	public static long dayTime(Level l) {
		return l.getDayTime() % 24000;
	}

	public static boolean isOutside(Player player) {
		if (player.isUnderWater()) return false;

		int blocks = 24;
		int start = 1;

		BlockPos playerPos = player.blockPosition();

		if (player.level().canSeeSky(playerPos)) return true;
		if (player.level().canSeeSkyFromBelowWater(playerPos)) return true;

		for (int i = start; i < start + blocks; i++) {
			BlockPos check = new BlockPos(playerPos.getX(), playerPos.getY() + i, playerPos.getZ());
			BlockState state = player.level().getBlockState(check);
			Block block = state.getBlock();

			if (player.level().isEmptyBlock(check)) continue;

			if (!state.canOcclude()) continue;

			if (player.level().canSeeSky(check)) return true;
			if (player.level().canSeeSkyFromBelowWater(check)) return true;
			if (state.canOcclude()) return false;
		}

		return player.level().canSeeSky(playerPos.above(blocks));
	}
}