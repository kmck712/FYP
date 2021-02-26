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
	Node2(double oldBias)
	{
		Weights = new ArrayList[3];
		changeInWeights = new ArrayList[3];
		for (int i = 0; i < Weights.length; i++)
		{
			Weights[i] = new ArrayList<>();
			changeInWeights[i] = new ArrayList<>();
		}
		bias = oldBias;
		chnageInBias = 0.0;
		delta = 0.0;
	}

	protected double calculateInputs(ArrayList<Double>inputs, double[] formWeights)
	{
		double output = formWeights[0]; //basis weight set at 1 so you just add it on
		for (int i =0; i < inputs.size(); i ++)
		{
			output += inputs.get(i)*formWeights[i+ 1];
		}
		return output;
	}
	protected double[] formatWeights()
	{
		double[] output = new double[0];
		for (int i = 0; i < Weights.length; i ++)
		{
			for(double j : Weights[i])
			{

			}
		}
		return output;
	}

	protected void changeAWeight(int type,int pos, double learningRate, double input, double Momentum )
	{
		double change = learningRate * delta* input + Momentum *changeInWeights[type].get(pos) ;
		Weights[type].set(pos, Weights[type].get(pos) + change);
		changeInWeights[type].set(pos, change);
	}

	protected void changeAllWeights(double learningRate,  ArrayList <Double> inputs,double Momentum )
	{
		//changeAWeight(, learningRate,1,Momentum );
		int cnt = 1;
		// look into should be running for 6 times but is running for 12;
		for (int i = 0; i<  Weights.length; i ++)
		{
			for (int j = 0; j < Weights[i].size(); j ++)
			{
				System.out.println(" input size " + inputs.size());
				//System.out.println(" input size " + inputs.size());
				//changeAWeight(i,j, learningRate,inputs.get(cnt-1),Momentum );
				System.out.println(" input size " + cnt);
				cnt ++;
			}
		}
	}

	//------------------------------------------------------------------------------------------------
	//initalis the weights for the new item
	public void addNode(int type)
	{
		Weights[type-1].add(0.5);
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

	public double getBias()
	{
		return bias;
	}

}
//CREATE SETTERS AND REMOVE ALL INSTANCE OF INTERCLASS VALUE ALTERING
