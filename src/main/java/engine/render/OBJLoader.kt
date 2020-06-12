package engine.render

import org.joml.Vector2f
import org.joml.Vector3f
import java.util.*

object OBJLoader {
    fun loadMesh(fileName: String): Mesh {
        val lines = this::class.java.getResource("/models/$fileName").readText().split("\n")

        val vertices: MutableList<Vector3f> = ArrayList()
        val textures: MutableList<Vector2f> = ArrayList()
        val normals: MutableList<Vector3f> = ArrayList()
        val faces: MutableList<Face> = ArrayList<Face>()

        for (line in lines) {
            val tokens = line.split(Regex("\\s+"))
            when (tokens[0]) {
                "v" -> vertices.add(Vector3f(
                        tokens[1].toFloat(),
                        tokens[2].toFloat(),
                        tokens[3].toFloat()
                ))
                "vt" -> textures.add(Vector2f(
                        tokens[1].toFloat(),
                        tokens[2].toFloat()
                ))
                "vn" -> normals.add(Vector3f(
                        tokens[1].toFloat(),
                        tokens[2].toFloat(),
                        tokens[3].toFloat()
                ))
                "f" -> faces.add(Face(
                        tokens[1],
                        tokens[2],
                        tokens[3]
                ))

            }
        }
        println(vertices)
        return reorderLists(vertices, textures, normals, faces)
    }

    private fun reorderLists(
            posList: List<Vector3f>,
            textureCoordinateList: List<Vector2f>,
            normalList: List<Vector3f>,
            facesList: List<Face>
    ): Mesh {
        val indices: MutableList<Int> = mutableListOf()

        val posArr = FloatArray(posList.size * 3)
        for ((i, pos) in posList.withIndex()) {
            posArr[i * 3] = pos.x
            posArr[i * 3 + 1] = pos.y
            posArr[i * 3 + 2] = pos.z
        }

        val textureCoordinateArr = FloatArray(posList.size * 2)
        val normalArr = FloatArray(posList.size * 3)

        for (face in facesList) {
            val faceVertexIndices = face.vertexIndices
            for (indexValue in faceVertexIndices) {
                processFaceVertex(indexValue, textureCoordinateList, normalList, indices, textureCoordinateArr, normalArr)
            }
        }

        val indicesArr = IntArray(indices.size) {
            indices[it]
        }

        println(posArr.toList())
        return MeshLoader.createMesh(posArr, textureCoordinateArr, normalArr, indicesArr)
    }

    private fun processFaceVertex(
            indexValue: IndexGroup,
            textureCoordinateList: List<Vector2f>,
            normalList: List<Vector3f>,
            indices: MutableList<Int>,
            textureCoordinateArr: FloatArray,
            normalArr: FloatArray
    ) {
        val index = indexValue.indexPos
        indices.add(index)

        if (indexValue.indexTextureCoordinate >= 0) {
            val textureCoordinate = textureCoordinateList[indexValue.indexTextureCoordinate]
            textureCoordinateArr[index * 2] = textureCoordinate.x
            textureCoordinateArr[index * 2 + 1] = 1 - textureCoordinate.y
        }

        if (indexValue.indexVectorNormal >= 0) {
            val vectorNormal = normalList[indexValue.indexVectorNormal]
            normalArr[index * 3] = vectorNormal.x
            normalArr[index * 3 + 1] = vectorNormal.y
            normalArr[index * 3 + 2] = vectorNormal.z
        }
    }


    class IndexGroup {
        var indexPos = NO_VALUE
        var indexTextureCoordinate = NO_VALUE
        var indexVectorNormal = NO_VALUE

        companion object {
            const val NO_VALUE = -1
        }
    }

    class Face(vararg v: String) {
        val vertexIndices: Array<IndexGroup>

        init {
            vertexIndices = Array(v.size) {
                parseLine(v[it])
            }
        }

        private fun parseLine(line: String): IndexGroup {
            val indexGroup = IndexGroup()

            val lineTokens = line.split("/")
            val length = lineTokens.size
            indexGroup.indexPos = lineTokens[0].toInt() - 1
            if (length > 1) {
                val textureCoordinate: String = lineTokens[1]
                indexGroup.indexTextureCoordinate = if (textureCoordinate.isNotEmpty()) textureCoordinate.toInt() - 1 else IndexGroup.NO_VALUE
                if (length > 2) {
                    indexGroup.indexVectorNormal = lineTokens[2].toInt() - 1
                }
            }

            return indexGroup
        }
    }
}