package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.ImageFilterView;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private aiTestMain wardrobe;
    boolean test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent newIntent = getIntent();
        test = newIntent.getBooleanExtra("WARDROBE_PASSED", false);
        if ( test) {
            wardrobe = new aiTestMain(newIntent.getStringArrayExtra("WARDROBE_NAMES"),
                    newIntent.getDoubleArrayExtra("WARDROBE_WEIGHTS"),
                    newIntent.getDoubleArrayExtra("WARDROBE_DIMENSIONS"));

        }

    }

    public void screenSwitch(View view)
    {
        Intent intent = new Intent(this, itemAdding.class) ;
       startActivity(intent);
       finish();
    }


    public void drobeTest(View view)
    {

        ((TextView)findViewById(R.id.textView)).setText( "heya " + wardrobe.getEntireWardrobe());
    }
    public void randomOutfit(View view)
    {
        if (wardrobe != null)
        {
            wardrobe.running();
            ((TextView) findViewById(R.id.topResultText)).setText(wardrobe.currentBestOutfits[0].name);
            ((TextView) findViewById(R.id.underResultText)).setText(wardrobe.currentBestOutfits[1].name);
            ((TextView) findViewById(R.id.bottomResultText)).setText(wardrobe.currentBestOutfits[2].name);

        }
        else {
            ((TextView) findViewById(R.id.topResultText)).setText("Please add clothes");
        }
    }
    public void accept(View view)
    {

    }
    public void decline(View view)
    {

    }

}