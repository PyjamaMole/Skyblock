package protosky.gen;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.PackedIntegerArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.*;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.PalettedContainer;
import net.minecraft.world.chunk.ProtoChunk;
import net.minecraft.world.chunk.ReadableContainer;
import protosky.mixins.ProtoChunkAccessor;

import java.util.*;

public class WorldGenUtils
{
    public static void deleteBlocks(ProtoChunk chunk, ServerWorld world)
    {
        ChunkSection[] sections = chunk.getSectionArray();
        for (int i = 0; i < sections.length; i++) {
            ChunkSection chunkSection = sections[i];
            PalettedContainer<BlockState> blockStateContainer = new PalettedContainer<>(Block.STATE_IDS, Blocks.AIR.getDefaultState(), PalettedContainer.PaletteProvider.BLOCK_STATE);
            ReadableContainer<RegistryEntry<Biome>> biomeContainer = chunkSection.getBiomeContainer();
            int chunkPos = chunkSection.getYOffset() >> 4;
            sections[i] = new ChunkSection(chunkPos, blockStateContainer, biomeContainer);
        }
        for (BlockPos bePos : chunk.getBlockEntityPositions())
        {
            chunk.removeBlockEntity(bePos);
        }
        ((ProtoChunkAccessor) chunk).getLightSources().clear();
        // defined in Heightmap class constructor
        int elementBits = MathHelper.ceilLog2(chunk.getHeight() + 1);
        long[] emptyHeightmap = new PackedIntegerArray(elementBits, 256).getData();
        for (Map.Entry<Heightmap.Type, Heightmap> heightmapEntry : chunk.getHeightmaps())
        {
            heightmapEntry.getValue().setTo(chunk, heightmapEntry.getKey(), emptyHeightmap);
        }
    }


//    public static void clearEntities(ProtoChunk chunk, ServerWorldAccess world)
//    {
//        // erase entities
//        chunk.getEntities().clear();
//
//    }

}
