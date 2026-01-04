#version 330 core

in vec2 uv;
in vec4 vertexColor;

uniform sampler2D SamplerSceneColor;
uniform sampler2D BaseTexture;
uniform float GameTime;
uniform float TimeFactor;
uniform float SceneColorSampleStrength;
uniform float ChromaticAbberrationFactor;
uniform float MaxRadius;
uniform float BaseRadiusFactor;
uniform float RippleAmplitude;
uniform float RippleWidth;

out vec4 fragColor;

const vec2 c = vec2(0.5);

float offset(float radiusFactor, vec2 dir, float time) {
    float d = length(dir) - radiusFactor * MaxRadius;
    d = sin(d * 8. - time * 4.) / RippleAmplitude;
    d = abs(d);
    d *= 1. - smoothstep(0., RippleWidth, abs(d));

    return d;
}

void main() {
    vec2 dir = c - uv;
    float time = GameTime * TimeFactor;

    float offsetR = offset(BaseRadiusFactor + ChromaticAbberrationFactor, dir, time);
    float offsetG = offset(BaseRadiusFactor - ChromaticAbberrationFactor, dir, time);
    float offsetB = offset(BaseRadiusFactor, dir, time);
    float d = offset(BaseRadiusFactor, dir, time);

    dir = normalize(dir);

    vec2 uv0 = vec2(uv.x, 1. - uv.y) * 0.25 + 0.25;

    float r = texture(BaseTexture, uv + dir * offsetR).r;
    float rS = texture(SamplerSceneColor, uv0 + dir * offsetR).r;
    float g = texture(BaseTexture, uv + dir * offsetG).g;
    float gS = texture(SamplerSceneColor, uv0 + dir * offsetG).g;
    float b = texture(BaseTexture, uv + dir * offsetB).b;
    float bS = texture(SamplerSceneColor, uv0 + dir * offsetB).b;

    vec4 baseCol = vec4(r, g, b, 1.);
    vec4 sceneCol = vec4(rS, gS, bS, 1.);

    vec4 mixedCol = mix(baseCol, sceneCol, SceneColorSampleStrength);

    fragColor = mixedCol * vertexColor;
}