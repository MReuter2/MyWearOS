package com.example.mywearos.data.sensor

interface TrillFlexEvent{

}

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

class MultiTouch(val touches: List<Touch>): TrillFlexEvent

class MultiScroll(val scrolls: List<Scroll>): TrillFlexEvent

class NoEvent(): TrillFlexEvent{
    override fun toString(): String {
        return "No Event!"
    }
}