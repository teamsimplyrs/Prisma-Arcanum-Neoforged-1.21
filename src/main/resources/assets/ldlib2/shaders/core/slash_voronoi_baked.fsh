#version 330 core
#moj_import <photon:particle_utils.glsl>

in vec2 uv;
in vec4 vertexColor;

out vec4 fragColor;

uniform sampler2D BaseTexture;
uniform sampler2D NoiseTex;
uniform sampler2D SamplerCurve;
uniform sampler2D SamplerGradient;

uniform float GameTime;

uniform vec2 scrollRate;
uniform float timeFactor;
uniform float contrast;
uniform float alphaThreshold;
uniform float scale;
uniform float HDR;

void main() {
    float time = GameTime * timeFactor;

    vec2 uv0 = uv * scale + scrollRate * time;
    vec2 sUV = fract(uv0);

    float noise = texture(NoiseTex, sUV).r;
    noise = clamp((noise - 0.5) * contrast + 0.5, 0.0, 1.0);

    vec4 grad = getGradientValue(SamplerGradient, 0,  uv.x);
    vec4 base = texture(BaseTexture, uv);

    fragColor = vertexColor * vec4(vec3(noise), smoothstep(alphaThreshold, 1.0, noise)) * base * grad * HDR;
}