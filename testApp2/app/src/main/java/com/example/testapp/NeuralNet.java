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
import java.util.Random;

public class NeuralNet  {
	public Outfit currentAllBest;
	public Clothes[] currentBestOutfits; // make private and created getters and setters
	public double currentResult;
	private static ArrayList <Outfit> Outfits;
	private static ArrayList<Clothes>[] wardrobe;
	private static Node2 nodeLayer; // node layer contains [0] taste node and [1] the synergy node
	private static Node2 outfitLayer;
	private static ArrayList <Outfit> chosenList;

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
		chosenList = new ArrayList<>();
	}
	//---------------------------------------------------------------------------------------------
	//this is used for when passing the information back in from different activities
	public NeuralNet(String passNames,double [] outfitsId, double [] Weights, double [] outfitWeights, int[] Dimensions, String[] passedImgPaths)
	{
		chosenList = new ArrayList<>();
		String names[] = passNames.split(",");
		nodeLayer = new Node2(3,Weights[0]);
		outfitLayer = new Node2(1,outfitWeights[0]);
		wardrobe = new ArrayList[3];
		Outfits = new ArrayList<>();
		for (int i = 0; i < wardrobe.length; i++) {
			wardrobe[i] = new ArrayList<Clothes>();
		}

		//not a fan of this method. must be a better way
		int count = 0;
		// NEED TO IMPLIMENT THE CHANGE OF WEIGHTS BEING PASSED
		//ALONG WITH THAT I NEED TO FIND A WAY TO THROW ALL THE WEIGHTS IN TOGETHER IF I WANT TO HAVE HIDDEN LAYERS AND BACK PROPGATION
		for (int i = 0; i < Dimensions.length; i ++)
		{
			for (int j = 0; j < Dimensions[i]; j ++)
			{
				//addItem(i + 1,names[count]);
				wardrobe[i].add(new Clothes(names[count],i +1,wardrobe[i].size(),passedImgPaths[count]));
				nodeLayer.addPassedNode(i,Weights[count +1], 0);

				count++;
			}
		}
		System.out.println("passe wieght num " + outfitWeights.length);
		for (int i =1; i < outfitWeights.length; i ++)
		{
			outfitLayer.addPassedNode(0,outfitWeights[i],0);

		}

		Clothes[] passing = new Clothes[3];
		for (int i =0; i < (outfitsId.length/3); i ++)
		{
			for (int j = 0; j < 3; j++)
			{
				for (Clothes k : wardrobe[j])
				{
					if (k.getId() == outfitsId[j + (3*i)])
					{
						passing[j] = k;
						break;
					}
				}
			}
			Outfits.add(new Outfit(passing, Outfits.size()));

		}
		System.out.println(getAllOutfits());


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

		double output = nodeLayer.calculateInputs(inputs);
		double output2 = outfitLayer.calculateInputs(outfitInputs);
		//System.out.println("outfit prob " + output);
		//System.out.println("outfit prob " + output2);

		//This is wrong and needs to be changed so that all there are sepearte weights for he inbetween layer
		double finalOutput = output + output2;
		return finalOutput;

	}
	private static boolean onList(Outfit sentOut)
	{
		for (Outfit i : chosenList)
		{
			int cnt = 0;
			for (int j =0; j < 3; j ++ )
			{
				if(i.getIndavidualItem(j) == sentOut.getIndavidualItem(j))
				{
					cnt++;
				}
			}
			if (cnt == 3)
			{
				return false;
			}
		}
		return true;
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

					//boolean valid = dupValid(sending, allOutfit);
					if (onList(sending))
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

		ArrayList <Double> outfitInputs = new ArrayList();
		outfitInputs = calcOutfitInputs(outfit);

		outfitLayer.setDelta(error);
		outfitLayer.changeAllWeights(learningRate, outfitInputs,momentum);

	}

	public void outcomeChange(int YorN)
	{
		changeAllWeights(YorN-currentResult,currentBestOutfits);
		if (YorN == 1)
		{
			if (chosenList.size() >= Outfits.size()/2)
			{
				chosenList.remove(new Random().nextInt(Outfits.size()));
			}
			chosenList.add(new Outfit(currentBestOutfits,0));

		}
		// changeAllWeightsForOutfits(YorN-currentResult,currentBestOutfits);
	}
	//---------------------------------------------------------------------------------------------
	// process to add an item of clothing to the network
	public void addItem(int type, CharSequence name2, String photoDir)
	{
		String name = name2.toString();
		switch (type) {
			case 1:
				Clothes newItem1 = new Clothes(name, 1,wardrobe[0].size(), photoDir);
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
				Clothes newItem2 = new Clothes(name, 2,wardrobe[1].size(),photoDir);
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
				Clothes newItem3 = new Clothes(name, 3,wardrobe[2].size(),photoDir);
				wardrobe[2].add(newItem3);
				nodeLayer.addNode(type);
					for (Clothes i : wardrobe[0]) {
						for (Clothes j : wardrobe[1]) {
							Outfits.add(new Outfit(new Clothes[]{i, j, newItem3}, Outfits.size()));
							outfitLayer.addNode(1);

						}
					}

				break;
		}
		System.out.println(getAllOutfits() + " number of weights " + getAllOutfitWeights().length);
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

	public double[] getAllOutfitsId()
	{
		ArrayList <Double> output = new ArrayList<Double>();
		for(Outfit i : Outfits)
		{
			for(double j : i.getIndividualId())
			{
				output.add(j);
			}
		}
		double [] out = new double[output.size()];
		for(int i =0; i < output.size(); i ++)
		{
			out[i] = output.get(i);
		}

		return (out);
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
	public String getAllNamesOfType(int type)
	{
		String output = "";

			for (int i = 0; i < wardrobe[type].size(); i ++)
			{
				output += getName(type,i) + ",";
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
	public String getItemInfo(int type, int id)
	{
		String output = "";
		output += "Name: " + wardrobe[type].get(id).getName() + "\n";
		output += "Type: " + wardrobe[type].get(id).getType()+ "\n";
		output += "Weight " + nodeLayer.getWeight(type, id)+ "\n";
		return output;
	}

	public  double[] getAllOutfitWeights()
	{
		return  outfitLayer.getAllWeights();
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

	public String[] getAllPaths()
	{
		String[] output = new String[totalClassSize() +1];
		int k = 0;
		for(int i = 0; i < wardrobe.length; i++)
		{
			for (int j = 0; j < getClassSize(i);j ++)
			{
				output[k] = wardrobe[i].get(j).getImgPath();
				k ++;

			}
		}
		return  output;
	}

	public String getPath(Clothes item)
	{
		return  item.getImgPath();
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
	public void removeAnItem(int type,int id)
	{
		Clothes oldItem  = wardrobe[type].get(id);
		wardrobe[oldItem.getType()-1].remove(oldItem.id);
		nodeLayer.removeWeight(oldItem.getType()-1, oldItem.id);
		ArrayList<Integer>toBeRemoved = new ArrayList<Integer>();
		for(int i =0; i < Outfits.size();i ++)
		{
			for (Clothes j : Outfits.get(i).getIndavidual())
			{
				if(j == oldItem)
				{
					toBeRemoved.add(0,i);
					break;
				}
			}
		}

		for(int i : toBeRemoved)
		{
			Outfits.remove(i);
		}
	}
	public void setType(int type, int id, int newType)
	{
		Clothes oldItem = wardrobe[type].get(id);
		removeAnItem(type,id);
		addItem(newType ,oldItem.getName(), oldItem.getImgPath());

	}



	public void setName(int type, int id, CharSequence newName)
	{
		String name = newName.toString();
		wardrobe[type].get(id).setName(name);
	}

}
 
