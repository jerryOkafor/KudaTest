package me.jerryhanks.kudatest

interface Section {
    fun type(): Int
    fun sectionPosition(): Int
    companion object {
        const val HEADER = 0
        const val ITEM = 1
        const val CUSTOM_HEADER = 2
    }
}
