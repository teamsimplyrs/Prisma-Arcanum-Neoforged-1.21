#version 330 core

in vec2 uv;
in vec4 vertexColor;

uniform float GameTime;
uniform float TimeFactor;
uniform float VoronoiSmoothstepLow;
uniform float VoronoiSmoothstepHigh;
uniform float VoronoiSmoothstepAlphaLow;
uniform float VoronoiSmoothstepAlphaHigh;
uniform float PosterizeSteps;

out vec4 fragColor;

float hash21(vec2 p) {
    p = fract(p * vec2(127.1, 311.7));
    p += dot(p, p + 34.345);
    return fract(p.x * p.y);
}
vec2 hash22(vec2 p) {
    return vec2(hash21(p), hash21(p + 19.19));
}

vec2 voronoiRandom(vec2 cellUV, float angleOffset) {
    vec2 rnd = hash22(cellUV);
    return vec2(sin(rnd.y * angleOffset), cos(rnd.x * angleOffset)) * 0.5 + 0.5;
}

float voronoiDeterministic(in vec2 uv, float angleOffset, float cellDensity) {
    vec2 g = floor(uv * cellDensity);
    vec2 f = fract(uv * cellDensity);

    float best = 8.0;
    for (int y = -1; y <= 1; ++y) {
        for (int x = -1; x <= 1; ++x) {
            vec2 lattice = vec2(float(x), float(y));
            vec2 cell = g + lattice;
            vec2 offset = voronoiRandom(cell, angleOffset);
            float d = distance(lattice + offset, f);
            best = min(best, d);
        }
    }

    return best;
}

float posterize(float val, float steps) {
    return floor(val * steps) / steps;
}

void main() {
    float timeX = GameTime * TimeFactor;
    float timeY = sin(GameTime * TimeFactor);
    float angleOffset = timeX * 4.0;

    float cellDensity = mix(1.0, 3.0, clamp((timeY * 0.5) + 0.5, 0.0, 1.0));

    float v = voronoiDeterministic(uv, angleOffset, cellDensity);

    float outVColor = smoothstep(VoronoiSmoothstepLow, VoronoiSmoothstepHigh, v);

    vec3 rgb = vec3(outVColor);
    float alpha = smoothstep(VoronoiSmoothstepAlphaLow, VoronoiSmoothstepAlphaHigh, v);

    alpha = posterize(alpha, PosterizeSteps);

    fragColor = vec4(rgb, alpha) * vertexColor;
}
