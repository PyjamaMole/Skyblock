package protosky.mixins;

import com.mojang.datafixers.util.Either;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ServerLightingProvider;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.ProtoChunk;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import protosky.gen.WorldGenUtils;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;



@Mixin(ChunkStatus.class)
public abstract class ChunkStatusMixin
{
    // inserting skyblock code when chunk gets lit
    @Inject(method = "method_51376", at = @At("HEAD"))
    private static void getInitializeLightingFuture(ChunkStatus targetStatus, Executor executor, ServerWorld world, ChunkGenerator generator, StructureTemplateManager structureTemplateManager, ServerLightingProvider lightingProvider, Function fullChunkConverter, List chunks, Chunk chunk, CallbackInfoReturnable<CompletableFuture> cir)
    {
        WorldGenUtils.deleteBlocks((ProtoChunk) chunk, true);
        WorldGenUtils.clearEntities((ProtoChunk) chunk);
        WorldGenUtils.genHeightMaps((ProtoChunk) chunk);
    }

    @Inject(method = "method_20614", at = @At("HEAD"))
    private static void getLightingFuture(ChunkStatus targetStatus, Executor executor, ServerWorld world, ChunkGenerator generator, StructureTemplateManager structureTemplateManager, ServerLightingProvider lightingProvider, Function fullChunkConverter, List chunks, Chunk chunk, CallbackInfoReturnable<CompletableFuture> cir)
    {
        if(WorldGenUtils.deleteBlocks((ProtoChunk) chunk, false)) {
            WorldGenUtils.genHeightMaps((ProtoChunk) chunk);
            System.out.println("reset"+chunk.getPos().toString());
        }
    }

    // cancelling initial spawns of entities (not including those in structures)
    @Inject(method = "method_17033", at = @At("HEAD"), cancellable = true)
    private static void SPAWN(ChunkStatus targetStatus, ServerWorld world, ChunkGenerator generator, List chunks, Chunk chunk, CallbackInfo ci) {
        ci.cancel();
    }

}
