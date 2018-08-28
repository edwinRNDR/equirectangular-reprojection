#version 330 core

in vec2 v_texCoord0;
uniform sampler2D tex0;
out vec4 o_color;

uniform mat4 rotate;

vec3 fromSpherical(float theta, float phi, float radius) {
    float sinPhiRadius = sin(phi) * radius;
    return vec3(sinPhiRadius * sin(theta),
                cos(phi) * radius,
                sinPhiRadius * cos(theta));
}

vec3 toSpherical(vec3 p) {
    float l = length(p);
    return vec3(atan(p.x, p.z), acos(clamp(p.y / l, -1.0, 1.0)), l);
}

void main() {
    float pi = 3.141593;
    vec3 position = (rotate * vec4(fromSpherical(v_texCoord0.x * pi*2, v_texCoord0.y * pi, 1.0), 1.0)).xyz;
    vec3 reproject = toSpherical(position);
    o_color = texture(tex0, vec2( mod(reproject.x / (pi*2.0), 1.0), reproject.y / pi));
}