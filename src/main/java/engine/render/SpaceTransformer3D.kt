package engine.render

import engine.Window
import org.joml.Matrix4f
import org.joml.Vector3f

class SpaceTransformer3D {
    private val projectionMatrix: Matrix4f = Matrix4f()
    private val worldMatrix: Matrix4f = Matrix4f()

    fun getProjectionMatrix(fov: Float, size: Window.Size, zNear: Float, zFar: Float): Matrix4f {
        projectionMatrix.identity()
        projectionMatrix.perspective(fov, size.aspectRatio, zNear, zFar)
        return projectionMatrix
    }

    fun getWorldMatrix(offset: Vector3f, rotation: Vector3f, scale: Float): Matrix4f {
        worldMatrix
                .identity()
                .translate(offset)
                .rotateXYZ(
                        Math.toRadians(rotation.x.toDouble()).toFloat(),
                        Math.toRadians(rotation.y.toDouble()).toFloat(),
                        Math.toRadians(rotation.z.toDouble()).toFloat()
                ).scale(scale)
        return worldMatrix
    }

}