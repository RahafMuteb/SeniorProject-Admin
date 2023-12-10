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
import androidx.fragment.app.FragmentTransaction;

import com.example.googlemap.Models.LockersLocation;
import com.example.googlemap.R;
import com.example.googlemap.databinding.FragmentEditBinding;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.firebase.database.FirebaseDatabase;

import java.util.zip.Inflater;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class EditLocationScreen extends Fragment {

    FragmentEditBinding binding;
    private LockersLocation lockersLocation; // The Model object to edit
    private Inflater FragmentEditMarkerBinding;
    FirebaseDatabase firebaseDatabase;
    ProgressDialog progressDialog;
    private MaterialAutoCompleteTextView numberOfLockersDropdown;
    private int selectedNumberOfLockers = 0; // Initialize it to 0 or any default value
    private Spinner numberOfLockersSpinner;
    private ArrayAdapter<String> numberOfLockersAdapter;

    String[] lockerNumbers = new String[]{"10", "15", "20"};
    //private Model Model; // The Model object to edit
    public EditLocationScreen(LockersLocation lockersLocation) {
        // Required empty public constructor
        this.lockersLocation = lockersLocation;
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
        binding = FragmentEditBinding.inflate(inflater, container, false);

        // Initialize the Spinner
        numberOfLockersSpinner = binding.UpdatenumberOfLockersSpinner;
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Uploading..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Pleasw Wait..");
        progressDialog.setCancelable(false);

        // Initialize UI elements with the current Model data
        binding.UpdateAddress.setText(lockersLocation.getAddress());
        binding.UpdateName.setText(lockersLocation.getName());

        // Create an ArrayAdapter for the dropdown
        // Create an ArrayAdapter for the Spinner
        numberOfLockersAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, lockerNumbers
                ); // Add the desired values here



        // Specify the layout to use for the dropdown items
        numberOfLockersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        numberOfLockersSpinner.setAdapter(numberOfLockersAdapter);

        numberOfLockersSpinner.setSelection(getIndexOf(String.valueOf(lockersLocation.getSize())));

        numberOfLockersSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedNumberOfLockers = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.show();
                try {

                    lockersLocation.updatelocationInfo(binding.UpdateAddress.getText().toString().trim(),
                            binding.UpdateName.getText().toString().trim(),
                            selectedNumberOfLockers,
                            firebaseDatabase.getReference(),
                            new LockersLocation.OnUpdateCompleteListener() {
                                @Override
                                public void onUpdateComplete() {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                                    MarkersScreen fragment = new MarkersScreen();
                                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    transaction.replace(R.id.container, fragment).commit();
                                }

                                @Override
                                public void onUpdateFailure(String errorMessage) {
                                    Toast.makeText(getContext(), "ERROR" + errorMessage, Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                     });
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "An error occurred.", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });

        return binding.getRoot();

    }

    private int getIndexOf(String value)
    {
        for(int i  = 0; i < lockerNumbers.length; i++)
        {
            if(lockerNumbers[i].equals(value))
            {
                return i;
            }
        }

        return 0;
    }
}