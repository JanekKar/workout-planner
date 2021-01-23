package com.example.workoutplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.workoutplanner.database.ViewModels.ExerciseViewModel;
import com.example.workoutplanner.database.models.Exercise;
import com.example.workoutplanner.database.models.Set;

import java.util.ArrayList;
import java.util.List;

public class NewExerciseSetActivity extends AppCompatActivity {

    private Spinner exerciseSpinner;
    private int dayNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_exercise_set);

        RecyclerView recyclerView = findViewById(R.id.set_recyclerview);
        final SetAdapter adapter = new SetAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ExerciseViewModel evm = ViewModelProviders.of(this).get(ExerciseViewModel.class);
        List<Exercise> exerciseList= evm.getExercises().getValue();
        List<String> exerciseNames = new ArrayList<>();
        if(exerciseList != null){
            for(Exercise e : exerciseList){
                exerciseNames.add(e.getName());
            }

            exerciseSpinner = findViewById(R.id.exercise_spinner);
            ArrayAdapter<String> exerciseAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, (String[]) exerciseNames.toArray());
            exerciseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            exerciseSpinner.setAdapter(exerciseAdapter);

        }else{
            Log.d("MainActivity", exerciseList+"");
        }
    }

    private class SetHolder extends RecyclerView.ViewHolder{

        private EditText repsToDo;

        public SetHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.set_list_item, parent, false));

            repsToDo = findViewById(R.id.reps_to_do_edit_text);
        }

        public void bind(Set s){
            this.repsToDo.setText(s.getNumberOfRepsToDO());
        }
    }

    private class SetAdapter extends RecyclerView.Adapter<SetHolder>{
        private List<Set> sets;

        @NonNull
        @Override
        public SetHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new SetHolder(getLayoutInflater(), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull SetHolder holder, int position) {
            if(sets != null){
                Set s = sets.get(position);
                holder.bind(s);
            }
        }

        @Override
        public int getItemCount() {
             if(sets != null)
                 return sets.size();
            return 0;
        }

        public void setSets(List<Set> sets){
            this.sets = sets;
            notifyDataSetChanged();
        }
    }
}