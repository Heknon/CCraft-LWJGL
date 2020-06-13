package engine.render.model

import engine.render.Mesh
import engine.render.MeshLoader
import engine.render.WorldObject3D
import org.joml.Vector3f

class Cube(
        position: Vector3f = Vector3f(0f, 0f, 0f),
        rotation: Vector3f = Vector3f(0f, 0f, 0f),
        scale: Float = 1f
) : WorldObject3D(mesh, position, rotation, scale) {
    companion object {
        private val positions: FloatArray = floatArrayOf(
                0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f, -0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, -0.5f, -0.5f, -0.5f, -0.5f, -0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f
        )
        
        private val uvs = floatArrayOf(
                0.0f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 0.5f, 0.0f, 0.5f, 0.0f, 0.5f, 0.0f, 1.0f, 0.0f, 0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 0.5f, 1.0f, 0.5f, 0.0f, 0.5f, 0.5f, 0.0f, 0.5f, 0.5f, 0.5f, 0.5f, 0.0f, 0.5f, 0.0f, 0.0f, 0.5f, 1.0f, 2.0E-4f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.5f
        )
        
        private val normal = floatArrayOf(0.0f, 0.0f, -1.0f, 0.0f, 0.0f, 1.0f, -1.0f, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, 1.0f, -1.0f, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f, -1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, -1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f)

        private val indices = intArrayOf(10, 16, 12, 23, 21, 19, 14, 18, 11, 5, 20, 17, 2, 6, 22, 0, 3, 7, 8, 10, 12, 15, 23, 19, 9, 14, 11, 1, 5, 17, 13, 2, 22, 4, 0, 7)

        private val mesh: Mesh = MeshLoader.createMesh(positions, uvs, normal, indices).addTexture("textures/cube.png")
    }
}
