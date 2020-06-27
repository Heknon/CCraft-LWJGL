package game.world

data class Location<T : Number>(val x: T, val y: T, val z: T) {
    fun pack(): Long {
        return pack(x, y, z)
    }

    companion object {
        inline fun <reified T : Number> unpack(packed: Long): Location<T> {
            val x = (packed shl 37 shr 37).toInt()
            val y = (packed ushr 54).toInt()
            val z = (packed shl 10 shr 37).toInt()
            return Location(x as T, y as T, z as T)
        }

        fun <T : Number> pack(x: T, y: T, z: T): Long {
            return x.toLong() and 0x7FFFFFF or (z.toLong() and 0x7FFFFFF shl 27) or (y.toLong() shl 54)
        }
    }
}