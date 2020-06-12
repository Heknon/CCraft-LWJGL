package engine

import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.*


object Utils {
    fun loadResource(fileName: String): String {
        var result = ""
        this::class.java.getResourceAsStream(fileName).use {
            Scanner(it, StandardCharsets.UTF_8.name())
                    .use { scanner ->
                        result = scanner.useDelimiter("\\A").next()
                    }
        }
        return result
    }

    fun readAllLines(fileName: String): List<String> {
        val list: MutableList<String> = ArrayList()
        BufferedReader(InputStreamReader(Class.forName(Utils::class.java.name).getResourceAsStream(fileName))).use { br ->
            var line: String
            while (br.readLine().also { line = it } != null) {
                list.add(line)
            }
        }
        return list
    }

}