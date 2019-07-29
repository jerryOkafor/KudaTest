package me.jerryhanks.kudatest

class SectionHeader(private val section: Int) : Section {
    override fun type() =  Section.HEADER
    override fun sectionPosition() =  section

}
