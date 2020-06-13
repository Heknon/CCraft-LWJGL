#version 450 core

in vec3 position;
in vec3 normals;

uniform mat4 modelViewMatrix;
uniform mat4 projectionMatrix;

out vec3 color;

void main(void) {
    gl_Position = projectionMatrix * modelViewMatrix * vec4(position, 1);
    color = vec3(position.x + 0.5, position.y + 0.5, position.z + 0.5);
}
