package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class wardrobeActivity extends AppCompatActivity {
    private NeuralNet wardrobe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wardrobe);
        Intent newIntent = getIntent();
        //TextView clothesList = (TextView) findViewById(R.id.clothesListWar);
       // clothesList.setMovementMethod(new ScrollingMovementMethod());
        try {
            wardrobe = new NeuralNet(newIntent.getStringExtra("WARDROBE_NAMES"),
                    newIntent.getDoubleArrayExtra("WARDROBE_OUTFITS"),
                    newIntent.getDoubleArrayExtra("WARDROBE_WEIGHTS"),
                    newIntent.getDoubleArrayExtra("WARDROBE_OUTFITWEIGHTS"),
                    newIntent.getIntArrayExtra("WARDROBE_DIMENSIONS"));
            //passed = true;
            // need to change to a parcable and create a constructor which allows this to happen within aiTestMain
            displayAllClothes();
        }
        catch (Exception x)
        {
            System.out.println("No network ");
        }
    }
    public void displayAllClothes()
    {
        String [] clothes = wardrobe.getEntireWardrobe().split("'");
        LinearLayout target = (LinearLayout) findViewById(R.id.clothesListLayourWar);
        for (int i =0; i < target.getChildCount() && i < clothes.length; i ++)
        {

                TextView wardrobeItem = (TextView) target.getChildAt(i);
                wardrobeItem.setText(clothes[i]);
            int finalI = i;
            ((TextView) target.getChildAt(i)).setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        highlight((String)((TextView) target.getChildAt(finalI)).getText());
                    }
                });
        }
    }

   /* public void displayAllClothes()
    {
        String [] clothes = wardrobe.getEntireWardrobe().split("'");
        //TextView clothesWar = (TextView) findViewById(R.id.clothesListWar);
        LinearLayout target = (LinearLayout) findViewById(R.id.clothesListLayourWar);
        LinearLayout.LayoutParams nlparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, // Width Of The TextView
                LinearLayout.LayoutParams.WRAP_CONTENT);
        for (String i : clothes)
        {
            TextView newitem = new TextView(getApplicationContext());
            newitem.setLayoutParams(nlparams);
            newitem.setText(i);
            newitem.setTextColor(Color.parseColor("#000000"));
            newitem.setTextSize(20);
            newitem.setClickable(true);
            newitem.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v){
                    highlight((String) newitem.getText());
                }
            });
            target.addView(newitem);
        }
    }

    */
    public void highlight(String i)
    {
        ((TextView) findViewById(R.id.titleWard)).setText(i);
    }

    public void returnToMain(View view)
    {
        Intent intent = new Intent(this, MainActivity.class) ;
        //intent.putExtra("WARDROBE_OBJECT", wardrobe);
        try {
            intent.putExtra("WARDROBE_NAMES", wardrobe.getAllNames());
            intent.putExtra("WARDROBE_WEIGHTS", wardrobe.getAllWeights()); // all working here
            intent.putExtra("WARDROBE_DIMENSIONS", wardrobe.getAllClassSize());
            intent.putExtra("WARDROBE_PASSED", true);
            intent.putExtra("WARDROBE_OUTFITS", wardrobe.getAllOutfitsId());
            intent.putExtra("WARDROBE_OUTFITWEIGHTS", wardrobe.getAllOutfitWeights());
        }
        catch (Exception e)
        {
            System.out.println(e + "\n nothing to return");
        }
        startActivity(intent);
        finish();
    }
}