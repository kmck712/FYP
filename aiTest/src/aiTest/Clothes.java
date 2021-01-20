package aiTest;

public class Clothes {
	
	String name;
	double initalWeight;
	Node clothesN;
	int type; //type represent the kind of clothing it is. 1 = under top 2 = over top 3 = bottom
	int id;
	
	public Clothes(String name, double Weight, int type,int id,  double[] nWeight)
	{
		this.id = id;
		this.name = name;
		this.initalWeight = Weight;
		this.type = type;
		double defaultW[] = nWeight;
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
