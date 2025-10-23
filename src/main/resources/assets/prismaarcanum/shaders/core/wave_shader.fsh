#version 330
#moj_import <fog.glsl>

in vec2 uv;
in vec4 vertexColor;

uniform sampler2D BaseTexture;
uniform float GameTime;
uniform float u_AmplitudeMajorX;
uniform float u_WaveFrequencyMajorX;
uniform float u_AmplitudeMinorX;
uniform float u_WaveFrequencyMinorX;
uniform float u_AmplitudeMajorY;
uniform float u_WaveFrequencyMajorY;
uniform float u_AmplitudeMinorY;
uniform float u_WaveFrequencyMinorY;

uniform float u_TimeScaleFactorMajorX;
uniform float u_TimeScaleFactorMinorX;
uniform float u_TimeScaleFactorMajorY;
uniform float u_TimeScaleFactorMinorY;

out vec4 fragColor;

void main() {
    vec2 texSize = textureSize(BaseTexture, 0);

    float waveX = u_AmplitudeMajorX * sin(u_WaveFrequencyMajorX * uv.y + u_TimeScaleFactorMajorX * GameTime) + u_AmplitudeMinorX * sin(u_WaveFrequencyMinorX * uv.y + u_TimeScaleFactorMinorX * GameTime);
    float waveY = u_AmplitudeMajorY * sin(u_WaveFrequencyMajorY * uv.x + u_TimeScaleFactorMajorY * GameTime) + u_AmplitudeMinorY * sin(u_WaveFrequencyMinorY * uv.x + u_TimeScaleFactorMinorY * GameTime);

    vec2 outUV = uv + vec2(waveX, waveY);

    fragColor = texture(BaseTexture, outUV) * vertexColor;
}
