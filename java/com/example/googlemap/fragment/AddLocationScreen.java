package com.example.googlemap.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.googlemap.Models.LockersLocation;
import com.example.googlemap.R;
import com.example.googlemap.databinding.FragmentAddLatlngBinding;
import com.google.firebase.database.FirebaseDatabase;


public class AddLocationScreen extends Fragment {

    FragmentAddLatlngBinding binding;
    FirebaseDatabase firebaseDatabase;
    ProgressDialog progressDialog;
    private int selectedNumberOfLockers = 0; // Initialize it to 0 or any default value
    private Spinner numberOfLockersSpinner;
    private ArrayAdapter<String> numberOfLockersAdapter;

    public AddLocationScreen() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseDatabase = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddLatlngBinding.inflate(inflater, container, false);

        // Initialize the Spinner
        numberOfLockersSpinner = binding.getRoot().findViewById(R.id.numberOfLockersSpinner);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Uploading..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);

        Bundle bundle = this.getArguments();
        String latValue = bundle.getString("latitude");
        String longValue = bundle.getString("longitude");
        binding.Latitude2.setText(latValue);
        binding.Longitude2.setText(longValue);
        // Create an ArrayAdapter for the Spinner
        numberOfLockersAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item,
                new String[]{"10", "15", "20"}); // Add the desired values here

        // Specify the layout to use for the dropdown items
        numberOfLockersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Set the adapter for the Spinner
        numberOfLockersSpinner.setAdapter(numberOfLockersAdapter);
        // Set a selection listener for the Spinner
        numberOfLockersSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle the selected item
                String selectedValue = numberOfLockersAdapter.getItem(position);
                selectedNumberOfLockers = Integer.parseInt(selectedValue);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle case where nothing is selected
                Toast.makeText(getContext(), "Please select a number of lockers", Toast.LENGTH_SHORT).show();

            }
        });

        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LockersLocation post = new LockersLocation();
                post.setLatitude(Double.parseDouble(binding.Latitude2.getText().toString()));
                post.setLongitude(Double.parseDouble(binding.Longitude2.getText().toString().trim()));
                post.setAddress(binding.Address.getText().toString().trim());
                post.setName(binding.Name.getText().toString().trim());
                post.setSize(String.valueOf(selectedNumberOfLockers));
                post.addLocation(getContext(), progressDialog, selectedNumberOfLockers);

            }
        });

       return binding.getRoot();
    }
}