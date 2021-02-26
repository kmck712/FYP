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

	public Clothes[] currentBestOutfits; // make private and created getters and setters
	public double currentResult;
	private static ArrayList <Outfit> Outfits;
	private static ArrayList<Clothes>[] wardrobe;
	private static Node2 nodeLayer; // node layer contains [0] taste node and [1] the synergy node

	public NeuralNet() {
		nodeLayer = new Node2();

		wardrobe = new ArrayList[3];
		for (int i = 0; i < wardrobe.length; i++) {
			wardrobe[i] = new ArrayList<Clothes>();
		}

	}
	//---------------------------------------------------------------------------------------------
	//this is used for when passing the information back in from different activities
	public NeuralNet(String passNames, double [] Weights, int[] Dimensions)
	{
		String names[] = passNames.split(",");
		nodeLayer = new Node2(Weights[0]);
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
				addItem(i + 1,names[count]);
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
		ArrayList <Double> inputs = new ArrayList<Double>();

		for (int i = 0;i < totalClassSize() ; i ++)
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

	private static double execution(Clothes Outfit[])
	{
		ArrayList <Double> inputs = new ArrayList();
		inputs = calcInputs(Outfit);
		double output = nodeLayer.calculateInputs(inputs, getAllWeights());
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
					Outfit sending= new Outfit(new Clothes[]{i, j, k});

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
		currentResult = execution(bestOutfit);
		currentBestOutfits = bestOutfit;
	}
	//-----------------------------------------------------------------------------------------------
	private static void changeAllWeights(double error, Clothes[] outfit)
	{
		double momentum = 0.2;
		double learningRate = 0.2;
		ArrayList <Double> inputs = calcInputs(outfit);
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
				wardrobe[0].add(new Clothes(name, 1,wardrobe[0].size()));
				nodeLayer.addNode(type);
				break;
			case 2:
				wardrobe[1].add(new Clothes(name, 2,wardrobe[1].size()));
				nodeLayer.addNode(type);
				break;
			case 3:
				wardrobe[2].add(new Clothes(name, 3,wardrobe[2].size()));
				nodeLayer.addNode(type);
				break;
		}
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
 
