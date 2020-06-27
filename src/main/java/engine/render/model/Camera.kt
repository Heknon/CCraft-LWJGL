package engine.render.model

import engine.render.Mesh
import engine.render.WorldObject3D
import org.joml.Vector3d
import org.joml.Vector3f
import kotlin.math.cos
import kotlin.math.sin

class Camera(position: Vector3d = Vector3d(0.0, 0.0, 0.0), rotation: Vector3d = Vector3d(0.0, 0.0, 0.0)) : WorldObject3D(null, position, rotation, 0f) {
    fun movePosition(offsetX: Float, offsetY: Float, offsetZ: Float) {
        if (offsetZ != 0f) {
            position.x += sin(Math.toRadians(rotation.y)).toFloat() * -1.0f * offsetZ
            position.z += cos(Math.toRadians(rotation.y)).toFloat() * offsetZ
        }

        if (offsetX != 0f) {
            position.x += sin(Math.toRadians(rotation.y - 90)).toFloat() * -1.0f * offsetX
            position.z += cos(Math.toRadians(rotation.y - 90)).toFloat() * offsetX
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