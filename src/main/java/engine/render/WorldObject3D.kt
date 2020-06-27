package engine.render

import org.joml.Vector3d
import org.joml.Vector3f

open class WorldObject3D(
        var mesh: MeshHolder?,
        val position: Vector3d,
        val rotation: Vector3d = Vector3d(0.0, 0.0, 0.0),
        val scale: Float = 1f
) {
    fun setPosition(x: Double, y: Double, z: Double) {
        position.x = x
        position.y = y
        position.z = z
    }

    fun setRotation(x: Double, y: Double, z: Double) {
        rotation.x = x
        rotation.y = y
        rotation.z = z
    }
}