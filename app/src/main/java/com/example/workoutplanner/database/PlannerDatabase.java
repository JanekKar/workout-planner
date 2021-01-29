package com.example.workoutplanner.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.workoutplanner.database.daos.DoneSetDao;
import com.example.workoutplanner.database.daos.ExerciseDao;
import com.example.workoutplanner.database.daos.ProgressDao;
import com.example.workoutplanner.database.daos.SetDao;
import com.example.workoutplanner.database.daos.WorkoutDao;
import com.example.workoutplanner.database.daos.WorkoutSetDao;
import com.example.workoutplanner.database.models.DoneSet;
import com.example.workoutplanner.database.models.Exercise;
import com.example.workoutplanner.database.models.Set;
import com.example.workoutplanner.database.models.Workout;
import com.example.workoutplanner.database.models.WorkoutSet;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Exercise.class, Set.class, Workout.class, WorkoutSet.class, DoneSet.class},
        version = 1,
        exportSchema = false)
@TypeConverters({Converters.class})
public abstract class PlannerDatabase extends RoomDatabase {

    public static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    private static volatile PlannerDatabase INSTANCE;
    private static final RoomDatabase.Callback sPlannerDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            databaseWriteExecutor.execute(() -> {
//                ExerciseDao dao = INSTANCE.exerciseDao();
//                dao.deleteAll();
//                Exercise e = new Exercise("Pompki", "Ramiona", false);
//                dao.insertAll(e);
//                e = new Exercise("Pompki1", "Ramiona1", false);
//                dao.insertAll(e);
//                e = new Exercise("Pompki2", "Ramiona2", false);
//                dao.insertAll(e);
//
//                SetDao setDao = INSTANCE.setDao();
//                setDao.deleteAll();
//                Set s = new Set(1, 30, 0);
//                setDao.insertAll(s, s, s);
//
//                WorkoutDao wd = INSTANCE.workOutDao();
//                wd.deleteAll();
//
//                Workout w = new Workout(1, "Test name");
//                wd.insertAll(w);
//                w = new Workout(2, "Test name2");
//                wd.insertAll(w);
//                w = new Workout(3, "Test name3");
//                wd.insertAll(w);
//
//                WorkoutSetDao wsd = INSTANCE.workoutSetDao();
//                wsd.deleteAll();
//                WorkoutSet ws = new WorkoutSet(22, 2);
//                wsd.insertAll(ws);
//                ws = new WorkoutSet(22, 3);
//                wsd.insertAll(ws);
//                ws = new WorkoutSet(22, 5);
//                wsd.insertAll(ws);
            });
        }
    };

    public static PlannerDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PlannerDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PlannerDatabase.class, "planner_db")
                            .addCallback(sPlannerDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract ExerciseDao exerciseDao();

    public abstract SetDao setDao();

    public abstract WorkoutDao workOutDao();

    public abstract WorkoutSetDao workoutSetDao();

    public abstract DoneSetDao doneSetDao();

    public abstract ProgressDao progressDao();
}
