package com.example.testapp;

import java.util.ArrayList;

public class Node {


	private ArrayList<Double>[] Weights;
	private ArrayList<Double>[] changeInWeights;
	private double delta;
	private double bias;
	private double changeInBias;

	Node(int arraySize) {

		Weights = new ArrayList[arraySize];
		changeInWeights = new ArrayList[arraySize];
		for (int i = 0; i < Weights.length; i++)
		{
			Weights[i] = new ArrayList<>();
			changeInWeights[i] = new ArrayList<>();
		}
		bias = 0.1;
		changeInBias = 0.0;
		delta = 0.0;
	}
	Node(int arraySize, double oldBias)
	{
		Weights = new ArrayList[arraySize];
		changeInWeights = new ArrayList[arraySize];
		for (int i = 0; i < Weights.length; i++)
		{
			Weights[i] = new ArrayList<>();
			changeInWeights[i] = new ArrayList<>();
		}
		bias = oldBias;
		changeInBias = 0.0;
		delta = 0.0;

	}

	protected double calculateInputs(ArrayList<Double>inputs)
	{
		double output = 0;
		int cnt = 0;
		for (ArrayList<Double> i : Weights) {
			for (double j : i) {

				output += inputs.get(cnt) * j ;
				cnt ++;
			}
		}
		return output;
	}


	protected void changeAWeight(int type,int pos, double learningRate, double input, double Momentum )
	{
		double change = learningRate * delta* input + Momentum *changeInWeights[type].get(pos) ;
		change = Math.round(change*1000.0)/1000.0;
		Weights[type].set(pos, Weights[type].get(pos) + change);
		changeInWeights[type].set(pos, change);
	}

	protected void changeAllWeights(double learningRate,  ArrayList <Double> inputs,double Momentum )
	{
		double change = learningRate*delta + Momentum+ changeInBias;
		bias += change;
		changeInBias = change;
		bias = Math.round(bias * 1000.0) / 1000.0;
		int cnt = 0;
		for (int i = 0; i<  Weights.length; i ++) {
			for (int j = 0; j < Weights[i].size(); j++) {
				changeAWeight(i, j, learningRate, inputs.get(cnt), Momentum);
				cnt++;
			}
		}
	}
	/*protected void changeAllWeights(double learningRate,double Momentum )
	{
		double change = learningRate*delta + Momentum* changeInBias;
		bias += change;
		bias = Math.round(bias * 1000.0) / 1000.0;
		changeInBias = change;
		for (int i = 0; i<  Weights.length; i ++) {
			for (int j = 0; j < Weights[i].size(); j++) {
				changeAWeight(i, j, learningRate, 1, Momentum);
			}
		}
	}

	 */

	//------------------------------------------------------------------------------------------------
	//initalis the weights for the new item
	public void addNode(int type)
	{
		Weights[type-1].add(0.1);
		changeInWeights[type-1].add(0.0);
	}
	public void addPassedNode(int type, double weights, double change)
	{
		Weights[type].add(weights);
		changeInWeights[type].add(change);
	}
	protected void setDelta(double error)
	{
		delta = error;
	}
	//----------------------------------------------------------------------------------------------

	public double getWeight(int type, int pos)
	{
		return Weights[type].get(pos);
	}

	public int size()
	{
		int output =0;
		for(ArrayList<Double> i : Weights) { for(double j : i) { output++; } }
		return output;
	}
	public double[] getAllWeights()
	{
		double [] output = new double [size() + 1];
		output[0] = bias;
		int cnt = 1;
		for(ArrayList<Double> i : Weights)
		{
			for(double j : i)
			{
				output[cnt] = j;
				cnt ++;
			}
		}
		return output;
	}


	public double getBias()
	{
		return bias;
	}

	public double getDelta(){return delta;}

	public double getSize(int pos)
	{
		return Weights[pos].size();
	}

	public void removeWeight(int type, int id)
	{
		Weights[type].remove(id);
	}

}

