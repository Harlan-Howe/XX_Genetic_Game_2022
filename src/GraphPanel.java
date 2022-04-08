import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import javax.swing.JPanel;

public class GraphPanel extends JPanel {

	private List<Integer> scores;
	public static final int LEFT_MARGIN = 20;
	public static final int RIGHT_MARGIN = 10;
	public static final int TOP_MARGIN = 10;
	public static final int BOTTOM_MARGIN = 20;
	
	public GraphPanel()
	{
		super();
		this.setBackground(Color.LIGHT_GRAY);
	}
	
	public void setList(List<Integer> list)
	{
		scores = list;
		repaint();
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if (scores == null || scores.size()<2)
			return;
		int maxWidth = scores.size();
		int maxHeight = 0;
		for (int i:scores)
		{
			if (i>maxHeight)
				maxHeight = i;
		}
			
		double xScale = (this.getWidth()-LEFT_MARGIN-RIGHT_MARGIN)*1.0/maxWidth;
		double yScale = (this.getHeight()-TOP_MARGIN-BOTTOM_MARGIN)*1.0/maxHeight;
		
		g.setColor(Color.BLACK);
		g.drawLine(LEFT_MARGIN, TOP_MARGIN, LEFT_MARGIN, getHeight()-BOTTOM_MARGIN);
		g.drawLine(LEFT_MARGIN, getHeight()-BOTTOM_MARGIN, getWidth()-RIGHT_MARGIN, getHeight()-BOTTOM_MARGIN);
		
		for (int i=0; i<maxHeight; i+=25)
			g.drawLine(0, (int)(getHeight()-BOTTOM_MARGIN-yScale*i), 
					   LEFT_MARGIN, (int)(getHeight()-BOTTOM_MARGIN-yScale*i));
		
		for (int i=0; i<maxWidth; i+=50)
			g.drawLine((int)(LEFT_MARGIN +xScale*i), getHeight()-BOTTOM_MARGIN,
					   (int)(LEFT_MARGIN+ xScale*i), getHeight()-1);
		
		
		g.setColor(Color.RED);
		for (int i=0;i<maxWidth; i++)
		{	int x = (int)(LEFT_MARGIN+xScale*i);
		    int y = (int)(getHeight()-BOTTOM_MARGIN-yScale*scores.get(i));
		    g.fillOval(x-2,y-2,4,4);
		}
	
	}
}
