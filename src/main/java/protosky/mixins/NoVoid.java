package protosky.mixins;

import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BackgroundRenderer.class)
public class NoVoid {

    // makes sure the background color does not fade to black when approaching the void
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;getPos()Lnet/minecraft/util/math/Vec3d;", ordinal = 3))
    private static Vec3d high(Camera instance){
        return new Vec3d(instance.getPos().x, 1.0/0.0, instance.getPos().z);
    }

}
