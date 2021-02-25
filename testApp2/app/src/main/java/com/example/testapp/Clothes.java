package com.example.testapp;

public class Clothes {
	
	private String name;
	int inputValue;
	int type; //type represent the kind of clothing it is. 1 = under top 2 = over top 3 = bottom
	int id;
	
	public Clothes(String name, int type,int id)
	{
		this.id = id;
		this.name = name;
		this.type = type;
	}
	
	
	protected void setInputValue(int input)
	{
		inputValue = input;
	}
	
	protected int getInputValue()
	{
		return(inputValue);
	}
	
	protected String getName()
	{
		return(name);
	}
	protected int getId()
	{
		return(id);
	}
	protected int getType()
	{
		return(type);
	}
}
