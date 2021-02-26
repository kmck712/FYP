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


    private NeuralNet wardrobe;
    boolean test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Intent newIntent = getIntent();
        test = newIntent.getBooleanExtra("WARDROBE_PASSED", false);
        if ( test) {
            wardrobe = new NeuralNet(newIntent.getStringExtra("WARDROBE_NAMES"),
                    newIntent.getDoubleArrayExtra("WARDROBE_WEIGHTS"),
                    newIntent.getIntArrayExtra("WARDROBE_DIMENSIONS"));
                // need to change to a parcable and create a constructor which allows this to happen within aiTestMain

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
        // found the issue it's miss classifiy the weights being added by having class 1 as class 0
       String outTest = "";
       for (double i: wardrobe.getAllWeights())
       {
           outTest += "weight " + i + "\n";
       }

       // String output = wardrobe.getAllNames();
       //((TextView)findViewById(R.id.textView)).setText( output );
    }
    public void randomOutfit(View view)
    {
        if (wardrobe != null)
        {
            wardrobe.running();
            ((TextView) findViewById(R.id.topResultText)).setText(wardrobe.currentBestOutfits[0].getName());
            ((TextView) findViewById(R.id.underResultText)).setText(wardrobe.currentBestOutfits[1].getName());
            ((TextView) findViewById(R.id.bottomResultText)).setText(wardrobe.currentBestOutfits[2].getName());
            ((TextView)findViewById(R.id.textView)).setText("" +wardrobe.currentResult);
        }
        else {
            ((TextView) findViewById(R.id.topResultText)).setText("Please add clothes");
        }
    }
    public void accept(View view)
    {
        wardrobe.outcomeChange(1);
        ((TextView) findViewById(R.id.topResultText)).setText("");
        ((TextView) findViewById(R.id.underResultText)).setText("");
        ((TextView) findViewById(R.id.bottomResultText)).setText("");
    }
    public void decline(View view) {
       wardrobe.outcomeChange(0);
       ((TextView) findViewById(R.id.topResultText)).setText("");
       ((TextView) findViewById(R.id.underResultText)).setText("");
       ((TextView) findViewById(R.id.bottomResultText)).setText("");
    }

}