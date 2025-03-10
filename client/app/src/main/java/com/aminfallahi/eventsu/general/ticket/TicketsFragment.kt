package com.aminfallahi.eventsu.general.ticket

import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_tickets.view.*
import com.aminfallahi.eventsu.general.MainActivity
import com.aminfallahi.eventsu.general.R
import com.aminfallahi.eventsu.general.attendees.AttendeeFragment
import com.aminfallahi.eventsu.general.event.Event
import com.aminfallahi.eventsu.general.event.EventUtils
import com.aminfallahi.eventsu.general.utils.Utils
import com.aminfallahi.eventsu.general.utils.nullToEmpty
import org.koin.android.architecture.ext.viewModel

const val EVENT_ID: String = "EVENT_ID"
const val CURRENCY: String = "CURRENCY"
const val TICKET_ID_AND_QTY: String = "TICKET_ID_AND_QTY"

class TicketsFragment : Fragment() {
    private val ticketsRecyclerAdapter: TicketsRecyclerAdapter = TicketsRecyclerAdapter()
    private val ticketsViewModel by viewModel<TicketsViewModel>()
    private var id: Long = -1
    private var currency: String? = null
    private lateinit var rootView: View
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var ticketIdAndQty = ArrayList<Pair<Int, Int>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = this.arguments
        if (bundle != null) {
            id = bundle.getLong(EVENT_ID, -1)
            currency = bundle.getString(CURRENCY, null)
        }
        ticketsRecyclerAdapter.setCurrency(currency)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_tickets, container, false)
        val activity = activity as? AppCompatActivity
        activity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity?.supportActionBar?.title = "Ticket Details"
        setHasOptionsMenu(true)

        val ticketSelectedListener = object : TicketSelectedListener {
            override fun onSelected(ticketId: Int, quantity: Int) {
                handleTicketSelect(ticketId, quantity)
            }
        }
        ticketsRecyclerAdapter.setSelectListener(ticketSelectedListener)
        rootView.ticketsRecycler.layoutManager = LinearLayoutManager(activity)

        rootView.ticketsRecycler.adapter = ticketsRecyclerAdapter
        rootView.ticketsRecycler.isNestedScrollingEnabled = false

        linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        rootView.ticketsRecycler.layoutManager = linearLayoutManager

        ticketsViewModel.error.observe(this, Observer {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })

        ticketsViewModel.progressTickets.observe(this, Observer {
            it?.let {
                Utils.showProgressBar(rootView.progressBarTicket, it)
                rootView.ticketTableHeader.visibility = if (it) View.GONE else View.VISIBLE
                rootView.register.visibility = if (it) View.GONE else View.VISIBLE
            }
        })

        ticketsViewModel.event.observe(this, Observer {
            it?.let { loadEventDetails(it) }
        })

        ticketsViewModel.ticketTableVisibility.observe(this, Observer {
            it?.let {
                if (it) {
                    rootView.ticketTableHeader.visibility = View.VISIBLE
                    rootView.ticketsRecycler.visibility = View.VISIBLE
                    rootView.register.visibility = View.VISIBLE
                    rootView.ticketInfoTextView.visibility = View.GONE
                } else {
                    rootView.ticketTableHeader.visibility = View.GONE
                    rootView.register.visibility = View.GONE
                    rootView.ticketsRecycler.visibility = View.GONE
                    rootView.ticketInfoTextView.visibility = View.VISIBLE
                }
            }
        })

        ticketsViewModel.loadEvent(id)
        ticketsViewModel.loadTickets(id)

        ticketsViewModel.tickets.observe(this, Observer {
            it?.let {
                ticketsRecyclerAdapter.addAll(it)
            }
            ticketsRecyclerAdapter.notifyDataSetChanged()
        })

        rootView.register.setOnClickListener {
            if (!ticketsViewModel.totalTicketsEmpty(ticketIdAndQty)) {
                val fragment = AttendeeFragment()
                val bundle = Bundle()
                bundle.putLong(EVENT_ID, id)
                bundle.putSerializable(TICKET_ID_AND_QTY, ticketIdAndQty)
                fragment.arguments = bundle
                activity?.supportFragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.rootLayout, fragment)
                        ?.addToBackStack(null)
                        ?.commit()
            } else {
                handleNoTicketsSelected()
            }
        }

        return rootView
    }

    private fun handleTicketSelect(id: Int, quantity: Int) {
        val pos = ticketIdAndQty.map { it.first }.indexOf(id)
        if (pos == -1) {
            ticketIdAndQty.add(Pair(id, quantity))
        } else {
            ticketIdAndQty[pos] = Pair(id, quantity)
        }
    }

    override fun onDestroyView() {
        val activity = activity as? MainActivity
        activity?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        super.onDestroyView()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                activity?.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun loadEventDetails(event: Event) {
        rootView.eventName.text = event.name
        rootView.organizerName.text = "by ${event.organizerName.nullToEmpty()}"
        val startsAt = EventUtils.getLocalizedDateTime(event.startsAt)
        val endsAt = EventUtils.getLocalizedDateTime(event.endsAt)
        rootView.time.text = EventUtils.getFormattedDateTimeRangeDetailed(startsAt, endsAt)
    }

    private fun handleNoTicketsSelected() {
        val builder = AlertDialog.Builder(activity)
        builder.setMessage(resources.getString(R.string.no_tickets_message))
                .setTitle(resources.getString(R.string.whoops))
                .setPositiveButton(resources.getString(R.string.ok)) { dialog, _ -> dialog.cancel() }
        val alert = builder.create()
        alert.show()
    }
}
