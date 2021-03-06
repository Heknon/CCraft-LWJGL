package engine

interface IGameLogic {

    fun init(window: Window)
    fun render(window: Window)
    fun update(interval: Float, mouseInput: MouseInput)
    fun input(window: Window, mouseInput: MouseInput)
    fun cleanup()

}