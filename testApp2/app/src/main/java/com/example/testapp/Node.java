package com.example.testapp;

import java.util.ArrayList;

public class Node {

	private ArrayList<Double> Weights;
	private ArrayList<Double>[] Weights2;
	private ArrayList<Double> changeInWeights;
	private double finalOutput;
	private double delta;
	private double bias;

	Node() {
		Weights = new ArrayList();
		Weights2 = new ArrayList[3];
		for (int i = 0; i < Weights2.length; i++)
		{
			Weights2[i] = new ArrayList<>();
		}

		changeInWeights = new ArrayList();
		bias = 0.5;
		//Weights.add(0.5);
		changeInWeights.add(0.0);
		//output = 0.0;
		delta = 0.0;
	}
	Node(double[] oldWeights, double[] dimensions)
	{
		Weights = new ArrayList();
		Weights2 = new ArrayList[3];
		for (int i = 0; i < Weights2.length; i++)
		{
			Weights2[i] = new ArrayList<>();
		}
		changeInWeights = new ArrayList();
		changeInWeights.add(0.0); // change this so that it has the bias' change as currently is has none

		//this next part is horrible but it works for what i'm trying to do;
		int pos = 0;
		int pos2 = 0;
		bias = oldWeights[0];
		System.out.println("old Weight sise = " + oldWeights.length + "\n");
		for (int j = 0; j < dimensions.length; j++)
		{
			System.out.println("dimention = " + dimensions[j] + "\n");
			pos2 +=dimensions[j];
			for (int i = pos; i < pos2; i ++)
			{
				Weights2[j].add(oldWeights[i]);
				System.out.println("pos = " + pos + "\n");
				changeInWeights.add(0.0);
			}
			 pos += dimensions[j];
		}

		delta = 0.0;
	}

	public void addNode(int type)
	{
		Weights2[type-1].add(0.5);
		changeInWeights.add(0.0);//part of a default usage needs to be adated for great use for all classes and proper tracking
	}
	public void addExistingNode(double W)
	{
		Weights.add(W);

	}
	protected double calculateInputs(ArrayList<Double>inputs)
	{
		double output = Weights.get(0); //basis weight
		for (int i =0; i < inputs.size(); i ++)
		{
			output += inputs.get(i)*Weights.get(i+ 1);
		}
		return output;
	}
	protected void setFinalOutput(double output)
	{
		finalOutput = output;
	}
	protected void setDelta(double error)
	{
		delta = error;
	}
	protected void clearW()
	{
		Weights = new ArrayList<>();
	}
	protected void changeAllWeights(double learningRate,  ArrayList <Double> inputs,double Momentum )
	{
		/*for(Double j: inputs)
		{
			System.out.println(j);
		}
		*/
		for (int i = 0; i < Weights.size(); i ++)
		{
			//System.out.println("old Weight: \n" + Weights.get(i) + " orginal weight\n" + changeInWeights.get(i) + " previous\n");
			if (i == 0)
			{
				changeAWeight(i, learningRate,1,Momentum );
				// 1 for positive can be changed to 0 for negative bias
			}
			else
			{
				changeAWeight(i, learningRate,inputs.get(i-1),Momentum );
			}
			//System.out.println("new Weight: \n" +Weights.get(i) + " new weight\n" + changeInWeights.get(i) + " previous\nEnd of position " + i +"\ny");
		}
	}

	protected void changeAWeight(int weightNum, double learningRate, double input, double Momentum )
	{
		double change = learningRate * delta* input + Momentum *changeInWeights.get(weightNum) ;
		Weights.set(weightNum, Weights.get(weightNum) + change);
		changeInWeights.set(weightNum, change);
	}
	protected void formatWeights()
	{
		Weights.add(bias);
		for (int i = 0; i < Weights2.length; i ++)
		{
			for(double j : Weights2[i])
			{
				Weights.add(j);
			}
		}
	}
	protected ArrayList[] getW()
	{
		return Weights2;
	}
	protected double getWeight(int weightNum)
	{

		return Weights.get(weightNum);
	}
	protected double[] getAllWeights()
	{
		formatWeights();
		double[] allWeights = new double[Weights.size()];
		for (int i = 0; i< Weights.size(); i ++)
		{
			allWeights[i] = getWeight(i);
		}
		return allWeights;
	}
	protected ArrayList<Double>[] getWeightsss()
	{

		return Weights2;
	}

}
//CREATE SETTERS AND REMOVE ALL INSTANCE OF INTERCLASS VALUE ALTERING
