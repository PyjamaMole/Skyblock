package protosky.mixins;


import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeaturePlacementContext;
import net.minecraft.world.gen.feature.PlacedFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import protosky.gen.WorldGenUtils;

@Mixin(PlacedFeature.class)
public abstract class PlacedFeatureMixin
{

    @Shadow public abstract RegistryEntry<ConfiguredFeature<?, ?>> feature();

    // makes sure features dont get placed when the world first generates, as none of these besides the end spikes spawn entities they dont add anything anyway
    // and only get in the way by placing blocks in already generated chunks
    @Inject(method = "generate(Lnet/minecraft/world/gen/feature/FeaturePlacementContext;Lnet/minecraft/util/math/random/Random;Lnet/minecraft/util/math/BlockPos;)Z", at = @At("HEAD"), cancellable = true)
    private void generate(FeaturePlacementContext context, Random random, BlockPos pos, CallbackInfoReturnable<Boolean> cir){

        if(WorldGenUtils.insideBorder(pos)) {
            Feature feature = this.feature().value().feature();
            if (!context.getWorld().getChunk(pos).getStatus().isAtLeast(ChunkStatus.SPAWN) && feature != Feature.END_SPIKE) cir.setReturnValue(true);
        }

    }
}
