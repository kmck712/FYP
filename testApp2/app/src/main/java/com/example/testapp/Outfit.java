package com.example.testapp;

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
			out[i] =  getIndavidualItem(i);
		}
		return out;
	}
	protected Clothes getIndavidualItem(int pos)
	{
		return outfit.get(pos);
	}
	protected String getItemName(int id)
	{
		String output = outfit.get(id).getName();
		return output;
	}
}
