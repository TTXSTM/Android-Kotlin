package space.mairi.application_architecture.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import space.mairi.application_architecture.R
import space.mairi.application_architecture.databinding.FragmentMainBinding
import space.mairi.application_architecture.model.Weather
import space.mairi.application_architecture.view.details.DetailsFragment
import space.mairi.application_architecture.app.AppState
import space.mairi.application_architecture.model.City
import space.mairi.application_architecture.viewmodel.MainViewModel
import java.io.IOException

private const val IS_WORLD_KEY = "IS_WORLD_KEY"
private const val REFRESH_PERIOD = 60000L
private const val MINIMAL_DISTANCE = 100f

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding
        get() = _binding!!

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel : MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private var isDataSetRus : Boolean = true

    private val adapter = MainFragmentAdapter(object : MainFragmentAdapter.OnItemClickListener {
        override fun onItemClick(weather: Weather) {
            openDetailsFragment(weather)
        }
    })

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {

        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            mainFragmentRecyclerView.adapter = adapter
            mainFragmentFAB.setOnClickListener{
                changeWeatherDataSet()
                saveListOfTowns()
            }

           mainFragmentFABLocation.setOnClickListener { checkPermission() }
        }

        showListOfTowns()

        val observer = Observer<AppState> { a ->
            renderData(a)
        }

        viewModel.getLiveData().observe(viewLifecycleOwner, observer)
    }

    private fun checkPermission() {
        activity?.let {
            when {
                ContextCompat.checkSelfPermission(it,
                    Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED -> {
                    getLocation()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
                -> {
                    showRationaleDialog()
                }
                else -> {
                    requestPermission()
                }
            }
        }
    }

    private fun showRationaleDialog() {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle(getString(R.string.dialog_rationale_title))
                .setMessage(getString(R.string.dialog_rationale_meaasge))
                .setPositiveButton(getString(R.string.dialog_rationale_give_access))
                { _, _ ->
                    requestPermission()
                }
                .setNegativeButton(getString(R.string.dialog_rationale_decline)) {
                        dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }

    private fun requestPermission() {
        requestPermissionsLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    val requestPermissionsLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGaranted : Boolean ->
            if(isGaranted) {
                getLocation()
            } else {
                showDialog(
                    getString(R.string.dialog_title_no_gps),
                    getString(R.string.dialog_message_no_gps)
                )
            }
        }

    private fun showDialog(title: String, message: String) {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton(getString(R.string.dialog_button_close)) { dialog, _ ->
                    dialog.dismiss()}
                .create()
                .show()
        }
    }

    private fun getLocation() {
        activity?.let { context ->
            if(ContextCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {

                val locationManager =
                    context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    val provider = locationManager.getProvider(LocationManager.GPS_PROVIDER)

                    provider?.let {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        REFRESH_PERIOD,
                        MINIMAL_DISTANCE,
                        onLocationListener)
                    }
                } else {
                    val location =
                        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    if(location == null) {
                        showDialog(
                            getString(R.string.dialog_title_gps_turned_off),
                            getString(R.string.dialog_message_last_location_unknown)
                        )
                    } else {
                        getAddressAsync(context, location)
                        showDialog(
                            getString(R.string.dialog_title_gps_turned_off),
                            getString(R.string.dialog_message_last_known_location)
                        )
                    }
                }
            } else {
                showRationaleDialog()
            }
        }
    }

    private val onLocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            context?.let { context ->
                getAddressAsync(context, location)
            }
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

        override fun onProviderEnabled(provider: String) {}

        override fun onProviderDisabled(provider: String) {}
    }

    private fun getAddressAsync(context: Context, location: Location) {
        val geocoder = Geocoder(context)
        Thread {
            try {
                val addresses = geocoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    1)

                binding.mainFragmentFAB.post {
                    showAdressDialog(addresses!![0].getAddressLine(0), location)
                }
            } catch (e : IOException) {
                e.printStackTrace()
            }
        }.start()
    }

    private fun showAdressDialog(address: String, location: Location) {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle(getString(R.string.dialog_address_title))
                .setMessage(address)
                .setPositiveButton(getString(R.string.dialog_address_get_weather)) { _, _ ->
                    openDetailsFragment(
                        Weather(
                            City(
                                address,
                                location.latitude,
                                location.latitude
                            )
                        )
                    )
                }
                .setNegativeButton(
                    getString(R.string.dialog_button_close)) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }

    private  fun openDetailsFragment(
        weather: Weather
    ) {
        activity?.supportFragmentManager?.apply {
            beginTransaction()
                .add(R.id.container, DetailsFragment.newInstance(Bundle().apply {
                    putParcelable(DetailsFragment.BUNDLE_EXTRA, weather)
                }))
                .addToBackStack("")
                .commitAllowingStateLoss()
        }
    }

    private fun showListOfTowns() {
        activity?.let {
            if(it.getPreferences(Context.MODE_PRIVATE).getBoolean(IS_WORLD_KEY, false)) {
                changeWeatherDataSet()
            } else {
                viewModel.getWeatherFomLocalStorageRus()
            }
        }
    }

    private fun saveListOfTowns() {
        activity?.let {
            with (it.getPreferences(Context.MODE_PRIVATE).edit()) {
                putBoolean(IS_WORLD_KEY, !isDataSetRus)
                apply()
            }
        }
    }

    private fun renderData(appState : AppState){
        when(appState){
            is AppState.Success -> {
                binding.mainFragmentLoadingLayout.visibility = View.GONE
                adapter.setWeather(appState.weatherData)
            }

            is AppState.Loading -> {

                binding.mainFragmentLoadingLayout.visibility = View.VISIBLE
            }

            is AppState.Error -> {
                binding.mainFragmentLoadingLayout.visibility = View.GONE

                binding.mainFragmentFAB.showSnackbar(
                    getString(R.string.error),
                    getString(R.string.reload),
                    { viewModel.getWeatherFomLocalStorageRus() }
                )
            }
        }
    }

    private fun changeWeatherDataSet(){
        if(isDataSetRus){
            viewModel.getWeatherFomLocalStorageWorld()
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_earth)
        }else{
            viewModel.getWeatherFomLocalStorageRus()
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_russia)
        }.also { isDataSetRus = !isDataSetRus }

        saveListOfTowns()
    }

    private fun View.showSnackbar(
        text : String,
        actionText : String,
        action : (View) -> Unit,
        lenght : Int = Snackbar.LENGTH_INDEFINITE
    ) {
        Snackbar.make(this, text, lenght).setAction(actionText, action).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}