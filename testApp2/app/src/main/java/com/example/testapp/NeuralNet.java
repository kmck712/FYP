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

public class NeuralNet  {

	public Clothes[] currentBestOutfits; // make private and created getters and setters
	private static ArrayList <Outfit> Outfits;
	private static ArrayList<Clothes>[] wardrobe;
	private static Node2 nodeLayer; // node layer contains [0] taste node and [1] the synergy node

	public NeuralNet() {
		nodeLayer = new Node2();

		wardrobe = new ArrayList[3];
		for (int i = 0; i < wardrobe.length; i++) {
			wardrobe[i] = new ArrayList<Clothes>();
		}

	}

	//this is used for when passing the information back in from different activities
	public NeuralNet(String[] names, int[] Weights, double[] Dimensions)
	{


	}

	// process to add an item of clothing to the network
	public void addItem(int type, CharSequence name2)
	{
		String name = name2.toString();
		switch (type) {
			case 1:
				wardrobe[0].add(new Clothes(name, 1,wardrobe[0].size()));
				nodeLayer.addNode(type);
				break;
			case 2:
				wardrobe[1].add(new Clothes(name, 2,wardrobe[1].size()));
				nodeLayer.addNode(type);
				break;
			case 3:
				wardrobe[2].add(new Clothes(name, 3,wardrobe[2].size()));
				nodeLayer.addNode(type);
				break;
		}
	}

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

	public double getWeight(int type, int pos)
	{
		return nodeLayer.getWeight(type,pos);
	}

	public double[] getAllWeights()
	{
		double[] output = new double[totalClassSize() +1];
		output[0] = nodeLayer.getBias();
		int k = 0; // there has to be a better way to do this then to use k to track the variable
		for(int i = 0; i < wardrobe.length; i++)
		{
			for (int j = 0; j < getClassSize(i);j ++)
			{
				output[k] = getWeight(i,j);
				k ++;
			}
		}
		return output;
	}

	public int getClassSize(int pos)
	{
		return wardrobe[pos].size();
	}

	public int[] getAllClassSize()
	{
		int [] output = new int[wardrobe.length];
		for (int i = 0; i < output.length; i ++)
		{
			output[i] = getClassSize(i);
		}
		return output;
	}

	public int totalClassSize()
	{
		int output = 0;
		for (int i = 0; i < wardrobe.length; i ++)
		{
			output += getClassSize(i);
		}
		return  output;
	}
}
 
