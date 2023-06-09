package com.example.a160420124_marcelladivaviorent_healthcareumc.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.a160420124_marcelladivaviorent_healthcareumc.R
import com.example.a160420124_marcelladivaviorent_healthcareumc.viewmodel.DoctorListViewModel
import com.example.a160420124_marcelladivaviorent_healthcareumc.viewmodel.DrugListViewModel

class DrugListFragment : Fragment() {

    private lateinit var viewModel: DrugListViewModel
    private val drugListAdapter = DrugListAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_drug_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(DrugListViewModel::class.java)
        viewModel.refresh()

        val recViewDrug = view?.findViewById<RecyclerView>(R.id.recViewDrug)
        recViewDrug?.layoutManager = LinearLayoutManager(context)
        recViewDrug?.adapter = drugListAdapter


        observeViewModel()

        val refreshLayoutDrug = view.findViewById<SwipeRefreshLayout>(R.id.refreshLayoutDrug)
        val txtErrorDrug = view.findViewById<TextView>(R.id.txtErrorDrug)
        val progressLoadDrug = view.findViewById<ProgressBar>(R.id.progressLoadDrug)

        refreshLayoutDrug?.setOnRefreshListener {
            recViewDrug?.visibility = View.GONE
            txtErrorDrug?.visibility = View.GONE
            progressLoadDrug?.visibility = View.VISIBLE
            viewModel.refresh()
            refreshLayoutDrug?.isRefreshing = false
        }
    }

    fun observeViewModel(){

        viewModel.drugLD.observe(viewLifecycleOwner, Observer {
            drugListAdapter.updateDrugList(it)
        })

        viewModel.drugLoadErrorLD.observe(viewLifecycleOwner, Observer {
            val txtErrorDrug = view?.findViewById<TextView>(R.id.txtErrorDrug)
            if(it == true) {
                txtErrorDrug?.visibility = View.VISIBLE
            } else {
                txtErrorDrug?.visibility = View.GONE
            }
        })

        viewModel.loadingLD.observe(viewLifecycleOwner, Observer {
            val recViewDrug = view?.findViewById<RecyclerView>(R.id.recViewDrug)
            val progressLoadDrug = view?.findViewById<ProgressBar>(R.id.progressLoadDrug)
            if(it == true) {
                recViewDrug?.visibility = View.GONE
                progressLoadDrug?.visibility = View.VISIBLE
            } else {
                recViewDrug?.visibility = View.VISIBLE
                progressLoadDrug?.visibility = View.GONE
            }
        })
    }
}