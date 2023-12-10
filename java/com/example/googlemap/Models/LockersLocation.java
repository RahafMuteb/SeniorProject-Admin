package com.example.googlemap.Models;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.googlemap.LockersLocationDetailsAdapter;
import com.example.googlemap.R;
import com.example.googlemap.fragment.MarkersScreen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.List;

public class LockersLocation implements Serializable {

    String Name,Address,Size;
    Double latitude;
    Double longitude;
    private String key;


    private int price;
    // Add fields for locker details
    private int numberOfLockers;
    private String lockerSizes;
    private String availabilityStatus;



    public LockersLocation() {
    }

    public LockersLocation(String address, String Name, String size, Double latitude, Double longitude) {
        this.Name= Name;
        Address = address;
        Size = size;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public int getNumberOfLockers() {
        return numberOfLockers;
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        Size = size;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public int getPrice() {
        return price;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String Key) {
        this.key = Key;
    }


    // Method for creating and uploading a new instance to Firebase
    public void addLocation(final Context context, final ProgressDialog progressDialog, final int selectedNumberOfLockers) {
        progressDialog.show();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference postReference = firebaseDatabase.getReference().child("post").push();
        String postKey = postReference.getKey();
        // Create a new Model instance with the provided data
        LockersLocation post = new LockersLocation();
        post.setLatitude(Double.parseDouble(getLatitude().toString()));
        post.setLongitude(Double.parseDouble(getLongitude().toString()));
        post.setAddress(getAddress());
        post.setName(getName());
        post.setSize(String.valueOf(selectedNumberOfLockers));
        post.setKey(postKey);
        // Set the values to the database
        postReference.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
                Toast.makeText(context, "Upload Successfully", Toast.LENGTH_SHORT).show();
                // Assuming that this method is called from a Fragment, you can replace the fragment here
                MarkersScreen fragment = new MarkersScreen();
                FragmentTransaction transaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, fragment).commit();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "ERROR" + e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
    // Method for deleting a location from the Firebase Realtime Database
    public static void deleteLocation(LockersLocation lockersLocation, List<LockersLocation> markerInfoList, Context context, LockersLocationDetailsAdapter adapter) {
        // Implement marker deletion logic here (e.g., remove from the database)
        FirebaseDatabase.getInstance().getReference()
                .child("post")
                .child(lockersLocation.getKey())
                .setValue(null)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Remove the item from the list
                            markerInfoList.remove(lockersLocation);
                            // Notify the adapter that the data has changed
                            adapter.notifyDataSetChanged();

                            Toast.makeText(context, "Marker deleted", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Error occurred while deletion", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Method for updating Model information
    public void updatelocationInfo(String updatedAddress, String updatedName, int selectedNumberOfLockers, DatabaseReference databaseReference,
                                  final OnUpdateCompleteListener listener) {
        setAddress(updatedAddress);
        setName(updatedName);
        setSize(String.valueOf(selectedNumberOfLockers));

        // Reference to the database path where the model is stored
        DatabaseReference modelRef = databaseReference.child("post").child(getKey());

        // Set the updated values to the database
        modelRef.setValue(this).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                listener.onUpdateComplete();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onUpdateFailure(e.getMessage());
            }
        });
    }
    // Interface to handle update completion
    public interface OnUpdateCompleteListener {
        void onUpdateComplete();
        void onUpdateFailure(String errorMessage);
    }



}