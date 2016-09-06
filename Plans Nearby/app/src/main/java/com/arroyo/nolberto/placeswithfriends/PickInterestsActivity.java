package com.arroyo.nolberto.placeswithfriends;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.arroyo.nolberto.placeswithfriends.Activities.MainActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class PickInterestsActivity extends AppCompatActivity implements View.OnClickListener {
    ArrayList<String> userInterests;
    private TextView music, businessProfessional, foodDrink, communityCulture, performingVisualArts, filmMediaEntertainment, other, sportsFitness,
            healthWellness, scienceTech, travelOutdoor, charityCauses, religionSpirituality,
            familyEducation, seasonalHoliday, govPolitics, fashionBeauty, autoAirBoat, homeLifestyle, hobbiesSpecialInterests;
    private Button finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_interests);
        setViews();
        userInterests = new ArrayList<>();
        music.setOnClickListener(this);
        businessProfessional.setOnClickListener(this);
        foodDrink.setOnClickListener(this);
        communityCulture.setOnClickListener(this);
        performingVisualArts.setOnClickListener(this);
        filmMediaEntertainment.setOnClickListener(this);
        other.setOnClickListener(this);
        sportsFitness.setOnClickListener(this);
        healthWellness.setOnClickListener(this);
        scienceTech.setOnClickListener(this);
        travelOutdoor.setOnClickListener(this);
        charityCauses.setOnClickListener(this);
        religionSpirituality.setOnClickListener(this);
        familyEducation.setOnClickListener(this);
        seasonalHoliday.setOnClickListener(this);
        govPolitics.setOnClickListener(this);
        fashionBeauty.setOnClickListener(this);
        autoAirBoat.setOnClickListener(this);
        homeLifestyle.setOnClickListener(this);
        hobbiesSpecialInterests.setOnClickListener(this);

        String [] interests = userInterests.toArray(new String[userInterests.size()]);
        saveArray(interests,"interests",this);
        finish.setOnClickListener(this);



    }

    public void setViews() {
        music = (TextView) findViewById(R.id.music);
        businessProfessional = (TextView) findViewById(R.id.business_professional);
        foodDrink = (TextView) findViewById(R.id.food_drink);
        communityCulture = (TextView) findViewById(R.id.community_culture);
        performingVisualArts = (TextView) findViewById(R.id.performing_visual_arts);
        filmMediaEntertainment = (TextView) findViewById(R.id.film_media_entertainment);
        other = (TextView) findViewById(R.id.other);
        sportsFitness = (TextView) findViewById(R.id.sports_fitness);
        healthWellness = (TextView) findViewById(R.id.health_wellness);
        scienceTech = (TextView) findViewById(R.id.science_tech);
        travelOutdoor = (TextView) findViewById(R.id.travel_outdoor);
        charityCauses = (TextView) findViewById(R.id.charity_causes);
        religionSpirituality = (TextView) findViewById(R.id.religion_spirituality);
        familyEducation = (TextView) findViewById(R.id.family_education);
        seasonalHoliday = (TextView) findViewById(R.id.seasonal_holiday);
        govPolitics = (TextView) findViewById(R.id.gov_politics);
        fashionBeauty = (TextView) findViewById(R.id.fashion_beauty);
        autoAirBoat = (TextView) findViewById(R.id.auto_air_boat);
        homeLifestyle = (TextView) findViewById(R.id.home_lifestyle);
        hobbiesSpecialInterests = (TextView) findViewById(R.id.hobbies_special_interests);
        finish = (Button)findViewById(R.id.finishButton);


    }

    @Override
    public void onClick(View view) {
        String categoryId;
        switch (view.getId()){
            case R.id.music:
                categoryId = "103";
                userInterests.add(categoryId);
                music.setBackgroundColor(getResources().getColor(R.color.green));
                break;
            case R.id.business_professional:
                categoryId = "101";
                userInterests.add(categoryId);
                break;
            case R.id.food_drink:
                categoryId = "110";
                userInterests.add(categoryId);
                break;
            case R.id.community_culture:
                categoryId = "113";
                userInterests.add(categoryId);
                break;
            case R.id.performing_visual_arts:
                categoryId = "105";
                userInterests.add(categoryId);
                break;
            case R.id.film_media_entertainment:
                categoryId = "104";
                userInterests.add(categoryId);
                break;
            case R.id.sports_fitness:
                categoryId = "108";
                userInterests.add(categoryId);
                break;
            case R.id.health_wellness:
                categoryId = "107";
                userInterests.add(categoryId);
                break;
            case R.id.science_tech:
                categoryId = "102";
                userInterests.add(categoryId);
                break;
            case R.id.travel_outdoor:
                categoryId = "109";
                userInterests.add(categoryId);
                break;
            case R.id.charity_causes:
                categoryId = "111";
                userInterests.add(categoryId);
                break;
            case R.id.religion_spirituality:
                categoryId = "114";
                userInterests.add(categoryId);
                break;
            case R.id.family_education:
                categoryId = "115";
                userInterests.add(categoryId);
                break;
            case R.id.seasonal_holiday:
                categoryId = "116";
                userInterests.add(categoryId);
                break;
            case R.id.gov_politics:
                categoryId = "112";
                userInterests.add(categoryId);
                break;
            case R.id.fashion_beauty:
                categoryId = "106";
                userInterests.add(categoryId);
                break;
            case R.id.home_lifestyle:
                categoryId = "107";
                userInterests.add(categoryId);
                break;
            case R.id.auto_air_boat:
                categoryId = "118";
                userInterests.add(categoryId);
                break;
            case R.id.hobbies_special_interests:
                categoryId = "119";
                userInterests.add(categoryId);
                break;
            case R.id.other:
                categoryId = "199";
                userInterests.add(categoryId);
                break;
            case R.id.finishButton:
                String [] interests = userInterests.toArray(new String[userInterests.size()]);
                saveArray(interests,"interests",this);
                Intent intent = new Intent(PickInterestsActivity.this, MainActivity.class);
                startActivity(intent);
                break;





        }

    }
    public boolean saveArray(String[] array, String arrayName, Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("preferencename", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(arrayName +"_size", array.length);
        for(int i=0;i<array.length;i++)
            editor.putString(arrayName + "_" + i, array[i]);
        return editor.commit();
    }
}
