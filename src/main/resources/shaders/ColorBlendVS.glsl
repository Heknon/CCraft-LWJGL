#version 450 core

in vec3 position;

uniform mat4 worldMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

out vec3 color;

void main(void) {
    gl_Position = projectionMatrix * worldMatrix * vec4(position, 1);
    color = vec3(position.x + 0.5, position.y + 0.5, position.z + 0.5);
}
