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

public class NeuralNet  {
	public Outfit currentAllBest;
	public Clothes[] currentBestOutfits; // make private and created getters and setters
	public double currentResult;
	private static ArrayList <Outfit> Outfits;
	private static ArrayList<Clothes>[] wardrobe;
	private static Node2 nodeLayer; // node layer contains [0] taste node and [1] the synergy node
	private static Node2 outfitLayer;

	//----------------------------------------------------------------------------------------------
	//Inital constructor for the Neural network
	public NeuralNet() {
		nodeLayer = new Node2(3);
		outfitLayer = new Node2(1);

		Outfits = new ArrayList<>();
		wardrobe = new ArrayList[3];
		for (int i = 0; i < wardrobe.length; i++) {
			wardrobe[i] = new ArrayList<>();
		}


	}
	//---------------------------------------------------------------------------------------------
	//this is used for when passing the information back in from different activities
	public NeuralNet(String passNames, double [] Weights, int[] Dimensions)
	{
		String names[] = passNames.split(",");
		nodeLayer = new Node2(3,Weights[0]);
		outfitLayer = new Node2(1);
		wardrobe = new ArrayList[3];

		for (int i = 0; i < wardrobe.length; i++) {
			wardrobe[i] = new ArrayList<Clothes>();
		}

		//not a fan of this method. must be a better way
		int count = 0;

		for (int i = 0; i < Dimensions.length; i ++)
		{
			for (int j = 0; j < Dimensions[i]; j ++)
			{
				//addItem(i + 1,names[count]);
				wardrobe[i].add(new Clothes(names[count],i +1,wardrobe[i].size()));
				nodeLayer.addPassedNode(i,Weights[count +1], 0);

				count++;
			}
		}


	}
	//---------------------------------------------------------------------------------------------

	private static boolean dupValid(Outfit subject, ArrayList<Outfit> compareOutfit)
	//goal is ensure there are no duplicate outfits. this is done by checking if the current putfit 'subject' is in the current array'outfit'
	{
		if(compareOutfit.size() == 0)
		{
			return true;
		}
		else
		{
			for(int i = 0; i < compareOutfit.size(); i ++)
			{
				int cnt = 0;
				for (Clothes j :compareOutfit.get(i).getIndavidual())
				{
					if (j == subject.getIndavidualItem(0)|| j == subject.getIndavidualItem(1)|| j == subject.getIndavidualItem(2))
					{
						cnt ++;
					}
				}
				if (cnt == 3)
				{
					return false;
				}
			}
		}
		return true;
	}

	private static ArrayList<Double> calcInputs(Clothes Outfit[])
	{
		ArrayList <Double>inputs = new ArrayList();
		//for(ArrayList i : inputs){i = new ArrayList<Double>();}

		//System.out.println("item id's1 " + Outfit[0].getId());

		//System.out.println("item id's1 " + Outfit[1].getId());

		//System.out.println("item id's1 " + Outfit[2].getId());
		int cnt = 0;
		for (ArrayList<Clothes> i: wardrobe) {
			for (Clothes j : i) {
				if (j.getId() == Outfit[cnt].getId())
				{
					inputs.add(1.0);
				}
				else
				{
					inputs.add(0.0);
				}
			}
			cnt++;
		}
		//System.out.println("outpit " + inputs);
		return inputs;

	}

	private static ArrayList<Double> calcOutfitInputs(Clothes Outfit[])
	{
		ArrayList <Double> inputs = new ArrayList<Double>();

		for (Clothes i : wardrobe[0]) {
			for (Clothes j : wardrobe[1]){
				for (Clothes k: wardrobe[2])
				{
					if(i.getId() == Outfit[0].getId() && j.getId() == Outfit[1].getId() && k.getId() == Outfit[2].getId())
					{
						inputs.add(1.0);
					}
					else
					{inputs.add(0.0);}
				}
			}
		}
		return inputs;

	}

	private static double execution(Clothes Outfit[])
	{
		ArrayList <Double> inputs = new ArrayList();
		inputs = calcInputs(Outfit);
		ArrayList <Double> outfitInputs = new ArrayList();
		outfitInputs = calcOutfitInputs(Outfit);
		System.out.println(outfitInputs);
		double output = nodeLayer.calculateInputs(inputs);
		double output2 = outfitLayer.calculateInputs(outfitInputs);
		System.out.println("outfit prob " + output2);
		//double finalOutput = output*output2;
		//nodeLayer.setFinalOutput(output);
		return output;
	}

	private static Clothes[] outfitProb()
	{
		ArrayList <Outfit> allOutfit= new ArrayList();
		int clothesNum[] = getAllClassSize();
		//contains the highest value in index 0 and the highest value idex in index 1
		double highest[] = {0,0};
		int cnt = 0;
		// for every possible combinaiton of clothes.
		for (Clothes i : wardrobe[0])
		{
			for (Clothes j : wardrobe[1])
			{
				for (Clothes k : wardrobe[2])
				{
					//the oufit being sent for get results
					Outfit sending= new Outfit(new Clothes[]{i, j, k}, 0);

					boolean valid = dupValid(sending, allOutfit);
					if (valid == true)
					{
						double result = execution(sending.getIndavidual());
						//System.out.println("attempt: " + result + "  Highest:" + highest[0]);
						if (result > highest[0])
						{
							highest[0] = result;
							highest[1] = cnt;
						}
						allOutfit.add(sending);
						cnt ++;
					}
					else
					{

					}
				}
			}
		}
		return allOutfit.get((int) highest[1]).getIndavidual();
	}

