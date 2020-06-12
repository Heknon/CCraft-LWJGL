package engine.shader.types

import org.joml.Matrix4f

interface WorldMatrixShader {
    var transformationMatrixLocation: Int
    fun loadWorldMatrix(matrix: Matrix4f)
}