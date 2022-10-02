package com.hirno.assignment.ui.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.hirno.assignment.MainApplication
import com.hirno.assignment.R
import com.hirno.assignment.databinding.FragmentMainBinding
import com.hirno.assignment.ui.view.RectanglesView
import com.hirno.assignment.ui.view.RectanglesView.UpdateListener
import java.util.logging.Logger

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    private lateinit var viewDataBinding: FragmentMainBinding

    /**
     * A dynamic BroadcastReceiver to listen for network connectivity changes
     */
    private val networkConnectivityChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            viewModel.onNetworkConnectivityChanged()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModelFactory = MainViewModelFactory((requireContext().applicationContext as MainApplication).rectanglesRepository)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        viewModel.loadRectangles()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = FragmentMainBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }.also { binding ->
            // Listen for any updates made to the rectangles to reflect on cache
            binding.rectangles.updateListener = UpdateListener { rectangle ->
                viewModel.updateRectangle(rectangle)
            }
        }
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Set the lifecycle owner to the lifecycle of the view
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
    }

    override fun onResume() {
        super.onResume()
        requireContext().registerReceiver(
            networkConnectivityChangeReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION) // Only deprecated for manifest-registered broadcasts
        )
    }

    override fun onPause() {
        super.onPause()
        requireContext().unregisterReceiver(networkConnectivityChangeReceiver)
    }
}