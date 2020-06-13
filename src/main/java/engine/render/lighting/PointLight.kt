package engine.render.lighting

import org.joml.Vector3f

class PointLight(
        val color: Vector3f,
        val position: Vector3f,
        var intensity: Float,
        var attenuation: Attenuation = Attenuation(1f, 0f, 0f)
) {

    constructor(pointLight: PointLight) : this(
            Vector3f(pointLight.color),
            Vector3f(pointLight.position),
            pointLight.intensity,
            Attenuation(pointLight.attenuation)
    )

    data class Attenuation(var constant: Float, var linear: Float, var exponent: Float) {
        constructor(attenuation: Attenuation) : this(attenuation.constant, attenuation.linear, attenuation.exponent)
    }

    override fun toString(): String {
        return "PointLight(color=$color, position=$position, intensity=$intensity, attenuation=$attenuation)"
    }


}