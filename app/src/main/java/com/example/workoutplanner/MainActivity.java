package com.example.workoutplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.workoutplanner.database.WorkoutViewModel;
import com.example.workoutplanner.database.models.Workout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final WorkoutAdapter adapter = new WorkoutAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        WorkoutViewModel w =ViewModelProviders.of(this).get(WorkoutViewModel.class);
        w.getWorkouts().observe(this, new Observer<List<Workout>>() {
            @Override
            public void onChanged(List<Workout> workouts) {
                adapter.setWorkouts(workouts);
            }
        });


        FloatingActionButton addWorkoutFab = findViewById(R.id.add_button);
        addWorkoutFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Intent intent = new Intent(MainActivity.this, EditBookActivity.class);
                //startActivityForResult(intent, NEW_BOOK_ACTIVITY_REQUEST_CODE);
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
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }


    private class WorkoutHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        private TextView dayTextView;
        private TextView exerciseCountTextView;
        private Workout workout;

        public WorkoutHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.workout_list_item, parent, false));
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            dayTextView = itemView.findViewById(R.id.day_number);
            exerciseCountTextView = itemView.findViewById(R.id.exerciseCount);
        }

        public void bind(Workout workout){
            this.workout = workout;
            Log.d("MainActivity", workout.toString());
            dayTextView.setText(workout.getWeekDay()+"");
            exerciseCountTextView.setText(workout.getWeekDay()+"");
        }

        @Override
        public void onClick(View v) {

        }

        @Override
        public boolean onLongClick(View v) {
            Snackbar.make(findViewById(R.id.coordinator_layout), "LONG TEST", Snackbar.LENGTH_LONG).show();
            return true;
        }
    }

    private class WorkoutAdapter extends RecyclerView.Adapter<WorkoutHolder> {
        private List<Workout> workouts;

        @NonNull
        @Override
        public WorkoutHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new WorkoutHolder(getLayoutInflater(), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull WorkoutHolder holder, int position) {
            if(workouts != null){
                Workout workout = workouts.get(position);
                holder.bind(workout);
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
    }
}