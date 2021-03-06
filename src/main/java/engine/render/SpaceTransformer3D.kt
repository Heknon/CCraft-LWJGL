package engine.render

import engine.Window
import engine.render.model.Camera
import org.joml.Matrix4f
import org.joml.Vector3f

class SpaceTransformer3D {
    private val projectionMatrix: Matrix4f = Matrix4f()
    private val modelViewMatrix: Matrix4f = Matrix4f()
    private val viewMatrix: Matrix4f = Matrix4f()

    fun getProjectionMatrix(fov: Float, size: Window.Size, zNear: Float, zFar: Float): Matrix4f {
        projectionMatrix.identity()
        projectionMatrix.perspective(fov, size.aspectRatio, zNear, zFar)
        return projectionMatrix
    }

    fun getModelViewMatrix(obj: WorldObject3D, viewMatrix: Matrix4f): Matrix4f {
        val rot = obj.rotation

        modelViewMatrix
                .identity()
                .translate(obj.position.x.toFloat(), obj.position.y.toFloat(), obj.position.z.toFloat())
                .rotateXYZ(
                        Math.toRadians(-rot.x).toFloat(),
                        Math.toRadians(-rot.y).toFloat(),
                        Math.toRadians(-rot.z).toFloat()
                ).scale(obj.scale)

        val viewCurr = Matrix4f(viewMatrix)
        return viewCurr.mul(modelViewMatrix)
    }

    fun getViewMatrix(camera: Camera): Matrix4f {
        val pos = camera.position
        val rot = camera.rotation

        viewMatrix.identity()
        viewMatrix
                .rotate(Math.toRadians(rot.x).toFloat(), Vector3f(1f, 0f, 0f))
                .rotate(Math.toRadians(rot.y).toFloat(), Vector3f(0f, 1f, 0f))

        viewMatrix.translate(-pos.x.toFloat(), -pos.y.toFloat(), -pos.z.toFloat())
        return viewMatrix
    }

}