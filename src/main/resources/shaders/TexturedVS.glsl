#version 450 core

in vec3 position;
in vec2 uvs;
in vec3 normals;

uniform mat4 modelViewMatrix;
uniform mat4 projectionMatrix;

out vec2 pass_uvs;
out vec3 mvNormals;
out vec3 mvPosition;

void main(void) {
    vec4 mvPos = modelViewMatrix * vec4(position, 1.0);
    gl_Position = projectionMatrix * mvPos;
    pass_uvs = uvs;
    mvNormals = normalize(modelViewMatrix * vec4(normals, 0.0)).xyz;
    mvPosition = mvPos.xyz;
}
