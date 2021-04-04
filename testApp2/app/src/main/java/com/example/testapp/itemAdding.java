package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;


public class itemAdding extends AppCompatActivity {
    private NeuralNet wardrobe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_adding);
        wardrobe = new NeuralNet();
        Intent newIntent = getIntent();

       try {
            wardrobe = new NeuralNet(newIntent.getStringExtra("WARDROBE_NAMES"),
                    newIntent.getDoubleArrayExtra("WARDROBE_OUTFITS"),
                    newIntent.getDoubleArrayExtra("WARDROBE_WEIGHTS"),
                    newIntent.getDoubleArrayExtra("WARDROBE_OUTFITWEIGHTS"),
                    newIntent.getIntArrayExtra("WARDROBE_DIMENSIONS"));
            // need to change to a parcable and create a constructor which allows this to happen within aiTestMain

        }
       catch (Exception x)
       {
           System.out.println("no item passed");
       }
    }

    public void itemAdd(View view)
    {
        //TextView testT = (TextView) findViewById(R.id.testtext);
        CharSequence inputName =  ((TextView) findViewById(R.id.addClothesName)).getText();
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
            ((TextView) findViewById(R.id.itemAddInfo)).setText("Please select the type of clothes");
        }
        else
        {
            wardrobe.addItem(inputType, inputName);

            ((TextView) findViewById(R.id.itemAddInfo)).setText(  wardrobe.getName( inputType-1,wardrobe.getClassSize(inputType- 1) - 1) + " has been successfully added!");

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
        //intent.putExtra("WARDROBE_OBJECT", wardrobe);
        intent.putExtra("WARDROBE_NAMES", wardrobe.getAllNames());
        intent.putExtra("WARDROBE_WEIGHTS", wardrobe.getAllWeights()); // all working here
        intent.putExtra("WARDROBE_DIMENSIONS", wardrobe.getAllClassSize());
        intent.putExtra("WARDROBE_PASSED", true);
        intent.putExtra("WARDROBE_OUTFITS", wardrobe.getAllOutfitsId());
        intent.putExtra("WARDROBE_OUTFITWEIGHTS",wardrobe.getAllOutfitWeights());
        startActivity(intent);
        finish();
    }
}