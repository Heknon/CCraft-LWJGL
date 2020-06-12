#version 450 core

in vec3 position;
in vec2 uvs;

uniform mat4 worldMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

out vec2 pass_uvs;

void main(void) {
    gl_Position = projectionMatrix * worldMatrix * vec4(position, 1.0);
    pass_uvs = uvs;
}
