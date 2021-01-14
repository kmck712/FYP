package aiTest;

public class Clothes {
	
	String name;
	double initalWeight;
	Node clothesN;
	int type; //type represent the kind of clothing it is. 1 = under top 2 = over top 3 = bottom
	
	public Clothes(String name, double Weight, int type)
	{
		this.name = name;
		this.initalWeight = Weight;
		this.type = type;
		double defaultW[] = {0.5,0.5};
		clothesN = new Node(defaultW);
	}
	
	void changeW(double error)
	{
		clothesN.changeW(error);
	}
	
	
	double getWeight()
	{
		return(initalWeight);
	}
}
