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
	
	private static ArrayList<Clothes>wardrobe;

	private static double execution(Clothes Outfit[], Node tst, Node syn)
	{
		double tasteInp = 0;
		double outfitInp = 0;
		for(Clothes i: Outfit)
		{
			tasteInp += i.getWeight() * i.clothesN.getWeight(0);
			outfitInp += i.getWeight() * i.clothesN.getWeight(1);
		}
		double finalOut = 0;
		finalOut += tasteInp * tst.getWeight(0);
		finalOut += outfitInp * syn.getWeight(0);
		
		return finalOut;
	}
	
	public static void main(String args[]) 
	{  
		double defaultEnd[] = {0.5};
		wardrobe = new ArrayList<Clothes>();
		wardrobe.add(new Clothes("shirt", 0.6, 1));
		wardrobe.add(new Clothes("jacket", 0.5, 2));
		wardrobe.add(new Clothes("trouser", 0.4, 3));
		Node taste = new Node(defaultEnd);
		Node synergy = new Node(defaultEnd);
		
		
		/*for (Clothes i  : wardrobe)
		{
			System.out.println(i.clothesN.getWeights());
		}*/
		boolean endProg = false;
		
		while (endProg ==false)
		{
			Scanner myObj = new Scanner(System.in); 
			Clothes sending[]= {wardrobe.get(0),wardrobe.get(1),wardrobe.get(2)};
			/*	sending [0]= wardrobe.get(0);
				sending [1]= wardrobe.get(1);
				sending [2]= wardrobe.get(2);
				*/
			double result  = execution(sending, taste, synergy);
			System.out.println(result);
			if (result > 0)
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
			}
		}
	} 
}
