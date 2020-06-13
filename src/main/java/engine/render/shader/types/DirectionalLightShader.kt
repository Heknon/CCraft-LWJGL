package engine.render.shader.types

import engine.render.lighting.DirectionalLight

interface DirectionalLightShader {
    var directionalLightLocation: MutableMap<String, Int>

    fun loadDirectionLight(directionalLight: DirectionalLight)
}