package com.example.testapp;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Node {

	private ArrayList<Double> Weights;
	private ArrayList<Double> changeInWeights;
	private double finalOutput;
	private double delta;

	Node(int inputNum)
	{
		Weights = new ArrayList<Double>();
		changeInWeights = new ArrayList<Double>();
		for (int i = 0; i < inputNum; i ++)
		{
			Weights.add(0.5);
			changeInWeights.add(0.0);
		}
		//output = 0.0;
		delta = 0.0;
	}

	public void addNode(int type, int size1, int size2)
	{
		switch (type) {
			case 1:
				Weights.add(size1, 0.5); // know error pls fix will index the wrong location if the pervious type is input twice
				break;
			case 2:
				Weights.add(size1 + size2,0.5);
				break;
			case 3:
				Weights.add(0.5);
				break;
		}

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

	protected double getWeight(int weightNum)
	{

		return Weights.get(weightNum);
	}

}
//CREATE SETTERS AND REMOVE ALL INSTANCE OF INTERCLASS VALUE ALTERING
