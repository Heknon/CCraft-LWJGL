package engine.render.lighting

import org.joml.Vector3f

class DirectionalLight(val color: Vector3f, var direction: Vector3f, var intensity: Float) {

    constructor(directionalLight: DirectionalLight) : this(
            Vector3f(directionalLight.color),
            Vector3f(directionalLight.direction),
            directionalLight.intensity
    )
}