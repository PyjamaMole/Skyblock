package protosky.mixins;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.HeightLimitView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientWorld.Properties.class)
public class YeetDarkness {

    // makes sure the bottom half of the sky is not turned to black when you go below the horizon
    @Inject(method = "getSkyDarknessHeight", at = @At("HEAD"), cancellable = true)
    public void darkness(HeightLimitView world, CallbackInfoReturnable<Double> cir){
        cir.setReturnValue(-1.0/0.0);
    }

    /*@Inject(method = "getHorizonShadingRatio", at = @At("HEAD"), cancellable = true)
    public void lightness(CallbackInfoReturnable<Float> cir){ cir.setReturnValue(1.0f/0.0f);}*/

}
