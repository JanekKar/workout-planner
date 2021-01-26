package com.example.workoutplanner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutplanner.database.ViewModels.ExerciseViewModel;
import com.example.workoutplanner.database.models.Exercise;
import com.example.workoutplanner.database.models.Set;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewWorkoutActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner daySpinner;
    private EditText workout_name;
    private int dayNum;

    private HashMap<Exercise, List<Set>> exerciseSetMap;
    private int NEW_EXERCISE_SET_ACTIVITY_REQUEST_CODE = 21;
    private ExerciseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_workout);

        RecyclerView recyclerView = findViewById(R.id.exercise_recyclerview);
        adapter = new ExerciseAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        exerciseSetMap = new HashMap<>();

        daySpinner = (Spinner) findViewById(R.id.day_spinner);
        workout_name = findViewById(R.id.workout_name_editText);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.days_of_week, android.R.layout.simple_spinner_dropdown_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        daySpinner.setAdapter(spinnerAdapter);
        daySpinner.setOnItemSelectedListener(this);

        FloatingActionButton addWorkoutFab = findViewById(R.id.add_button);
        addWorkoutFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewWorkoutActivity.this, NewExerciseSetActivity.class);
                startActivityForResult(intent, NEW_EXERCISE_SET_ACTIVITY_REQUEST_CODE);
                exerciseSetMap.put(new Exercise("", "", false), null);
                adapter.setExercises(new ArrayList<>(exerciseSetMap.keySet()));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_EXERCISE_SET_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList<Set> setlist = data.getParcelableArrayListExtra("list_of_sets");
            if (!setlist.isEmpty()) {
                long exerciseId = setlist.get(0).getExerciseId();
                ExerciseViewModel evm = ViewModelProviders.of(this).get(ExerciseViewModel.class);
                evm.getExercises().observe(this, new Observer<List<Exercise>>() {
                    @Override
                    public void onChanged(List<Exercise> exercises) {
                        for (Exercise e : exercises) {
                            if (e.getExerciseId() == exerciseId) {
                                addExerciseToList(e, setlist);
                            }
                        }
                    }
                });
            }
        }
    }

    private void addExerciseToList(Exercise e, List<Set> setList) {
        Log.d("MainActivity", e+"");
        Log.d("MainActivity", setList+"");
        exerciseSetMap.put(e, setList);
        adapter.setExercises((new ArrayList<>(exerciseSetMap.keySet())));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        dayNum = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //TODO block adding new workout
    }

    private class ExerciseHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView exerciseName;

        public ExerciseHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.exercise_list_item, parent, false));
            itemView.setOnClickListener(this);

            exerciseName = itemView.findViewById(R.id.exercise_name);
        }

        public void bind(Exercise e) {
            this.exerciseName.setText(e.getName());
        }

        @Override
        public void onClick(View v) {
            //TODO edycja ćwiczenia
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