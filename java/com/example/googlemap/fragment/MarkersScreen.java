package com.example.googlemap.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.googlemap.LockersLocationDetailsAdapter;
import com.example.googlemap.Models.LockersLocation;
import com.example.googlemap.R;
import com.example.googlemap.databinding.FragmentManageBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MarkersScreen#//newInstance} factory method to
 * create an instance of this fragment.
 */
public class MarkersScreen extends Fragment implements OnMapReadyCallback{
    SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Marker marker;
    private MarkerOptions markerOptions;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private RecyclerView recyclerViewMarkerInfo;
    private LockersLocationDetailsAdapter lockersLocationDetailsAdapter;
    private final List<LockersLocation> markerInfoList = new ArrayList<>();



    FragmentManageBinding binding;
    public MarkersScreen() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_manage, container, false);

        recyclerViewMarkerInfo = binding.getRoot().findViewById(R.id.recyclerViewMarkerInfo);
        recyclerViewMarkerInfo.setLayoutManager(new LinearLayoutManager(getContext()));
        lockersLocationDetailsAdapter = new LockersLocationDetailsAdapter(getContext(), markerInfoList);
        recyclerViewMarkerInfo.setAdapter(lockersLocationDetailsAdapter);


        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        databaseReference= FirebaseDatabase.getInstance().getReference("post");

        mapInitialize();

        binding.recyclerViewMarkerInfo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });
        return binding.getRoot();


    }

    private void mapInitialize() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

    }

    private void gotoLatLng(double latitude, double longitude, float v) {

        LatLng latLng = new LatLng(latitude, longitude);

        CameraUpdate update= CameraUpdateFactory.newLatLngZoom(latLng,17f);
        mMap.animateCamera(update);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        mMap = googleMap;
        Dexter.withContext(getContext())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                            return;
                        }

                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                    LockersLocation lockersLocation = dataSnapshot.getValue(LockersLocation.class);
                                    lockersLocation.setKey(dataSnapshot.getKey());
                                    if(lockersLocation.getLatitude() != null)
                                    {

                                        LatLng latLng = new LatLng(lockersLocation.getLatitude(), lockersLocation.getLongitude());

                                        markerOptions= new MarkerOptions();
                                        markerOptions.position(latLng)
                                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                                                );

                                        marker = mMap.addMarker(markerOptions);
                                        marker.setTag(lockersLocation);

                                        //where you handle marker click events
                                        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                            @Override
                                            public boolean onMarkerClick(Marker clickedMarker) {
                                                LockersLocation lockersLocation = (LockersLocation) clickedMarker.getTag(); // Retrieve the custom object (Model)

                                                if (lockersLocation != null) {
                                                    // Clear the existing marker info list
                                                    markerInfoList.clear();
                                                    // Add the clicked Model object to the list
                                                    markerInfoList.add(lockersLocation);

                                                    // Notify the adapter that the data has changed
                                                    lockersLocationDetailsAdapter.notifyDataSetChanged();

                                                    // Animate the RecyclerView into view
                                                    recyclerViewMarkerInfo.setVisibility(View.VISIBLE);
                                                    recyclerViewMarkerInfo.animate().translationY(0).setDuration(300);
                                                }
                                                return true; // Consume the click event to prevent the default behavio
                                            }
                                        });
                                    }
                                    else
                                    {
                                        FirebaseDatabase.getInstance().getReference()
                                                .child("post")
                                                .child(lockersLocation.getKey())
                                                .setValue(null);
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                        mMap.setMyLocationEnabled(true);
                        fusedLocationProviderClient.getLastLocation().addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(),"error"+e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }).addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(21.4259317, 39.8271968), 17));

                            }
                        });

                    }
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse){
                        Toast.makeText(getContext(),"Permission"+
                                permissionDeniedResponse.getPermissionName()+""+"was denied!", Toast.LENGTH_SHORT).show();

                    }
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest , PermissionToken permissionToken){
                        permissionToken.continuePermissionRequest();

                    }
                }).check();
        mMap.setContentDescription(String.valueOf(this));
    }

}