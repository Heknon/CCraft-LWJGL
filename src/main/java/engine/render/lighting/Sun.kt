package engine.render.lighting

import org.joml.Vector3f
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

class Sun(
        lightPosition: Vector3f = Vector3f(-1f, 0f, 0f),
        lightColor: Vector3f = Vector3f(1f, 1f, 1f),
        lightIntensity: Float
) {
    private var lightAngle: Float = -90f
    private val directionalLight: DirectionalLight = DirectionalLight(lightPosition, lightColor, lightIntensity)
    val directionalLightClone get() = DirectionalLight(directionalLight)

    fun update() {
        lightAngle += 1.1f

        if (lightAngle > 90) {
            directionalLight.intensity = 0f
            if (lightAngle >= 360) {
                lightAngle = -90f
            }
        } else if (lightAngle <= -80 || lightAngle >= 80) {
            val factor = 1 - (abs(lightAngle) - 80) / 10f
            directionalLight.intensity = factor
            directionalLight.color.y = max(factor, 0.9f)
            directionalLight.color.z = max(factor, 0.5f)
        } else {
            directionalLight.intensity = 1f
            directionalLight.color.x = 1f
            directionalLight.color.y = 1f
            directionalLight.color.z = 1f
        }

        val angRad = Math.toRadians(lightAngle.toDouble())
        directionalLight.direction.x = sin(angRad).toFloat()
        directionalLight.direction.y = cos(angRad).toFloat()
    }
}