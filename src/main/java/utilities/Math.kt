package utilities

import org.joml.Matrix4f
import org.joml.Vector3f
import render.Rotation

object Math {
    fun createTransformationMatrix(translation: Vector3f, rotation: Rotation, scale: Float): Matrix4f {
        val matrix = Matrix4f()

        matrix.translate(translation, matrix)
        matrix.rotateXYZ(rotation.x, rotation.y, rotation.z, matrix)
        matrix.scale(Vector3f(scale, scale, scale), matrix)

        return matrix
    }
}