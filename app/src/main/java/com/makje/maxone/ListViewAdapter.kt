package com.makje.maxone

import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.ColorUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import java.io.Serializable

class ListViewAdapter: BaseAdapter {

    val context: Context

    var items: List<RowData> = emptyList<RowData>()

    constructor(context: Context) {
        this.context = context
    }

    override fun getCount(): Int {
        return items.size
    }

    override fun getItemId(position: Int): Long {
        return items[position].percent.toLong()
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
                .inflate(R.layout.row_item, parent, false)

        val item = getItem(position) as RowData
        val percentText = rowView.findViewById(R.id.percentText) as TextView
        percentText.text = "${item.percent} %"
        val weightText = rowView.findViewById(R.id.weightText) as TextView
        weightText.text = "%.1f kg".format(item.weight)

        if (100==item.percent) {
            rowView.setBackgroundColor(ContextCompat.getColor(context, R.color.Gold))
        }
        else {
            val color = ContextCompat.getColor(context, R.color.LemonChiffon)
            val factor = (100 - ((if (item.percent <= 100) 100 - item.percent else item.percent - 100) * 2)) / 100.0
            rowView.setBackgroundColor(ColorUtils.setAlphaComponent(color,
                    (Color.alpha(color) * (if (factor < 0) 0.0 else factor)).toInt()))
        }

        return rowView
    }

}
