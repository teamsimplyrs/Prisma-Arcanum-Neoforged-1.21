#version 330 core
#moj_import <fog.glsl>

in vec2 uv;
in vec4 vertexColor;

uniform sampler2D BaseTexture;
uniform sampler2D ParallaxOverlayTexture;
uniform vec2 ScreenSize;
uniform float GameTime;


out vec4 fragColor;

void main() {
    vec4 baseTex = texture(BaseTexture, uv);
    vec4 parallaxOverlay = texture(ParallaxOverlayTexture, gl_FragCoord.xy / ScreenSize);
    vec4 outColor = baseTex * parallaxOverlay * vertexColor;
    outColor.r = clamp(outColor.r, 0., 1.);
    outColor.g = clamp(outColor.r, 0., 1.);
    outColor.b = clamp(outColor.r, 0., 1.);
    outColor.a = 1.;

    fragColor = outColor;
}