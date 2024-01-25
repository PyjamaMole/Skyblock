package protosky.mixins;

import net.minecraft.server.world.ServerLightingProvider;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.util.math.ChunkPos;
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
    private static void getInitializeLightingFuture(ChunkStatus targetStatus, Executor executor, ServerWorld world, ChunkGenerator generator, StructureTemplateManager structureTemplateManager, ServerLightingProvider lightingProvider, Function fullChunkConverter, List chunks, Chunk chunk, CallbackInfoReturnable<CompletableFuture> cir) {

        ChunkPos pos = chunk.getPos();
        if(WorldGenUtils.insideBorder(pos)) {

            //((ProtoChunk) chunk).getEntities().clear();
            //if (world.getRegistryKey() != ServerWorld.END || pos.x != 0 || pos.z != 0) {
            //    generator.populateEntities(new ChunkRegion(world, chunks, targetStatus, -1));
            //}
            WorldGenUtils.deleteBlocks((ProtoChunk) chunk, true);
            WorldGenUtils.genHeightMaps((ProtoChunk) chunk);
        }
    }

    // makes sure no stray blocks are left behind
    @Inject(method = "method_20614", at = @At("HEAD"))
    private static void getLightingFuture(ChunkStatus targetStatus, Executor executor, ServerWorld world, ChunkGenerator generator, StructureTemplateManager structureTemplateManager, ServerLightingProvider lightingProvider, Function fullChunkConverter, List chunks, Chunk chunk, CallbackInfoReturnable<CompletableFuture> cir) {

        if(WorldGenUtils.insideBorder(chunk.getPos())) {
            if (WorldGenUtils.deleteBlocks((ProtoChunk) chunk, false)) {
                WorldGenUtils.genHeightMaps((ProtoChunk) chunk);
                chunk.refreshSurfaceY();
                System.out.println("reset" + chunk.getPos().toString());
            }
        }

    }

    // cancelling initial spawns of entities (not including those in structures), the world is already gone anyway
    @Inject(method = "method_17033", at = @At("HEAD"), cancellable = true)
    private static void SPAWN(ChunkStatus targetStatus, ServerWorld world, ChunkGenerator generator, List chunks, Chunk chunk, CallbackInfo ci) {
        if(WorldGenUtils.insideBorder(chunk.getPos())) {
            ci.cancel();
        }
    }

    // none of the usefull structures locations actually depend on these changes to the world so they can be skipped
    @Inject(method = "method_38282", at = @At("HEAD"), cancellable = true)
    private static void CARVE(ChunkStatus targetStatus, ServerWorld world, ChunkGenerator generator, List chunks, Chunk chunk, CallbackInfo ci){
        if(WorldGenUtils.insideBorder(chunk.getPos())) {
            ci.cancel();
        }
    }

    @Inject(method = "method_16569", at = @At("HEAD"), cancellable = true)
    private static void SURFACE(ChunkStatus targetStatus, ServerWorld world, ChunkGenerator generator, List chunks, Chunk chunk, CallbackInfo ci){
        if(WorldGenUtils.insideBorder(chunk.getPos())) {
            ci.cancel();
        }
    }

}
