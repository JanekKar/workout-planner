package com.example.workoutplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.workoutplanner.database.ViewModels.ExerciseViewModel;
import com.example.workoutplanner.database.ViewModels.WorkoutSetViewModel;
import com.example.workoutplanner.database.ViewModels.WorkoutViewModel;
import com.example.workoutplanner.database.models.Exercise;
import com.example.workoutplanner.database.models.Workout;
import com.example.workoutplanner.database.models.WorkoutSet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private int NEW_BOOK_ACTIVITY_REQUEST_CODE = 0;
    public static String WORKOUT_ID_EXTRA = "WORKOUT_ID_EXTRA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final WorkoutAdapter adapter = new WorkoutAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        WorkoutSetViewModel ws = ViewModelProviders.of(this).get(WorkoutSetViewModel.class);
        WorkoutViewModel w =ViewModelProviders.of(this).get(WorkoutViewModel.class);
        w.getWorkouts().observe(this, new Observer<List<Workout>>() {
            @Override
            public void onChanged(List<Workout> workouts) {
                adapter.setWorkouts(workouts);

                List<Integer> numberOfExercises = new ArrayList<>();
                for(Workout w: workouts){
                    List<Long> temp = ws.getAllExercises(w.getId()).getValue();
                    if(temp!=null)
                        numberOfExercises.add(temp.size());
                    else
                        numberOfExercises.add(0);
                }
                adapter.setExerciseList(numberOfExercises);
            }
        });



        ws.getAllWorkoutsSets().observe(this, new Observer<List<WorkoutSet>>() {
            @Override
            public void onChanged(List<WorkoutSet> workoutSets) {

            }
        });


        FloatingActionButton addWorkoutFab = findViewById(R.id.add_button);
        addWorkoutFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewWorkoutActivity.class);
                startActivityForResult(intent, NEW_BOOK_ACTIVITY_REQUEST_CODE);
                Workout ww = new Workout(0, "");
                long id = w.addWorkout(ww);
                Log.d("MainActivity", ww.getId()+"");

                //TODO on result fail delete new workout, and all sets conected to it
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.exercise_list) {
            Intent intent = new Intent(MainActivity.this, ExerciseListActivity.class);
        }

        return super.onOptionsItemSelected(item);
    }


    private class WorkoutHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        private TextView dayTextView;
        private TextView nameTextView;
        private Workout workout;

        public WorkoutHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.workout_list_item, parent, false));
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            dayTextView = itemView.findViewById(R.id.workout_day);
            nameTextView = itemView.findViewById(R.id.workout_name);
        }

        public void bind(Workout workout,int num){
            this.workout = workout;

            dayTextView.setText("Day: " + workout.getId());
            nameTextView.setText("Number of sets: " + workout.getName());
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, WorkoutActivity.class);
            intent.putExtra(WORKOUT_ID_EXTRA, workout.getId());
            startActivity(intent);

        }

        @Override
        public boolean onLongClick(View v) {
            Snackbar.make(findViewById(R.id.coordinator_layout), "LONG TEST", Snackbar.LENGTH_LONG).show();
            return true;
        }
    }

    private class WorkoutAdapter extends RecyclerView.Adapter<WorkoutHolder> {
        private List<Workout> workouts;
        private List<Integer> numOfEx;

        @NonNull
        @Override
        public WorkoutHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new WorkoutHolder(getLayoutInflater(), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull WorkoutHolder holder, int position) {
            if(workouts != null && numOfEx != null){
                Workout workout = workouts.get(position);
                int num = numOfEx.get(position);
                holder.bind(workout, num);
            }else{
                Log.d("MainActivity", "No workouts");
            }
        }

        @Override
        public int getItemCount() {
            if(workouts != null){
                return workouts.size();
            }else{
                return 0;
            }
        }

        void setWorkouts(List<Workout> workouts){
            this.workouts = workouts;
            notifyDataSetChanged();
        }

        void setExerciseList(List<Integer> numberOfExercises){
            this.numOfEx = numberOfExercises;
            notifyDataSetChanged();
        }

    }
}