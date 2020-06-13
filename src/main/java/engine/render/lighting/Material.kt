package engine.render.lighting

import org.joml.Vector4f

data class Material(
        val ambientColor: Vector4f = DEFAULT_COLOUR,
        val diffuseColor: Vector4f = DEFAULT_COLOUR,
        val specularColor: Vector4f = DEFAULT_COLOUR,
        var reflectance: Float = 0f,
        var textureId: Int? = null
) {
    val isTextured: Boolean get() = textureId != null

    companion object {
        val DEFAULT_COLOUR = Vector4f(1.0f, 1.0f, 1.0f, 1.0f)
    }
}