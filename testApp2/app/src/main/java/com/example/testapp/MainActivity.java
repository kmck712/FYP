package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {


    private NeuralNet wardrobe;
    private boolean passed = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Intent newIntent = getIntent();

        //Sets up the on click listeners for both of the menu buttons.
        ((Button)findViewById(R.id.wardrobeButton)).setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                screenSwitch(wardrobeActivity.class);
            }
        });
        ((Button)findViewById(R.id.screenSwap)).setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                screenSwitch(itemAdding.class);
            }
        });

        try {
            wardrobe = new NeuralNet(newIntent.getStringExtra("WARDROBE_NAMES"),
                    newIntent.getDoubleArrayExtra("WARDROBE_OUTFITS"),
                    newIntent.getDoubleArrayExtra("WARDROBE_WEIGHTS"),
                    newIntent.getDoubleArrayExtra("WARDROBE_OUTFITWEIGHTS"),
                    newIntent.getIntArrayExtra("WARDROBE_DIMENSIONS"),
                    newIntent.getStringArrayExtra("WARDROBE_IMAGE_PATH"));
            passed = true;
                // need to change to a parcable and create a constructor which allows this to happen within aiTestMain


        }
        catch (Exception x)
        {
            System.out.println("No network ");
        }

    }


    public void screenSwitch(Class i )
    {
        Intent intent = new Intent(this, i) ;
        if (passed == true)
        {
            intent.putExtra("WARDROBE_NAMES", wardrobe.getAllNames());
            intent.putExtra("WARDROBE_WEIGHTS", wardrobe.getAllWeights()); // all working here
            intent.putExtra("WARDROBE_DIMENSIONS", wardrobe.getAllClassSize());
            intent.putExtra("WARDROBE_PASSED", true);
            intent.putExtra("WARDROBE_OUTFITS", wardrobe.getAllOutfitsId());
            intent.putExtra("WARDROBE_OUTFITWEIGHTS", wardrobe.getAllOutfitWeights());
            intent.putExtra("WARDROBE_IMAGE_PATH",wardrobe.getAllPaths());
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
       //((TextView)findViewById(R.id.textView)).setText( outTest + "\n" + output );
    }
    public Bitmap setPic(String currentPhotoPath, ImageView imageView) {

        // Get the dimensions of the View
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.max(1, Math.min(photoW/targetW, photoH/targetH));

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        return bitmap;
    }

    public void randomOutfit(View view)
    {

            wardrobe.running();
            ((TextView) findViewById(R.id.topResultText)).setText(wardrobe.currentBestOutfits[0].getName());
            ((TextView) findViewById(R.id.underResultText)).setText(wardrobe.currentBestOutfits[1].getName());
            ((TextView) findViewById(R.id.bottomResultText)).setText(wardrobe.currentBestOutfits[2].getName());
        ((ImageView) findViewById(R.id.topImage)).setImageBitmap(setPic(wardrobe.getPath(wardrobe.currentBestOutfits[0]),((ImageView) findViewById(R.id.topImage))));
        ((ImageView) findViewById(R.id.underTopImage)).setImageBitmap(setPic(wardrobe.getPath(wardrobe.currentBestOutfits[1]),((ImageView) findViewById(R.id.topImage))));
        ((ImageView) findViewById(R.id.bottomImage)).setImageBitmap(setPic(wardrobe.getPath(wardrobe.currentBestOutfits[2]),((ImageView) findViewById(R.id.topImage))));
           // ((TextView)findViewById(R.id.textView)).setText("" +wardrobe.currentResult);
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