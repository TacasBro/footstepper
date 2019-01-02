package com.example.footstepper.footstepper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;


public class AchievementsActivity extends AppCompatActivity {
    // ListView przechowujące wszystkie Achievementy
    private ListView achievsListView;
    // TextView pokazujące kroki od instalacji aplikacji
    private TextView totalStepsTextView;

    // Tablica Achievementów, wyświetlana w ListView
    private Achievement[] achievements;

    //      Funkcja włączana przy starcie aplikacji
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // inicjalizacja
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

        // Wyświetlanie wszystkich kroków
        totalStepsTextView = (TextView) findViewById(R.id.total_steps_text_view);
        String totalStepsTitle = getResources().getString(R.string.total_steps_title) + String.valueOf(MainActivity.getAmountOfStepsEver());
        totalStepsTextView.setText(totalStepsTitle);
        // Wyświetlanie wszystkich Achievementów
        initializeAchievements();
        achievsListView = (ListView) findViewById(R.id.achievsListView);

        AchievementsListViewAdapter achievementsListViewAdapterAdapter =
                new AchievementsListViewAdapter(this, achievements);
        achievsListView.setAdapter(achievementsListViewAdapterAdapter);

    }

    // Wczytanie domyślnych Achievementów
    private void initializeAchievements(){
        achievements = new Achievement[5];
        achievements[0] = new Achievement("Początek!", "Zrobiłeś conajmniej 1 krok", 1);
        achievements[1] = new Achievement("Do lodówki to max!", "Zrobiłeś conajmniej 10 kroków", 10);
        achievements[2] = new Achievement("Spacerek?", "Zrobiłeś conajmniej 100 kroków", 100);
        achievements[3] = new Achievement("Trochę ruchu", "Zrobiłeś conajmniej 1000 kroków", 1000);
        achievements[4] = new Achievement("Niezły trening!", "Zrobiłeś conajmniej 10000 kroków", 10000);
    }
}
