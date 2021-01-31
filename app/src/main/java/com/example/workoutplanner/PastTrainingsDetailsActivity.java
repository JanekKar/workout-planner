package com.example.workoutplanner;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutplanner.database.ViewModels.DoneSetViewModel;
import com.example.workoutplanner.database.ViewModels.ExerciseViewModel;
import com.example.workoutplanner.database.ViewModels.WorkoutViewModel;
import com.example.workoutplanner.database.models.DoneSet;
import com.example.workoutplanner.database.models.Exercise;
import com.example.workoutplanner.database.models.Workout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PastTrainingsDetailsActivity extends AppCompatActivity {


    private WorkoutAdapter adapter;
    private WorkoutViewModel wvm;
    private ExerciseViewModel evm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_trainings_details);


        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        adapter = new WorkoutAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        wvm = ViewModelProviders.of(this).get(WorkoutViewModel.class);
        evm = ViewModelProviders.of(this).get(ExerciseViewModel.class);

        Date date = new Date(getIntent().getLongExtra(PastTrainingsActivity.WORKOUT_IDS_EXTRA, 0));

        this.getSupportActionBar().setSubtitle(getResources().getString(R.string.past_trainings_details, new SimpleDateFormat("dd.MM.yyyy").format(date)));

        DoneSetViewModel dsvm = ViewModelProviders.of(this).get(DoneSetViewModel.class);
        dsvm.get(date, date).observe(this, new Observer<List<DoneSet>>() {
            @Override
            public void onChanged(List<DoneSet> doneSets) {

                ArrayList<Long> workouts = new ArrayList<>();

                for (DoneSet ds : doneSets) {
                    if (!workouts.contains(ds.getWorkout().getWorkoutId()))
                        workouts.add(ds.getWorkout().getWorkoutId());
                }

                adapter.setData(workouts, new ArrayList<>(doneSets));
            }
        });

        evm.getExercises().observe(this, new Observer<List<Exercise>>() {
            @Override
            public void onChanged(List<Exercise> exercises) {
                adapter.setExercises(exercises);
            }
        });

        wvm.getAllWorkouts().observe(this, new Observer<List<Workout>>() {
            @Override
            public void onChanged(List<Workout> workouts) {
                adapter.setWorkouts(workouts);
            }
        });
    }


    private class WorkoutHolder extends RecyclerView.ViewHolder {

        private final TextView workoutName;
        private final TextView secondaryInfo;

        public WorkoutHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.training_detail_list_item, parent, false));

            workoutName = itemView.findViewById(R.id.workout_name);
            secondaryInfo = itemView.findViewById(R.id.secondary_info);


        }

        public void bind(Workout workout, ArrayList<DoneSet> dss, List<Exercise> exerciseListc) {



            workoutName.setText(workout.getName());

            ArrayList<Long> exerciseId = new ArrayList<>();
            ArrayList<Integer> setCount = new ArrayList<>();

            for (DoneSet ds : dss) {
                if (!exerciseId.contains(ds.getSet().getExerciseId())) {
                    setCount.add(1);
                    exerciseId.add(ds.getSet().getExerciseId());
                } else
                    setCount.set(exerciseId.indexOf(ds.getSet().getExerciseId()), setCount.get(exerciseId.indexOf(ds.getSet().getExerciseId())) + 1);
            }

            ArrayList<Exercise> exercises = new ArrayList<>();
            for (long id : exerciseId) {
                for (Exercise exercise : exerciseListc)
                    if (exercise.getExerciseId() == id)
                        exercises.add(exercise);
            }

            StringBuffer sb = new StringBuffer();

            for (int i = 0; i < exercises.size(); i++) {
                Exercise e = exercises.get(i);
                sb.append(e.getName() + "\n");
                sb.append(getResources().getString(R.string.set_count_details, setCount.get(i)) + "\n");
                for (DoneSet ds : dss) {
                    sb.append(getResources().getString(R.string.set_details, ds.getSet().getNumberOfRepsToDO(), ds.getDoneReps(), ds.getSet().getAdditionalWeight()) + "\n");
                }
            }
            Log.d("MainActivity", sb.toString());

            secondaryInfo.setText(sb.toString());


        }
    }

    private class WorkoutAdapter extends RecyclerView.Adapter<WorkoutHolder> {

        private ArrayList<Long> workoutsIds;
        private ArrayList<DoneSet> doneSets;
        private List<Exercise> exercises;
        private List<Workout> workoutsDetailsList;

        @NonNull
        @Override
        public WorkoutHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new WorkoutHolder(getLayoutInflater(), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull WorkoutHolder holder, int position) {
            long id = workoutsIds.get(position);

            Log.d("Main23", workoutsDetailsList+"");

            Workout workout = null;
            for (Workout w : workoutsDetailsList)
                if (w.getWorkoutId() == id)
                    workout = w;

            ArrayList<DoneSet> dss = new ArrayList<>();
            for (DoneSet ds : doneSets) {
                if (ds.getWorkout().getWorkoutId() == id)
                    dss.add(ds);
            }
            holder.bind(workout, dss, exercises);

        }

        @Override
        public int getItemCount() {
            if (workoutsIds != null) {
                return workoutsIds.size();
            } else
                return 0;
        }

        void setData(ArrayList<Long> workouts, ArrayList<DoneSet> doneSets) {
            this.workoutsIds = workouts;
            this.doneSets = doneSets;
            notifyDataSetChanged();
        }

        void setExercises(List<Exercise> exercises) {
            this.exercises = exercises;
            notifyDataSetChanged();
        }

        public void setWorkouts(List<Workout> workoutsIds) {
            this.workoutsDetailsList = workoutsIds;
        }
    }

}