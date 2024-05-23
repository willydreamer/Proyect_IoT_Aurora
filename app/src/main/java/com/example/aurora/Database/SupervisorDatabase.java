/*package com.example.aurora.Database;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.aurora.Bean.Supervisor;
import com.example.aurora.Dao.SupervisorDao;

@Database(entities = {Supervisor.class},version=1)
public abstract class SupervisorDatabase extends RoomDatabase {

    public abstract SupervisorDao supervisorDao();

    private static SupervisorDatabase INSTANCE;

    static SupervisorDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (SupervisorDatabase.class){
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            SupervisorDatabase.class,"netwise")
                            .build();
                }

            }
        }

        return INSTANCE;

    }
}
*/
