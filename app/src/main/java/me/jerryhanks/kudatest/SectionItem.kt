package me.jerryhanks.kudatest

class SectionItem(private val section: Int, val value : Float) : Section {
    override fun type() =  Section.ITEM
    override fun sectionPosition() =  section


}
