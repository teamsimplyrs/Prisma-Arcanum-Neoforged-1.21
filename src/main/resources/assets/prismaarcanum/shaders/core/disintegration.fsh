#version 330 core
#moj_import <fog.glsl>
#moj_import <photon:particle_utils.glsl>

in vec2 uv;
in vec4 vertexColor; // particle color
in float vertexDistance;
in vec3 vertexNormal;

uniform sampler2D MainTex;
uniform sampler2D NoiseTex;

uniform vec4 MainTexHDRColor;
uniform int HDRMode; // 0 -> none, 1 -> multiplicative, 2 -> additive

uniform vec4 EdgeColor;
uniform float AlphaClipThreshold;
uniform float NoiseStrength;
uniform float CutoffHeight;
uniform float CutoffInitialValue;
uniform float EdgeThickness;
uniform float Direction;
uniform int ParticleLifetime;

uniform float GameTime;
uniform float TimeFactor;

out vec4 fragColor;

vec4 sampleMainTex() {
    vec4 mainTex = texture(MainTex, uv);
    return mainTex;
}

float sampleNoise() {
    vec4 noise = texture(NoiseTex, uv);
    float avg = (noise.r + noise.g + noise.b) / 3.;

    float result = avg * NoiseStrength;
    return result;
}

vec4 getColoredEdge(vec4 mainTex, float y) {
    float steppedEdge = step(y, EdgeThickness);
    vec4 coloredEdge = steppedEdge * EdgeColor;

    vec4 result = coloredEdge + mainTex;
    return result;
}

void main() {
    vec4 mainTex = sampleMainTex();
    float noise = sampleNoise();

    float time = GameTime * TimeFactor;
//    float lifetimeNormalized = ParticleLifetime * TimeFactor / 24000.;
    float cutoffFactor = (CutoffInitialValue + time);
//    cutoffFactor = clamp(cutoffFactor, -1., 2.);
    float dir = clamp(Direction, -1., 1.);

    float cutoff = (1. - cutoffFactor) - uv.y * dir;

    float noisyDissolve = (noise + cutoff) * mainTex.a;
    vec4 coloredEdge = getColoredEdge(mainTex, noisyDissolve);

    float alpha = smoothstep(0., AlphaClipThreshold, noisyDissolve);
    vec4 resultColor = vec4(coloredEdge.rgb, alpha);

    fragColor = resultColor * vertexColor;
}