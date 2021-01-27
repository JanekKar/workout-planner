package com.example.workoutplanner;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutplanner.database.ViewModels.ExerciseViewModel;
import com.example.workoutplanner.database.models.Exercise;
import com.example.workoutplanner.database.models.Set;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class NewExerciseSetActivity extends AppCompatActivity {

    public static final String EXTRA_SET_LIST_TO_SAVE = "extra_set_list_to_save";
    private Spinner exerciseSpinner;

    private ArrayList<Set> setList;
    private SetAdapter adapter;
    private Button saveButton;
    private long exerciseId;
    private boolean weight;
    private boolean edit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_exercise_set);

        RecyclerView recyclerView = findViewById(R.id.set_recyclerview);
        adapter = new SetAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (getIntent().hasExtra(NewWorkoutActivity.EXTRA_SET_LIST)) {
            setList = getIntent().getParcelableArrayListExtra(NewWorkoutActivity.EXTRA_SET_LIST);
            exerciseId = setList.get(0).getExerciseId();
            edit = true;
        } else {
            setList = new ArrayList<>();
            setList.add(new Set(0, 0, 0));
            setList.add(new Set(0, 0, 0));
            setList.add(new Set(0, 0, 0));
            exerciseId = 0;
        }

        adapter.setSets(setList);


        ExerciseViewModel evm = ViewModelProviders.of(this).get(ExerciseViewModel.class);
        evm.getExercises().observe(this, new Observer<List<Exercise>>() {
            @Override
            public void onChanged(List<Exercise> exercises) {
                setupExerciseSpinner(exercises);
            }
        });

        FloatingActionButton addWorkoutFab = findViewById(R.id.add_set_button);
        addWorkoutFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setList.add(new Set(0, 0, 0));
                adapter.setSets(setList);
            }
        });

        saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSets();
            }
        });
    }

    private void saveSets() {
        ArrayList<Set> toSave = new ArrayList<>();
        for (Set set : setList) {
            if (set.getNumberOfRepsToDO() != 0) {
                set.setExerciseId(exerciseId);
                toSave.add(set);
            }
        }

        Intent replyIntent = new Intent();
        replyIntent.putParcelableArrayListExtra(EXTRA_SET_LIST_TO_SAVE, toSave);
        setResult(RESULT_OK, replyIntent);
        finish();
    }


    protected void deleteRow(Set s) {
        setList.remove(s);
        adapter.setSets(setList);
    }


    public void setupExerciseSpinner(List<Exercise> exercises) {
        int pos = 0;
        List<String> exerciseNames = new ArrayList<>();
        if (exercises != null) {
            for (Exercise e : exercises) {
                exerciseNames.add(e.getName());
                if (e.getExerciseId() == exerciseId)
                    pos = exerciseNames.indexOf(e.getName());

            }

            exerciseSpinner = findViewById(R.id.exercise_spinner);
            ArrayAdapter<String> exerciseAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, exerciseNames);
            exerciseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            exerciseSpinner.setAdapter(exerciseAdapter);
            exerciseSpinner.setSelection(pos);

        } else {
            Log.d("MainActivity", "No exercises found");
        }

        exerciseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                exerciseId = exercises.get(position).getExerciseId();
                weight = exercises.get(position).isAditiona_weight();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private class SetHolder extends RecyclerView.ViewHolder {

        private final EditText repsToDo;
        private final EditText additionalWeight;
        private final Button deleteSet;
        private Set set;

        public SetHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.set_list_item, parent, false));


            repsToDo = itemView.findViewById(R.id.reps_to_do_edit_text);
            additionalWeight = itemView.findViewById(R.id.additional_weight_edit_text);
            deleteSet = itemView.findViewById(R.id.delete_set_button);
            deleteSet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteRow(set);
                    Snackbar.make(findViewById(R.id.coordinator_layout), getResources().getString(R.string.set_deleted), Snackbar.LENGTH_LONG).show();
                }
            });

            repsToDo.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!s.toString().equals(""))
                        set.setNumberOfRepsToDO(Integer.parseInt(s.toString()));
                    else {
                        set.setNumberOfRepsToDO(0);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            additionalWeight.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!s.toString().equals(""))
                        set.setAdditionalWeight(Integer.parseInt(s.toString()));
                    else {
                        set.setAdditionalWeight(0);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

        public void bind(Set s) {
            this.set = s;
            if (set.getNumberOfRepsToDO() != 0)
                repsToDo.setText(s.getNumberOfRepsToDO() + "");
            else {
                repsToDo.setText("");
            }
            additionalWeight.setEnabled(weight);
            if (set.getAdditionalWeight() != 0 || weight) {
                additionalWeight.setText(s.getAdditionalWeight() + "");
            } else {
                additionalWeight.setText("");
            }
        }
    }

    private class SetAdapter extends RecyclerView.Adapter<SetHolder> {
        private List<Set> sets;

        @NonNull
        @Override
        public SetHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new SetHolder(getLayoutInflater(), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull SetHolder holder, int position) {
            if (sets != null) {
                Set s = sets.get(position);
                holder.bind(s);
            }
        }

        @Override
        public int getItemCount() {
            if (sets != null) {
                return sets.size();
            }
            return 0;
        }

        public void setSets(List<Set> sets) {
            this.sets = null;
            notifyDataSetChanged();
            this.sets = sets;
            Log.d("MainActivity", sets.toString());
            notifyDataSetChanged();
        }
    }
}