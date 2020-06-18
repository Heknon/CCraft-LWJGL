package engine.render.shader.types

import engine.render.lighting.LightMaterial
import engine.render.lighting.PointLight
import org.joml.Vector3f

interface PhongShader {
    var pointLightLocation: MutableMap<String, Int>
    var materialLocation: MutableMap<String, Int>
    var specularPowerLocation: Int
    var ambientLightLocation: Int

    fun loadMaterial(lightMaterial: LightMaterial)
    fun loadPointLight(pointLight: PointLight)
    fun loadAmbientLight(ambientLight: Vector3f)
    fun loadSpecularPower(specularPower: Float)
}