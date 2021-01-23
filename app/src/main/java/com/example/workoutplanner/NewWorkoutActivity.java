package com.example.workoutplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.workoutplanner.database.models.Exercise;
import com.example.workoutplanner.database.models.Set;
import com.example.workoutplanner.database.models.Workout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewWorkoutActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner daySpinner;
    private EditText workout_name;
    private int dayNum;

    private HashMap<Exercise, List<Set>> exerciseSetMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_workout);

        RecyclerView recyclerView = findViewById(R.id.exercise_recyclerview);
        final ExerciseAdapter adapter = new ExerciseAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        exerciseSetMap = new HashMap<>();


        daySpinner = (Spinner)findViewById(R.id.day_spinner);
        workout_name = findViewById(R.id.workout_name_editText);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.days_of_week, android.R.layout.simple_spinner_dropdown_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        daySpinner.setAdapter(spinnerAdapter);
        daySpinner.setOnItemSelectedListener(this);

        FloatingActionButton addWorkoutFab = findViewById(R.id.add_button);
        addWorkoutFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, NewWorkoutActivity.class);
//                startActivityForResult(intent, NEW_BOOK_ACTIVITY_REQUEST_CODE);
                exerciseSetMap.put(new Exercise("test", "test"), null);
                adapter.setExercises(new ArrayList<>(exerciseSetMap.keySet()));
            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        dayNum = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //TODO block adding new exercise
    }

    private class ExerciseHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView exerciseName;

        public ExerciseHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.exercise_list_item, parent, false));
            itemView.setOnClickListener(this);

            exerciseName = itemView.findViewById(R.id.exercise_name);
        }

        public void bind(Exercise e){
            this.exerciseName.setText(e.getName());
        }

        @Override
        public void onClick(View v) {

        }
    }

    private class ExerciseAdapter extends RecyclerView.Adapter<ExerciseHolder>{
        private List<Exercise> exercises;

        @NonNull
        @Override
        public ExerciseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ExerciseHolder(getLayoutInflater(), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ExerciseHolder holder, int position) {
            if(exercises != null){
                Exercise e = exercises.get(position);
                holder.bind(e);
            }
        }

        @Override
        public int getItemCount() {
            if(exercises != null)
                return exercises.size();
            return 0;
        }

        public void setExercises(List<Exercise> exercises) {
            this.exercises = exercises;
            notifyDataSetChanged();
        }
    }
}