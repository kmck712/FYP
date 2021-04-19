package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class itemAdding extends AppCompatActivity {
    private NeuralNet wardrobe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_adding);
        wardrobe = new NeuralNet();
        Intent newIntent = getIntent();

       try {
           Bundle w = newIntent.getBundleExtra("WEIGHTS");
           double[][] Weights = (double[][]) w.getSerializable("WEIGHTS");

           wardrobe = new NeuralNet(newIntent.getStringExtra("WARDROBE_NAMES"),
                   newIntent.getDoubleArrayExtra("WARDROBE_OUTFITS"),
                   Weights,
                   newIntent.getIntArrayExtra("WARDROBE_DIMENSIONS"),
                   newIntent.getStringArrayExtra("WARDROBE_IMAGE_PATH"));
            // need to change to a parcable and create a constructor which allows this to happen within aiTestMain

        }
       catch (Exception x)
       {
           System.out.println("no item passed");
       }
    }

    public void itemAdd(View view)
    {
        TextView addInfo = (TextView) findViewById(R.id.itemAddInfo);
        CharSequence inputName;
        try {
            inputName = ((TextView) findViewById(R.id.addClothesName)).getText();
            int inputType = 0;
            if (((RadioButton) findViewById(R.id.topButton)).isChecked())
            {
                inputType =1;
            }
            else if (((RadioButton) findViewById(R.id.underShirtButton)).isChecked())
            {
                inputType =2;
            }
            else if (((RadioButton) findViewById(R.id.bottomsButton)).isChecked())
            {
                inputType =3;
            }

            if (inputType == 0)
            {
                addInfo.setText("Please select the type of clothes");
            }
            else
            {
                if(currentPhotoPath != ""){

                    wardrobe.addItem(inputType, inputName, currentPhotoPath);

                    addInfo.setText(wardrobe.getName( inputType-1,wardrobe.getClassSize(inputType- 1) - 1) + " has been successfully added!");
                    ((ImageView)findViewById(R.id.itemPicLayout)).setImageBitmap(null);
                    currentPhotoPath = "";

                }
                else
                {
                    addInfo.setText("Please take a photo for the item");
                }

            }
        }
        catch (Exception E)
        {
            addInfo.setText("Please set the item's name");
        }



    }
    public void showAllItems(View view)
    {
        String outfitWardrobe = "";
        outfitWardrobe = wardrobe.getEntireWardrobe();
        ((TextView)findViewById(R.id.itemAddInfo)).setText(outfitWardrobe);
    }
    public void returnToMain(View view)
    {
        Intent intent = new Intent(this, MainActivity.class) ;
        intent.putExtra("WARDROBE_NAMES", wardrobe.getAllNames());
        intent.putExtra("WARDROBE_DIMENSIONS", wardrobe.getAllClassSize());
        intent.putExtra("WARDROBE_PASSED", true);
        intent.putExtra("WARDROBE_OUTFITS", wardrobe.getAllOutfitsId());
        intent.putExtra("WARDROBE_IMAGE_PATH",wardrobe.getAllPaths());

        Bundle weightBundle = new Bundle();
        weightBundle.putSerializable("WEIGHTS", wardrobe.getAllWeights());
        intent.putExtras( weightBundle);

        startActivity(intent);
        finish();
    }


    static final int REQUEST_IMAGE_CAPTURE = 1;

    public void dispatchTakePictureIntent(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (photoFile != null) {

                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }

        }
    }
   @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            setPic();
        }
    }


    public void setPic() {
        ImageView imageView = (ImageView)findViewById(R.id.itemPicLayout);
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
        imageView.setImageBitmap(bitmap);
        imageView.setRotation(90);
    }

    String currentPhotoPath = "";

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

}