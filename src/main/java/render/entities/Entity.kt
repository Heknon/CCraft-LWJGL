package render.entities

import org.joml.Vector3f
import render.Mesh
import render.Rotation

open class Entity(val mesh: Mesh, val position: Vector3f, val rotation: Rotation, val scale: Float) {
}