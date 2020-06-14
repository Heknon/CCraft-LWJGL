package engine.render.model

import engine.render.Mesh
import engine.render.MeshLoader
import engine.render.WorldObject3D
import org.joml.Vector3f

open class Cube(
        position: Vector3f = Vector3f(0f, 0f, 0f),
        rotation: Vector3f = Vector3f(0f, 0f, 0f),
        scale: Float = 1f,
        mesh: Mesh
) : WorldObject3D(mesh, position, rotation, scale) {

}
