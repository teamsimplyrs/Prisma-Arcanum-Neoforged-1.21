#version 330 core
#moj_import <fog.glsl>
#moj_import <photon:particle_utils.glsl>

in vec2 uv;
in vec4 vertexColor;
in float vertexDistance;
in vec3 vertexNormal;

uniform sampler2D SamplerGradient;
uniform sampler2D TrailTex;
uniform vec2 TexScrollSpeed;
uniform vec2 NoiseScrollSpeed;
uniform float NoiseScale;
uniform float TimeFactor;
uniform float GameTime;

out vec4 fragCol;

float hash_tchou_2_1(vec2 p)
{
    p = fract(p * vec2(443.8975, 397.2973));
    p += dot(p, p + vec2(19.19));
    return fract(p.x * p.y);
}

float simpleNoiseValue(vec2 UV)
{
    vec2 i = floor(UV);
    vec2 f = fract(UV);

    f = f * f * (3.0 - 2.0 * f);
    UV = abs(fract(UV) - vec2(0.5));

    vec2 c0 = i + vec2(0.0, 0.0);
    vec2 c1 = i + vec2(1.0, 0.0);
    vec2 c2 = i + vec2(0.0, 1.0);
    vec2 c3 = i + vec2(1.0, 1.0);

    float r0 = hash_tchou_2_1(c0);
    float r1 = hash_tchou_2_1(c1);
    float r2 = hash_tchou_2_1(c2);
    float r3 = hash_tchou_2_1(c3);

    float bottom = mix(r0, r1, f.x);
    float top = mix(r2, r3, f.x);

    return mix(bottom, top, f.y);
}

float simpleNoise(vec2 UV, float scale)
{
    float outNoise = 0.0;
    {
        float freq = pow(2.0, 0.0);
        float amp  = pow(0.5, 3.0 - 0.0);
        outNoise += simpleNoiseValue(UV * (scale / freq)) * amp;
    }

    {
        float freq = pow(2.0, 1.0);
        float amp  = pow(0.5, 3.0 - 1.0);
        outNoise += simpleNoiseValue(UV * (scale / freq)) * amp;
    }

    {
        float freq = pow(2.0, 2.0);
        float amp  = pow(0.5, 3.0 - 2.0);
        outNoise += simpleNoiseValue(UV * (scale / freq)) * amp;
    }

    return outNoise;
}

vec2 tileAndOffset(vec2 UV, vec2 Tiling, vec2 Offset) {
    vec2 Out = UV * Tiling + Offset;
    return Out;
}

void main() {
    float time = GameTime * TimeFactor;
    float noise = simpleNoise(uv + NoiseScrollSpeed * time, NoiseScale);
    noise = clamp(noise, 0.0, 1.0);
    float noiseGradient = (noise + 1. - uv.x) - 0.5 * uv.x;
    vec2 texScrolledUV = tileAndOffset(uv, vec2(1.), time * TexScrollSpeed);
    vec4 trailTex = texture(TrailTex, texScrolledUV);
    vec4 scrolledNoisyTrail = vec4(vec3(noiseGradient), 1.) * trailTex;

    scrolledNoisyTrail.a = clamp(scrolledNoisyTrail, vec4(0.), vec4(1.)).r;
    vec4 sampledGradient = getGradientValue(SamplerGradient, 0, uv.x);

    fragCol = scrolledNoisyTrail * sampledGradient;
}