package engine.render

import org.joml.Vector3f

open class WorldObject3D(
        val mesh: Mesh,
        val position: Vector3f = Vector3f(0f, 0f, 0f),
        val rotation: Vector3f = Vector3f(0f, 0f, 0f),
        val scale: Float = 1f
) {
    fun setPosition(x: Float, y: Float, z: Float) {
        position.x = x
        position.y = y
        position.z = z
    }

    fun setRotation(x: Float, y: Float, z: Float) {
        rotation.x = x
        rotation.y = y
        rotation.z = z
    }
}