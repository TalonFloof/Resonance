package sh.talonfloof.resonance;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
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