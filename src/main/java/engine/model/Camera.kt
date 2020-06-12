package engine.model

import engine.render.Mesh
import engine.render.WorldObject3D
import org.joml.Vector3f
import kotlin.math.cos
import kotlin.math.sin

class Camera(position: Vector3f = Vector3f(0f, 0f, 0f), rotation: Vector3f = Vector3f(0f, 0f, 0f)) : WorldObject3D(mesh, position, rotation, 0f) {
    fun movePosition(offsetX: Float, offsetY: Float, offsetZ: Float) {
        if (offsetZ != 0f) {
            position.x += sin(Math.toRadians(rotation.y.toDouble())).toFloat() * -1.0f * offsetZ
            position.z += cos(Math.toRadians(rotation.y.toDouble())).toFloat() * offsetZ
        }

        if (offsetX != 0f) {
            position.x += sin(Math.toRadians(rotation.y.toDouble() - 90)).toFloat() * -1.0f * offsetX
            position.z += cos(Math.toRadians(rotation.y.toDouble() - 90)).toFloat() * offsetX
        }

        position.y += offsetY
    }

    fun moveRotation(offsetX: Float, offsetY: Float, offsetZ: Float) {
        rotation.x += offsetX
        rotation.y += offsetY
        rotation.z += offsetZ
    }

    companion object {
        val mesh: Mesh = Mesh(0, 0)
    }
}