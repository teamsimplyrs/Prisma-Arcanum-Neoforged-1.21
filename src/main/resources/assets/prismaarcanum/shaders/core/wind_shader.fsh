#version 330
#moj_import <fog.glsl>

in vec2 uv;
in vec4 vertexColor;
uniform float GameTime;
out vec4 fragColor;

float hash21(vec2 p)
{
    vec3 p3 = fract(vec3(p.xyx) * 0.1031);
    p3 += dot(p3, p3.yzx + 33.33);
    return fract((p3.x + p3.y) * p3.z);
}

float noise(vec2 p)
{
    vec2 i = floor(p);
    vec2 f = fract(p);

    float a = hash21(i);
    float b = hash21(i + vec2(1.0, 0.0));
    float c = hash21(i + vec2(0.0, 1.0));
    float d = hash21(i + vec2(1.0, 1.0));

    vec2 u = f * f * (3.0 - 2.0 * f);
    return mix(mix(a, b, u.x), mix(c, d, u.x), u.y);
}

float fbm(vec2 p)
{
    float v = 0.0;
    float a = 0.5;
    for (int i = 0; i < 4; i++)
    {
        v += a * noise(p);
        p = p * 2.4 + vec2(7.1, 3.7);
        a *= 0.5;
    }
    return v;
}

float windBand(vec2 p, float row, float t)
{
    float x = p.x * 2.0 - 1.0;
    float y = (p.y - 0.5) * 2.0;

    float lane = mix(-0.5, 0.5, row);
    float curve = sin(x * 6.0 + t * 2.2 + row * 4.3) * 0.15;

    float d = abs(y - lane - curve);

    float core = exp(- d * d * 32.0);
    float soft = exp(- d * d * 8.0);

    float fadeL = smoothstep(- 0.9, - 0.2, x);
    float fadeR = 1.0 - smoothstep(0.2, 0.9, x);
    float fadeX = fadeL * fadeR;

    float n = fbm(vec2(p.x * 8.0 + t * 0.7, row * 17.3));
    float breaks = smoothstep(0.25, 0.8, n);

    return (core * 1.4 + soft * 0.4) * fadeX * breaks;
}

void main()
{
    float t = GameTime * 10000;

    vec3 col = vec3(0.0);
    float alpha = 0.0;

    for (int i = 0; i < 6; i++)
    {
        float row = float(i) / 5.0;
        float b = windBand(uv, row, t);
        col += vec3(b);
        alpha += b;
    }

    alpha = clamp(alpha * 1.6, 0.0, 1.0);
    col = pow(col, vec3(1.2));

    fragColor = vec4(col, alpha) * vertexColor;
}
