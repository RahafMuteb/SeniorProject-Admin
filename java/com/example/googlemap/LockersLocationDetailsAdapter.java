package com.example.googlemap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.googlemap.Models.LockersLocation;
import com.example.googlemap.fragment.EditLocationScreen;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class LockersLocationDetailsAdapter extends RecyclerView.Adapter<LockersLocationDetailsAdapter.ViewHolder> {
    private List<LockersLocation> markerInfoList;
    private Context context; // Add a context field
    private Marker marker;

    public LockersLocationDetailsAdapter(Context context, List<LockersLocation> markerInfoList) {
        this.context = context;
        this.markerInfoList = markerInfoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.marker_info_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LockersLocation lockersLocation = markerInfoList.get(position);

        // Bind the properties from the Model to your TextViews or other views
        holder.textViewTitle.setText(lockersLocation.getName());
        holder.textViewDescription.setText("Location: " + lockersLocation.getAddress());
        holder.textViewLockers.setText("Number of Locker: " + lockersLocation.getSize());
        // Add a click listener to the item view
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle item click here
                // For example, start SeatBookingActivity with the selected Model
                Intent intent = new Intent(context, LockerBookingActivity.class);
                intent.putExtra("model", lockersLocation); // Pass the selected Model to the next activity
                context.startActivity(intent);
            }
        });
        // Handle button clicks
        holder.buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, LockerBookingActivity.class);
                intent.putExtra("model", lockersLocation); // Pass the selected Model to the next activity
                context.startActivity(intent);
            }
        });

        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LockersLocation.deleteLocation(lockersLocation, markerInfoList, context, LockersLocationDetailsAdapter.this);

            }

            private void deleteMarker(LockersLocation lockersLocation) {
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
                                    notifyDataSetChanged();
                                    Toast.makeText(context, "Marker deleted", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(context, "Error occurred while deletion", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });

        holder.buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                EditLocationScreen EditLocationScreen = new EditLocationScreen(lockersLocation);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, EditLocationScreen).addToBackStack(null)
                        .commit();
            }
        });
    }


    @Override
    public int getItemCount() {
        return markerInfoList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewDescription;
        TextView textViewLockers;
        Button buttonNext;
        Button buttonDelete;
        Button buttonEdit;


        ViewHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewLockers = itemView.findViewById(R.id.textViewLockers); // Initialize other views here if needed
            buttonNext = itemView.findViewById(R.id.buttonNext);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
        }
    }
}