package render.shader

import org.joml.Matrix4f
import org.joml.Matrix4fc
import org.joml.Vector3f
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20
import java.nio.FloatBuffer
import kotlin.properties.Delegates

abstract class Shader(private val vertexShader: String, private val fragmentShader: String) {
    private val matrix: FloatBuffer = BufferUtils.createFloatBuffer(16)
    private var programID by Delegates.notNull<Int>()
    private var vertexID by Delegates.notNull<Int>()
    private var fragmentID by Delegates.notNull<Int>()
    abstract val attributeAmount: Int

    protected abstract fun bindAttributes()
    protected abstract fun getAllUniformLocations()

    init {
        vertexID = loadShader(vertexShader, GL20.GL_VERTEX_SHADER)
        fragmentID = loadShader(fragmentShader, GL20.GL_FRAGMENT_SHADER)
        programID = GL20.glCreateProgram()
        GL20.glAttachShader(programID, vertexID)
        GL20.glAttachShader(programID, fragmentID)
        bindAttributes()
        GL20.glLinkProgram(programID)
        GL20.glValidateProgram(programID)
        getAllUniformLocations()
    }

    fun start() {
        GL20.glUseProgram(programID)
    }

    fun stop() {
        GL20.glUseProgram(0)
    }

    fun cleanUp() {
        stop()
        GL20.glDetachShader(programID, vertexID)
        GL20.glDetachShader(programID, fragmentID)
        GL20.glDeleteShader(vertexID)
        GL20.glDeleteShader(fragmentID)
        GL20.glDeleteProgram(programID)
    }

    protected fun getUniformLocation(uniformName: String): Int {
        return GL20.glGetUniformLocation(programID, uniformName)
    }

    protected fun bindAttribute(attribute: Int, variableName: String) {
        GL20.glBindAttribLocation(programID, attribute, variableName)
    }

    protected fun loadFloat(location: Int, value: Float) {
        GL20.glUniform1f(location, value)
    }

    protected fun loadVector(location: Int, vector: Vector3f) {
        GL20.glUniform3f(location, vector.x, vector.y, vector.z)
    }

    protected fun loadBoolean(location: Int, value: Boolean) {
        GL20.glUniform1f(location, if (value) 1f else 0f)
    }

    protected open fun loadMatrix(location: Int, value: Matrix4f) {
        value.get(matrix)
        matrix.flip()
        GL20.glUniformMatrix4fv(location, false, matrix)
    }

    companion object {
        private fun loadShader(file: String, type: Int): Int {
            val shaderSourceURL = javaClass.getResource("/shaders/${file}")
                    ?: throw RuntimeException("Can't get shader file $file")
            val shaderSource = shaderSourceURL.readText()
            val id = GL20.glCreateShader(type)
            GL20.glShaderSource(id, shaderSource)
            GL20.glCompileShader(id)
            if (GL20.glGetShaderi(id, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
                throw RuntimeException("\nFILE: $file\nTYPE: $type\n" + GL20.glGetShaderInfoLog(id, 512))
            }

            return id
        }
    }

}