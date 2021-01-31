package com.example.workoutplanner;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutplanner.database.ViewModels.DoneSetViewModel;
import com.example.workoutplanner.database.ViewModels.WorkoutSetViewModel;
import com.example.workoutplanner.database.ViewModels.WorkoutViewModel;
import com.example.workoutplanner.database.models.DoneSet;
import com.example.workoutplanner.database.models.Workout;
import com.example.workoutplanner.database.models.WorkoutSet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static String WORKOUT_ID_EXTRA = "WORKOUT_ID_EXTRA";
    private final int NEW_WORKOUT_ACTIVITY_REQUEST_CODE = 0;

    private WorkoutSetViewModel ws;
    private DoneSetViewModel dsvm;
    private WorkoutViewModel wvm;

    private final FragmentActivity owner = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.getSupportActionBar().setSubtitle("");

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final WorkoutAdapter adapter = new WorkoutAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ws = ViewModelProviders.of(this).get(WorkoutSetViewModel.class);
        dsvm = ViewModelProviders.of(this).get(DoneSetViewModel.class);
        wvm = ViewModelProviders.of(this).get(WorkoutViewModel.class);

        WorkoutViewModel w = ViewModelProviders.of(this).get(WorkoutViewModel.class);
        w.getWorkouts().observe(this, new Observer<List<Workout>>() {
            @Override
            public void onChanged(List<Workout> workouts) {
                adapter.setWorkouts(workouts);

                List<Integer> numberOfExercises = new ArrayList<>();
                for (Workout w : workouts) {
                    List<Long> temp = ws.getAllExercises(w.getWorkoutId()).getValue();
                    if (temp != null)
                        numberOfExercises.add(temp.size());
                    else
                        numberOfExercises.add(0);
                }
                adapter.setExerciseList(numberOfExercises);
            }
        });

        FloatingActionButton addWorkoutFab = findViewById(R.id.add_button);
        addWorkoutFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewWorkoutActivity.class);
                startActivityForResult(intent, NEW_WORKOUT_ACTIVITY_REQUEST_CODE);
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


        Intent intent;
        switch (id) {
            case R.id.exercise_list:
                intent = new Intent(MainActivity.this, ExerciseListActivity.class);
                break;
            case R.id.past_trainings:
                intent = new Intent(MainActivity.this, PastTrainingsActivity.class);
                break;
            case R.id.progress:
                intent = new Intent(MainActivity.this, ProgressActivity.class);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + id);
        }

        startActivity(intent);


        return super.onOptionsItemSelected(item);
    }


    public String dayNumberToName(int number) {
        Resources res = getResources();
        String[] days = res.getStringArray(R.array.days_of_week);
        return days[number];
    }

    private class WorkoutHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        private final TextView dayTextView;
        private final TextView nameTextView;
        private final ProgressBar progressBar;
        private final RelativeLayout layout;

        private Workout workout;

        public WorkoutHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.workout_list_item, parent, false));
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            dayTextView = itemView.findViewById(R.id.workout_day);
            nameTextView = itemView.findViewById(R.id.workout_name);
            progressBar = itemView.findViewById(R.id.progressBar);
            layout = itemView.findViewById(R.id.layout);
        }

        public void bind(Workout workout, int num) {
            this.workout = workout;

            nameTextView.setText(workout.getName());
            dayTextView.setText(dayNumberToName(workout.getWeekDay()));

            Calendar cal = Calendar.getInstance();

            int currentDayNumber = getCUrrentDay(cal);
            if (workout.getWeekDay() == currentDayNumber) {
                layout.setBackgroundColor(getResources().getColor(R.color.todays_workout));
            }

            Log.d("MainActivity", "Day of week " + currentDayNumber + "");


            ws.getAllSets(workout.getWorkoutId()).observe(owner, new Observer<List<WorkoutSet>>() {
                @Override
                public void onChanged(List<WorkoutSet> workoutSets) {
                    int max = workoutSets.size();

                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.DAY_OF_MONTH, -6);

                    Date beginning = cal.getTime();
                    Date now = new Date();

                    dsvm.get(beginning, now, workout.getWorkoutId()).observe(owner, new Observer<List<DoneSet>>() {
                        @Override
                        public void onChanged(List<DoneSet> doneSets) {
                            int done = 0;
                            for (DoneSet ds : doneSets) {
                                for (WorkoutSet ws : workoutSets) {
                                    if (ws.getSetId() == ds.getSet().getSetId()) {
                                        done++;
                                    }
                                }
                            }

                            progressBar.setMax(max);
                            progressBar.setProgress(done);

                            if (done == max) {
                                layout.setBackgroundColor(getResources().getColor(R.color.done_workout));
                            }

                        }
                    });
                }
            });

        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, WorkoutActivity.class);
            intent.putExtra(WORKOUT_ID_EXTRA, workout.getWorkoutId());
            startActivity(intent);

        }

        @Override
        public boolean onLongClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setMessage(getResources().getString(R.string.dialog_message, workout.getName()))
                    .setTitle(R.string.dialog_title);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                    Log.d("MainActivity", "Ok, delete");
                    workout.setDeleted(true);
                    wvm.update(workout);

                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });

            builder.create();
            builder.show();

            return true;
        }
    }

    private int getCUrrentDay(Calendar cal) {
        int temp = (cal.get(Calendar.DAY_OF_WEEK) - cal.getFirstDayOfWeek())-1;
        if(temp<0)
            return 6;
        return temp;
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
            if (workouts != null && numOfEx != null) {
                Workout workout = workouts.get(position);
                int num = numOfEx.get(position);
                holder.bind(workout, num);
            } else {
                Log.d("MainActivity", "No workouts");
            }
        }

        @Override
        public int getItemCount() {
            if (workouts != null) {
                return workouts.size();
            } else {
                return 0;
            }
        }

        void setWorkouts(List<Workout> workouts) {
            this.workouts = workouts;
            notifyDataSetChanged();
        }

        void setExerciseList(List<Integer> numberOfExercises) {
            this.numOfEx = numberOfExercises;
            notifyDataSetChanged();
        }

    }
}