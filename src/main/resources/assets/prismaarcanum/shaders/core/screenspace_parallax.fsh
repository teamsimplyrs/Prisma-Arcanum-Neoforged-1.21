#version 330 core
#moj_import <fog.glsl>

in vec2 uv;
in vec4 vertexColor;
in float vertexDistance;
in vec3 vertexNormal;

uniform sampler2D BaseTexture;
uniform sampler2D ParallaxOverlayTexture;
uniform vec2 ScreenSize;
uniform float GameTime;
uniform float ParallaxResolutionMultiplier;
uniform float TextureMixFactor;

uniform float AlphaThresholdLow;
uniform float AlphaThresholdHigh;

uniform float FresnelPower;
uniform float FresnelThresholdLow;
uniform float FresnelThresholdHigh;

uniform vec3 U_CameraPosition;

out vec4 fragColor;

const vec2 center = vec2(0.5);

float fresnel(float amount, vec3 normal, vec3 view){
    return pow(
    1.0 - clamp(dot(normalize(normal), normalize(view)), 0.0, 1.0),
    amount
    );
}

void main() {
    vec4 baseTex = texture(BaseTexture, uv);
    vec4 parallaxOverlay = texture(ParallaxOverlayTexture, vec2(ParallaxResolutionMultiplier) * gl_FragCoord.xy / ScreenSize);
    vec4 outColor = (baseTex + parallaxOverlay) * vertexColor;
    float fresnelEffect = fresnel(FresnelPower * gl_FragDepth, vertexNormal, U_CameraPosition);

    fresnelEffect = smoothstep(FresnelThresholdLow, FresnelThresholdHigh, fresnelEffect);

    outColor.r = clamp(outColor.r + fresnelEffect, 0., 1.);
    outColor.g = clamp(outColor.g + fresnelEffect, 0., 1.);
    outColor.b = clamp(outColor.b + fresnelEffect, 0., 1.);

    vec2 dir = center - uv;
    float d = length(dir);

    outColor.a = 1. - smoothstep(AlphaThresholdLow, AlphaThresholdHigh, d);


    fragColor = outColor;
}