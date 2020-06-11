package render.shader

import org.joml.Matrix4f

interface ProjectableShader {
    var projectionMatrixLocation: Int
    fun loadProjectionMatrix(matrix: Matrix4f)
}