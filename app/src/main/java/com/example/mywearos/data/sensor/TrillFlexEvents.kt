package com.example.mywearos.data.sensor

interface TrillFlexEvent

//Location: 0-3712, Size: 1-...
class Touch: TrillFlexEvent{
    override fun toString(): String {
        return "Touch"
    }
}

//Pace: 0-1f
class Scroll(val pace: Int): TrillFlexEvent{
    override fun toString(): String {
        return "Scroll, Pace: $pace"
    }
}

class TwoFingerScroll(val pace: Int): TrillFlexEvent{
    override fun toString(): String {
        return "TwoFingerScoll, Pace: $pace"
    }
}

class NoEvent(): TrillFlexEvent{
    override fun toString(): String {
        return "No Event!"
    }
}