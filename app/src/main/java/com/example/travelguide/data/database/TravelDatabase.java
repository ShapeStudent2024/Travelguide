package com.example.travelguide.data.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import android.content.Context;
import com.example.travelguide.data.converter.StringListConverter;
import com.example.travelguide.data.dao.PostDao;
import com.example.travelguide.data.entity.Post;

@Database(
        entities = {Post.class},
        version = 1,
        exportSchema = false
)
@TypeConverters({StringListConverter.class})
public abstract class TravelDatabase extends RoomDatabase {

    private static volatile TravelDatabase INSTANCE;

    public abstract PostDao postDao();

    public static TravelDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TravelDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            TravelDatabase.class,
                            "travel_database"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}