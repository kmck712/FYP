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

import org.w3c.dom.Node;

import java.lang.reflect.Type;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class NeuralNet  {

	public Outfit currentAllBest;
	public Clothes[] currentBestOutfits; // make private and created getters and setters
	public double currentResult;
	private static ArrayList <Outfit> Outfits;
	private static ArrayList<Clothes>[] wardrobe;

	//layer of the NN network
	private static Node2[] nodeLayers;
	private static Node2[][] hiddenLayers;
	private static Node2 outputLayer;
	private static Network nnFull;

	private static ArrayList <Outfit> chosenList;
	private static int numberOfHL, nodesInHL;

	//----------------------------------------------------------------------------------------------
	//Inital constructor for the Neural network
	public NeuralNet() {

		Network nnFull = new Network();
		//all the possible outfits
		Outfits = new ArrayList<>();
		//all the items of clothing
		wardrobe = new ArrayList[3];

		//initialises the wardrobe with an array the size of the types
		for (int i = 0; i < wardrobe.length; i++) {
			wardrobe[i] = new ArrayList<>();
		}

		//sets up the nodes for the network with the amount of depth in the layers.
		nodeLayers = new Node2[3];
		nodeLayers[0] = new Node2(3);
		for(int i = 1; i < nodeLayers.length; i ++)
		{
			nodeLayers[i] = new Node2(1);
		}

		nodeLayers[2].addNode(1);
		nodeLayers[2].addNode(1);



		outputLayer =  new Node2(1);


		nodesInHL = 3;
		numberOfHL = 1;
		hiddenLayers = new Node2[numberOfHL][nodesInHL];
		for (int i = 0;  i < numberOfHL; i ++)
		{
			for(int j = 0; j < nodesInHL; j ++)
			{
				hiddenLayers[i][j] = new Node2(1);
				//cheating a solution, basically means you can't have <2 nodes in a HL
				if (i == 0 )
				{
					for (int k = 0; k < 2; k ++)
					{
						hiddenLayers[i][j].addNode(1);
					}
				}
				else {
					for (int k = 0; k < nodesInHL; k++) {
						hiddenLayers[i][j].addNode(1);
					}
				}
			}
		}
		for (int i =0; i < nodesInHL; i ++) {
			outputLayer.addNode(1);
		}
		nnFull.addNode(nodeLayers[0]);
		nnFull.addNode(nodeLayers[1]);
		nnFull.addNode(nodeLayers[2]);


		//sets up the list for the blacklisted items.
		chosenList = new ArrayList<>();
		System.out.println("second layer weights " + nodeLayers[2].size());
	}
	//---------------------------------------------------------------------------------------------
	//this is used for when passing the information back in from different activities
	public NeuralNet(String passNames,double [] outfitsId, double [] Weights, double [] outfitWeights, int[] Dimensions, String[] passedImgPaths)
	{
		chosenList = new ArrayList<>();
		String names[] = passNames.split(",");
		nodeLayers[0] = new Node2(3,Weights[0]);
		nodeLayers[1] = new Node2(1,outfitWeights[0]);
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
				nodeLayers[0].addPassedNode(i,Weights[count +1], 0);

				count++;
			}
		}

		for (int i =1; i < outfitWeights.length; i ++)
		{
			nodeLayers[1].addPassedNode(0,outfitWeights[i],0);

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

		nodesInHL = 3;
		numberOfHL = 2;
		hiddenLayers = new Node2[numberOfHL][nodesInHL];
		for (int i = 0;  i < numberOfHL; i ++)
		{
			for(int j = 0; j < nodesInHL; j ++)
			{
				hiddenLayers[i][j] = new Node2(1);
			}
		}
	}
	//---------------------------------------------------------------------------------------------

	private static ArrayList<Double> calcInputs(Clothes Outfit[])
	{
		ArrayList <Double>inputs = new ArrayList();
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

	/*private static double execution(Clothes Outfit[])
	{

		ArrayList <Double> inputs = new ArrayList();
		inputs = calcInputs(Outfit);

		ArrayList <Double> outfitInputs = new ArrayList();
		outfitInputs = calcOutfitInputs(Outfit);

		//double output = nodeLayers[0].calculateInputs(inputs);
		//double output2 = nodeLayers[1].calculateInputs(outfitInputs);
		ArrayList<Double> outputs = new ArrayList<>();
		outputs.add(nodeLayers[0].calculateInputs(inputs));
		outputs.add(nodeLayers[1].calculateInputs(outfitInputs));

		//This is wrong and needs to be changed so that all there are sepearte weights for he inbetween layer
		double finalOutput = hiddenlayerExecution(outputs, 0);
		return finalOutput;

	}

	 */
	private static double execution(Clothes Outfit[])
	{

		ArrayList <Double> inputs = new ArrayList();
		inputs = calcInputs(Outfit);

		ArrayList <Double> outfitInputs = new ArrayList();
		outfitInputs = calcOutfitInputs(Outfit);

		//double output = nodeLayers[0].calculateInputs(inputs);
		//double output2 = nodeLayers[1].calculateInputs(outfitInputs);
		ArrayList<Double> outputs = new ArrayList<>();
		outputs.add(nodeLayers[0].calculateInputs(inputs));
		outputs.add(nodeLayers[1].calculateInputs(outfitInputs));
		double finalOutput = nodeLayers[2].calculateInputs(outputs);

		return finalOutput;

	}

	private static double hiddenlayerExecution(ArrayList<Double> itemsInputs, int pos)
	{
		ArrayList<Double> nextInputs = new ArrayList<>();
		for(Node2 i : hiddenLayers[pos])
		{
			nextInputs.add(i.calculateInputs(itemsInputs));
		}
		if (pos == numberOfHL-1)
		{
			double output = outputLayer.calculateInputs(nextInputs);
			return output;
		}
		else
		{
			hiddenlayerExecution(nextInputs,pos+1);
		}
		return 0;
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
		//contains the highest value in index 0 and the highest value idex in index 1
		double highest[] = {0,0};
		ArrayList<Double> result = new ArrayList<>();
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
					result.add(execution(sending.getIndavidual()));
					double currentResult = execution(sending.getIndavidual());
					allOutfit.add(sending);
					cnt ++;
					//boolean valid = dupValid(sending, allOutfit);
					if (onList(sending))
					{
						result.add(execution(sending.getIndavidual()));
						//System.out.println("attempt: " + result + "  Highest:" + highest[0]);
						if (currentResult > highest[0])
						{
							highest[0] = currentResult;
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
		trueRunning();
		//System.out.println("outfits " + Outfits.size());
		//System.out.println("Weights " + getAllItemInfo());
		//learnRunning();
		//System.out.println("Weights " + getAllItemInfo());
		//testRunning();
		//currentAllBest = new Outfit(bestOutfit);
	}
	private void trueRunning()
	{
		Clothes[] bestOutfit = outfitProb();
		currentResult = execution(bestOutfit);
		currentBestOutfits = bestOutfit;
	}
	private  void learnRunning()
	{
		for (int i =0; i <10; i ++)
		{
			Clothes[] bestOutfit = outfitProb();
			currentResult = execution(bestOutfit);
			currentBestOutfits = bestOutfit;
			int score = preferences();
			if (score > 0)
			{
				outcomeChange(22);
			}
			else
			{
				outcomeChange(0);
			}

		}                                                                                   	
	}


	private  void testRunning()
	{
		int totalScore = 0;
		for (int i =0; i <10; i ++)
		{
			Clothes[] bestOutfit = outfitProb();
			currentResult = execution(bestOutfit);
			currentBestOutfits = bestOutfit;
			int score = preferences();

			totalScore += score;


		}
		System.out.println("total score " + totalScore + " average score " +( double)totalScore/10);
	}
	private int preferences()
	{
		int score = 0;
	/*	for (int i = 0; i < currentBestOutfits.length; i ++)
		{
			int[] ids = new int[2];
			int cnt = 0;

			for(Clothes j : currentBestOutfits)
			{
				if (j.type != i + 1)
				{
					ids[cnt] = j.getId();
					cnt ++;
				}
			}

	 */
			ArrayList[] synergies;
			synergies = scores(0,currentBestOutfits[0].getId());

			if(synergies[0].contains(currentBestOutfits[1].getId()))
			{
				score++;
			}
			if(synergies[1].contains(currentBestOutfits[2].getId()))
			{
				score++;
			}

			synergies = scores(1,currentBestOutfits[2].getId());

			if(synergies[0].contains(currentBestOutfits[1].getId()))
			{
				score++;
	    	}
		//dave taste list

		return score;
	};
	private ArrayList[] scores(int type, int id)
	{
		ArrayList<Integer> checking[] = new ArrayList[2];
		checking[1] = new ArrayList<>();
		checking[0] = new ArrayList<>();
		switch (type){
			case 0:
				switch (id)
				{
					case 0:
						checking[0].addAll(Arrays.asList(0,1,2,3));
						checking[1].addAll(Arrays.asList(0,1));
						break;
					case 1:
						checking[0].addAll(Arrays.asList(0,1,2,3,6));
						checking[1].addAll(Arrays.asList(1,2));
						break;
					case 2:
						checking[0].addAll(Arrays.asList(3,6));
						checking[1].addAll(Arrays.asList(1,2));
						break;
				}
				break;
			case 1:
				switch (id)
				{
					case 0:
						checking[0].addAll(Arrays.asList(4,5,7));
						break;
					case 1:
						checking[0].addAll(Arrays.asList(0,1,2,3,4,5,8));
						break;
					case 2:
						checking[0].addAll(Arrays.asList(0,1,2,3,8));
						break;
				}
		}

		return checking;

	}
	//-----------------------------------------------------------------------------------------------
	private static void changeAllWeights(double error, Clothes[] outfit)
	{
		//Very unstable. only coded to work with the current version of the network being 1 hl and no sigmoid
		double momentum = 0.2;
		double learningRate = 0.2;
		ArrayList <Double> inputs = calcInputs(outfit);
		ArrayList <Double> outfitInputs = calcOutfitInputs(outfit);


		//useing back propgation
		//setting errors
		//first layer
		nodeLayers[2].setDelta(error);
		//second layer
		nodeLayers[0].setDelta(nodeLayers[2].getDelta() * nodeLayers[2].getWeight(1,0));
		nodeLayers[1].setDelta(nodeLayers[2].getDelta() * nodeLayers[2].getWeight(1,2));

		//need to get the output of the layer 0,1;
		nodeLayers[2].changeAllWeights(learningRate, , momentum);
		nodeLayers[0].changeAllWeights(learningRate, inputs, momentum);
		nodeLayers[1].changeAllWeights(learningRate, outfitInputs,momentum);
	}

	/*private static void changeAllWeights(double error, Clothes[] outfit)
	{
		//Very unstable. only coded to work with the current version of the network being 1 hl and no sigmoid
		double momentum = 0.2;
		double learningRate = 0.2;
		ArrayList <Double> inputs = calcInputs(outfit);

		ArrayList <Double> outfitInputs = calcOutfitInputs(outfit);
		outputLayer.setDelta(error);

		for(int i =0; i< nodesInHL; i ++)
		{
			hiddenLayers[numberOfHL-1][i].setDelta(error*outputLayer.getWeight(0,i));

		}
		double finalError1 = 0;
		for (Node2 i : hiddenLayers[0])
		{
			finalError1 += i.getDelta() * i.getWeight(0, 0);
		}
		double finalError2 = 0;
		for (Node2 i : hiddenLayers[0])
		{
			finalError2 += i.getDelta() * i.getWeight(0, 1);
		}

		nodeLayers[0].setDelta(finalError1);

		nodeLayers[1].setDelta(finalError2);



		outputLayer.changeAllWeights(learningRate,momentum);
		for(int i =0; i< nodesInHL; i ++) {
			hiddenLayers[numberOfHL - 1][i].changeAllWeights(learningRate, momentum);
		}
		nodeLayers[0].changeAllWeights(learningRate, inputs, momentum);
		nodeLayers[1].changeAllWeights(learningRate, outfitInputs,momentum);
	}

	 */

	public void outcomeChange(int YorN)
	{
		changeAllWeights(YorN-currentResult,currentBestOutfits);
		/*if (YorN == 1)
		{
			if (chosenList.size() >= Outfits.size()/2)
			{
				chosenList.remove(new Random().nextInt(Outfits.size()));
			}
			chosenList.add(new Outfit(currentBestOutfits,0));

		}


		 */
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
				nodeLayers[0].addNode(type);
				for(Clothes i : wardrobe[1]) {
					for(Clothes j : wardrobe[2])
					{
						Outfits.add(new Outfit(new Clothes[]{newItem1,i, j},Outfits.size()));
						nodeLayers[1].addNode(1);

					}
				}

				break;
			case 2:
				Clothes newItem2 = new Clothes(name, 2,wardrobe[1].size(),photoDir);
				wardrobe[1].add(newItem2);
				nodeLayers[0].addNode(type);
				for(Clothes i : wardrobe[0]) {
					for(Clothes j : wardrobe[2])
					{
						Outfits.add(new Outfit(new Clothes[]{i,newItem2,j},Outfits.size()));
						nodeLayers[1].addNode(1);

					}
				}
				break;
			case 3:
				Clothes newItem3 = new Clothes(name, 3,wardrobe[2].size(),photoDir);
				wardrobe[2].add(newItem3);
				nodeLayers[0].addNode(type);
					for (Clothes i : wardrobe[0]) {
						for (Clothes j : wardrobe[1]) {
							Outfits.add(new Outfit(new Clothes[]{i, j, newItem3}, Outfits.size()));
							nodeLayers[1].addNode(1);

						}
					}

				break;
		}
		//System.out.println(getAllOutfits() + " number of weights " + getAllOutfitWeights().length);
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
		return nodeLayers[0].getWeight(type,pos);
	}

	public static double[] getAllWeights()
	{
		double[] output = new double[totalClassSize() +1];
		//System.out.println(" WEEEEEEEEIGHT s " +  (totalClassSize() +1));
		output[0] = nodeLayers[0].getBias();
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
		output += "Weight " + nodeLayers[0].getWeight(type, id)+ "\n";
		return output;
	}
	public String getAllItemInfo() {
		String output = "";
		for (int i = 0; i < wardrobe.length; i++) {
			for (int j = 0; j < wardrobe[i].size(); j++) {
				output += getItemInfo(i, j);
			}
		}
		return output;
	}
	public  double[] getAllOutfitWeights()
	{
		return  nodeLayers[1].getAllWeights();
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

		}
		return  output;
	}
	public void removeAnItem(int type,int id)
	{
		Clothes oldItem  = wardrobe[type].get(id);
		wardrobe[oldItem.getType()-1].remove(oldItem.id);
		nodeLayers[0].removeWeight(oldItem.getType()-1, oldItem.id);
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
			nodeLayers[1].removeWeight(0,i);
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
 
