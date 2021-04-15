package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class wardrobeActivity extends AppCompatActivity {
    protected NeuralNet wardrobe;
    private int currentSelected;
    private int currentSelectedType;
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
                    newIntent.getIntArrayExtra("WARDROBE_DIMENSIONS"),
                    newIntent.getStringArrayExtra("WARDROBE_IMAGE_PATH"));
            //passed = true;
            // need to change to a parcable and create a constructor which allows this to happen within aiTestMain


            spinnerSetup();
            currentSelected = -1;
        }
        catch (Exception x)
        {
            System.out.println("No network ");
        }
    }
    public void displayAllClothes()
    {
        String [] clothes = wardrobe.getAllNames().split(",");
        LinearLayout target = (LinearLayout) findViewById(R.id.clothesListLayourWar);
        for (int i =0; i < target.getChildCount() && i < clothes.length; i ++)
        {
                TextView wardrobeItem = (TextView) target.getChildAt(i);
                wardrobeItem.setText(clothes[i]);
                wardrobeItem.setBackgroundColor(Color.TRANSPARENT);
                int finalI = i;
                wardrobeItem.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        highlight(finalI);
                    }
                });
        }
    }
    public void displayClothes(int type )
    {
        String [] clothes = wardrobe.getAllNamesOfType(type).split(",");
        LinearLayout target = (LinearLayout) findViewById(R.id.clothesListLayourWar);
        for (int i =0; i < target.getChildCount(); i ++)
        {
            if (i < clothes.length) {
                TextView wardrobeItem = (TextView) target.getChildAt(i);
                wardrobeItem.setText(clothes[i]);
            }
            else
            {
                TextView wardrobeItem = (TextView) target.getChildAt(i);
                wardrobeItem.setText("");
            }
        }
    }
    public void showItemType(int i)
    {
        displayClothes(i);
    }

    private void spinnerSetup()
    {
        Spinner optionSpinner = (Spinner) findViewById(R.id.clotheOptionsSpinner);
        String[] types = new String[]{"All", "Top", "UnderTop", "Bottom"};
        ArrayAdapter<String> cTypes = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, types);
        optionSpinner.setAdapter(cTypes);
        optionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {

                    case 1: // for item 1
                        showItemType(0);
                        currentSelectedType = 0;
                        break;

                    case 2:
                        showItemType(1);
                        currentSelectedType = 1;
                        break;
                    case 3:
                        showItemType(2);
                        currentSelectedType = 2;
                        break;

                    default:
                        displayAllClothes();
                        currentSelectedType = -1;
                        //need to find a way to define the type of a slected item when in all items
                        //NEEEEEEEEEEEEEEEEEEEEEEEED TO FIIIIIIIIIIIIIIIIIIIIIIIIIIX
                }
               // currentSelected = -1;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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


    public void highlight(int id)
    {
        TextView wardrobeItem = (TextView) ((LinearLayout) findViewById(R.id.clothesListLayourWar)).getChildAt(id);
        //((TextView) findViewById(R.id.titleWard)).setText(wardrobeItem.getText());

        if (((ColorDrawable) wardrobeItem.getBackground()).getColor() == Color.RED)
        {
            wardrobeItem.setBackgroundColor(Color.TRANSPARENT);
            currentSelected = -1;
        }
        else
        {
            wardrobeItem.setBackgroundColor(Color.RED);
            if (currentSelected != -1)
            {
                ((TextView) ((LinearLayout) findViewById(R.id.clothesListLayourWar)).getChildAt(currentSelected)).setBackgroundColor(Color.TRANSPARENT);
                currentSelected = id;
            }
        }

        currentSelected = id;
    }

    public void editSettings(View view)
    {
     /*  ((LinearLayout) findViewById(R.id.clothesListLayourWar)).setVisibility(view.GONE);
        ((Spinner) findViewById(R.id.clotheOptionsSpinner)).setVisibility(view.GONE);
        String infoText = wardrobe.getItemInfo(currentSelected, currentSelectedType);
        ((TextView) findViewById(R.id.ItemName)).setText(infoText);
        ((LinearLayout) findViewById(R.id.EditItemWar)).setVisibility(view.VISIBLE);
    */

        try {
            ((LinearLayout) findViewById(R.id.clothesListLayourWar)).setVisibility(view.GONE);
            ((Spinner) findViewById(R.id.clotheOptionsSpinner)).setVisibility(view.GONE);
            String infoText = wardrobe.getItemInfo(currentSelected, currentSelectedType);
            ((TextView) findViewById(R.id.ItemName)).setText(infoText);
            ((LinearLayout) findViewById(R.id.EditItemWar)).setVisibility(view.VISIBLE);
            ((Button) findViewById(R.id.editButton)).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    cancel(view);
                }
            });
        }
        catch (Exception e)
        {
            System.out.println("No item selected Error:"+e );
        }
    }


    public void changeName(View view)
    {
        CharSequence name =  ((TextView)findViewById(R.id.editTextTextPersonName)).getText();
        if (name != "")
        {
            wardrobe.setName(currentSelectedType,currentSelected, name);
            String infoText = wardrobe.getItemInfo(currentSelected, currentSelectedType);
            ((TextView) findViewById(R.id.ItemName)).setText(infoText);
        }
    }
    public void changeType(View view)
    {
        int newType = ((Spinner)findViewById(R.id.TypeSpiner)).getSelectedItemPosition() ;
        wardrobe.setType(currentSelectedType,currentSelected, newType);


    }
    public void delete(View view)
    {
        wardrobe.removeAnItem(currentSelectedType,currentSelected);
        cancel(view);
    }

    public void cancel(View view)
    {
        try {
            displayAllClothes();
            ((LinearLayout) findViewById(R.id.clothesListLayourWar)).setVisibility(view.VISIBLE);
            ((Spinner) findViewById(R.id.clotheOptionsSpinner)).setVisibility(view.VISIBLE);
            ((Spinner) findViewById(R.id.clotheOptionsSpinner)).setSelection(0);
            ((LinearLayout) findViewById(R.id.EditItemWar)).setVisibility(view.GONE);
            ((Button) findViewById(R.id.editButton)).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    editSettings(view);
                }
            });
        }
        catch (Exception e)
        {
            System.out.println("No item selected Error:"+e );
        }
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
            intent.putExtra("WARDROBE_IMAGE_PATH",wardrobe.getAllPaths());
        }
        catch (Exception e)
        {
            System.out.println(e + "\n nothing to return Error:"+e);
        }
        startActivity(intent);
        finish();
    }
}