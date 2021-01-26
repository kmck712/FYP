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
	
	private static ArrayList<Clothes>[] wardrobe;
	private static ArrayList<Node>nodeLayer; // node layer contains [0] taste node and [1] the synergy node

	private static double[] execution(Clothes Outfit[])
	{
		double tasteInp = 0;
		double outfitInp = 0;
		for(Clothes i: Outfit)
		{
			
			tasteInp += i.getWeight() * i.clothesN.getWeight(0);
			outfitInp += i.getWeight() * i.clothesN.getWeight(1);
		}
		double[] finalOut = new double[3];
		//find a better way of doing this a half the function which need a final out don't need final out [0][1]
		finalOut [0]= tasteInp * nodeLayer.get(0).getWeight(0);
		finalOut [1]= outfitInp * nodeLayer.get(1).getWeight(0);
		finalOut [2] = finalOut[1] + finalOut[0];
		
		return finalOut;
	}
	
	private static int[] numOutfits()
	{
		int clothesNum[] = {wardrobe[0].size(),wardrobe[1].size(),wardrobe[2].size()};
		/*for(Clothes i:wardrobe)
		{
			switch(i.type)
			{
			case 1:
				clothesNum[0] += 1;
				break;
			case 2:
				clothesNum[1] += 1;
				break;
			case 3:
				clothesNum[2] += 1;
				break;
			}
		}
		*/
		return (clothesNum);
	}
	private static boolean dupValid(Clothes[] subject, ArrayList<Clothes[]> outfit)
	{
		boolean valid = true;
		for (Clothes[] i: outfit)
		{
			if (i == subject)
			{
				valid = false;
			}
		}
		return valid;
	}
	
	/*private static Clothes[] outfitProb()
	{
		ArrayList <Clothes[]>outfit= new ArrayList<Clothes[]>();
		int clothesNum[] = numOutfits();
		double highest[] = {0,0};
		int cnt = 0;
		System.out.println(clothesNum[0] + "hh " +  clothesNum[2]);
			for (int i = 0; i < clothesNum[0];i ++)
			{
				for (int k = 0; k < clothesNum[2]; k ++)
				{
					Clothes sending[]= {wardrobe[0].get(i),wardrobe[2].get(k)};
					boolean valid = dupValid(sending, outfit); 
					if (valid == true)
					{
						double result = execution(sending);
						if (result > highest[0])
						{
							highest[0] = result;
							highest[1] = cnt;
						}
						outfit.add(sending);
					}
					cnt = cnt + 1;
				}
			}
		return outfit.get((int) highest[1]);
	}
	*/
	
	private static Clothes[] outfitProbRNG()
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
	
	private static void changingWeights(Clothes[] finalOut, double error)
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
	
	private static int[] running(int loops)

	{
		boolean endProg = false;
		Scanner myObj = new Scanner(System.in); 
		int count = 1;
		int[] ans = new int[2];
		while (endProg ==false)
		{
			int totalOut[] = numOutfits();
			int totalClothes = (totalOut[0]*(totalOut[1] + 1) * totalOut[2]);
			System.out.println(totalClothes);
			
			Clothes[] bestOutfit = outfitProbRNG();
			
			double[] result = execution(bestOutfit);
			System.out.println("result: " + result[2]);
			
			if (result[2] > 0)
			{
				String fOutfit = "";
				for (Clothes i: bestOutfit)
				{
					fOutfit += i.name + "\n";
				}
				System.out.println(fOutfit + "Do you like the outfit? y/n:\n");
				//String inp =  myObj.nextLine();	
				//if (inp.equals("y") == true)
				if (testing(bestOutfit)== true)
				{
					System.out.println("Yes");
					ans[0] ++;
					double error = 1 - result[2];
					changingWeights(bestOutfit, error);
					//TURN INTO A FUNCTION. REDUCE REDUNDANT CODE
					if (result[0]> result[1])
					{
						nodeLayer.get(0).setLearningRate(0.3);
						nodeLayer.get(1).setLearningRate(0.2);
						nodeLayer.get(0).changeW(error);
						nodeLayer.get(1).changeW(error);
					}
					else
					{
						nodeLayer.get(0).setLearningRate(0.2);
						nodeLayer.get(1).setLearningRate(0.3);
						nodeLayer.get(0).changeW(error);
						nodeLayer.get(1).changeW(error);
					}
				}
				else
				{
					System.out.println("No");
					ans[1]++;
					double error = - result[2];
					changingWeights(bestOutfit, error);
					if (result[0]> result[1])
					{
						nodeLayer.get(0).setLearningRate(0.6);
						nodeLayer.get(1).setLearningRate(0.4);
						nodeLayer.get(0).changeW(error);
						nodeLayer.get(1).changeW(error);
					}
					else
					{
						nodeLayer.get(0).setLearningRate(0.4);
						nodeLayer.get(1).setLearningRate(0.6);
						nodeLayer.get(0).changeW(error);
						nodeLayer.get(1).changeW(error);
					}
				}
			
			}
			System.out.println(count);
			count ++;
			if(count > loops)
			{
				
				endProg = true;
			}
		}
		return ans;
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
	private static void testRun()
	{
		int[] results = running(10);
		System.out.println("The results are: " + results[0] + "/" + (results[0] + results[1]));
	}
	
	/**
	 * @param args
	 */
	public static void main(String args[]) 
	{  
		//these represent certain default weights which can be applied to the objects
		double defaultEnd[] = {0.5};
		double defaultEnd1[] = {0.5,0.5};
		double defaultEnd2[] = {0.7,0.5};
		double baseInp[] = {0.2,0.5,0.8};
		
		//initalises both the nodelayer and the wardrobe
		nodeLayer = new ArrayList<Node>();
		wardrobe =  new ArrayList[3];
		//initialises the wardrobe arraylist by creating a new array list object for each of the type of objects
		for (int i = 0; i < 3; i ++)
		{
			wardrobe[i] = new ArrayList<Clothes>();
		}
		
		//adds the default clothes into the system
		wardrobe[0].add(new Clothes("black and White Shirt", baseInp[1], 1,wardrobe[0].size(), defaultEnd1));
		wardrobe[0].add(new Clothes("band Shirt", baseInp[2], 1,wardrobe[0].size(), defaultEnd1));
		wardrobe[0].add(new Clothes("Plain Red Shirt", baseInp[1], 1,wardrobe[0].size(), defaultEnd1));
		wardrobe[0].add(new Clothes("Smart Shirt", baseInp[0], 1,wardrobe[0].size(), defaultEnd1));
		
		//temporary solution to the no jacket combination
		wardrobe[1].add(new Clothes("Nothing", baseInp[1], 2,wardrobe[1].size(), defaultEnd1));
			
		wardrobe[1].add(new Clothes("Leather Jacket", baseInp[2], 2,wardrobe[1].size(), defaultEnd1));
		wardrobe[1].add(new Clothes("Blue Hoodie", baseInp[1], 2,wardrobe[1].size(), defaultEnd1));
		
		wardrobe[2].add(new Clothes("Blue Jeans", baseInp[1], 3,wardrobe[2].size(),defaultEnd1));
		wardrobe[2].add(new Clothes("Grey Jeans", baseInp[1], 3,wardrobe[2].size(),defaultEnd1));
		wardrobe[2].add(new Clothes("Smart Trousers", baseInp[0], 3,wardrobe[2].size(),defaultEnd1));
		
		//initialises the the taste node [0]and the synergy node[1] 
		nodeLayer.add(new Node(defaultEnd, 0.6));
		nodeLayer.add(new Node(defaultEnd,0.4));
		running(100);
		testRun();
		
		}
}
 
