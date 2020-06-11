package render.shader

class TexturedShader : MatrixShader("TexturedVS.glsl", "TexturedFS.glsl") {
    override val attributeAmount: Int = 1

    private var transformMatrixLoc = 0

    override fun bindAttributes() {
        bindAttribute(0, "position")
        bindAttribute(1, "uvs")
    }
}