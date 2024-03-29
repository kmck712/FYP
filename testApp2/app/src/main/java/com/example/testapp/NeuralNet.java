/**
 * 
 */
package com.example.testapp;

/**
 * @author jmcke
 *
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class NeuralNet  {

	public Outfit currentAllBest;
	public Clothes[] currentBestOutfits; // make private and created getters and setters
	public double currentResult;
	private static ArrayList <Outfit> Outfits;
	private static ArrayList<Clothes>[] wardrobe;

	private static Network nnFull;
	//layer of the NN network
	//private static Node2[][] hiddenLayers;

	//private static ArrayList <Outfit> chosenList;
	//private static int numberOfHL, nodesInHL;

	//----------------------------------------------------------------------------------------------
	//Inital constructor for the Neural network
	public NeuralNet() {

		nnFull = new Network();
		//all the possible outfits
		Outfits = new ArrayList<>();
		//all the items of clothing
		wardrobe = new ArrayList[3];

		//initialises the wardrobe with an array the size of the types
		for (int i = 0; i < wardrobe.length; i++) {
			wardrobe[i] = new ArrayList<>();
		}
		//adds the clothes and outfit layers
		nnFull.addNewLayer();

		//adds the output layer
		nnFull.addNewLayer(1);

	}
	//---------------------------------------------------------------------------------------------
	//this is used for when passing the information back in from different activities
	public NeuralNet(String passNames,double [] outfitsId, double [][] Weights, int[] Dimensions, String[] passedImgPaths)
	{
		//fIIIIIIIIIIIIIX
		//setting up the wardrobe
		String names[] = passNames.split(",");
		wardrobe = new ArrayList[3];
		Outfits = new ArrayList<>();
		for (int i = 0; i < wardrobe.length; i++) {
			wardrobe[i] = new ArrayList<Clothes>();
		}
		//set's up the network filled with passed weights
		nnFull = new Network(new Node[]{new Node(3,Weights[0][0]), new Node(1,Weights[1][0])});

		int count = 0;
		for (int i = 0; i < Dimensions.length; i ++)
		{
			for (int j = 0; j < Dimensions[i]; j ++)
			{
				//addItem(i + 1,names[count]);
				wardrobe[i].add(new Clothes(names[count],i +1,wardrobe[i].size(),passedImgPaths[count]));
				nnFull.addOldWeight(0,0,i,Weights[0][count +1], 0);
				count++;
			}
		}

		for (int i =1; i < Weights[1].length; i ++)
		{
			nnFull.addOldWeight(0,1,0,Weights[1][i],0);

		}

		Clothes[] passing = new Clothes[3];
		for (int i =0; i < (outfitsId.length/3); i ++) {
			for (int j = 0; j < 3; j++) {
				for (Clothes k : wardrobe[j]) {
					if (k.getId() == outfitsId[j + (3 * i)]) {
						passing[j] = k;
						break;
					}
				}
			}
			Outfits.add(new Outfit(passing, Outfits.size()));
		}

		nnFull.addNewLayer(Weights[2][0]);

		for (int i = 1; i < Weights[2].length; i++)
		{
			nnFull.addOldWeight(1,0,0,Weights[2][i],0);
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
			if(cnt > 2)
			{
				break;
			}
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


	private static double execution(Clothes Outfit[])
	{
		//to calculate the inputs for the individual items
		ArrayList <Double> inputs = new ArrayList();
		inputs = calcInputs(Outfit);
		//to calculate the inputs for which outfit is being calculated
		ArrayList <Double> outfitInputs = new ArrayList();
		outfitInputs = calcOutfitInputs(Outfit);
		//returns the output from the neural network
		return nnFull.execution(inputs,outfitInputs);

	}

	private static Clothes[] outfitProb()
	{
		ArrayList <Outfit> allOutfit= new ArrayList();
		//contains the highest value in index 0 and the highest value idex in index 1
		ArrayList<Double> result = new ArrayList<>();
		// for every possible combinaiton of clothes.
		for (Clothes i : wardrobe[0])
		{ for (Clothes j : wardrobe[1])
			{ for (Clothes k : wardrobe[2])
				{
					//the outfit being sent for get results
					Outfit sending= new Outfit(new Clothes[]{i, j, k}, 0);
					//computing the output within the network
					result.add(execution(sending.getIndavidual()));
					//adding outfit to list of processed outfits
					allOutfit.add(sending);
				} } }
		//for the total value of the outputs
		double total = 0.0;
		for(Double i : result){total +=i;};
		//choosing a random value between 0 and the sum of the outputs
		double rng = (new Random().nextDouble() * (total -0.0)) + 0.0;
		//used to store the position in the outfits represented by the usm of outputs
		double sum = 0;
		for(int j = 0; j < result.size(); j ++)
		{
			sum +=result.get(j);
			if (rng < sum)
			{
				//returns the outfit when the sum value is within range
				return allOutfit.get(j).getIndavidual();
			}
		}
		return null;
	}

	public void running()
	{
		trueRunning();
		//System.out.println("outfits " + Outfits.size());
		//System.out.println("Weights " + getAllItemInfo());
		//learnRunning();
		//System.out.println("TEST RESUKTS " + testRunning());


	}
	private void trueRunning()
	{
		Clothes[] bestOutfit = outfitProb();
		currentResult = execution(bestOutfit);
		currentBestOutfits = bestOutfit;
	}
	private  void learnRunning()
	{
		for (int i =0; i <200; i ++)
		{
			Clothes[] bestOutfit = outfitProb();
			currentResult = execution(bestOutfit);
			currentBestOutfits = bestOutfit;
			int score = preferences();
			if (score > 0)
			{
				outcomeChange(1);
			}
			else
			{
				outcomeChange(0);
			}

		}                                                                                   	
	}


	private  int testRunning()
	{
		int goodOutfits = 0;
		for (int i =0; i <100; i ++)
		{

			Clothes[] bestOutfit = outfitProb();
			currentResult = execution(bestOutfit);
			currentBestOutfits = bestOutfit;
			int score = preferences();
			if (score > 0)
			{
				goodOutfits ++;
			}

		}
		//System.out.println("good outfits " + goodOutfits);
		return goodOutfits;
	}
	//dave
	/*private int preferences()
	{
		int output = 0;
		if (currentBestOutfits[0].getId() == 0)
		{
			int firstId = currentBestOutfits[1].getId();
			int secondId = currentBestOutfits[2].getId();
			if(firstId == 0);
			{ output++;}
			if(firstId == 1){output--;}
			if(secondId==1){output++;}
		}
		if (currentBestOutfits[0].getId() == 1)
		{
			int firstId = currentBestOutfits[1].getId();
			int secondId = currentBestOutfits[2].getId();
			if(firstId == 1){ output++;}
			if(secondId==0){output++;}
		}
		if (currentBestOutfits[2].getId() == 0)
		{
			int firstId = currentBestOutfits[1].getId();
			if(firstId == 2){ output++;}
			if(firstId == 0|| firstId == 1){ output--;}
		}
		if (currentBestOutfits[2].getId() == 1)
		{
			int firstId = currentBestOutfits[1].getId();
			if( firstId == 1){ output--;}
		}
		return output;
	}

	 */

	//julia
	private int preferences()
	{
		int output = 0;
		if (currentBestOutfits[0].getId() == 0)
		{
			int firstId = currentBestOutfits[1].getId();
			int secondId = currentBestOutfits[2].getId();
			if(firstId == 0){ output--;}
			if(firstId == 2){ output--;}
			if(firstId == 4){ output--;}
			if(firstId ==3){ output++;}

			if(secondId==0){output++;}
			if(secondId==2){output--;}
		}
		if (currentBestOutfits[0].getId() == 1)
		{
			int firstId = currentBestOutfits[1].getId();
			int secondId = currentBestOutfits[2].getId();
			if(firstId == 0){ output++;}
			if(firstId == 4){ output++;}
			if(firstId ==3){ output--;}

		}
		if (currentBestOutfits[0].getId() == 2)
		{
			int firstId = currentBestOutfits[1].getId();
			int secondId = currentBestOutfits[2].getId();
			if(firstId == 2){ output--;}

			if(secondId==0){output++;}
			if(secondId==1){output++;}
			if(secondId==2){output--;}
		}
		if (currentBestOutfits[0].getId() == 3)
		{
			int firstId = currentBestOutfits[1].getId();
			int secondId = currentBestOutfits[2].getId();
			if(secondId==1){output++;}
			if(secondId==2){output--;}
		}
		if (currentBestOutfits[2].getId() == 0)
		{
			int firstId = currentBestOutfits[1].getId();
			if(firstId == 2){ output--;}
			if(firstId == 4){ output--;}
			if(firstId == 3){ output++;}
		}
		if (currentBestOutfits[2].getId() == 1)
		{
			int firstId = currentBestOutfits[1].getId();
			if(firstId == 1){ output++;}
		}
		if (currentBestOutfits[2].getId() == 2)
		{
			int firstId = currentBestOutfits[1].getId();
			if(firstId == 2){ output++;}
		}

		return output;
	}


	//-----------------------------------------------------------------------------------------------
	private static void changeAllWeights(double error, Clothes[] outfit)
	{
		//setting up momentum and learning rate for the learning of the neural network
		double momentum = 0.35;
		double learningRate = 0.25;
		//the initial inputs used  for the outfit
		ArrayList <Double> inputs = calcInputs(outfit);
		ArrayList <Double> outfitInputs = calcOutfitInputs(outfit);

		//sets the network up with the outputs and results of the current outfit
		nnFull.calculateInitialOutput(inputs, outfitInputs);
		nnFull.calculateOutput();
		//starts the changing of the weights of the network.
		nnFull.changeNodes(error , learningRate, momentum, inputs, outfitInputs);

	}



	public void outcomeChange(double YorN)
	{
		changeAllWeights(YorN-currentResult,currentBestOutfits);
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
				nnFull.addNode(0,type);
				for(Clothes i : wardrobe[1]) {
					for(Clothes j : wardrobe[2])
					{
						Outfits.add(new Outfit(new Clothes[]{newItem1,i, j},Outfits.size()));
						nnFull.addNode(1,1);

					}
				}

				break;
			case 2:
				Clothes newItem2 = new Clothes(name, 2,wardrobe[1].size(),photoDir);
				wardrobe[1].add(newItem2);
				nnFull.addNode(0,type);
				for(Clothes i : wardrobe[0]) {
					for(Clothes j : wardrobe[2])
					{
						Outfits.add(new Outfit(new Clothes[]{i,newItem2,j},Outfits.size()));
						nnFull.addNode(1,1);

					}
				}
				break;
			case 3:
				Clothes newItem3 = new Clothes(name, 3,wardrobe[2].size(),photoDir);
				wardrobe[2].add(newItem3);
				nnFull.addNode(0,type);
					for (Clothes i : wardrobe[0]) {
						for (Clothes j : wardrobe[1]) {
							Outfits.add(new Outfit(new Clothes[]{i, j, newItem3}, Outfits.size()));
							nnFull.addNode(1,1);

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
		return nnFull.getWeight(0,type,pos);
	}

	/*public static double[] getAllWeights()
	{
		double[] output = new double[totalClassSize() +1];
		//System.out.println(" WEEEEEEEEIGHT s " +  (totalClassSize() +1));
		output[0] = nnFull.getBias(0);
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

	 */

	public static double[][] getAllWeights()
	{
		double[][] output = {nnFull.getAllWeights(0,0), nnFull.getAllWeights(0,1), nnFull.getAllWeights(1,0)};

		return output;
	}

	public String getItemInfo(int type, int id)
	{
		String output = "";
		output += "Name: " + wardrobe[type].get(id).getName() + "\n";
		output += "Type: " + wardrobe[type].get(id).getType()+ "\n";
		output += "Weight " + nnFull.getWeight(0,type, id)+ "\n";
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
		return  nnFull.getAllWeights(0,1);
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
		nnFull.removeWeight(0,0,oldItem.getType()-1, oldItem.id);
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
			nnFull.removeWeight(0,1,0,i);
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

//old hidden layer code which needs to be adapted
		/*
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

		 */
//old code: sets up the list for the blacklisted items.
//chosenList = new ArrayList<>();
//System.out.println("second layer weights " + nodeLayers[2].size());

//----------------------------------------
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
//-------------------------------------------
/*
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

 */
//---------------------------------------

	/*private static double hiddenlayerExecution(ArrayList<Double> itemsInputs, int pos)
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

	 */


	/*private static Clothes[] outfitProb()
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
		System.out.println("all results ");
		for (double i : result)
		{
			System.out.println(i);
		}
		return allOutfit.get((int) highest[1]).getIndavidual();
	}

	 */
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

/*private int preferences()
	{
		int score = 0;
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
						checking[0].addAll(Arrays.asList(0,1));
						checking[1].addAll(Arrays.asList(1));
						break;
					case 1:
						checking[0].addAll(Arrays.asList(1,2));
						checking[1].addAll(Arrays.asList(0));
						break;

				}
				break;
			case 1:
				switch (id)
				{
					case 0:
						checking[0].addAll(Arrays.asList(0,1,2));
						break;
					case 1:
						checking[0].addAll(Arrays.asList(1));
						break;
				}
		}

		return checking;

	}

	 */

//*/
	/*private ArrayList[] scores(int type, int id)
	{
		ArrayList<Integer> checking[] = new ArrayList[2];
		checking[1] = new ArrayList<>();
		checking[0] = new ArrayList<>();
		switch (type){
			case 0:
				switch (id)
				{
					case 0:
						checking[0].addAll(Arrays.asList(1));
						checking[1].addAll(Arrays.asList(1,2));
						break;
					case 1:
						checking[0].addAll(Arrays.asList(0,2));
						checking[1].addAll(Arrays.asList(0,3));
						break;
					case 2:
						checking[0].addAll(Arrays.asList(0,1,2));
						checking[1].addAll(Arrays.asList(1,2));
						break;
					case 3:
						checking[0].addAll(Arrays.asList(0,3));
						checking[1].addAll(Arrays.asList(0,2));
						break;
				}
				break;
			case 1:
				switch (id)
				{
					case 0:
						checking[0].addAll(Arrays.asList(1,2,3));
						break;
					case 1:
						checking[0].addAll(Arrays.asList(0));
						break;
					case 2:
						checking[0].addAll(Arrays.asList(0,2,3));
						break;
					case 3:
						checking[0].addAll(Arrays.asList(1));
						break;
				}
		}

		return checking;


	}
	*/
