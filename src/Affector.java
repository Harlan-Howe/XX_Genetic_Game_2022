
public class Affector {
	private int type,r,c;
	private String[] typeCode = {"L","R","H","F"};
	
	public Affector(int type, int r, int c)
	{
		this.type = type;
		this.r = r;
		this.c = c;
	}
	
	public Affector(String s)
	{
		String typeString = s.substring(0, 1);
		for (type=0; type<typeCode.length; type++)
			if (typeString.equals(typeCode[type]))
				break;
		r= Integer.parseInt(s.substring(1,3));
		c= Integer.parseInt(s.substring(3,5));
		
		
	}
	
	public String toString()
	{
		return typeCode[type]+String.format("%02d%02d", r,c);
	}

	public int getType() {
		return type;
	}

	public int getR() {
		return r;
	}

	public int getC() {
		return c;
	}
	public String getLabel()
	{
		return typeCode[type];
	}
	
	
}
