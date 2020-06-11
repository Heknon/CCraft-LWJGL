package render

data class Mesh(val vertexArrayObjectID: Int, val vertexCount: Int) {
    var texture: Int = 0
        private set

    fun addTexture(texture: String): Mesh {
        this.texture = Texture.loadTexture(texture)
        return this
    }
}