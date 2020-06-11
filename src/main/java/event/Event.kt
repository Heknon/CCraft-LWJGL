package event

open class Event(open val window: Long, open val action: Int) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Event

        if (window != other.window) return false
        if (action != other.action) return false

        return true
    }

    override fun hashCode(): Int {
        var result = window.hashCode()
        result = 31 * result + action
        return result
    }

    override fun toString(): String {
        return "Event(window=$window, action=$action)"
    }
}