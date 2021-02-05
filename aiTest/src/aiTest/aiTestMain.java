/**
 * 
 */
package aiTest;

/**
 * @author jmcke
 *
 */
import java.util.ArrayList;  
import java.util.Arrays; 
import java.util.Scanner;
import java.lang.Math;   
import java.util.Random;   

public class aiTestMain {
	
	private static ArrayList <Outfit> Outfits;
	private static ArrayList<Clothes>[] wardrobe;
	private static ArrayList<Node>nodeLayer; // node layer contains [0] taste node and [1] the synergy node

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
	
	
	private static boolean dupValid(Clothes[] subject)
	//goal is ensure there are no duplicate outfits. this is done by checking if the current putfit 'subject' is in the current array'outfit'
	{
		boolean valid = true;
		if(Outfits.size() == 0)
		{
			return valid;
		}
		else
		{
			for(int i = 0; i < Outfits.size(); i ++)
			{
				int cnt = 0;
				for (Clothes j :Outfits.get(i).getIndavidual())
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
		ArrayList <Clothes[]>outfit= new ArrayList<Clothes[]>();
		int clothesNum[] = numOutfits2();
		double highest[] = {0,0};
		int cnt = 0;
		//System.out.println(clothesNum[0] + "hh " +  clothesNum[2]);
			for (int i = 0; i < clothesNum[0];i ++)
			{
				for (int j = 0; j < clothesNum[1];j ++ )
				{
					for (int k = 0; k < clothesNum[2]; k ++)
					{
						Clothes sending[]= {wardrobe[0].get(i),wardrobe[1].get(j),wardrobe[2].get(k)};
						boolean valid = dupValid(sending); 
						if (valid == true)
						{
							double result = execution(sending);
							//System.out.println("attempt: " + result + "  Highest:" + highest[0]);
							if (result > highest[0])
							{
								highest[0] = result;
								highest[1] = cnt;
							}
							outfit.add(sending);
							cnt ++;
						}
						else 
						{
							
						}
					}
				}
			}
		return outfit.get((int) highest[1]);
	}
	
	
	/*private static Clothes[] outfitProbRNG()
	{
		ArrayList <Clothes[]>outfit= new ArrayList<Clothes[]>();
		int clothesNum[] = numOutfits();
		double[] highest = new double[clothesNum[0]*(clothesNum[1])*clothesNum[2]];
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
							double[] result = execution(sending);
							highest[cnt] = result[2];			
							outfit.add(sending);
						}
						cnt = cnt + 1;
					}
				}
			}
		//clean these loops up. there has to be a better way to do  this ->
			//here
		double  sum = 0;
		for(Double i: highest)
		{
			sum += i;
		}
		Random random = new Random(); 
		double chosen = random.nextDouble();
		for(int i = 1; i < highest.length;i ++ )
		{
			highest[i] = highest[i] + highest[i-1];
		}
		for(int i = 0; i < highest.length;i ++ )
		{
			highest[i] = highest[i]/sum;
		}
		cnt = 0;
		for(Double i: highest)
		{
			if (i >= chosen)
			{
				return outfit.get(cnt);
			}
			cnt ++;
		}
		// end of clean up
		return null;
	}
	*/
	
	/*private static void changingWeights(Clothes[] finalOut, double error)
	{
		// see if there is a way to reduce the amount of loop statements /streamline
		for (Clothes i: finalOut)
		{
			switch (i.type) {
				case 1:
					for (int j = 0; j < wardrobe[0].size(); j ++)
					{
						if (wardrobe[0].get(j).id == i.id )
						{
							wardrobe[0].get(j).changeW(error);
							//System.out.println("w2 " + wardrobe[0].get(j).clothesN.getWeight(1));
							break;
						}
					}
					break;
				case 2:
					for (int j = 0; j < wardrobe[1].size(); j ++)
					{
						if (wardrobe[1].get(j).id == i.id )
						{
							wardrobe[1].get(j).changeW(error);
							//System.out.println(wardrobe[1].get(j).clothesN.getWeight(0));
							break;
						}
					}
					break;
				case 3:
					for (int j = 0; j < wardrobe[2].size(); j ++)
					{
						if (wardrobe[2].get(j).id == i.id )
						{
							wardrobe[2].get(j).changeW(error);
							//System.out.println(wardrobe[2].get(j).clothesN.getWeight(0));
							break;
						}
					}
					break;
			}
		}
	}
	*/
	
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
	
	private static int[] running(int loops)

	{
		int[] results = {0,0};
		boolean endProg = false;
		Scanner myObj = new Scanner(System.in); 
		int count = 1;
		while (endProg ==false)
		{
			Clothes[] bestOutfit = outfitProb();
			double result = execution(bestOutfit);	
			
			Outfit bestOutfits = new Outfit(bestOutfit);
			double synergyResult = executionOutfits(bestOutfits);
			
			System.out.println("result: " + result + " synergy result: " +synergyResult);
			String fOutfit = "";
			for (Clothes i: bestOutfit)
			{
				fOutfit += i.name + "\n";
			}	
			System.out.println(fOutfit);
			/*
			System.out.println(fOutfit + "Do you like the outfit? y/n:\n");
			String inp =  myObj.nextLine();	
			
			if (inp.equals("y") == true)
			*/
			if (testing(bestOutfit) == true)
			{
				results[0] ++;
				System.out.println("Yes");
				changeAllWeights(1-result,bestOutfit);
				changeAllWeightsForOutfits(1-result,bestOutfits);
			}	
			else
			{
				results[1] ++;
				System.out.println("No");
				changeAllWeights(-result,bestOutfit);
				changeAllWeightsForOutfits(-result,bestOutfits);
			}
			//System.out.println(count);
			count ++;
			if(count > loops)
			{	
				endProg = true;
			}
			if (Outfits.size() < 5)
			{
				Outfits.add(new Outfit(bestOutfit));
			}
			else 
			{
				Outfits.remove(0);
				Outfits.add(new Outfit(bestOutfit));
			}
	
		}
		return results;
	}
	
	private static int[] running2(int loops)
	{
		int[] results = {0,0};
		boolean endProg = false;
		Scanner myObj = new Scanner(System.in); 
		int count = 1;
		while (endProg ==false)
		{
			Clothes[] bestOutfit = outfitProb();
			double result = execution(bestOutfit);	
			
			Outfit bestOutfits = new Outfit(bestOutfit);
			double synergyResult = executionOutfits(bestOutfits);
			
			System.out.println("result: " + result + " synergy result: " +synergyResult);
			String fOutfit = "";
			for (Clothes i: bestOutfit)
			{
				fOutfit += i.name + "\n";
			}	
			System.out.println(fOutfit);
		
			if (testing(bestOutfit) == true)
			{
				results[0] ++;
				System.out.println("Yes");
			}	
			else
			{
				results[1] ++;
				System.out.println("No");
			}
			//System.out.println(count);
			count ++;
			if(count > loops)
			{	
				endProg = true;
			}
			if (Outfits.size() < 5)
			{
				Outfits.add(new Outfit(bestOutfit));
			}
			else 
			{
				Outfits.remove(0);
				Outfits.add(new Outfit(bestOutfit));
			}
	
		}
		return results;
	}
	
	private static boolean testing(Clothes[] outfitTest)
	{
		if (outfitTest[0].getName().equals("Plain Red Shirt") && (outfitTest[1].getName().equals("Blue Hoodie")||outfitTest[2].getName().equals("Blue Jeans")))
				{
					return false;
				}
		if (outfitTest[0].getName().equals("Smart Shirt") || outfitTest[2].getName().equals("Smart Trousers"))
		{
			if (outfitTest[0].getName().equals("Smart Shirt") && outfitTest[1].getName().equals("Nothing")&&outfitTest[2].getName().equals("Smart Trousers"))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		return true;
	}
	
	private static void testRun(int epochs)
	{
		int[] results = running2(epochs);
		System.out.println("The results are: " + results[0] + "/" + (results[0] + results[1]));
	}
	
	/**
	 * @param args
	 */
	public static void main(String args[]) 
	{  
		//initalises both the nodelayer and the wardrobe
		nodeLayer = new ArrayList<Node>();
		wardrobe =  new ArrayList[3];
		//initialises the wardrobe arraylist by creating a new array list object for each of the type of objects
		for (int i = 0; i < 3; i ++)
		{
			wardrobe[i] = new ArrayList<Clothes>();
		}
		
		//adds the default clothes into the system
		wardrobe[0].add(new Clothes("black and White Shirt", 1,wardrobe[0].size()));
		wardrobe[0].add(new Clothes("band Shirt", 1,wardrobe[0].size()));
		wardrobe[0].add(new Clothes("Plain Red Shirt", 1,wardrobe[0].size()));
		wardrobe[0].add(new Clothes("Smart Shirt", 1,wardrobe[0].size()));
		
		//temporary solution to the no jacket combination
		wardrobe[1].add(new Clothes("Nothing", 2,wardrobe[1].size()));
			
		wardrobe[1].add(new Clothes("Leather Jacket", 2,wardrobe[1].size()));
		wardrobe[1].add(new Clothes("Blue Hoodie", 2,wardrobe[1].size()));
		
		wardrobe[2].add(new Clothes("Blue Jeans", 3,wardrobe[2].size()));
		wardrobe[2].add(new Clothes("Grey Jeans", 3,wardrobe[2].size()));
		wardrobe[2].add(new Clothes("Smart Trousers", 3,wardrobe[2].size()));
		
		
		
		int allInputs = 1;
		allInputs += numClothes();
		Outfits = new ArrayList<Outfit>();
		
		//initialises the the taste node [0]and the synergy node[1] 
		nodeLayer.add(new Node(allInputs));
		
		nodeLayer.add(new Node(numOutfits() +1));
		running(0);
		//testRun(100);
		
		}
}
 
