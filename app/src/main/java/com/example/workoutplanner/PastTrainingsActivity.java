package com.example.workoutplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import android.widget.TextView;

import com.example.workoutplanner.database.ViewModels.DoneSetViewModel;
import com.example.workoutplanner.database.models.DoneSet;
import com.example.workoutplanner.database.models.Workout;

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

    private WorkoutAdapter adapter;

    private DoneSetViewModel dsvm;

    private SimpleDateFormat sdf;

    private HashMap<Date, List<Long>> dateWorkoutMap;
    private ArrayList<Date> orderedDates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_training);

        dateWorkoutMap = new HashMap<>();

        startEditText = findViewById(R.id.from_editTextDate);
        endEditText = findViewById(R.id.to_editTextDate);
        searchButton = findViewById(R.id.search_button);

        dsvm = new ViewModelProviders().of(this).get(DoneSetViewModel.class);


        sdf = new SimpleDateFormat("dd.MM.yyyy");

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        adapter = new WorkoutAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        DatePickerDialog.OnDateSetListener startDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                startCal.set(Calendar.YEAR, year);
                startCal.set(Calendar.MONTH, month);
                startCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);


                startCal.set(Calendar.HOUR_OF_DAY, 0);
                startCal.set(Calendar.MINUTE, 0);
                startCal.set(Calendar.SECOND, 0);
                startCal.set(Calendar.MILLISECOND, 0);

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

                endCal.set(Calendar.HOUR_OF_DAY, 0);
                endCal.set(Calendar.MINUTE, 0);
                endCal.set(Calendar.SECOND, 0);
                endCal.set(Calendar.MILLISECOND, 0);

                endEditText.setText(sdf.format(endCal.getTime()));
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
        orderedDates = new ArrayList<>();
        dsvm.get(startCal.getTime(), endCal.getTime()).observe(this, new Observer<List<DoneSet>>() {
            @Override
            public void onChanged(List<DoneSet> doneSets) {
                for(DoneSet ds : doneSets){

                    if(!orderedDates.contains(ds.getDate()))
                        orderedDates.add(ds.getDate());
                    if(dateWorkoutMap.containsKey(ds.getDate())){
                        if(!dateWorkoutMap.get(ds.getDate()).contains(ds.getWorkoutId()))
                            dateWorkoutMap.get(ds.getDate()).add(ds.getWorkoutId());
                    }else{
                        List<Long> temp = new ArrayList<>();
                        temp.add(ds.getWorkoutId());
                        dateWorkoutMap.put(ds.getDate(), temp);
                    }
                }

                Log.d("MainActivity", orderedDates.size()+"");
                adapter.setDate(dateWorkoutMap, orderedDates);
                
                Log.d("MainActivity", dateWorkoutMap.size()+"");
            }
        });
    }

    //TODO rewrite this holder and adapter

    private class WorkoutHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView dateTextView;
        private final TextView workoutCountTextView;

        private Date date;
        private List<Long> ids;

        private Workout workout;

        public WorkoutHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.past_training_list_item, parent, false));
            itemView.setOnClickListener(this);

            dateTextView = itemView.findViewById(R.id.date_label);
            workoutCountTextView = itemView.findViewById(R.id.workout_names_label);
        }

        public void bind(Date date, List<Long> ids) {
            this.ids = ids;
            this.date = date;

            this.dateTextView.setText(sdf.format(date)+"");
            this.workoutCountTextView.setText(getResources().getString(R.string.workout_count, ids.size()));
        }

        @Override
        public void onClick(View v) {
            //TODO new oacitvity, list of exercisess with set info - one card per exercise with all ioformation
//            Intent intent = new Intent(PastTrainingsActivity.this, WorkoutActivity.class);
////            intent.putExtra(WORKOUT_ID_EXTRA, workout.getId());
//            startActivity(intent);

        }
    }

    private class WorkoutAdapter extends RecyclerView.Adapter<WorkoutHolder> {

        private HashMap<Date, List<Long>> data;
        private List<Date> orderedDates;

        @NonNull
        @Override
        public WorkoutHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new WorkoutHolder(getLayoutInflater(), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull WorkoutHolder holder, int position) {
            Date date = orderedDates.get(position);
            holder.bind(date,data.get(date));
        }

        @Override
        public int getItemCount() {
            if (data != null) {
                return data.size();
            } else {
                return 0;
            }
        }

        void setDate(HashMap<Date, List<Long>> map, List<Date> orderedDates) {
            this.orderedDates = orderedDates;
            this.data = map;
            notifyDataSetChanged();
        }

    }
}