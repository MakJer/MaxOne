package com.makje.maxone

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import java.io.Serializable
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: ListViewAdapter

    private lateinit var weightComponent: EditText
    private lateinit var repsComponent: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val resultList = findViewById(R.id.results) as ListView
        adapter = ListViewAdapter(this)
        resultList.adapter = adapter

        val onDoneAction = TextView.OnEditorActionListener { textView, actionId, _ -> (
            if (EditorInfo.IME_ACTION_DONE == actionId) {
                onClick(textView)
                (findViewById(R.id.results) as View).requestFocus()
                true
            } else false
        )}

        weightComponent = findViewById(R.id.weight) as EditText
        repsComponent = findViewById(R.id.reps) as EditText

        repsComponent.setOnEditorActionListener(onDoneAction)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putSerializable("values", this.adapter.items as Serializable)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)

        @Suppress("UNCHECKED_CAST")
        this.adapter.items = savedInstanceState?.getSerializable("values") as List<RowData>
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.about -> {
                val intent = Intent(applicationContext, AboutScreenActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun onClick(view: View) {

        this.adapter.items = LinkedList()

        fun <T> valueOrToast(f: () -> T, toastText: String): T? {
            return try { f() } catch (e: Exception) {
                val toast = Toast.makeText(applicationContext, toastText, Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL, 0, 0)
                toast.show()
                null
            }
        }

        val weight = valueOrToast(
                { weightComponent.text.toString().toDouble() },
                getString(R.string.valid_weight) ) ?: return@onClick

        val reps = valueOrToast(
                { repsComponent.text.toString().toInt() },
                getString(R.string.valid_reps) ) ?: return@onClick

        val oneRepMax = valueOrToast(
                { (weight * FACTORS[reps - 1] / 100 ) },
                getString(R.string.valid_reps) ) ?: return@onClick

        // Hide input
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)

        val rows = (5..120 step 5).map { percent -> RowData(percent, oneRepMax*percent/100) }

        this.adapter.items = LinkedList(rows.asReversed())
    }

    companion object {
        private val FACTORS = doubleArrayOf(100.0, 105.2, 110.4, 113.7, 117.1, 120.4, 123.8, 127.1, 130.0, 133.0, 136.3, 139.5, 142.4, 145.1, 148.0)
    }
}
