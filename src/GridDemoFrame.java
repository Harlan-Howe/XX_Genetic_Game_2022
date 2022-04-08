import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GridDemoFrame extends JFrame
{
	SimulationPanel thePanel;
	JLabel scoreLabel, messageLabel;
	GraphPanel gPanel;
	public GridDemoFrame()
	{
		super("Grid Demo");
		
		setSize(1200,600+24+16);
		
		this.getContentPane().setLayout(new BorderLayout());
		gPanel = new GraphPanel();
		
		thePanel = new SimulationPanel(this,gPanel);
		scoreLabel = new JLabel("Score: 0");
		messageLabel = new JLabel("");
		Box southPanel = Box.createHorizontalBox();
		
		
		Box centerPanel = Box.createHorizontalBox();
//		centerPanel.setMinimumSize(new Dimension(300,300));
		thePanel.setPreferredSize(new Dimension(900,700));
		gPanel.setPreferredSize(new Dimension(300,300));
		centerPanel.add(thePanel);
		centerPanel.add(gPanel);
//		this.getContentPane().add(thePanel,BorderLayout.CENTER);
		this.getContentPane().add(southPanel, BorderLayout.SOUTH);
		this.getContentPane().add(centerPanel, BorderLayout.CENTER);
		
		
		southPanel.add(Box.createHorizontalStrut(10));
		southPanel.add(scoreLabel, BorderLayout.SOUTH);
		southPanel.add(Box.createGlue());
		southPanel.add(messageLabel, BorderLayout.SOUTH);
		southPanel.add(Box.createHorizontalStrut(10));
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);	
		thePanel.initiateAnimationLoop(); // uncomment this line if your program uses animation.
		thePanel.assignSimulationPanelBehavior();
	}
	
	public void updateMessage(String message)
	{
		messageLabel.setText(message);
		messageLabel.repaint();
	}
	
	public void updateScore(int score)
	{
		scoreLabel.setText("Score: "+score);
		scoreLabel.repaint();
	}
}
