package com.example.testapp;

import java.util.ArrayList;

public class Node2 {


	private ArrayList<Double>[] Weights;
	private ArrayList<Double>[] changeInWeights;
	private double finalOutput;
	private double delta;
	private double bias;
	private double chnageInBias;

	Node2() {

		Weights = new ArrayList[3];
		changeInWeights = new ArrayList[3];
		for (int i = 0; i < Weights.length; i++)
		{
			Weights[i] = new ArrayList<>();
			changeInWeights[i] = new ArrayList<>();
		}
		bias = 0.5;
		chnageInBias = 0.0;
		delta = 0.0;
	}
	Node2(double[] oldWeights, double[] dimensions)
	{

	}


	//initalis the weights for the new item
	public void addNode(int type)
	{
		Weights[type-1].add(0.5);
		changeInWeights[type-1].add(0.0);
	}

	public double getWeight(int type, int pos)
	{
		return Weights[type].get(pos);
	}

	public double getBias()
	{
		return bias;
	}

}
//CREATE SETTERS AND REMOVE ALL INSTANCE OF INTERCLASS VALUE ALTERING
