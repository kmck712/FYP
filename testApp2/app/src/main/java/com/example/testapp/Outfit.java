package com.example.testapp;

import java.util.ArrayList;

public class Outfit{

	private ArrayList<Clothes> outfit;
	private int id;
	Outfit(Clothes[] items, int id) {
		outfit = new ArrayList<Clothes>();
		this.id = id;
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

	protected double [] getIndividualId()
	{
		double [] output = new double[outfit.size()];
		int cnt = 0;
		for(Clothes i : outfit)
		{
			output[cnt] = i.getId();
			cnt ++;
		}
		return output;
	}
	protected String getAllItemNames()
	{
		String output = "";
		for (Clothes i : outfit) {
			output += i.getName() + "\n";
		}
		return output;
	}
	protected int getId(){return id;}
}
