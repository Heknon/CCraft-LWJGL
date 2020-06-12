package engine

interface IGameLogic {

    fun init()
    fun render(window: Window)
    fun update(interval: Float)
    fun input(window: Window)
    fun cleanup()

}