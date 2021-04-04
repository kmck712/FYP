/**
 * 
 */
package com.example.testapp;

/**
 * @author jmcke
 *
 */
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays; 
import java.util.Scanner;
import java.lang.Math;   
import java.util.Random;   

public class aiTestMain  {

	public Clothes[] currentBestOutfits; // make private and created getters and setters
	private static ArrayList <Outfit> Outfits;
	private static ArrayList<Clothes>[] wardrobe;
	private static ArrayList<Node>nodeLayer; // node layer contains [0] taste node and [1] the synergy node
	private double currentResult;
	public aiTestMain()
	{
		//initalises both the nodelayer and the wardrobe
		nodeLayer = new ArrayList<>();
		wardrobe =  new ArrayList[3];
		//initialises the wardrobe arraylist by creating a new array list object for each of the type of objects
		for (int i = 0; i < wardrobe.length; i ++)
		{
			wardrobe[i] = new ArrayList<Clothes>();
		}
		nodeLayer.add(new Node());
		//nodeLayer.add(new Node());
	}

	public aiTestMain(String[] names, double[] Weights, double[] Dimensions)
	{
		nodeLayer = new ArrayList<Node>();
		wardrobe =  new ArrayList[3];
		//initialises the wardrobe arraylist by creating a new array list object for each of the type of objects

		for (int i = 0; i < 3; i ++)
		{
			wardrobe[i] = new ArrayList<Clothes>();
		}
		int k = 0;
		for(int i = 0; i < 3; i ++)
		{
			for (int j =0 ; j <Dimensions[i]; j ++)
			{
				wardrobe[i].add(new Clothes(names[k], j + 1, wardrobe[j].size()));
				k ++;
			}
		}
		nodeLayer.add(new Node(Weights, Dimensions));
		nodeLayer.add(new Node());

	}
	public void addItem(int type, CharSequence name2)
	{
		String name = name2.toString();
		switch (type) {
			case 1:
				wardrobe[0].add(new Clothes(name, 1,wardrobe[0].size()));
				nodeLayer.get(0).addNode(type);
				break;
			case 2:
				wardrobe[1].add(new Clothes(name, 2,wardrobe[1].size()));
				nodeLayer.get(0).addNode(type);
				break;
			case 3:
				wardrobe[2].add(new Clothes(name, 3,wardrobe[2].size()));
				nodeLayer.get(0).addNode(type);
				break;
		}
	}






	private static double execution(Clothes Outfit[])
	{
		ArrayList <Double> inputs = new ArrayList<Double>();
		inputs = calcInputs(Outfit);
		//System.out.println(inputs);
		double output = nodeLayer.get(0).calculateInputs(inputs);
		nodeLayer.get(0).setFinalOutput(output);
		//System.out.println(output);
		
		
		
		return output;
	}
	
	private static double executionOutfits(Outfit outfits)
	{
		ArrayList <Double> inputs = new ArrayList<Double>();
		inputs = calcInputsForOutfits(outfits);
		//System.out.println(inputs);
		double output = nodeLayer.get(1).calculateInputs(inputs);
		nodeLayer.get(1).setFinalOutput(output);
		//System.out.println(output);
		
		
		
		return output;
	}
	
	private static ArrayList<Double> calcInputs(Clothes Outfit[])
	{
		ArrayList <Double> inputs = new ArrayList<Double>();
		for (int i = 0; i < numClothes(); i ++)
		{
			inputs.add(0.0);
		}
		for (Clothes i : Outfit)
		{
			if(i.getType() == 1)
			{
				inputs.set(i.getId(), 1.0);
			}
			else if(i.getType() == 2)
			{
				inputs.set(i.getId() + wardrobe[0].size(), 1.0);
			}
			else if(i.getType() == 3)
			{
				inputs.set(i.getId()+wardrobe[0].size() + wardrobe[1].size(), 1.0);
			}
		}
		return inputs;
		
	}
	
	private static ArrayList<Double> calcInputsForOutfits(Outfit outfits)
	{
		ArrayList <Double> inputs = new ArrayList<Double>();
		for (int i = 0; i < numOutfits(); i ++)
		{
			inputs.add(0.0);
		}
		Clothes[] currentOut = outfits.getIndavidual();
		double index =0;
		index  += currentOut[0].id + currentOut[1].id * 3 + currentOut[2].id * 9; 
		System.out.println("chosen outfit index: " + index );
		
		inputs.set(1, index);
		
		return inputs;
		
	}
	
	private static int numClothes()
	{
		int allInputs = 0;
		for (int i = 0; i < wardrobe.length; i ++)
		{
			allInputs += wardrobe[i].size();
		}
		return allInputs;
	}
	
