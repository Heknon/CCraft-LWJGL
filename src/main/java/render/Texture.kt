package render

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL30
import org.lwjgl.stb.STBImage
import org.lwjgl.system.MemoryStack
import java.nio.ByteBuffer

class Texture {

    companion object {
        private val idMap: MutableMap<String, Int> = mutableMapOf()

        fun loadTexture(resourcePath: String): Int {
            if (idMap.containsKey(resourcePath)) {
                return idMap["/$resourcePath"]!!
            }

            val width: Int
            val height: Int
            val buffer: ByteBuffer?
            try {
                val stack: MemoryStack = MemoryStack.stackPush()
                val w = stack.mallocInt(1)
                val h = stack.mallocInt(1)
                val channels = stack.mallocInt(1)

                val url = javaClass.getResource("/textures/${resourcePath}")
                buffer = STBImage.stbi_load(url.file.substring(1, url.file.length), w, h, channels, 4)
                if (buffer == null) {
                    throw RuntimeException("Can't load file $resourcePath ${STBImage.stbi_failure_reason()}")
                }

                width = w.get()
                height = h.get()
                val id = GL11.glGenTextures()
                idMap[resourcePath] = id
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, id)
                GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1)

                GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer)
                GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D)
                STBImage.stbi_image_free(buffer)
                return id


            } catch (e: Exception) {
                e.printStackTrace()
            }
            return -1
        }

        fun cleanUp() {
            GL11.glDeleteTextures(idMap.values.toIntArray())
        }
    }
}