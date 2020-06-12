package engine.shader.types

import org.joml.Matrix4f

interface ViewMatrixShader {
    var projectionMatrixLocation: Int
    fun loadProjectionMatrix(matrix: Matrix4f)
}