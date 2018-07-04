package com.example.joaquin.workoutplan;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Converters {


    @TypeConverter
    public static ArrayList<String> fromString(String string) {
        Type listType = new TypeToken<ArrayList<String>>() {}.getType();
        return new Gson().fromJson(string, listType);
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<String> list){
        return new Gson().toJson(list);
    }

}
