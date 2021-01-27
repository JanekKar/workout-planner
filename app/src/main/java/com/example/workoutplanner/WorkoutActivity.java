package com.example.workoutplanner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.workoutplanner.database.ViewModels.SetViewModel;
import com.example.workoutplanner.database.ViewModels.WorkoutSetViewModel;
import com.example.workoutplanner.database.ViewModels.WorkoutViewModel;
import com.example.workoutplanner.database.models.Exercise;
import com.example.workoutplanner.database.models.Set;
import com.example.workoutplanner.database.models.Workout;
import com.example.workoutplanner.database.models.WorkoutSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WorkoutActivity extends AppCompatActivity {

    public static final String EXERCISE_NAME_EXTRA = "exercise_name_extra";
    public static final String EXERCISE_SET_LIST_EXTRA = "exercise_set_list_extra";
    public List<Set> setList;
    private long workoutId;
    private Workout workout = null;
    private TextView workoutName;
    private TextView totalNumberOfExercises;
    private TextView totalNumberOfSets;
    private ExerciseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        workoutName = findViewById(R.id.workout_name);
        totalNumberOfExercises = findViewById(R.id.exercise_count);
        totalNumberOfSets = findViewById(R.id.total_set_count);

        RecyclerView recyclerView = findViewById(R.id.exercise_recyclerview);
        adapter = new ExerciseAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        workoutId = getIntent().getLongExtra(MainActivity.WORKOUT_ID_EXTRA, -1);


        WorkoutViewModel workoutViewModel = ViewModelProviders.of(this).get(WorkoutViewModel.class);

        workoutViewModel.getWorkouts().observe(this, new Observer<List<Workout>>() {
            @Override
            public void onChanged(List<Workout> workouts) {
                for (Workout w : workouts) {
                    if (w.getId() == workoutId) {
                        workout = w;
                        workoutName.setText(w.getName());
                        getSets();

                    }
                }
            }
        });
    }

    private void getSets() {
        WorkoutSetViewModel workoutSetViewModel = ViewModelProviders.of(this).get(WorkoutSetViewModel.class);
        workoutSetViewModel.getAllSets(workout.getId()).observe(this, new Observer<List<WorkoutSet>>() {
            @Override
            public void onChanged(List<WorkoutSet> workoutSets) {
                long[] setIds = new long[workoutSets.size()];
                int i = 0;
                for (WorkoutSet ws : workoutSets) {
                    setIds[i++] = ws.getSetId();
                }
                getSetsInfo(setIds);

            }
        });

    }

    private void getSetsInfo(long[] setIds) {
        SetViewModel swm = ViewModelProviders.of(this).get(SetViewModel.class);
        swm.getSets(setIds).observe(this, new Observer<List<Set>>() {
            @Override
            public void onChanged(List<Set> sets) {
                setList = sets;
                Log.d("MainActivity", setList + "");
                HashMap<Long, Integer> exerciseNumOfSets = new HashMap<>();
                for (Set set : sets) {
                    if (exerciseNumOfSets.containsKey(set.getExerciseId())) {
                        int temp = exerciseNumOfSets.get(set.getExerciseId());
                        temp++;
                        exerciseNumOfSets.put(set.getExerciseId(), temp);
                    } else {
                        exerciseNumOfSets.put(set.getExerciseId(), 1);
                    }
                }
                totalNumberOfSets.setText(getResources().getString(R.string.total_sets_label, sets.size()));
                Log.d("MainActivity", exerciseNumOfSets + "");

                getAllExercises(new ArrayList<Long>(exerciseNumOfSets.keySet()));
            }
        });
    }

    private void getAllExercises(ArrayList<Long> setList) {
        ExerciseViewModel evm = ViewModelProviders.of(this).get(ExerciseViewModel.class);
        Log.d("MainActivity", setList + "");
        evm.getExercisesById(setList.toArray(new Long[0])).observe(this, new Observer<List<Exercise>>() {
            @Override
            public void onChanged(List<Exercise> exercises) {
                for (Exercise e : exercises) {
                    Log.d("MainActivity", e + "");
                }

                totalNumberOfExercises.setText(getResources().getString(R.string.total_exercise_label, exercises.size()));
                adapter.setExercises(exercises);
            }
        });
    }


    private class ExerciseHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView exerciseName;
        private Exercise exercise;

        public ExerciseHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.exercise_card_list_item, parent, false));
            itemView.setOnClickListener(this);

            exerciseName = itemView.findViewById(R.id.exercise_name);
        }

        public void bind(Exercise e) {
            this.exercise = e;
            this.exerciseName.setText(e.getName());
        }

        @Override
        public void onClick(View v) {
            ArrayList<Set> exerciseSetList = new ArrayList<>();
            for (Set set : setList) {
                if (set.getExerciseId() == exercise.getExerciseId()) ;
                exerciseSetList.add(set);
            }
            Intent intent = new Intent(WorkoutActivity.this, TrainingActivity.class);
            intent.putParcelableArrayListExtra(EXERCISE_SET_LIST_EXTRA, exerciseSetList);
            intent.putExtra(EXERCISE_NAME_EXTRA, exercise.getName());
            startActivity(intent);
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