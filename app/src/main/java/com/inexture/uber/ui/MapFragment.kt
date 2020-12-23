package com.inexture.uber.ui


import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.inexture.uber.R
import com.inexture.uber.databinding.FragmentMapBinding
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class MapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var mBinding: FragmentMapBinding
    private var placesClient: PlacesClient? = null
    private var AUTOCOMPLETE_REQUEST_CODE = 1234
    private lateinit var mMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // return after the user has made a selection.
        val fields = Arrays.asList(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.LAT_LNG,
            Place.Field.ADDRESS
        )

        mBinding.etSearchStartAddress.setOnClickListener {
            placePickerIntent(fields)
        }
        mBinding.etSearchEndAddress.setOnClickListener {
            placePickerIntent(fields)
        }

    }

    private fun placePickerIntent(fields: MutableList<Place.Field>) {
        // Start the autocomplete intent.
        val intent = context?.let {
            Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields
            )
                .build(it)
        }
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode === AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode === RESULT_OK) {
                val place = data?.let { Autocomplete.getPlaceFromIntent(it) }
                Log.d("===", place.toString())
                mMap.addMarker(place?.latLng?.let { MarkerOptions().position(it).title(place.name) })
                mMap.moveCamera(CameraUpdateFactory.newLatLng(place?.latLng))
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place?.latLng,6f))

            } else if (resultCode === AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                val status = data?.let { Autocomplete.getStatusFromIntent(it) }

            }
        }
    }

    override fun onMapReady(map: GoogleMap) {
        mMap = map
    }
}
