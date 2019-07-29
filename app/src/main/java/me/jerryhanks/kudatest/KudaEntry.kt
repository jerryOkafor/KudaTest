package me.jerryhanks.kudatest

import android.graphics.drawable.Drawable
import com.github.mikephil.charting.data.Entry


/**
 * @author jerry on 2019-07-28
 * for KudaTest
 **/

class KudaEntry(x:Float,y:Float,icon:Drawable, val section: Section) : Entry(x,y,icon)