package com.example.mywearos.data.sensor

interface TrillFlexEvent{
    val numberOfFingers: Int
}

//Location: 0-3712, Size: 1-...
class Touch(override val numberOfFingers: Int): TrillFlexEvent{
    override fun toString(): String {
        return "Touch, Fingers: $numberOfFingers"
    }
}

//Pace: 0-1f
class Scroll(val direction: ActionDirection, val pace: Int, override val numberOfFingers: Int): TrillFlexEvent{
    override fun toString(): String {
        return "Scroll, Fingers: $numberOfFingers, Pace: $pace"
    }
}

class Swipe(val direction: ActionDirection, override val numberOfFingers: Int): TrillFlexEvent{
    override fun toString(): String {
        return "Swipe, Direction: $direction, Fingers: $numberOfFingers"
    }
}

class NoEvent(): TrillFlexEvent{
    override val numberOfFingers: Int = 0
    override fun toString(): String {
        return "No Event!"
    }
}

enum class ActionDirection{
    POSITIVE,
    NEGATIVE
}