package protosky.gen;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.collection.PackedIntegerArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.*;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.*;

import java.util.*;

public class WorldGenUtils
{
    public static void deleteBlocks(ProtoChunk chunk)
    {
        // remove blocks
        ChunkSection[] sections = chunk.getSectionArray();
        for (int i = 0; i < sections.length; i++) {
            ChunkSection chunkSection = sections[i];
            PalettedContainer<BlockState> blockStateContainer = new PalettedContainer<>(Block.STATE_IDS, Blocks.AIR.getDefaultState(), PalettedContainer.PaletteProvider.BLOCK_STATE);
            ReadableContainer<RegistryEntry<Biome>> biomeContainer = chunkSection.getBiomeContainer();
            sections[i] = new ChunkSection(blockStateContainer, biomeContainer);
        }

        // remove block entities
        for (BlockPos bePos : chunk.getBlockEntityPositions())
        {
            chunk.removeBlockEntity(bePos);
        }

    }


    public static void clearEntities(ProtoChunk chunk)
    {
        // erase entities
        chunk.getEntities().clear();

    }

    public static void genHeightMaps(ProtoChunk chunk)
    {

        // set heightmaps
        int elementBits = MathHelper.ceilLog2(chunk.getHeight() + 1);
        long[] emptyHeightmap = new PackedIntegerArray(elementBits, 256).getData();
        for (Map.Entry<Heightmap.Type, Heightmap> heightmapEntry : chunk.getHeightmaps())
        {
            heightmapEntry.getValue().setTo(chunk, heightmapEntry.getKey(), emptyHeightmap);
        }

    }

}
