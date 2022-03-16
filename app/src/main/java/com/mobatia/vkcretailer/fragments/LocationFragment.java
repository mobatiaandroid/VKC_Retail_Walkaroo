package com.mobatia.vkcretailer.fragments;

import android.app.ActionBar;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mobatia.vkcretailer.R;
import com.mobatia.vkcretailer.constants.VKCUrlConstants;
import com.mobatia.vkcretailer.controller.AppController;

public class LocationFragment extends Fragment implements VKCUrlConstants,OnMapReadyCallback {
	// this Fragment will be called from MainActivity
	private View mRootView;
	//MapView mapView;
	GoogleMap googleMap;
	private FragmentActivity myContext;
	SupportMapFragment mapFragment;
	public LocationFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		mRootView = inflater.inflate(R.layout.location_fragment, container,
				false);
		AppController.isCart=false;
		final androidx.appcompat.app.ActionBar abar = ((AppCompatActivity)getActivity()).getSupportActionBar();

		View viewActionBar = getActivity().getLayoutInflater().inflate(
				R.layout.actionbar_imageview, null);
		androidx.appcompat.app.ActionBar.LayoutParams params = new androidx.appcompat.app.ActionBar.LayoutParams(
				// Center the textview in the ActionBar !
				androidx.appcompat.app.ActionBar.LayoutParams.WRAP_CONTENT,
				androidx.appcompat.app.ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER);

		abar.setCustomView(viewActionBar, params);
		abar.setDisplayShowCustomEnabled(true);

		mapFragment = SupportMapFragment.newInstance();
		mapFragment.getMapAsync(new OnMapReadyCallback() {
			@Override
			public void onMapReady(GoogleMap googleMap) {
				LatLng vkc = new LatLng(11.20377, 75.82870);
				googleMap.addMarker(new MarkerOptions().position(vkc)
						.title("VKC"));
				googleMap.moveCamera(CameraUpdateFactory.newLatLng(vkc));
				googleMap.animateCamera( CameraUpdateFactory.zoomTo( 15.0f ) );
			}
		});

		///getChildFragmentManager().beginTransaction().replace(R.id.mapview, mapFragment).commit();
		//init(mRootView, savedInstanceState);
		return mRootView;
	}




	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);


	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getChildFragmentManager().beginTransaction().replace(R.id.mapview, mapFragment).commit();
	}

	@Override
	public void onResume() {
		//	mapView.onResume();
		//mapFragment.getMapAsync(this);
		super.onResume();


	}

	@Override
	public void onStop() {
		super.onStop();
		getChildFragmentManager().beginTransaction().replace(R.id.llMap, new LocationFragment()).commit();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		//mapView.onDestroy();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		//mapView.onLowMemory();
	}

	@Override
	public void onMapReady(GoogleMap map) {
		MapsInitializer.initialize(getActivity());
		this.googleMap=map;

		LatLng vkc = new LatLng(11.20377, 75.82870);
		googleMap.addMarker(new MarkerOptions().position(vkc)
				.title("VKC"));
		googleMap.moveCamera(CameraUpdateFactory.newLatLng(vkc));

	}
}
