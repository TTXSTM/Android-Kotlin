package space.mairi.kotlineapp;

import android.content.Intent;

public class MyJavaClass {
    void myFunc(){
        Repository.INSTANCE.getWeatherList();

        Intent intent = MainActivity.Companion.getIntent();
    }
}
