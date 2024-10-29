package com.app.penyakitdaunjagung.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.penyakitdaunjagung.R
import com.app.penyakitdaunjagung.databinding.FragmentHomeBinding
import com.app.penyakitdaunjagung.ui.adapter.HistoryAdapter
import com.app.penyakitdaunjagung.ui.detail.DetailActivity
import com.app.penyakitdaunjagung.ui.detection.DetectionActivity
import com.app.penyakitdaunjagung.view_model.ViewModelFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import jp.wasabeef.glide.transformations.ColorFilterTransformation
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

class HomeFragment : Fragment() {

    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireActivity().applicationContext)
    }
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding
    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireActivity())
    }
    private lateinit var locationRequest: LocationRequest
    private var location: Location? = null
    private val historyAdapter by lazy {
        HistoryAdapter(
            onItemClick = {
                val intent = Intent(requireActivity(), DetailActivity::class.java).apply {
                    putExtra(DetailActivity.EXTRA_TYPE, DetailActivity.TYPE_HISTORY)
                    putExtra(DetailActivity.EXTRA_HISTORY, it.id)
                }
                startActivity(intent)
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createLocationRequest()

        binding?.apply {
            rvHistory.apply {
                layoutManager = LinearLayoutManager(requireActivity())
                adapter = historyAdapter
            }
            viewModel.getHistories().observe(viewLifecycleOwner) {
                historyAdapter.submitList(it)
                viewEmpty.isVisible = it.isEmpty()
            }
            btnDetect.setOnClickListener {
                startActivity(Intent(requireActivity(), DetectionActivity::class.java))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getMyLastLocation()
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest.Builder(1).apply {
            setIntervalMillis(TimeUnit.SECONDS.toMillis(1))
            setMaxUpdateDelayMillis(TimeUnit.SECONDS.toMillis(1))
            setPriority(Priority.PRIORITY_HIGH_ACCURACY)
        }.build()

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(requireActivity())

        client.checkLocationSettings(builder.build())
            .addOnSuccessListener { getMyLastLocation() }
            .addOnFailureListener { e ->
                if (e is ResolvableApiException) {
                    try {
                        resolutionLauncher.launch(IntentSenderRequest.Builder(e.resolution).build())
                    } catch (e: IntentSender.SendIntentException) {
                        Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    @SuppressLint("SetTextI18n")
    private fun getMyLastLocation() {
        if (checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
            and checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                this.location = location
                if (location == null) {
                    Toast.makeText(requireActivity(), "Lokasi tidak ditemukan", Toast.LENGTH_SHORT)
                        .show()
                }
                binding?.apply {
                    viewModel.getCurrentWeather("${location?.latitude ?: -5.17047176058038}, ${location?.longitude ?: 119.4357983804193}").observe(viewLifecycleOwner) { result ->
                        tvLocation.text = "${result.location.name}, ${result.location.region}"
                        tvWeather.text = "${result.current.condition.text} • ${result.current.tempC?.roundToInt()}\u2103 / ${result.current.tempF?.roundToInt()}\u2109"
                        Glide.with(requireActivity())
                            .load("https:${result.current.condition.icon}")
                            .apply(RequestOptions.bitmapTransform(ColorFilterTransformation(Color.parseColor("#FF353535"))))
                            .into(ivWeather)
                    }
                }
            }
        } else {
            requestPermissionLauncher.launch(REQUIRED_PERMISSIONS)
        }
    }

    private fun showLocationDialog() {
        MaterialAlertDialogBuilder(requireActivity())
            .setTitle("Peringatan")
            .setMessage("Lokasi tidak ditemukan, hidupkan lokasi di pengaturan")
            .setPositiveButton("Buka Pengaturan") { dialog, _ ->
                dialog.dismiss()
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
            .create()
            .show()
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                getMyLastLocation()
            }
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                getMyLastLocation()
            }
        }
    }

    private val resolutionLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        when (result.resultCode) {
            Activity.RESULT_OK -> Toast.makeText(requireActivity(), "Menunggu...", Toast.LENGTH_SHORT).show()
            Activity.RESULT_CANCELED -> {
                showLocationDialog()
                Toast.makeText(requireActivity(), "Gagal mendapatkan lokasi", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
    
    private fun checkPermission(permission: String) =
        ContextCompat.checkSelfPermission(
            requireActivity(),
            permission
        ) == PackageManager.PERMISSION_GRANTED

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }
}