	public void running()
	{
		Clothes[] bestOutfit = outfitProb();
		//System.out.println("outpit " + currentResult);
		currentResult = execution(bestOutfit);
		currentBestOutfits = bestOutfit;
		//currentAllBest = new Outfit(bestOutfit);
	}
	//-----------------------------------------------------------------------------------------------
	private static void changeAllWeights(double error, Clothes[] outfit)
	{
		double momentum = 0.2;
		double learningRate = 0.2;
		ArrayList <Double> inputs = calcInputs(outfit);
		System.out.println("inputs " + inputs);
		nodeLayer.setDelta(error);
		nodeLayer.changeAllWeights(learningRate, inputs, momentum);
	}

	public void outcomeChange(int YorN)
	{
		changeAllWeights(YorN-currentResult,currentBestOutfits);
		// changeAllWeightsForOutfits(YorN-currentResult,currentBestOutfits);
	}
	//---------------------------------------------------------------------------------------------
	// process to add an item of clothing to the network
	public void addItem(int type, CharSequence name2)
	{
		String name = name2.toString();
		switch (type) {
			case 1:
				Clothes newItem1 = new Clothes(name, 1,wardrobe[0].size());
				wardrobe[0].add(newItem1);
				nodeLayer.addNode(type);
				for(Clothes i : wardrobe[1]) {
					for(Clothes j : wardrobe[2])
					{
						Outfits.add(new Outfit(new Clothes[]{newItem1,i, j},Outfits.size()));
						outfitLayer.addNode(1);
					}
				}

				break;
			case 2:
				Clothes newItem2 = new Clothes(name, 2,wardrobe[1].size());
				wardrobe[1].add(newItem2);
				nodeLayer.addNode(type);
				for(Clothes i : wardrobe[0]) {
					for(Clothes j : wardrobe[2])
					{
						Outfits.add(new Outfit(new Clothes[]{i,newItem2,j},Outfits.size()));
						outfitLayer.addNode(1);
					}
				}
				break;
			case 3:
				Clothes newItem3 = new Clothes(name, 3,wardrobe[2].size());
				wardrobe[2].add(newItem3);
				nodeLayer.addNode(type);
				for(Clothes i : wardrobe[0]) {
					for(Clothes j : wardrobe[1])
					{
						Outfits.add(new Outfit(new Clothes[]{i, j, newItem3},Outfits.size()));
						outfitLayer.addNode(1);
					}
				}
				break;
		}
		System.out.println(getAllOutfits());
	}



	//---------------------------------------------------------------------------------------------
	//GETTERS

	//for displaying text
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

	public String getAllOutfits()
	{
		String output = "";
		for(Outfit i : Outfits)
		{
			output += i.getId() + " \n"+ i.getAllItemNames() ;
		}
		return (output);
	}
	public String getName(int type, int pos)
	{
		return wardrobe[type].get(pos).getName();
	}

	//for sending text
	public String getAllNames()
	{
		String output = "";
		for (int i = 0; i < wardrobe.length; i ++)
		{
			for (int j = 0; j < wardrobe[i].size(); j ++)
			{
				output += getName(i,j) + ",";
			}
		}
		return (output);
	}

	public static double getWeight(int type, int pos)
	{
		return nodeLayer.getWeight(type,pos);
	}

	public static double[] getAllWeights()
	{
		double[] output = new double[totalClassSize() +1];
		//System.out.println(" WEEEEEEEEIGHT s " +  (totalClassSize() +1));
		output[0] = nodeLayer.getBias();
		int k = 1; // there has to be a better way to do this then to use k to track the variable
		//System.out.println(" WEEEEEEEEIGHT l" +  wardrobe.length);
		for(int i = 0; i < wardrobe.length; i++)
		{
			//System.out.println(" getC l" +  getClassSize(i));
			for (int j = 0; j < getClassSize(i);j ++)
			{
				output[k] = getWeight(i,j);
				k ++;
				//System.out.println(" working " +  k);
			}
		}



		return output;
	}
	///error above with not providign the rigth results
	public static int getClassSize(int pos)
	{
		return wardrobe[pos].size();
	}

	public static int[] getAllClassSize()
	{
		int [] output = new int[wardrobe.length];

		for (int i = 0; i < output.length; i ++)
		{
			output[i] = getClassSize(i);

		}
		return output;
	}

	public static int totalClassSize()
	{
		int output = 0;
		for (int i = 0; i < wardrobe.length; i ++)
		{
			output += getClassSize(i);
			//System.out.println(" SIIIIIIIIIIIZE " +  getClassSize(i));
		}
		return  output;
	}

}
 
