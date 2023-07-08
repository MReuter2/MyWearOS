package com.example.mywearos.data

sealed class TrillFlexEvent(val numberOfFingers: Int)

//Location: 0-3712, Size: 1-...
class Touch(numberOfFingers: Int): TrillFlexEvent(numberOfFingers) {
    override fun toString(): String {
        return "Touch, Fingers: $numberOfFingers"
    }
}

class Scroll(val pace: Int, numberOfFingers: Int):
    TrillFlexEvent(numberOfFingers) {
    override fun toString(): String {
        return "Scroll, Fingers: $numberOfFingers, Pace: $pace"
    }
}

class Swipe(numberOfFingers: Int, val pace: Int): TrillFlexEvent(numberOfFingers) {
    override fun toString(): String {
        return "Swipe, Fingers: $numberOfFingers"
    }
}

class NoEvent: TrillFlexEvent(0) {
    override fun toString(): String {
        return "No Event!"
    }
}