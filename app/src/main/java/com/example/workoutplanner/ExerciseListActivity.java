package com.example.workoutplanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutplanner.database.ViewModels.ExerciseViewModel;
import com.example.workoutplanner.database.models.Exercise;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class ExerciseListActivity extends AppCompatActivity {

    public static String KEY_EXTRA_EEXERCISE_NAME = "exercise_name";
    public static String KEY_EXTRA_EEXERCISE_TYPE = "exercise_type";
    public static String KEY_EXTRA_EEXERCISE_WEIGHT = "exercise_weight";
    public static String KEY_EXTRA_EEXERCISE_ID = "exercise_id";

    private ExerciseViewModel evm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_list);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final ExerciseAdapter adapter = new ExerciseAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        evm = ViewModelProviders.of(this).get(ExerciseViewModel.class);
        evm.getExercises().observe(this, new Observer<List<Exercise>>() {
            @Override
            public void onChanged(List<Exercise> exercises) {
                adapter.setExercises(exercises);
            }
        });

        FloatingActionButton addWorkoutFab = findViewById(R.id.add_button);
        addWorkoutFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExerciseListActivity.this, NewExerciseActivity.class);
                startActivity(intent);
            }
        });
    }


    private class ExerciseHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private final TextView exerciseName;
        private final TextView exerciseType;
        private Exercise e;

        public ExerciseHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.exercise_list_item, parent, false));
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            exerciseName = itemView.findViewById(R.id.exercise_name);
            exerciseType = itemView.findViewById(R.id.secondary_info);
        }

        public void bind(Exercise e) {
            this.e = e;
            this.exerciseName.setText(e.getName());
            this.exerciseType.setText(e.getType());
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ExerciseListActivity.this, NewExerciseActivity.class);
            intent.putExtra(KEY_EXTRA_EEXERCISE_NAME, e.getName());
            intent.putExtra(KEY_EXTRA_EEXERCISE_TYPE, e.getType());
            intent.putExtra(KEY_EXTRA_EEXERCISE_WEIGHT, e.isAditiona_weight());
            intent.putExtra(KEY_EXTRA_EEXERCISE_ID, e.getExerciseId());
            startActivity(intent);
        }

        @Override
        public boolean onLongClick(View v) {
            evm.delete(e);
            Snackbar.make(findViewById(R.id.coordinator_layout), getResources().getString(R.string.exercise_deleted, e.getName()), Snackbar.LENGTH_LONG).show();
            return true;
        }
    }

    private class ExerciseAdapter extends RecyclerView.Adapter<ExerciseHolder> {
        private List<Exercise> exercises;

        @NonNull
        @Override
        public ExerciseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ExerciseHolder(getLayoutInflater(), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ExerciseHolder holder, int position) {
            if (exercises != null) {
                Exercise e = exercises.get(position);
                holder.bind(e);
            }
        }

        @Override
        public int getItemCount() {
            if (exercises != null)
                return exercises.size();
            return 0;
        }

        public void setExercises(List<Exercise> exercises) {
            this.exercises = exercises;
            notifyDataSetChanged();
        }
    }

}