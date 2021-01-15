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

public class aiTestMain {
	
	private static ArrayList<Clothes>[] wardrobe = new ArrayList<Clothes>[3];
	private static ArrayList<Node>nodeLayer;

	private static double execution(Clothes Outfit[])
	{
		double tasteInp = 0;
		double outfitInp = 0;
		for(Clothes i: Outfit)
		{
			
			tasteInp += i.getWeight() * i.clothesN.getWeight(0);
			outfitInp += i.getWeight() * i.clothesN.getWeight(1);
		}
		double finalOut = 0;
		finalOut += tasteInp * nodeLayer.get(0).getWeight(0);
		finalOut += outfitInp * nodeLayer.get(1).getWeight(0);
		
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
	
	private static Clothes[] outfitProb()
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
	
	private static void running()
	{
		boolean endProg = false;
		Scanner myObj = new Scanner(System.in); 
		while (endProg ==false)
		{
			int totalOut[] = numOutfits();
			int totalClothes = (totalOut[0]*(totalOut[1] + 1) * totalOut[2]);
			System.out.println(totalClothes);
			
			Clothes[] bestOutfit = outfitProb();
			System.out.println(bestOutfit[1].name);
			System.out.println(bestOutfit[0].name);
			
			String test = myObj.nextLine();
			/*if (result > 0)
			{
				String fOutfit = "";
				for (Clothes i: sending)
				{
					fOutfit += i.name + "\n";
				}
				System.out.println(fOutfit + "\n Do you like this outfit?");
				String inp =  myObj.nextLine();	
				if (inp.equals("y") == true)
				{
					double error = 1 - result;
					for (Clothes i: wardrobe)
					{
						i.changeW(error);
						System.out.println(i.clothesN.getWeight(0));
					}
				}
				else
				{
					double error = - result;
					for (Clothes i: wardrobe)
					{
						i.changeW(error);
					}
				}
			}*/
		}
	}
	public static void main(String args[]) 
	{  
		double defaultEnd[] = {0.5};
		double defaultEnd1[] = {0.5,0.5};
		double defaultEnd2[] = {0.7,0.5};
		/*
		wardrobe[0] = new ArrayList<Clothes>();
		wardrobe[1] = new ArrayList<Clothes>();
		wardrobe[2] = new ArrayList<Clothes>();
		*/
		
		nodeLayer = new ArrayList<Node>();
		
		
		wardrobe[0].add(new Clothes("shirt", 0.7, 1, defaultEnd2));
		wardrobe[1].add(new Clothes("jacket", 0.5, 2,defaultEnd1));
		wardrobe[2].add(new Clothes("trouser1", 0.7, 3,defaultEnd1));
		wardrobe[2].add(new Clothes("trouser2", 0.5, 3,defaultEnd1));
		nodeLayer.add(new Node(defaultEnd));
		nodeLayer.add(new Node(defaultEnd));
		running();
		
		}
}

// NOTES:	TRY SORTING WITH THE OUTFIT ARRAY LIST 
//			TRY 2 DIMENTIONAL ARRAY LISTS		  
