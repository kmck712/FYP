package com.example.testapp;

import java.util.ArrayList;

public class Network {

	ArrayList <Node2> nodes;
	ArrayList<Double> outputs;

	Network()
	{
		nodes = new ArrayList<>();
		outputs = new ArrayList<>();
	}

	public void addNode(Node2 newNode)
	{
		nodes.add(newNode);
	}

	public void calculateOutput(int id, )
	{
		nodes.get(id).calculateInputs()
	}
}
