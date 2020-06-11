package render.shader

import org.joml.Matrix4f

interface TransformableShader {
    var transformationMatrixLocation: Int
    fun loadTransformationMatrix(matrix: Matrix4f)
}