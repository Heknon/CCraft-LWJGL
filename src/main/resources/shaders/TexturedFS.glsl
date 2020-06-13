#version 150 core

in vec2 pass_uvs;
in vec3 mvNormals;
in vec3 mvPosition;

out vec4 out_Color;

struct Attenuation {
    float constant;
    float linear;
    float exponent;
};

struct PointLight {
    vec3 color;
    vec3 position;    // Light position is assumed to be in view coordinates
    float intensity;
    Attenuation att;
};

struct Material {
    vec4 ambient;
    vec4 diffuse;
    vec4 specular;
    int hasTexture;
    float reflectance;
};

struct DirectionalLight {
    vec3 color;
    vec3 direction;
    float intensity;
};


uniform sampler2D textureSampler;
uniform vec3 ambientLight;
uniform float specularPower;
uniform Material material;
uniform PointLight pointLight;
uniform vec3 camera_pos;
uniform DirectionalLight directionalLight;

vec4 ambientC;
vec4 diffuseC;
vec4 specularC;

void setupColors(Material material, vec2 texCoord) {
    if (material.hasTexture == 0f) {
        ambientC = texture(textureSampler, texCoord);
        diffuseC = ambientC;
        specularC = ambientC;
    } else {
        ambientC = material.ambient;
        diffuseC = material.diffuse;
        specularC = material.specular;
        ambientC = vec4(0, 0, 0, 0);
        diffuseC = vec4(0, 0, 0, 0);
        specularC = vec4(0, 0, 0, 0);
    }
}

vec4 calcLightColor(vec3 lightColor, float lightIntensity, vec3 position, vec3 toLightDirection, vec3 normal) {
    vec4 diffuseColor = vec4(0, 0, 0, 0);
    vec4 specularColor = vec4(0, 0, 0, 0);

    // diffuse light
    float diffuseFactor = max(dot(normal, toLightDirection), 0.0);
    diffuseColor = diffuseC * vec4(lightColor, 1.0) * lightIntensity * diffuseFactor;

    // Specular Light
    vec3 cameraDirection = normalize(camera_pos - position);
    vec3 fromLightDirection = -toLightDirection;
    vec3 reflectedLight = normalize(reflect(fromLightDirection, normal));
    float specularFactor = max(dot(cameraDirection, reflectedLight), 0.0);
    specularFactor = pow(specularFactor, specularPower);
    specularColor = specularC * lightIntensity * specularFactor * material.reflectance * vec4(lightColor, 1.0);

    return diffuseColor + specularColor;
}

vec4 calcPointLight(PointLight light, vec3 position, vec3 normal) {
    vec3 lightDirection = light.position - position;
    vec3 toLightDirection = normalize(lightDirection);
    vec4 lightColor = calcLightColor(light.color, light.intensity, position, toLightDirection, normal);


    // Attenuation
    float distance = length(lightDirection);
    float attenuationInverse = light.att.constant + light.att.linear * distance + light.att.exponent * distance * distance;

    return lightColor / attenuationInverse;
}

vec4 calcDirectionalLight(DirectionalLight light, vec3 position, vec3 normal) {
    return calcLightColor(light.color, light.intensity, position, normalize(light.direction), normal);
}

void main() {
    setupColors(material, pass_uvs);

    vec4 diffuseSpecularComp = calcDirectionalLight(directionalLight, mvPosition, mvNormals);
    diffuseSpecularComp += calcPointLight(pointLight, mvPosition, mvNormals);

    out_Color = ambientC * vec4(ambientLight, 1) + diffuseSpecularComp;
}
