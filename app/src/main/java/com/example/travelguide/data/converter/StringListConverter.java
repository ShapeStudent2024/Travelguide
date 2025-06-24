package com.example.travelguide.data.converter;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

public class StringListConverter {

    @TypeConverter
    public static String fromStringList(List<String> stringList) {
        if (stringList == null) {
            return null;
        }
        Gson gson = new Gson();
        return gson.toJson(stringList);
    }

    @TypeConverter
    public static List<String> toStringList(String stringListString) {
        if (stringListString == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {}.getType();
        return gson.fromJson(stringListString, type);
    }

    @TypeConverter
    public static Long fromDate(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }
}