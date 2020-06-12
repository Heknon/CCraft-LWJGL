package engine

class GameEngine(
        private val title: String,
        private val size: Window.Size,
        private val gameLogic: IGameLogic,
        private var vSync: Boolean = true
) : Runnable {
    private val gameLoopThread: Thread = Thread(this, "GAME LOOP THREAD | $title")
    private val window: Window = Window(title, size, vSync)
    private val timer: Timer = Timer()
    private val mouseInput: MouseInput = MouseInput()

    fun start() {
        if (System.getProperty("os.name").contains("Mac")) {
            gameLoopThread.run()
        } else {
            gameLoopThread.start()
        }
    }

    override fun run() {
        try {
            init()
            gameLoop()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cleanup()
        }
    }

    fun render() {
        gameLogic.render(window)
        window.update()
    }

    fun input() {
        mouseInput.input(window)
        gameLogic.input(window, mouseInput)
    }

    fun update(interval: Float) {
        gameLogic.update(interval, mouseInput)
    }

    private fun cleanup() {
        gameLogic.cleanup()
    }

    private fun init() {
        window.init()
        timer.init()
        gameLogic.init()
        mouseInput.init(window)
    }

    private fun gameLoop() {
        var elapsedTime: Float
        var accumulator = 0f
        val interval = 1f / TARGET_UPS

        var running = true
        while (running && !window.shouldClose()) {
            elapsedTime = timer.elapsedTime
            accumulator += elapsedTime

            input()

            // keep game updating if can't render
            while (accumulator >= interval) {
                update(interval)
                accumulator -= interval
            }

            render()

            if (!window.vSync) {
                sync()
            }
        }
    }

    private fun sync() {
        val loopSlot = 1f / TARGET_FPS
        val endTime = timer.lastLoopTime + loopSlot

        while (timer.time < endTime) {
            try {
                Thread.sleep(1)
            } catch (e: InterruptedException) {

            }
        }
    }

    companion object {
        const val TARGET_UPS = 30
        const val TARGET_FPS = 75
    }
}