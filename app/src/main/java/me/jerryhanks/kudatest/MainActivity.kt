package me.jerryhanks.kudatest

import android.graphics.DashPathEffect
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var items : ArrayList<Section>

    private val chartValueListener  = object  : OnChartValueSelectedListener{
        override fun onNothingSelected() {

        }

        override fun onValueSelected(e: Entry?, h: Highlight?) {

            val section = (e as KudaEntry).section

            lineCharView.setOnChartValueSelectedListener(null)

            transactionRecyclerView.scrollToPosition(section.sectionPosition() * 10)

            lineCharView.setOnChartValueSelectedListener(this)
        }

    }

    private  val tabSelectedListener = object:TabLayout.OnTabSelectedListener{
        override fun onTabReselected(tab: TabLayout.Tab?) {

        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
        }

        override fun onTabSelected(tab: TabLayout.Tab?) {

            tabLayout.removeOnTabSelectedListener(this)

            val position = ((tab?.position ?: 0) + 1) * 10
            Log.d(TAG,"Position: $position")

            transactionRecyclerView.scrollToPosition(position)

            tabLayout.addOnTabSelectedListener(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        items = object : ArrayList<Section>() {
            init {
                for (i in 0..120) {

                    if (arrayOf(0,10,20,30,40,50,60,70,80,90,100,110).contains(i)){
                        add(SectionHeader(i/10))
                        continue
                    }
                    val value = (Math.random() * i/10).toFloat()
                    add(SectionItem(i/10,value))

                }
            }
        }

        //remove extra padding from chart
        lineCharView.setViewPortOffsets(0f,0f,0f,0f)

        //set value selected listener
        lineCharView.setOnChartValueSelectedListener(chartValueListener)

        // create marker to display box when values are selected
        val mv = MyMarkerView(this, R.layout.custom_marker_view)

        // Set the marker to the chart
        mv.chartView = lineCharView
        lineCharView.marker = mv

        lineCharView.axisLeft.setDrawTopYLabelEntry(false)
        lineCharView.axisRight.setDrawTopYLabelEntry(false)


        lineCharView.legend.setDrawInside(false)
        lineCharView.description.isEnabled  = false
        lineCharView.legend.isEnabled = false

        lineCharView.xAxis.setDrawAxisLine(false)
        lineCharView.xAxis.setDrawGridLines(false)
        lineCharView.xAxis.setDrawGridLinesBehindData(false)
        lineCharView.xAxis.setDrawLabels(false)

        lineCharView.axisRight.setDrawGridLines(false)
        lineCharView.axisRight.setDrawAxisLine(false)
        lineCharView.axisRight.setDrawTopYLabelEntry(false)
        lineCharView.axisRight.setDrawTopYLabelEntry(false)
        lineCharView.axisRight.setDrawLabels(false)


        lineCharView.axisLeft.setDrawGridLines(false)
        lineCharView.axisLeft.setDrawAxisLine(false)
        lineCharView.axisLeft.setDrawTopYLabelEntry(false)
        lineCharView.axisLeft.setDrawLabels(false)

        val dividerDecorator = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        transactionRecyclerView.addItemDecoration(dividerDecorator)

        transactionRecyclerView.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        val adapter = TransactionAdapter()
        transactionRecyclerView.adapter = adapter

        val decorator = StickyHeaderItemDecorator(adapter,object  : StickyHeaderUpdateCallback{
            override fun onUpdateStickyHeader(position: Int) {
                tabLayout.removeOnTabSelectedListener(tabSelectedListener)

                tabLayout.getTabAt(position)?.select()

//                val index = position + 1
//                val item = (items.filter { it.type()  == Section.ITEM }[index] as SectionItem)
//                val h = Highlight(index.toFloat(),item.value,index)
//                lineCharView.highlightValue(h)

                tabLayout.addOnTabSelectedListener(tabSelectedListener)

            }
        })
        decorator.attachToRecyclerView(transactionRecyclerView)

        adapter.items = items
        adapter.notifyDataSetChanged()

        //filter off all the section items in adapter items and use it and populate the tab

        val sectionItems = adapter.items.filter { it.type() == Section.HEADER }

        tabLayout.removeAllTabs()
        sectionItems.forEachIndexed { _, section ->
            val tab = tabLayout.newTab()
            tab.text = Util.getHeaderText(section.sectionPosition())

            tabLayout.addTab(tab)
        }

        tabLayout.addOnTabSelectedListener(tabSelectedListener)

        //set chart data
        setData()
    }


    private fun setData() {

        val sectionItems = items.filter { it.type() == Section.ITEM }

        val values =   sectionItems.mapIndexed { index, section ->

            KudaEntry(index.toFloat(), (section as SectionItem).value, resources
                .getDrawable(R.drawable.star),section) }

        val transactionDataSet: LineDataSet

        if (lineCharView.data != null && lineCharView.data.dataSetCount > 0) {
            transactionDataSet = lineCharView.data.getDataSetByIndex(0) as LineDataSet
            transactionDataSet.values = values
            transactionDataSet.notifyDataSetChanged()
            lineCharView.data.notifyDataChanged()
            lineCharView.notifyDataSetChanged()
        } else {
            // create a dataset and give it a type
            transactionDataSet = LineDataSet(values, "DataSet 1")

            //do not draw icons
            transactionDataSet.setDrawIcons(false)

            //no circles
            transactionDataSet.setDrawCircles(true)

            //disable drawing of values.
            transactionDataSet.setDrawValues(false)

            // draw dashed line
            transactionDataSet.enableDashedLine(10f, 5f, 0f)

            // line thickness and point size
            transactionDataSet.lineWidth = 2.5f
            transactionDataSet.circleRadius = 3f

            // draw points as solid circles
            transactionDataSet.setDrawCircleHole(false)

            // customize legend entry
            transactionDataSet.formLineWidth = 1f
            transactionDataSet.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
            transactionDataSet.formSize = 15f

            // text size of values
            transactionDataSet.valueTextSize = 9f

            // draw selection line as dashed
            transactionDataSet.enableDashedHighlightLine(10f, 5f, 0f)

            // set the filled area
            transactionDataSet.setDrawFilled(true)
            transactionDataSet.fillFormatter = IFillFormatter { _, _ -> lineCharView.axisLeft.axisMinimum }

            // set color of filled area
            val drawable = ContextCompat.getDrawable(this, R.drawable.line_chart_gradient)
            transactionDataSet.fillDrawable = drawable

            val dataSets = ArrayList<ILineDataSet>().apply { add(transactionDataSet) }

            // create a data object with the data sets
            val data = LineData(dataSets)

            // set data
            lineCharView.data = data
        }
    }

    companion object{
        const val TAG = "MainActivity"
    }
}
