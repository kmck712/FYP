package com.example.testapp;

import java.util.ArrayList;
import java.util.Arrays;

public class Network {

	//stores the output of each layer in the network
	ArrayList<Double[]> outputs;
	//Stores the layers of nodes in the network including hidden,input and output layers
	ArrayList<Node[]> nodeLayers;

	Network() {
		//initialises each array list
		nodeLayers = new ArrayList();
		outputs = new ArrayList<>();

	}

	//need to have it so it takes the other layers as well
	Network(Node[] oldNodes)
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
		nodeLayers.add(new Node[] {new Node(3), new Node(1)});
		outputs.add(null);
	}

	public void addNewLayer(int nodesIn)
	{
		nodeLayers.add(createNodes(nodesIn,nodeLayers.get(0).length));
		outputs.add(null);

	}
	public void addNewLayer(double Weights)
	{
		nodeLayers.add(new Node[]{new Node(3, Weights)});
	}

	private Node[] createNodes(int size, int previousLayer)
	{
		Node[] output = new Node[size];
		for (int i = 0; i < size; i ++)
		{
			output[i] = new Node(1);
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
	public void calculateOutput()
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
		//Computes the outputs for the first layer of inputs
		outputs.set(0, new Double[]{nodeLayers.get(0)[0].calculateInputs(inputs1), nodeLayers.get(0)[1].calculateInputs(inputs2)});

		//computes all of the hidden layer node output
		for (int i = 1; i < nodeLayers.size(); i ++)
		{
			//calculates the outputs for the layer and stores them for the next layer to use
			Double [] layerOutputs = new Double[nodeLayers.get(i).length];
			for (int j = 0; j < nodeLayers.get(i).length; j ++)
			{
				//calculates the output based on the previous output
				layerOutputs[j] = nodeLayers.get(i)[j].calculateInputs(toArrayList(outputs.get(i-1)));
			}
			outputs.set(i,layerOutputs);
		}
		return outputs.get(outputs.size()-1)[0];
		//return  nodeLayers.get(1)[0].calculateInputs(toArrayList(outputs.get(outputs.size())));
	}

	public int getNumberOfLayers()
	{return nodeLayers.size();}

	public int getNumberOfWeights(int layer){return 0;}
}
