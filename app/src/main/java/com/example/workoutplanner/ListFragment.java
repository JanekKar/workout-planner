package com.example.workoutplanner;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutplanner.database.ViewModels.ProgressViewModel;
import com.example.workoutplanner.database.models.Progress;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.List;

public class ListFragment extends Fragment {


    private WorkoutAdapter adapter = null;
    private RecyclerView recyclerView;

    private ProgressViewModel pvm;

    private SimpleDateFormat sdf;

    private List<Progress> progressList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_progress_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerview);


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateView();
        // Inflate the layout for this fragment

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToB();
            }
        });

        Log.d("MainActivity", "onCreateView");

        sdf = new SimpleDateFormat("dd.MM.yyyy");

        return view;
    }

    @Override
    public void onResume() {
        adapter.clear();
        updateView();
        super.onResume();
    }

    private void updateView() {

        Log.d("MainActivity", recyclerView + "");
//        if (adapter == null) {
        adapter = new WorkoutAdapter();
        recyclerView.setAdapter(adapter);
        Log.d("MainActivity", "new Adapter");

//        }

        pvm = ViewModelProviders.of(this).get(ProgressViewModel.class);

        pvm.getAll().observe(getViewLifecycleOwner(), new Observer<List<Progress>>() {
            @Override
            public void onChanged(List<Progress> progresses) {
                progressList = progresses;
                adapter.setProgressList(progresses);
            }
        });

        adapter.setProgressList(progressList);
    }

    private void switchToB() {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, AddProgressFragment.class, null)
                .setReorderingAllowed(true)
                .addToBackStack(null).commit();
        getActivity().getSupportFragmentManager().executePendingTransactions();
    }


    private class WorkoutHolder extends RecyclerView.ViewHolder {

        private final TextView name;
        private final TextView description;
        private final TextView date;
        private final TextView doneSets;
        private final ImageView imageView;

        public WorkoutHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.progress_list_item, parent, false));

            name = itemView.findViewById(R.id.progress_name);
            description = itemView.findViewById(R.id.description_field);
            date = itemView.findViewById(R.id.date_filed);
            doneSets = itemView.findViewById(R.id.done_sets);
            imageView = itemView.findViewById(R.id.imageView);
        }

        public void bind(Progress progress) {
            Log.d("MainActivity", "Bind");
            date.setText(sdf.format(progress.getDate()));
            description.setText(progress.getDescription());
            name.setText(progress.getName());
            doneSets.setText(progress.getDoneSets() + "");

            if (progress.getPhotoUri() != null)
                setPic(progress.getPhotoUri());

        }

        public void setPic(String currentPhotoPath) {
            // Get the dimensions of the View
            int targetW = (int) getResources().getDimension(R.dimen.image_dim);
            int targetH = (int) getResources().getDimension(R.dimen.image_dim);

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;

            BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.max(1, Math.min(photoW / targetW, photoH / targetH));

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
            imageView.setImageBitmap(bitmap);
        }
    }

    private class WorkoutAdapter extends RecyclerView.Adapter<WorkoutHolder> {
        private List<Progress> progressList;

        @NonNull
        @Override
        public WorkoutHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new WorkoutHolder(getLayoutInflater(), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull WorkoutHolder holder, int position) {

            Log.d("MainActivity", "onBindViewHolder");
            if (progressList != null) {
                Progress progress = progressList.get(position);
                holder.bind(progress);
            } else {
                Log.d("MainActivity", "No workouts");
            }
        }

        @Override
        public int getItemCount() {
            if (progressList != null) {
                return progressList.size();
            } else {
                return 0;
            }
        }

        void setProgressList(List<Progress> progress) {
            this.progressList = progress;
            Log.d("MainActivity", "setProgress");
            notifyDataSetChanged();
        }

        void clear() {
            if (this.progressList != null)
                this.progressList.clear();
            notifyDataSetChanged();
        }

    }

}