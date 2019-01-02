package com.example.footstepper.footstepper;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AchievementsListViewAdapter extends ArrayAdapter<Achievement> {

    private Achievement[] achievements;
    private Activity context;
    public AchievementsListViewAdapter(Activity context, Achievement[] achievements) {
        super(context, R.layout.achievements_layout, achievements);
        this.context = context;
        setAchievements(achievements);

    }

    public Achievement[]getAchievements() {
        return achievements;
    }

    private void setAchievements(Achievement[] achievements) {
        this.achievements = achievements;
    }

    //      Fukncja wykonywana przy pobieraniu Achievementa na konkretnej pozycji
    @NonNull
    @Override
    public View getView(int position, @Nullable View contentView, @NonNull ViewGroup parent){

        View r = contentView;
        ViewHolder viewHolder;

        if(r==null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.achievements_layout, null, true);
            viewHolder =  new ViewHolder(r);
            r.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) r.getTag();
        }
        // Określi nazwę i opis Achievementa
        viewHolder.titleTextView.setText(achievements[position].getName());
        viewHolder.descriptionTextView.setText(achievements[position].getDescription());
        // Przypisz odblokowany bądź zablokowany obrazek do Achievementa
        int imgResource;
        if(achievements[position].getMinimalTotalSteps() > MainActivity.getAmountOfStepsEver()){
            imgResource = R.drawable.locked;
        }
        else{
            imgResource = R.drawable.unlocked;
        }
        viewHolder.achievementImageView.setImageResource(imgResource);

        // Zwróć widok jednego Achievementa
        return r;
    }

    // klasa pomocnicza do przechowywania widoki pojedynczego Achievementa
    class ViewHolder{
        TextView titleTextView;
        TextView descriptionTextView;
        ImageView achievementImageView;

        ViewHolder(View v){
            titleTextView = v.findViewById(R.id.achievement_title_text_view);
            descriptionTextView = v.findViewById(R.id.achievement_description_text_view);
            achievementImageView = v.findViewById(R.id.achievement_image_view);

        }
    }
}
