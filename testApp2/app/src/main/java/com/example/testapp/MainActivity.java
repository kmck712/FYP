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
    private boolean passed = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Intent newIntent = getIntent();

        try {
            wardrobe = new NeuralNet(newIntent.getStringExtra("WARDROBE_NAMES"),
                    newIntent.getDoubleArrayExtra("WARDROBE_OUTFITS"),
                    newIntent.getDoubleArrayExtra("WARDROBE_WEIGHTS"),
                    newIntent.getDoubleArrayExtra("WARDROBE_OUTFITWEIGHTS"),
                    newIntent.getIntArrayExtra("WARDROBE_DIMENSIONS"));
            passed = true;
                // need to change to a parcable and create a constructor which allows this to happen within aiTestMain

        }
        catch (Exception x)
        {
            System.out.println("No network ");
        }

    }

    public void screenSwitch(View view)
    {
        Intent intent = new Intent(this, itemAdding.class) ;
        if (passed == true)
        {
            intent.putExtra("WARDROBE_NAMES", wardrobe.getAllNames());
            intent.putExtra("WARDROBE_WEIGHTS", wardrobe.getAllWeights()); // all working here
            intent.putExtra("WARDROBE_DIMENSIONS", wardrobe.getAllClassSize());
            intent.putExtra("WARDROBE_PASSED", true);
            intent.putExtra("WARDROBE_OUTFITS", wardrobe.getAllOutfitsId());
            intent.putExtra("WARDROBE_OUTFITWEIGHTS", wardrobe.getAllOutfitWeights());
        }
       /* }
        catch (Exception x)
        {
            System.out.println("Nothing to Send");
        }

        */
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

       String output = wardrobe.getAllNames();
       ((TextView)findViewById(R.id.textView)).setText( outTest + "\n" + output );
    }

    public void randomOutfit(View view)
    {

            wardrobe.running();
            ((TextView) findViewById(R.id.topResultText)).setText(wardrobe.currentBestOutfits[0].getName());
            ((TextView) findViewById(R.id.underResultText)).setText(wardrobe.currentBestOutfits[1].getName());
            ((TextView) findViewById(R.id.bottomResultText)).setText(wardrobe.currentBestOutfits[2].getName());
            ((TextView)findViewById(R.id.textView)).setText("" +wardrobe.currentResult);
        try{}
        catch (Exception X)
        {
            ((TextView) findViewById(R.id.topResultText)).setText("Please add clothes: " + X);
        }
    }
    public void accept(View view)
    {
        try{
            wardrobe.outcomeChange(1);
            ((TextView) findViewById(R.id.topResultText)).setText("");
            ((TextView) findViewById(R.id.underResultText)).setText("");
            ((TextView) findViewById(R.id.bottomResultText)).setText("");
        }
        catch (Exception x)
        {
            System.out.println("No Outfit Randomised");
        }

    }
    public void decline(View view) {
        try{
            wardrobe.outcomeChange(0);
            ((TextView) findViewById(R.id.topResultText)).setText("");
            ((TextView) findViewById(R.id.underResultText)).setText("");
            ((TextView) findViewById(R.id.bottomResultText)).setText("");
        }
        catch (Exception x)
        {
            System.out.println("No Outfit Randomised");
        }

    }

}