	private static int numOutfits()
	{
		int clothesNum = wardrobe[0].size()*wardrobe[1].size()*wardrobe[2].size();
	
		return (clothesNum);
	}
	
	
	
	private static int[] numOutfits2()
	{
		int clothesNum[] = {wardrobe[0].size(),wardrobe[1].size(),wardrobe[2].size()};
	
		return (clothesNum);
	}
	
	
	private static boolean dupValid(Clothes[] subject, ArrayList<Outfit> compareOutfit)
	//goal is ensure there are no duplicate outfits. this is done by checking if the current putfit 'subject' is in the current array'outfit'
	{
		boolean valid = true;
		if(compareOutfit.size() == 0)
		{
			return valid;
		}
		else
		{
			for(int i = 0; i < compareOutfit.size(); i ++)
			{
				int cnt = 0;
				for (Clothes j :compareOutfit.get(i).getIndavidual())
				{
					if (j == subject[0]|| j == subject[1] || j == subject[2])
					{
						cnt ++;
					}
				}
				if (cnt == 3)
				{
					valid = false;
					return valid;
				}
			}
		}
		return valid;
	}

	private static Clothes[] outfitProb()
	{
		ArrayList <Outfit> outfit= new ArrayList<Outfit>();
		int clothesNum[] = numOutfits2();
		double highest[] = {0,0};
		int cnt = 0;
			for (int i = 0; i < clothesNum[0];i ++)
			{
				for (int j = 0; j < clothesNum[1];j ++ )
				{
					for (int k = 0; k < clothesNum[2]; k ++)
					{
						Clothes sending[]= {wardrobe[0].get(i),wardrobe[1].get(j),wardrobe[2].get(k)};
						boolean valid = dupValid(sending, outfit);
						if (valid == true)
						{
							double result = execution(sending);
							//System.out.println("attempt: " + result + "  Highest:" + highest[0]);
							if (result > highest[0])
							{
								highest[0] = result;
								highest[1] = cnt;
							}
							outfit.add(new Outfit(sending,0));
							cnt ++;
						}
						else 
						{
							
						}
					}
				}
			}
		return outfit.get((int) highest[1]).getIndavidual();
	}

	
	private static void changeAllWeights(double error, Clothes[] outfit)
	{
		double momentum = 0.2;
		double learningRate = 0.2;
		ArrayList <Double> inputs = calcInputs(outfit);
		nodeLayer.get(0).setDelta(error);
		nodeLayer.get(0).changeAllWeights(learningRate, inputs, momentum);
	}
	
	private static void changeAllWeightsForOutfits(double error, Outfit outfits)
	{
		double momentum = 0.2;
		double learningRate = 0.2;
		ArrayList <Double> inputs = calcInputsForOutfits(outfits);
		nodeLayer.get(1).setDelta(error);
		nodeLayer.get(1).changeAllWeights(learningRate, inputs, momentum);
	}
	
	public void running()
	{
		nodeLayer.get(0).clearW(); // this is a temp method to facilitate the current use of the passing and is not even close to being the ideal.
		nodeLayer.get(0).formatWeights();
		Clothes[] bestOutfit = outfitProb();
		currentResult = execution(bestOutfit);
		//Outfit bestOutfits = new Outfit(bestOutfit);
		//double synergyResult = executionOutfits(bestOutfits);
		currentBestOutfits = bestOutfit;
	}

	public void outcomeChange(int YorN)
	{
		 changeAllWeights(YorN-currentResult,currentBestOutfits);
		// changeAllWeightsForOutfits(YorN-currentResult,currentBestOutfits);
	}



	public String getName(int position, int type)
	{
		return wardrobe[type].get(position).getName();
	}

	public String[] getAllNames()
	{
		String allNames ="";
		for (int i = 0; i < wardrobe.length; i ++ ) {
			for (int j = 0; j < wardrobe[i].size(); j++) {
				allNames += getName(j, i) + ",";
			}
		}
		String items[] = allNames.split(",");

		return items;
	}

	public double[] getAllWeights()
	{
		return  nodeLayer.get(0).getAllWeights();
	}
	public ArrayList[] getAllWeightsSending()
	{
		return  nodeLayer.get(0).getW() ;
	}
	public double[] getDimensions()
	{
		double[]  dimentions = new double[wardrobe.length];
		for (int i = 0; i < wardrobe.length; i ++ ){
				dimentions[i] = wardrobe[i].size();
		}
		return dimentions;
	}
	public String getEntireWardrobe()
	{
		String output = "";
		for(int j = 0 ;j < 3; j ++)
		{
			for(Clothes i : wardrobe[j])
			{
				output += i.getName()+ "\n";
			}
		}

		return (output);
	}

	public int getClassSize(int type)
	{
		return wardrobe[type].size() -1  ;
	}




}
 
