package aiTest;

import java.util.ArrayList;  

public class Node {

	public ArrayList<Double> Weights;
	public ArrayList<Double> LearningR;
	
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
		double change = Weights.get(0) * LearningR.get(0) * error;  
		Weights.set(0, Weights.get(0) + change);
		Weights.set(1, Weights.get(1) + change);
		
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
