package engine.render.shader.types

import org.joml.Matrix4f

interface ModelViewMatrixShader {
    var modelViewMatrixLocation: Int
    fun loadModelViewMatrix(matrix: Matrix4f)
}