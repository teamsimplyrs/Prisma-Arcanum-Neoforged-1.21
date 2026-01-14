#version 330
#moj_import <fog.glsl>

in vec2 uv;
in vec4 vertexColor;
uniform float GameTime;
out vec4 fragColor;

// -------- Random + Noise --------
float hash21(vec2 p) {
    p = fract(p * vec2(123.34, 456.21));
    p += dot(p, p + 19.19);
    return fract(p.x * p.y);
}

float noise(float x) {
    float i = floor(x);
    float f = fract(x);
    float u = f * f * (3.0 - 2.0*f);
    return mix(hash21(vec2(i, i*2.0)), hash21(vec2(i+1.0, (i+1.0)*2.0)), u);
}

float fbm(float x) {
    float v = 0.0;
    float a = 0.5;
    for(int i = 0; i < 4; i++) {
        v += a * noise(x);
        x = x * 2.3 + 5.7;
        a *= 0.5;
    }
    return v;
}

// -------- Distance to Segment Helper --------
float segmentDist(vec2 p, vec2 a, vec2 b) {
    vec2 ab = b - a;
    float t = dot(p - a, ab) / dot(ab, ab);
    t = clamp(t, 0.0, 1.0);
    vec2 c = a + ab * t;
    return length(p - c);
}

// -------- Lightning Path --------
vec2 boltPoint(float t, float time) {
    float x = t * 2.0 - 1.0;     // left → right
    float n = fbm(t * 10.0 + time * 0.9);
    float y = (n - 0.5) * 0.70;  // vertical chaos
    return vec2(x, y);
}

float lightningCore(vec2 p, float time) {
    const int SEGMENTS = 26;
    float d = 1e5;
    vec2 prev = boltPoint(0.0, time);

    for(int i = 1; i <= SEGMENTS; i++) {
        float t = float(i) / float(SEGMENTS);
        vec2 cur = boltPoint(t, time);
        d = min(d, segmentDist(p, prev, cur));
        prev = cur;
    }
    return d;
}

// -------- Branching Paths --------
float lightningBranches(vec2 p, float time) {
    float total = 0.0;
    const int BR = 10;

    for(int i = 0; i < BR; i++) {
        float seed = float(i)*7.85 + floor(time*2.0);
        float start = hash21(vec2(seed, seed*3.1));

        vec2 prev = boltPoint(start, time);
        for(int j = 1; j < 18; j++) {
            float t = start + float(j)*0.05;
            vec2 cur = boltPoint(t, time) + vec2(0.0, fbm(seed+t*6.0)*0.4);
            float d = segmentDist(p, prev, cur);
            total += exp(-d * 110.0) * smoothstep(0.0, 1.0, 1.0 - t);
            prev = cur;
        }
    }
    return total;
}

// =================================================
void main() {
    float time = GameTime * 5000;

    // remap UV → centered screen-space
    vec2 p = vec2(uv.x * 2.0 - 1.0, uv.y * 2.0 - 1.0);

    // stormy background influence
    vec3 col = vec3(0.0);

    float dCore = lightningCore(p, time);
    float core = exp(-dCore * 140.0);

    float branchGlow = lightningBranches(p, time);

    float glow = exp(-dCore * 40.0) + branchGlow;

    col += vec3(1.0) * glow * 1.2;
    col += vec3(1.0) * core * 2.0;

    float alpha = clamp(glow * 1.6 + core * 2.2, 0.0, 1.0);
    col = clamp(col,0.0,1.0);
    fragColor = vec4(col, alpha) * vertexColor;

    // apply fog — required for MC consistency
    //fragColor = linear_fog(fragColor);
}
