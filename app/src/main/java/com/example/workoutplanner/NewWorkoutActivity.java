package com.example.workoutplanner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.example.workoutplanner.database.ViewModels.SetViewModel;
import com.example.workoutplanner.database.ViewModels.WorkoutSetViewModel;
import com.example.workoutplanner.database.ViewModels.WorkoutViewModel;
import com.example.workoutplanner.database.models.Exercise;
import com.example.workoutplanner.database.models.Set;
import com.example.workoutplanner.database.models.Workout;
import com.example.workoutplanner.database.models.WorkoutSet;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class NewWorkoutActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String EXTRA_SET_LIST = "EXTRA_SET_LIST";
    private static final int NEW_EXERCISE_SET_ACTIVITY_REQUEST_CODE = 21;
    private static final int EDIT_EXERCISE_SET_ACTIVITY_REQUEST_CODE = 22;
    private Spinner daySpinner;
    private EditText workout_name;
    private int weekDay;

    private HashMap<Exercise, ArrayList<Set>> exerciseSetMap;
    private ExerciseAdapter adapter;
    private Button saveButton;

    public static long workoutID = -1;
    public static long[] setsId = null;

    private SetViewModel svm;
    private WorkoutSetViewModel wsvm;

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
        saveButton = findViewById(R.id.save_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!exerciseSetMap.isEmpty()) {
                    saveWorkout();
                } else {
                    Snackbar.make(findViewById(R.id.coordinator_layout), getResources().getString(R.string.add_set_snack_info), Snackbar.LENGTH_LONG).show();
                }
            }
        });

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.days_of_week, android.R.layout.simple_spinner_dropdown_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        daySpinner.setAdapter(spinnerAdapter);
        daySpinner.setOnItemSelectedListener(this);

//        FloatingActionButton addWorkoutFab = findViewById(R.id.add_button);
        Button addWorkoutFab = findViewById(R.id.add_exercise_button);
        addWorkoutFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewWorkoutActivity.this, NewExerciseSetActivity.class);
                startActivityForResult(intent, NEW_EXERCISE_SET_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    private void deleteExercise(Exercise e) {
        exerciseSetMap.remove(e);
        adapter.setExercises(new ArrayList<>(exerciseSetMap.keySet()));

    }

    private void saveWorkout() {
        if (!workout_name.getText().toString().isEmpty()) {
            workoutID = -1;
            Workout w = new Workout(weekDay, workout_name.getText().toString());
            WorkoutViewModel wvm = ViewModelProviders.of(this).get(WorkoutViewModel.class);
            wvm.addWorkout(w);

            // TODO Synchronizacja wontków - do poprawy jak bedzie czas
            while (workoutID == -1) {
            }
            saveSetsInWorkout();
        } else {
            Snackbar.make(findViewById(R.id.coordinator_layout), getResources().getString(R.string.enter_workout_name), Snackbar.LENGTH_LONG).show();
        }
    }

    private void saveSetsInWorkout() {
        Log.d("MainActivity", "Saving all sets in workout");
        Log.d("MainActivity", exerciseSetMap + "");
        setsId = null;
        List<Set> setList = new ArrayList<>();
        for (List<Set> list : exerciseSetMap.values()) {
            setList.addAll(list);
        }


        Log.d("MainActivity", setList + "");

        svm = ViewModelProviders.of(this).get(SetViewModel.class);
        svm.addSets(setList);

        while (setsId == null) {
        }

        Log.d("MainActivity", Arrays.toString(setsId));


        wsvm = ViewModelProviders.of(this).get(WorkoutSetViewModel.class);

        for (long setId : setsId) {
            wsvm.connectSetToWorkout(new WorkoutSet(workoutID, setId));
        }

        finish();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == NEW_EXERCISE_SET_ACTIVITY_REQUEST_CODE
                || requestCode == EDIT_EXERCISE_SET_ACTIVITY_REQUEST_CODE)
                && resultCode == RESULT_OK) {
            ArrayList<Set> setlist = data.getParcelableArrayListExtra(NewExerciseSetActivity.EXTRA_SET_LIST_TO_SAVE);
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

    private void addExerciseToList(Exercise e, ArrayList<Set> setList) {
        Log.d("MainActivity", e + "");
        Log.d("MainActivity", setList + "");
        exerciseSetMap.put(e, setList);
        adapter.setExercises((new ArrayList<>(exerciseSetMap.keySet())));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        weekDay = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //TODO block adding new workout
    }

    private class ExerciseHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView exerciseName;
        private Exercise exercise;

        public ExerciseHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.exercise_list_item, parent, false));
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            exerciseName = itemView.findViewById(R.id.exercise_name);
        }

        public void bind(Exercise e) {
            this.exercise = e;
            this.exerciseName.setText(e.getName());
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(NewWorkoutActivity.this, NewExerciseSetActivity.class);
            intent.putParcelableArrayListExtra(EXTRA_SET_LIST, exerciseSetMap.get(exercise));
            startActivityForResult(intent, EDIT_EXERCISE_SET_ACTIVITY_REQUEST_CODE);
            deleteExercise(exercise);
        }

        @Override
        public boolean onLongClick(View v) {
            deleteExercise(exercise);
            Snackbar.make(findViewById(R.id.coordinator_layout), getResources().getString(R.string.exercise_set_deleted, exercise.getName()), Snackbar.LENGTH_LONG).show();
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