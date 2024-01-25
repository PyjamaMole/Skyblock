package protosky.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(WorldRenderer.class)
public class ClearHorizon {

    // sets a custom clear sky layer around the player so there is no color change at the horizon
    @Inject(method = "renderSky(Lnet/minecraft/client/render/BufferBuilder;F)Lnet/minecraft/client/render/BufferBuilder$BuiltBuffer;", at = @At("HEAD"), cancellable = true)
    private static void clear(BufferBuilder builder, float f, CallbackInfoReturnable<BufferBuilder.BuiltBuffer> cir){
        RenderSystem.setShader(GameRenderer::getPositionProgram);
        builder.begin(VertexFormat.DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION);

        builder.vertex(-f,f,f).next();
        builder.vertex(f,f,f).next();
        builder.vertex(-f,-f,f).next();
        builder.vertex(f,-f,f).next();
        builder.vertex(f,-f,-f).next();
        builder.vertex(f,f,f).next();
        builder.vertex(f,f,-f).next();
        builder.vertex(-f,f,f).next();
        builder.vertex(-f,f,-f).next();
        builder.vertex(-f,-f,f).next();
        builder.vertex(-f,-f,-f).next();
        builder.vertex(f,-f,-f).next();
        builder.vertex(-f,f,-f).next();
        builder.vertex(f,f,-f).next();

        cir.setReturnValue(builder.end());

    }

}
