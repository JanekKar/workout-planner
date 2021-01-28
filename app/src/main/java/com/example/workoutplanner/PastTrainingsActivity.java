package com.example.workoutplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.workoutplanner.database.ViewModels.DoneSetViewModel;
import com.example.workoutplanner.database.models.DoneSet;
import com.example.workoutplanner.database.models.Workout;
import com.example.workoutplanner.database.models.WorkoutSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class PastTrainingsActivity extends AppCompatActivity {

    private Calendar startCal = Calendar.getInstance();
    private Calendar endCal = Calendar.getInstance();

    private EditText startEditText;
    private EditText endEditText;
    private Button searchButton;

    private DoneSetViewModel dsvm;


    private HashMap<Date, List<Long>> dateWorkoutMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_training);

        dateWorkoutMap = new HashMap<>();

        startEditText = findViewById(R.id.from_editTextDate);
        endEditText = findViewById(R.id.to_editTextDate);
        searchButton = findViewById(R.id.search_button);

        dsvm = new ViewModelProviders().of(this).get(DoneSetViewModel.class);


        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");


        DatePickerDialog.OnDateSetListener startDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                startCal.set(Calendar.YEAR, year);
                startCal.set(Calendar.MONTH, month);
                startCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                startEditText.setText(sdf.format(startCal.getTime()));
            }
        };

        startEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(PastTrainingsActivity.this, startDate, startCal.get(Calendar.YEAR), startCal.get(Calendar.MONTH), startCal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        DatePickerDialog.OnDateSetListener endDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                endCal.set(Calendar.YEAR, year);
                endCal.set(Calendar.MONTH, month);
                endCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                endEditText.setText(sdf.format(startCal.getTime()));
            }
        };

        endEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(PastTrainingsActivity.this, endDate, endCal.get(Calendar.YEAR), endCal.get(Calendar.MONTH), endCal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               populateList();
            }
        });


    }

    private void populateList() {
        dsvm.get(startCal.getTime(), endCal.getTime()).observe(this, new Observer<List<DoneSet>>() {
            @Override
            public void onChanged(List<DoneSet> doneSets) {
                for(DoneSet ds : doneSets){
                    if(dateWorkoutMap.containsKey(ds.getDate())){
                        dateWorkoutMap.get(ds.getDate()).add(ds.getWorkoutId());
                    }else{
                        List<Long> temp = new ArrayList<>();
                        temp.add(ds.getWorkoutId());
                        dateWorkoutMap.put(ds.getDate(), temp);
                    }
                }

                Log.d("MainActivity", dateWorkoutMap.toString());
            }
        });
    }

    //TODO rewrite this holder and adapter

    private class WorkoutHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView dayTextView;
        private final TextView nameTextView;
        private final ProgressBar progressBar;
        private RelativeLayout layout;

        private Workout workout;

        public WorkoutHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.workout_list_item, parent, false));
            itemView.setOnClickListener(this);

            dayTextView = itemView.findViewById(R.id.workout_day);
            nameTextView = itemView.findViewById(R.id.workout_name);
            progressBar = itemView.findViewById(R.id.progressBar);
            layout = itemView.findViewById(R.id.layout);
        }

        public void bind(Workout workout) {

        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, WorkoutActivity.class);
//            intent.putExtra(WORKOUT_ID_EXTRA, workout.getId());
            startActivity(intent);

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