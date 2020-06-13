package engine.render.shader.shaders

import engine.render.lighting.DirectionalLight
import engine.render.lighting.Material
import engine.render.lighting.PointLight
import engine.render.shader.Shader
import engine.render.shader.types.DirectionalLightShader
import engine.render.shader.types.ModelViewMatrixShader
import engine.render.shader.types.PhongShader
import engine.render.shader.types.ProjectionMatrixShader
import org.joml.Matrix4f
import org.joml.Vector3f

abstract class MatrixShader(
        vertexShader: String,
        fragmentShader: String
) : Shader(vertexShader, fragmentShader), ProjectionMatrixShader, ModelViewMatrixShader, PhongShader, DirectionalLightShader {
    override var projectionMatrixLocation = 0
    override var modelViewMatrixLocation: Int = 0
    override var ambientLightLocation: Int = 0
    override var specularPowerLocation: Int = 0
    override lateinit var materialLocation: MutableMap<String, Int>
    override lateinit var pointLightLocation: MutableMap<String, Int>
    override lateinit var directionalLightLocation: MutableMap<String, Int>

    override fun getAllUniformLocations() {
        modelViewMatrixLocation = getUniformLocation("modelViewMatrix")
        projectionMatrixLocation = getUniformLocation("projectionMatrix")
        materialLocation = mutableMapOf()
        pointLightLocation = mutableMapOf()
        directionalLightLocation = mutableMapOf()
        arrayOf("color", "direction", "intensity").forEach {
            directionalLightLocation[it] = getUniformLocation("directionalLight.$it")
        }

        arrayOf("ambient", "diffuse", "specular", "hasTexture", "reflectance").forEach {
            materialLocation[it] = getUniformLocation("material.$it")
        }

        arrayOf("color", "position", "intensity", "att.constant", "att.linear", "att.exponent").forEach {
            pointLightLocation[it] = getUniformLocation("pointLight.$it")
        }

        specularPowerLocation = getUniformLocation("specularPower")
        ambientLightLocation = getUniformLocation("ambientLight")
    }


    override fun loadProjectionMatrix(matrix: Matrix4f) {
        load(projectionMatrixLocation, matrix)
    }

    override fun loadModelViewMatrix(matrix: Matrix4f) {
        load(modelViewMatrixLocation, matrix)
    }

    override fun loadMaterial(material: Material) {
        load(materialLocation["ambient"]!!, material.ambientColor)
        load(materialLocation["diffuse"]!!, material.diffuseColor)
        load(materialLocation["specular"]!!, material.specularColor)
        load(materialLocation["hasTexture"]!!, material.isTextured)
        load(materialLocation["reflectance"]!!, material.reflectance)
    }

    override fun loadPointLight(pointLight: PointLight) {
        load(pointLightLocation["color"]!!, pointLight.color)
        load(pointLightLocation["position"]!!, pointLight.position)
        load(pointLightLocation["intensity"]!!, pointLight.intensity)
        load(pointLightLocation["att.constant"]!!, pointLight.attenuation.constant)
        load(pointLightLocation["att.linear"]!!, pointLight.attenuation.linear)
        load(pointLightLocation["att.exponent"]!!, pointLight.attenuation.exponent)
    }

    override fun loadDirectionLight(directionalLight: DirectionalLight) {
        load(directionalLightLocation["color"]!!, directionalLight.color)
        load(directionalLightLocation["direction"]!!, directionalLight.direction)
        load(directionalLightLocation["intensity"]!!, directionalLight.intensity)
    }

    override fun loadAmbientLight(ambientLight: Vector3f) {
        load(ambientLightLocation, ambientLight)
    }

    override fun loadSpecularPower(specularPower: Float) {
        load(specularPowerLocation, specularPower)
    }
}