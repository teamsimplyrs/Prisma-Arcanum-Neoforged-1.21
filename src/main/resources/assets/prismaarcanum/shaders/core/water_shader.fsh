#version 330
#moj_import <fog.glsl>

in vec2 uv;
in vec4 vertexColor;
uniform float GameTime;
out vec4 fragColor;

// ------------------ Random + Noise ------------------

float hash21(vec2 p) {
    p = fract(p * vec2(123.34, 456.21));
    p += dot(p, p + 34.23);
    return fract(p.x * p.y);
}

float noise(vec2 p) {
    vec2 i = floor(p);
    vec2 f = fract(p);
    float a = hash21(i);
    float b = hash21(i + vec2(1.0, 0.0));
    float c = hash21(i + vec2(0.0, 1.0));
    float d = hash21(i + vec2(1.0, 1.0));
    vec2 u = f * f * (3.0 - 2.0 * f);
    return mix(mix(a, b, u.x), mix(c, d, u.x), u.y);
}

float fbm(vec2 p) {
    float v = 0.0;
    float a = 0.5;
    for(int i = 0; i < 4; i++) {
        v += a * noise(p);
        p = p * 2.0 + vec2(20.0);
        a *= 0.5;
    }
    return v;
}

// ------------------ Main Shader ------------------

void main() {
    vec2 p = uv;

    // Seamless horizontal wrap around cylinder
    p.x = fract(p.x);

    // Animation time
    float time = GameTime * 2000;

    // Add subtle upward motion (so water flows upward + around)
    float verticalShift = time * 0.5f;   // small but noticeable
    p.y += verticalShift;

    // Distortion field for caustics
    vec2 flow = vec2(
    fbm(vec2(p.x * 4.0 + time, p.y * 3.0)),
    fbm(vec2(p.x * 4.0 - time, p.y * 3.0 + 4.0))
    );

    // Caustic wave shapes (moved by flow)
    float pattern = sin((p.y + flow.x * 0.4 + flow.y * 0.3) * 35.0 + time * 3.0);

    // Sharpen white streaks
    float caustics = smoothstep(0.6, 0.95, pattern);

    // Water + highlights (stylized)
    vec3 deepWater = vec3(0.0, 0.3, 0.52);
    vec3 highlight = vec3(1.0);

    vec3 col = mix(deepWater, highlight, caustics * 1.2);

    // Final output with vertex tinting
    fragColor = vec4(col, 1.0) * vertexColor;
}
