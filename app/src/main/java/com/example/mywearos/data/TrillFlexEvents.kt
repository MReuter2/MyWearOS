package com.example.mywearos.data

sealed class TrillFlexEvent(val numberOfFingers: Int, val actionDirection: ActionDirection)

//Location: 0-3712, Size: 1-...
class Touch(numberOfFingers: Int): TrillFlexEvent(numberOfFingers, ActionDirection.NEUTRAL) {
    override fun toString(): String {
        return "Touch, Fingers: $numberOfFingers"
    }
}

class Scroll(direction: ActionDirection, val pace: Int, numberOfFingers: Int):
    TrillFlexEvent(numberOfFingers, direction) {
    override fun toString(): String {
        return "Scroll, Fingers: $numberOfFingers, Pace: $pace"
    }
}

class Swipe(direction: ActionDirection, numberOfFingers: Int): TrillFlexEvent(numberOfFingers, direction) {
    override fun toString(): String {
        return "Swipe, Direction: $actionDirection, Fingers: $numberOfFingers"
    }
}

class NoEvent: TrillFlexEvent(0, ActionDirection.NEUTRAL) {
    override fun toString(): String {
        return "No Event!"
    }
}

enum class ActionDirection{
    POSITIVE,
    NEGATIVE,
    NEUTRAL
}