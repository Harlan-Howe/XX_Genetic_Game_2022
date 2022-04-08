import java.util.HashSet;
import java.util.Set;

public class Target {
	private int r,c;
	private Set<Integer> touchedSet;
	private final static int MAX_VALUE = 9;
	
	
	public Target()
	{
		r=0;
		c=0;
		touchedSet = new HashSet<Integer>();
	}
	
	public Target(int r, int c)
	{
		this();
		this.r=r;
		this.c=c;
	}
	
	public void touch(int t)
	{
		touchedSet.add(t);
	}
	
	public int count()
	{
		return Math.min(touchedSet.size(),MAX_VALUE);
	}
	
	public void reset()
	{
		touchedSet.clear();
	}

	public int getR() {
		return r;
	}

	public int getC() {
		return c;
	}
	
	
}
