package aiTest;

import java.util.ArrayList;  

public class Node {

	private ArrayList<Double> Weights;
	private ArrayList<Double> LearningR;
	
	Node(double weights[])
	{
		Weights = new ArrayList<Double>();
		LearningR = new ArrayList<Double>();
		for(int i = 0; i < weights.length; i ++)
		{
			Weights.add(weights[i]);
			LearningR.add(0.2);
		}
	}
	protected void changeW(double error)
	{
		int count = 0;
		for (Double i: Weights)
		{
			double change = i * LearningR.get(count) * error;
			Weights.set(count, i + change);
			count ++;
		}
		
	}
	protected void setLearningRate(double learningR)
	{
		//only currently work for the single weight node layer. may need to change if needing to be applicable to other node objects.
		this.LearningR.set(0, learningR);
	}
	protected double getWeight(int weightNum)
	{
		
		return Weights.get(weightNum);
	}
	protected double getLearningR(int weightNum)
	{
		
		return Weights.get(weightNum);
	}
}
//CREATE SETTERS AND REMOVE ALL INSTANCE OF INTERCLASS VALUE ALTERING
