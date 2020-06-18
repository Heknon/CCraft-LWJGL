import game.world.OpenSimplexNoise
import org.joml.Random
import java.awt.image.BufferedImage
import java.io.File
import java.util.concurrent.ThreadLocalRandom
import javax.imageio.ImageIO


fun main() {
    generateRandomPicture("a")
    generateRandomPicture("b")
    generateRandomPicture("c")
    generateRandomPicture("d")
}

fun generateRandomPicture(name: String) {
    Thread {
        val height = 2160
        val width = 4096
        val rand = ThreadLocalRandom.current()
        val featureSize = rand.nextInt(30, 190)
        val img = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        val gen = OpenSimplexNoise(rand.nextLong(Random.newSeed(), Long.MAX_VALUE))

        for (r in 0 until height) for (c in 0 until width) {

            val noise = gen.eval(r.toDouble() / featureSize, c.toDouble() / featureSize)

            val rgb = 0x010101 * ((noise + 1) * 255f).toInt()
            img.setRGB(c, r, rgb)
        }
        ImageIO.write(img, "png", File("C:\\Users\\Ori\\Desktop\\Development\\FinalProject\\CCraft\\$name.png"))
        println("image created")
    }.start()
}