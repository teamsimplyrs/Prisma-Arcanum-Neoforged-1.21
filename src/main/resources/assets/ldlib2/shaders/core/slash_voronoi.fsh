#version 330 core

in vec2 uv;
out vec4 FragColor;

uniform sampler2D BaseTexture;
uniform float GameTime;
uniform float u_scaleA;     // coarse scale, e.g. 4.0
uniform float u_scaleB;     // fine scale, e.g. 30.0
uniform float u_jitter;     // 0..1 e.g. 0.8
uniform float u_speed;      // animation speed e.g. 1.0
uniform int   u_mode;       // 0=F1,1=F2,2=F2-F1,3=mix F1+(F2-F1)
uniform float u_mix;        // mixing amount between coarse/fine: 0..1
uniform float u_contrast;   // >0, e.g. 2.0
uniform float u_noiseAmt;
uniform float u_alphaClipThreshold;

// small hash/noise functions (cheap)
float hash21(vec2 p) {
    p = fract(p * vec2(127.1,311.7));
    p += dot(p, p + 34.345);
    return fract(p.x * p.y);
}
vec2 hash22(vec2 p) {
    return vec2(hash21(p), hash21(p + 19.19));
}

// voronoi core (returns f1,f2)
vec2 voronoiF1F2(vec2 uv, float scale, float jitter, float time) {
    vec2 scaled = uv * scale;
    vec2 ip = floor(scaled);
    vec2 fp = fract(scaled);

    float best1 = 1e10;
    float best2 = 1e10;

    for (int oy=-1; oy<=1; oy++) {
        for (int ox=-1; ox<=1; ox++) {
            vec2 off = vec2(float(ox), float(oy));
            vec2 cell = ip + off;
            vec2 rnd = hash22(cell);

            vec2 seed = 0.5 + (rnd - 0.5) * jitter;
            // slight animation of seed
            float anim = 0.025;
            float phase = hash21(cell) * 6.283185;
            seed += anim * vec2(sin(time * u_speed + phase), cos(time * u_speed + phase*1.37));

            vec2 diff = off + seed - fp;
            float dsq = dot(diff, diff);
            if (dsq < best1) { best2 = best1; best1 = dsq; }
            else if (dsq < best2) { best2 = dsq; }
        }
    }
    return vec2(sqrt(best1), sqrt(best2));
}

// cheap fbm for uv perturbation
float noise21(vec2 p) {
    return hash21(p);
}
float fbm(vec2 p) {
    float v = 0.0;
    float a = 0.5;
    for (int i=0;i<4;i++){
        v += a * noise21(p);
        p *= 2.0;
        a *= 0.5;
    }
    return v;
}

void main(){
    vec2 uv = uv;
    float time = GameTime * u_speed;
    
    // small uv perturb so cells get irregular edges (adds micro detail)
    vec2 pnoise = vec2(fbm(uv * 2.0 + time), fbm(uv * 2.3 - time));
    uv += (pnoise - 0.5) * u_noiseAmt;

    // coarse and fine voronoi
    vec2 vfA = voronoiF1F2(uv, max(0.0001,u_scaleA), clamp(u_jitter,0.0,1.0), time);
    vec2 vfB = voronoiF1F2(uv, max(0.0001,u_scaleB), clamp(u_jitter,0.0,1.0), time);

    float f1A = vfA.x, f2A = vfA.y;
    float f1B = vfB.x, f2B = vfB.y;

    // choose modes per-octave (use F2-F1 for border clarity)
    float outA = (u_mode == 0) ? f1A : (u_mode==1) ? f2A : (u_mode==2) ? (f2A - f1A) : (f2A - f1A);
    float outB = (u_mode == 0) ? f1B : (u_mode==1) ? f2B : (u_mode==2) ? (f2B - f1B) : (f2B - f1B);

    // combine coarse + fine
    float combined = mix(outA, outB, clamp(u_mix,0.0,1.0));

    // contrast remap to increase "noise" feeling
    combined = clamp(combined * u_contrast, 0.0, 1.0);
    // optional smoothing to remove tiny artifacts
    combined = smoothstep(0.02, 0.98, combined);

    // invert if you prefer dark ridges
    float finalv = 1.0 - combined;

    FragColor = 1. - vec4(vec3(finalv), smoothstep(0.1, u_alphaClipThreshold, finalv));
}