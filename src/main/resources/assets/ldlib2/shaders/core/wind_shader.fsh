#version 330
#moj_import <fog.glsl>
//#moj_import <photon:particle.glsl>

in vec2 uv;
in vec4 vertexColor;
//in float LifeTime;

out vec4 fragColor;

uniform sampler2D BaseTexture;
uniform float GameTime;

float hash(float n) { return fract(sin(n)*43758.5453123); }

float noise(float x) {
float i = floor(x);
float f = fract(x);
return mix(hash(i), hash(i+1.0), f*f*(3.0-2.0*f));
}

// Soft windy ribbon function
float windyRibbon(vec2 uv, float thickness, float offset, float amp)
{
float curve =
sin(uv.x*3.0 + offset + GameTime*0.8) * amp +
sin(uv.x*9.0 - offset*1.3 + GameTime*1.6) * amp*0.4 +
noise(uv.x*6.0 + offset + GameTime*0.4) * amp*0.6;

float y = 0.5 + curve;
float d = abs(uv.y - y);

float core = smoothstep(thickness, 0.0, d);
float fade = smoothstep(0.0, 0.03, thickness - d);

return core * fade;
}

void main()
{
    vec3 col = vec3(0.0);

    // MAIN RIBBON — thick & bright
    float main = windyRibbon(uv, 0.09, 0.0, 0.13);
    col += main * vec3(0.50, 1.0, 0.95);

    // SMALLER WISPS — floating around
    float w1 = windyRibbon(uv, 0.03, 2.5, 0.07);
    float w2 = windyRibbon(uv, 0.02, -3.2, 0.05);
    float w3 = windyRibbon(uv, 0.015, 5.7, 0.06);

    float wispy = w1*0.7 + w2*0.6 + w3*0.5;
    col += wispy * vec3(0.40, 0.95, 1.0);

    // Glow boost
    col += (main + wispy) * 0.3 * col;

    float alpha = clamp(max(main, wispy), 0.0, 1.0);

    fragColor = vec4(col, alpha) * vertexColor;
}
