package com.aminfallahi.eventsu.general.search

import android.app.DatePickerDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_search_time.*
import com.aminfallahi.eventsu.general.MainActivity
import com.aminfallahi.eventsu.general.R
import org.koin.android.architecture.ext.viewModel
import java.text.SimpleDateFormat
import java.util.*

class SearchTimeActivity : AppCompatActivity() {
    private val searchTimeViewModel by viewModel<SearchTimeViewModel>()
    private val TO_SEARCH: String = "ToSearchFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_time)
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        this.supportActionBar?.title = ""

        val calendar = Calendar.getInstance()

        val date = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val dateFormat = "yyyy-MM-dd"
            val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.US)

            searchTimeViewModel.saveDate(simpleDateFormat.format(calendar.time))
            calendar.add(Calendar.DATE, 1)
            searchTimeViewModel.saveNextDate(simpleDateFormat.format(calendar.time))

            val intent = Intent(this, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            val bundle = Bundle()
            bundle.putBoolean(TO_SEARCH, true)
            intent.putExtras(bundle)
            startActivity(intent)
        }

        timeTextView.setOnClickListener {
            DatePickerDialog(this, date, calendar
                    .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
