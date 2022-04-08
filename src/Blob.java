
public class Blob {
	private static int next_id = 0;
	private int id,r,c;
	private int direction;
	private int world_size;
	private boolean isDoneMoving;
	
	
	public Blob()
	{
		id = next_id;
		next_id++;
		world_size = 2;
		r=0;
		c=(int)(Math.random()*world_size);
		direction = (int)(2*(Math.random()))*2-1;
		isDoneMoving = false;
	}
	
	public Blob(int world)
	{
		this();
		world_size = world;
		c = (int)(Math.random()*world_size);
	}
	
	public void moveLateral()
	{
		c+=direction;
		if (c==-1 || c==world_size)
			isDoneMoving = true;
		c = Math.min(Math.max(c, 0), world_size-1);
	}

	public void moveDown()
	{
		r++;
	}
	
	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public int getId() {
		return id;
	}

	public int getR() {
		return r;
	}

	public int getC() {
		return c;
	}

	public boolean isDoneMoving() {
		return isDoneMoving;
	}
	
	
	
}
