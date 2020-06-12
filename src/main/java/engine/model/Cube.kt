package engine.model

import engine.render.Mesh
import engine.render.MeshLoader
import engine.render.WorldObject3D
import org.joml.Vector3f

class Cube(position: Vector3f = Vector3f(0f, 0f, 0f), rotation: Vector3f = Vector3f(0f, 0f, 0f), scale: Float = 1f) : WorldObject3D(mesh, position, rotation, scale) {
    companion object {
        private val positions = floatArrayOf(
                -0.5f, 0.5f, 0.5f,
                -0.5f, -0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,
                -0.5f, 0.5f, -0.5f,
                0.5f, 0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f
        )

        private val indices = intArrayOf(
                0, 1, 3, 3, 1, 2,   // Front Face
                4, 0, 3, 5, 4, 3,   // Top Face
                3, 2, 7, 5, 3, 7,   // Right Face
                6, 1, 0, 6, 0, 4,   // Left Face
                2, 1, 6, 2, 6, 7,   // Bottom Face
                7, 6, 4, 7, 4, 5    // Back Face
        )

        private val uvs = floatArrayOf()

        private val mesh: Mesh = MeshLoader.createMesh(positions, uvs, indices)
    }
}
