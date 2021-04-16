package com.example.testapp;

import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Arrays;

public class Network {

	//convet to storing nodes a as an array of node in a list per layer
	ArrayList<Double[]> outputs;
	ArrayList<Node2[]> nodeLayers;

	Network()
	{
		nodeLayers = new ArrayList();
		outputs = new ArrayList<>();
		//need to have this rely on the inputs in the layer
		for (int i =0; i < 3; i ++)
		{
			outputs.add(null);

		}
	}

	//need to have it so it takes the other layers as well
	Network(Node2[] oldNodes)
	{
		nodeLayers = new ArrayList();
		outputs = new ArrayList<>();
		//need to have this rely on the inputs in the layer
		for (int i =0; i < 3; i ++)
		{
			outputs.add(null);

		}
		nodeLayers.add(oldNodes);
	}



	public void addOldWeight(int layer,int id,int type, double weights, double change )
	{
		nodeLayers.get(layer)[id].addPassedNode(type, weights, change);

	}
	public void addNewLayer()
	{
		nodeLayers.add(new Node2[] {new Node2(3), new Node2(1)});
	}

	public void addNewLayer(int nodesIn)
	{
		nodeLayers.add(createNodes(nodesIn,nodeLayers.get(0).length));

	}

	private Node2[] createNodes(int size, int previousLayer)
	{
		Node2[] output = new Node2[2];
		for (int i = 0; i < size; i ++)
		{
			output[i] = new Node2(1);
			for(int j = 0; j < previousLayer; j ++) {
				output[i].addNode(1);
			}
		}
		return  output;
	}

	public void addNode(int id, int type)
	{
		nodeLayers.get(0)[id].addNode(type);
	}
	public void calculateInitialOutput(ArrayList input1, ArrayList input2)
	{
		outputs.set(0,new Double[]{nodeLayers.get(0)[0].calculateInputs(input1), nodeLayers.get(0)[1].calculateInputs(input2)});

	}
	public void calculateOutput(int layer)
	{
		//layer would be used when layers are introduced for now it;s just for the output layer
		ArrayList <Double> inputs = new ArrayList<>();
		inputs.addAll(Arrays.asList(outputs.get(0)));
		outputs.set(1, new Double[] {nodeLayers.get(1)[0].calculateInputs(inputs)});
	}
	public void changeNodes(double error, double learningRate, double momentum, ArrayList<Double> inputs1, ArrayList<Double> inputs2)
	{
		nodeLayers.get(1)[0].setDelta(error);
		//second layer
		nodeLayers.get(0)[0].setDelta(nodeLayers.get(1)[0].getDelta() * nodeLayers.get(1)[0].getWeight(0,0));
		nodeLayers.get(0)[1].setDelta(nodeLayers.get(1)[0].getDelta() * nodeLayers.get(1)[0].getWeight(0,1));

		//need to get the output of the layer 0,1;
		nodeLayers.get(1)[0].changeAllWeights(learningRate, toArrayList(outputs.get(0)), momentum);
		nodeLayers.get(0)[0].changeAllWeights(learningRate, inputs1, momentum);
		nodeLayers.get(0)[1].changeAllWeights(learningRate, inputs2,momentum);
	}

	public double getWeight(int id , int type, int pos)
	{
		return nodeLayers.get(0)[id].getWeight(type, pos);
	}

	public double[] getAllWeights(int layer, int id)
	{
		return nodeLayers.get(layer)[id].getAllWeights();
	}
	public double getBias(int id)
	{
		return nodeLayers.get(0)[id].getBias();
	}

	private ArrayList<Double>toArrayList (Double[] convert)
	{
		ArrayList<Double> inputs = new ArrayList<>();
		inputs.addAll( Arrays.asList(convert));
		return  inputs;

	}

	public void removeWeight(int layer, int id, int type, int itemId)
	{
		nodeLayers.get(layer)[id].removeWeight(type,itemId);
	}

	public double execution(ArrayList inputs1, ArrayList inputs2)
	{
		//layer 1
		outputs.set(0, new Double[]{nodeLayers.get(0)[0].calculateInputs(inputs1), nodeLayers.get(0)[1].calculateInputs(inputs2)});

		return  nodeLayers.get(1)[0].calculateInputs(toArrayList(outputs.get(0)));
	}
}
