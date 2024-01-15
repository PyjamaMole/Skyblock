package protosky.gen;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerLightingProvider;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.PackedIntegerArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.*;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.*;

import java.util.*;

public class WorldGenUtils
{
    public static void deleteBlocks(ProtoChunk chunk)
    {
        ChunkSection[] sections = chunk.getSectionArray();
        for (int i = 0; i < sections.length; i++) {
            ChunkSection chunkSection = sections[i];
            PalettedContainer<BlockState> blockStateContainer = new PalettedContainer<>(Block.STATE_IDS, Blocks.AIR.getDefaultState(), PalettedContainer.PaletteProvider.BLOCK_STATE);
            ReadableContainer<RegistryEntry<Biome>> biomeContainer = chunkSection.getBiomeContainer();
            sections[i] = new ChunkSection(blockStateContainer, biomeContainer);
        }
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

    public static void genHeightMaps(ProtoChunk chunk, ServerLightingProvider lightingProvider)
    {

        //Tried to fix the lighting issue in a more elegant way but have yet to succeed, in the mean time this quick and dirty solution will
        //update both the light levels and the heightmap.

        /*
        int elementBits = MathHelper.ceilLog2(chunk.getHeight() + 1);
        long[] emptyHeightmap = new PackedIntegerArray(elementBits, 256).getData();
        for (Map.Entry<Heightmap.Type, Heightmap> heightmapEntry : chunk.getHeightmaps())
        {
            heightmapEntry.getValue().setTo(chunk, heightmapEntry.getKey(), emptyHeightmap);
        }

        for (int i = chunk.getBottomSectionCoord(); i < chunk.getTopSectionCoord(); i++){
            lightingProvider.enqueueSectionData(LightType.SKY, ChunkSectionPos.from(chunk.getPos(), i), new ChunkNibbleArray(15));
        }
        */

        int count = 0;
        int z = chunk.getTopY()-1;
        while (count <= 255) {
            int x = count / 16;
            int y = count % 16;
            chunk.setBlockState(new BlockPos(x, z, y), Blocks.STONE.getDefaultState(), false);
            chunk.setBlockState(new BlockPos(x, z, y), Blocks.AIR.getDefaultState(), false);

            //lightingProvider.setSectionStatus(new BlockPos(x, z, y), false);
            //lightingProvider.setSectionStatus(new BlockPos(x, z, y), true);
            //chunk.getChunkSkyLight().isSkyLightAccessible(chunk, x, z, y);
            //lightingProvider.checkBlock(new BlockPos(x, z, y));


            count++;
        }

    }

}
