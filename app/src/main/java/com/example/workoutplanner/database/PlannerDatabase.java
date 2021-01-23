package com.example.workoutplanner.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.workoutplanner.database.daos.ExerciseDao;
import com.example.workoutplanner.database.daos.SetDao;
import com.example.workoutplanner.database.daos.WorkOutDao;
import com.example.workoutplanner.database.models.Exercise;
import com.example.workoutplanner.database.models.Set;
import com.example.workoutplanner.database.models.Workout;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Exercise.class, Set.class, Workout.class}, version = 1, exportSchema = false)
public abstract class PlannerDatabase extends RoomDatabase {
    public abstract ExerciseDao exerciseDao();
    public abstract SetDao setDao();
    public abstract WorkOutDao workOutDao();

    private static volatile PlannerDatabase INSTANCE;
    public static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    private static RoomDatabase.Callback sPlannerDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            databaseWriteExecutor.execute(() -> {
                ExerciseDao dao = INSTANCE.exerciseDao();
                dao.deleteAll();
                Exercise e = new Exercise("Pompki", "Ramiona");
                dao.insertAll(e, e, e);

                SetDao setDao = INSTANCE.setDao();
                setDao.deleteAll();
                Set s = new Set(1, 30);
                setDao.insertAll(s, s, s);

                WorkOutDao wd = INSTANCE.workOutDao();
                wd.deleteAll();

                Workout w = new Workout(1, 1);
                wd.insertAll(w);
                w = new Workout(2, 2);
                wd.insertAll(w);
                w = new Workout(3, 2);
                wd.insertAll(w);
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
}
