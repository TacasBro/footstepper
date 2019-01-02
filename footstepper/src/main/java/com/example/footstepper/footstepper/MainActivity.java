package com.example.footstepper.footstepper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    //      Liczniki Krokow
    // Kroki od kliknięcia przycisku "restart"
    private static int amountOfStepsSinceRestart = 0;
    // Kroki zapisane przy poprzednim odczycie sensora
    private static int amountOfStepsOnLastSensorChange = 0;
    // Kroki zapisane przy ostatnim kliknięciu przycisku restart
    private static int amountOfStepsAtRestart = 0;
    // Kroki wykonane od zainstalowania aplikacji
    private static int amountOfStepsEver = 0;
    // Kroki wykonane w aktualnym dniu
    private static int amountOfStepsDaily = 0;
    // Ilość kroków wykonanych od instalacji aplikacji zanim zaczął się aktualny dzień
    private static int amountOfStepsAtDayStart = 0;


    //      String definujący konkretną zmienną w SharedPreferences
    // String definiujący kroki wykonane w ciągu aktualnego dnia
    private final static String dailyStepAmountKeyName = "daily";
    // String definiujący kroki zapisane przy ostatnim kliknięciu przycisku restart
    private final static String atRestartStepAmountKeyName = "atRestart";
    // kroki od instalacji
    private final static String everStepAmountKeyName = "ever";
    // indeks dnia, aby sprawdzic czy zmieniła się data od ostatniego włączenia aplikacji
    private final static String dayIndexKeyName = "dayIndex";

    //      Zarządzanie kalendarzem
    // Instancja kalendarza
    private static Calendar calendar = null;
    // Aktualny dzień - do sprawdzania zmiany daty
    private static int dayOfMonth;

    //      Zarządzenie Sensorem
    private SensorManager sensorManager;
    private Sensor stepCountingSensor;

    //      Elementy Layout'u
    private TextView daily;
    private TextView sinceRestart;


    //      Funkcja wykonywana przy tworzeniu aktywności
    // wczytywanie widoków
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // inicjalizacja
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ustawienie zarządzania sensorem oraz widoków
        daily = (TextView) findViewById(R.id.amount_daily);
        sinceRestart = (TextView) findViewById(R.id.amount_since_restart);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // określenie daty
        calendar = Calendar.getInstance();
        if(dayOfMonth == -1) {
            dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        }
    }

    //      Funkcja wykonywana przy wznowieniu aplikacji
    @Override
    protected void onResume(){
        super.onResume();

        //      Wczytanie instancji sensora i SharedPreferences
        stepCountingSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);

        //      Wczytanie zapisanych wartości w SharedPreferences
        // kroki początkiem dnia
        amountOfStepsAtDayStart = sharedPreferences.getInt(dailyStepAmountKeyName, 0);
        // kroki od restartu
        amountOfStepsAtRestart = sharedPreferences.getInt(atRestartStepAmountKeyName, 0);
        // kroki od instalacji i kroki od ostatniej zmiany sensora
        amountOfStepsEver = amountOfStepsOnLastSensorChange = sharedPreferences.getInt(everStepAmountKeyName, 0);
        // dzień kiedy ostatni raz aplikacja była używana
        dayOfMonth = sharedPreferences.getInt(dayIndexKeyName, -1);

        // sprawdzenie zmiany daty
        if(dayOfMonth != calendar.get(Calendar.DAY_OF_MONTH)){
            // jeśli dzień się zmienił ustawiamy stan z początku dnia na aktualny
            amountOfStepsAtDayStart = amountOfStepsEver;
            dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        }

        //      ustawienie wartości do wyświetlenia
        // kroki dzisiaj: dla nowej daty 0
        amountOfStepsDaily = amountOfStepsEver - amountOfStepsAtDayStart;
        // kroki od restartu
        amountOfStepsSinceRestart = amountOfStepsEver - amountOfStepsAtRestart;

        daily.setText(String.valueOf(amountOfStepsDaily));
        sinceRestart.setText(String.valueOf(amountOfStepsSinceRestart));

        // Jeśli urządzenie nie posiada sensora wyświetl komunikat
        if(stepCountingSensor == null){
            Toast.makeText(this, R.string.no_sensor_prompt, Toast.LENGTH_LONG).show();
        }
        // Jeśli posiada to zarejestruj sensor
        else{
            sensorManager.registerListener(
                    this,
                    stepCountingSensor,
                    SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // ilość kroków od ostatniej aktualizacji licznika
        int amountOfNewSteps = 0;
        // wartość zarejestrowana przez sensor
        int eventValue = (int)event.values[0];
        // jeśli licznik sensora się zrestartował to dodaj ilość kroków równą wskaźnikowi sensora
        if(amountOfStepsOnLastSensorChange > event.values[0]){
            amountOfNewSteps = eventValue;
        }
        // jeśli licznik się nie zresetował, nowe kroki to różnica
        else{
            amountOfNewSteps = eventValue - amountOfStepsOnLastSensorChange;
        }
        // dodaj nowe kroki
        amountOfStepsSinceRestart += amountOfNewSteps;
        amountOfStepsEver += amountOfNewSteps;
        // zapisz akutalny stan sensora
        amountOfStepsOnLastSensorChange = eventValue;
        // kroki dziennie
        amountOfStepsDaily = amountOfStepsEver - amountOfStepsAtDayStart;

        // wyświetl wartości
        daily.setText(String.valueOf(amountOfStepsDaily));
        sinceRestart.setText(String.valueOf(amountOfStepsSinceRestart));

    }

    //      Funkcja wykonywana przy zatrzymaniu aplikacji
    @Override
    protected void onPause(){
        super.onPause();

        // pobierz instancję SharedPreferences
        SharedPreferences.Editor sharedPreferencesEditor = this.getPreferences(Context.MODE_PRIVATE).edit();

        // zapisz wartości do SharedPreferences
        sharedPreferencesEditor.putInt(dailyStepAmountKeyName, amountOfStepsAtDayStart);
        sharedPreferencesEditor.putInt(atRestartStepAmountKeyName, amountOfStepsAtRestart);
        sharedPreferencesEditor.putInt(everStepAmountKeyName, amountOfStepsEver);
        sharedPreferencesEditor.putInt(dayIndexKeyName, dayOfMonth);

        // zapisz zmiany w SharedPreferences
        sharedPreferencesEditor.apply();

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //      Funkcja wykonywana przy resecie przyciskiem
    public void onButtonResetClicked(View view){
        // zrestartuj licznik aktualny
        amountOfStepsAtRestart = amountOfStepsOnLastSensorChange;
        amountOfStepsSinceRestart = 0;
        // wyświetl poprawioną wartość
        sinceRestart.setText(String.valueOf(amountOfStepsSinceRestart));


        // Wyświetl komunikat jeśli urządzenie nie posiada  sensora
        if(stepCountingSensor == null){
            Toast.makeText(this, R.string.no_sensor_prompt, Toast.LENGTH_LONG).show();
        }
    }

    //      Funkcja przechodząca do widoku Achievementów
    public void onAchievementsButtonClicked(View view){
        Intent intent = new Intent(this, AchievementsActivity.class);
        startActivity(intent);
    }

    public static int getAmountOfStepsEver() {
        return amountOfStepsEver;
    }

}
