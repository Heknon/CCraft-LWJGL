package engine.shader.types

import org.joml.Matrix4f

interface ProjectableMatrixShader {
    var projectionMatrixLocation: Int
    fun loadProjectionMatrix(matrix: Matrix4f)
}