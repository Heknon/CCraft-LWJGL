package render.shader

class ColorBlendShader : MatrixShader("ColorBlendVS.glsl", "ColorBlendFS.glsl") {
    override val attributeAmount: Int = 0

    override fun bindAttributes() {
        bindAttribute(0, "position")
    }
}