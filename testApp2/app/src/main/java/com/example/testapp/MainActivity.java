package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
            Bundle w = newIntent.getExtras();
            double[][] Weights = (double[][]) w.getSerializable("WEIGHTS");

            wardrobe = new NeuralNet(newIntent.getStringExtra("WARDROBE_NAMES"),
                    newIntent.getDoubleArrayExtra("WARDROBE_OUTFITS"),
                    Weights,
                    newIntent.getIntArrayExtra("WARDROBE_DIMENSIONS"),
                    newIntent.getStringArrayExtra("WARDROBE_IMAGE_PATH"));
            passed = true;
                // need to change to a parcable and create a constructor which allows this to happen within aiTestMain


        }
        catch (Exception x)
        {
            System.out.println("No network " + x);
            passed = true;
            wardrobe = new NeuralNet();
            testWardrobe();

        }


    }


    public void screenSwitch(Class i )
    {
        Intent intent = new Intent(this, i) ;
       // if (passed == true)
        try {
            intent.putExtra("WARDROBE_NAMES", wardrobe.getAllNames());
            intent.putExtra("WARDROBE_DIMENSIONS", wardrobe.getAllClassSize());
            intent.putExtra("WARDROBE_PASSED", true);
            intent.putExtra("WARDROBE_OUTFITS", wardrobe.getAllOutfitsId());
            intent.putExtra("WARDROBE_IMAGE_PATH",wardrobe.getAllPaths());
            Bundle weightBundle = new Bundle();
            weightBundle.putSerializable("WEIGHTS", wardrobe.getAllWeights());
            intent.putExtras( weightBundle);
        }
        catch (Exception x)
        {
            System.out.println("Nothing to Send");
        }
        startActivity(intent);
        finish();
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

    private void testWardrobe ()
    {

       // wardrobe.addItem(1,"Plaid Shirt", "");//2
        /*dave
        wardrobe.addItem(1,"Hoodie", ""); //1
        wardrobe.addItem(1,"Black Jacket", "");//3
        wardrobe.addItem(2,"band shirt1", "");//1
        wardrobe.addItem(2,"band shirt2", "");//2
        wardrobe.addItem(2,"band shirt3", "");//3
        wardrobe.addItem(3,"Black jeans", "");//2
        wardrobe.addItem(3,"Grey jeans", "");//3

         */
        ///*
        //Julia

        wardrobe.addItem(1,"suit jacket", ""); //1
        wardrobe.addItem(1,"hooide", "");//3
        wardrobe.addItem(1,"coat", ""); //1
        wardrobe.addItem(1,"jumper", "");//3

        wardrobe.addItem(2,"t-shirt", "");//1
        wardrobe.addItem(2,"smart shirt", "");//2
        wardrobe.addItem(2,"long sleeve", "");//3
        wardrobe.addItem(2,"vest", "");//3
        wardrobe.addItem(2,"polo", "");//9
        wardrobe.addItem(2,"grey band shirt", "");//7

        wardrobe.addItem(3,"smart trousers", "");//3
        wardrobe.addItem(3,"trousers", "");//2
        wardrobe.addItem(3,"shorts", "");//3
    }
    public void randomOutfit(View view)
    {
       /* //used to for testing
           for (int i = 0; i < 5; i++)
            {
                wardrobe = new NeuralNet();
                testWardrobe();
                wardrobe.running();
            }

        */


       // try {
            wardrobe.running();
            ((TextView) findViewById(R.id.topResultText)).setText(wardrobe.currentBestOutfits[0].getName());
            ((TextView) findViewById(R.id.underResultText)).setText(wardrobe.currentBestOutfits[1].getName());
            ((TextView) findViewById(R.id.bottomResultText)).setText(wardrobe.currentBestOutfits[2].getName());
         //   try {

                ((ImageView) findViewById(R.id.topImage)).setImageBitmap(setPic(wardrobe.getPath(wardrobe.currentBestOutfits[0]), ((ImageView) findViewById(R.id.topImage))));
                ((ImageView) findViewById(R.id.underTopImage)).setImageBitmap(setPic(wardrobe.getPath(wardrobe.currentBestOutfits[1]), ((ImageView) findViewById(R.id.underTopImage))));
                ((ImageView) findViewById(R.id.bottomImage)).setImageBitmap(setPic(wardrobe.getPath(wardrobe.currentBestOutfits[2]), ((ImageView) findViewById(R.id.bottomImage))));
                ((TextView) findViewById(R.id.mainTitle)).setText(R.string.Title);
           /* } catch (Exception e) {

            }
        }

        catch (Exception X)
        {
            ((TextView) findViewById(R.id.mainTitle)).setText("Please add clothes: ");
        }

            */

    }
    public void accept(View view)
    {
        try{
            wardrobe.outcomeChange(1);
            clearRandomiser();
         }
        catch (Exception x)
        {
            System.out.println("No Outfit Randomised");
        }
    }

    public void decline(View view) {
        try{
            wardrobe.outcomeChange(0);
            clearRandomiser();
        }
        catch (Exception x)
        {
            System.out.println("No Outfit Randomised");
        }

    }

    private void clearRandomiser()
    {
        ((TextView) findViewById(R.id.topResultText)).setText("");
        ((TextView) findViewById(R.id.underResultText)).setText("");
        ((TextView) findViewById(R.id.bottomResultText)).setText("");

        ((ImageView) findViewById(R.id.topImage)).setImageBitmap(null);
        ((ImageView) findViewById(R.id.underTopImage)).setImageBitmap(null);
        ((ImageView) findViewById(R.id.bottomImage)).setImageBitmap(null);
    }



}