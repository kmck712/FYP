package aiTest;

import java.util.ArrayList;

public class Outfit{

	private ArrayList<Clothes> outfit;
	
	Outfit(Clothes[] items) {
		outfit = new ArrayList<Clothes>();
		for (Clothes i: items)
		{
			outfit.add(i);
			
		}
	
	}
	
	protected Clothes[] getIndavidual()
	{
		Clothes[] out = new Clothes[3];
		for(int i = 0; i < outfit.size(); i ++)
		{
			out[i] = outfit.get(i);
		}
		return out;
	}
	protected String getItemName(int id)
	{
		String output = outfit.get(id).name; 
		return output;
	}
}
