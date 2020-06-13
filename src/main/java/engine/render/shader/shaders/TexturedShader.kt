package engine.render.shader.shaders

class TexturedShader : MatrixShader("TexturedVS.glsl", "TexturedFS.glsl") {
    override val attributeAmount: Int = 1

    override fun bindAttributes() {
        bindAttribute(0, "position")
        bindAttribute(1, "uvs")
        bindAttribute(2, "normals")
    }
}