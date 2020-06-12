package engine.shader.types

import org.joml.Matrix4f

interface ProjectionMatrixShader {
    var projectionMatrixLocation: Int
    fun loadProjectionMatrix(matrix: Matrix4f)
}