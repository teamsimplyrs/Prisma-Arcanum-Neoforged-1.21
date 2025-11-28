package com.teamsimplyrs.prismaarcanum.rendering;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;

import java.io.IOException;

public class MyRenderTypes {

    //This is what you should use in the renderer to pass to the MultiBufferSource
    public static RenderType waterShader() {
        return CustomRenderTypes.RENDERTYPE_WATER_SHADER;
    }

    @EventBusSubscriber(modid = PrismaArcanum.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ModClientEvents {

        @SubscribeEvent
        public static void registerShaders(RegisterShadersEvent event) throws IOException {
            event.registerShader(
                    new ShaderInstance(
                            event.getResourceProvider(),
                            ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, "water_shader_program"),
                            DefaultVertexFormat.NEW_ENTITY
                    ),
                    shader -> CustomRenderTypes.waterShader = shader
            );
        }
    }

    //Don't instantiate
    private static class CustomRenderTypes extends RenderType {

        // Loaded via RegisterShadersEvent
        private static ShaderInstance waterShader;

        private static final ShaderStateShard WATER_SHADER =
                new ShaderStateShard(() -> waterShader);

        public static final RenderType RENDERTYPE_WATER_SHADER = createWaterShader();

        private CustomRenderTypes(String name,
                                  VertexFormat format,
                                  VertexFormat.Mode mode,
                                  int bufferSize,
                                  boolean affectsCrumbling,
                                  boolean sortOnUpload,
                                  Runnable setupState,
                                  Runnable clearState) {
            super(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, setupState, clearState);
            throw new IllegalStateException("CustomRenderTypes is not meant to be instantiated");
        }

        private static RenderType createWaterShader() {
            CompositeState state = CompositeState.builder()
                    .setShaderState(WATER_SHADER)
                    // .setTextureState(new TextureStateShard(ignoredTexture, false, false))
                    .setTransparencyState(RenderStateShard.ADDITIVE_TRANSPARENCY)
                    .setLightmapState(LIGHTMAP)
                    .setOverlayState(NO_OVERLAY)
                    .createCompositeState(true);

            return create(
                    "water_shader",
                    DefaultVertexFormat.NEW_ENTITY,
                    VertexFormat.Mode.QUADS,
                    256,
                    true,
                    false,
                    state
            );
        }
    }
}