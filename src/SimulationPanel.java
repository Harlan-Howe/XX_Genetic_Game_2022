import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class SimulationPanel extends JPanel implements MouseListener, KeyListener
{
	// Constants
	public final static int NUM_ROWS = 24;
	public final static int NUM_COLS = 36;
	public final static int NUM_TARGETS = 24;
	public final static int NUM_BLOBS = 50;
	public final static int PLATFORM_MEAN_WIDTH = 10;
	public final static int PLATFORM_WIDTH_VARIATION = 4;
	public final static int PLATFORM_MEAN_SPACE = 5;
	public final static int PLATFORM_SPACE_VARIATION = 2;
	public final static int MAX_SIMULATION_STEPS = 100;
	public final static int NUM_AFFECTORS_PER_CHROMOSOME = 40;
	public final static int NUM_CHROMOSOMES = 32;
	public final static int NUM_TRIALS = 500;
	public final static double MUTATION_RATE = 0.005;
	
	private Cell[][] theGrid;
	private Cell[][] masterGrid;
	private ArrayList<Target> targetList;
	private ArrayList<Blob> blobList;
	
	private boolean isTraining;
	private boolean isSimulating;
	private boolean currentlyPainting;
	
	private int trial_num;
	private int simulationStep;
	
	private ArrayList<Affector> currentAffectorList;
	private int score;
	private int num_live_blobs;
	private ArrayList<Integer> bestResults;
	
	
	
	private GridDemoFrame myParent;
	private GraphPanel myGraph;
	
	private List<Integer> scoreList;
	private ArrayList<String> chromosomeList;
	
	
	private Font chromosomeFont;
	
	public SimulationPanel(GridDemoFrame parent, GraphPanel gPanel)
	{
		super();
		theGrid = new Cell[NUM_ROWS][NUM_COLS];
		chromosomeFont = new Font("Courier",Font.PLAIN,10);

		//resetCells();
		setupMasterGrid();
		updateGridFromMaster();
		setBackground(Color.BLACK);
		addMouseListener(this);
		myParent = parent;
		myGraph = gPanel;
		
		buildRandomChromosomeList();
		
		trial_num = 0;
		bestResults = new ArrayList<Integer>(); // data headed to the graph panel
		
		isTraining = false;
		isSimulating = true;
		currentlyPainting = false;
	}	
	
	/**
	 * if we are simulating, this is the method that will instigate each new run of the
	 * simulation for all the chromosomes, in turn.
	 */
	public void assignSimulationPanelBehavior()
	{
		while(isTraining) // if we are in training mode, sleep for a short time to let the training happen.
		{
			try
			{
				Thread.sleep(50);
			}catch (InterruptedException IExp)
			{
				System.out.println("Interrupted.");
			}
		}
		
		for (String s:chromosomeList)
		{
			isSimulating = false;
		
			// reset the simulation 
			updateGridFromMaster();
			resetTargets();
			setupBlobs();
			simulationStep =0;
			score = 0;
			// select the next chromosome on the list...
			currentAffectorList = (ArrayList<Affector>)(generateAffectorListFromChromosome(s));
			
			// start the simulation
			isSimulating = true;
			while (isSimulating) // don't do anything while the simulation is in progress. When that thread ends, it will set isSimulating to false.
				try
				{
					Thread.sleep(50);
				}catch (InterruptedException IExp)
			{
					System.out.println("Interrupted.");
			}
					
			System.out.println("That was "+s);
		}
		
	}	
	
	
	/**
	 * sorts the lists of scores and chromosomes so that the scores are sorted in descending order and the chromosomes
	 * are still aligned with their corresponding scores. So, for example, if the fifth score is the highest, you
	 * would expect that score to be moved to the start of the list, and the fifth chromosome will also be sorted to
	 * the start of the list. The primary goal of this method is to sort the chromosomes from most successful to least
	 * successful.
	 */
	public void sortListsByScoreDescending()
	{
		// we're going to use the "Bubble Sort" - it's really inefficient for long lists, 
		//   but should be fine for these short ones, and it's easy to write.
		for (int j=NUM_CHROMOSOMES-1; j>0; j--)
			for (int i=0; i<j; i++)
				if (scoreList.get(i)<scoreList.get(i+1))
				{
					int temp = scoreList.get(i);
					scoreList.set(i, scoreList.get(i+1));
					scoreList.set(i+1, temp);
					String tempchromosome = chromosomeList.get(i);
					chromosomeList.set(i, chromosomeList.get(i+1));
					chromosomeList.set(i+1, tempchromosome);
				}
		
	}
	/**
	 * based on the current set of chromosomes and their scores, generate a
	 * set of chromosomes that are likely to do better. 
	 * Replaces chromosomeList with a "better" list.
	 */
	public void evolve()
	{
		//TODO: You write this method!
	}
	
	/**
	 * randomly select one of the chromosomes, with proportional preference 
	 * to the high scoring ones.
	 * @param n - a randomly selected number 0 <= n < total score
	 * @return one of the chromosomes on the chromosomeList
	 */
	public String selectChromosome(int n)
	{
		// TODO: recommended - I think you will find this handy.
		return ""; // temp stub code for the sake of compiling. Replace this.
	}
		
	
	
	
	/**
	 * Produces a new chromosome found by swapping the portions of the given chromosome strings. 
	 * @param chromosome1 - a String
	 * @param chromosome2 - a String of the same length as chromosome1
	 * @return - a resulting "child" string.
	 */
	public String haveSex(String chromosome1, String chromosome2)
	{
		String child;
		child="";

		// TODO: You write this.
		
		return child;
	}
	
	
	/**
	 * makes a new "masterGrid," filled with charcoal blocks, floors and targets
	 */
	public void setupMasterGrid()
	{
		masterGrid = new Cell[NUM_ROWS][NUM_COLS];
		for (int r=0; r<NUM_ROWS; r++)
			for (int c=0; c<NUM_COLS; c++)
				masterGrid[r][c] = new Cell(0,r,c,"",false);
		setupFloors();
		setupTargets();
		
	}
	/**
	 * copies the masterGrid into theGrid. Used to reset theGrid to the game
	 * start state.
	 */
	public void updateGridFromMaster()
	{
		for (int r=0; r<NUM_ROWS; r++)
			for (int c=0; c<NUM_COLS; c++)
			{
				theGrid[r][c] = (Cell)masterGrid[r][c].clone();
				//System.out.println(theGrid[r][c]);
			}
		repaint();
	}
	
	/**
	 * creates a set of floors in the masterGrid. Likely to be called once
	 * at the start of the program.
	 */
	public void setupFloors()
	{
		// only puts floors on the even number rows
		for (int r=2; r<NUM_ROWS-2; r+=2)
		{
			int x=(int)(Math.random()*(PLATFORM_MEAN_WIDTH+PLATFORM_WIDTH_VARIATION)-PLATFORM_MEAN_WIDTH/2-PLATFORM_WIDTH_VARIATION/2);
			while (x<NUM_COLS)
			{
				// alternates sections of floor with gaps
				int numBlocks = PLATFORM_MEAN_WIDTH-PLATFORM_WIDTH_VARIATION+(int)(Math.random()*2*PLATFORM_WIDTH_VARIATION);
				for (int i=0; i<numBlocks; i++)
				{
					if (x>-1)
						masterGrid[r][x].setColorID(5);
					x++;
					if (x==NUM_COLS)
						break;
				}
				int numSpaces = PLATFORM_MEAN_SPACE-PLATFORM_SPACE_VARIATION+(int)(Math.random()*2*PLATFORM_SPACE_VARIATION);
				x+= numSpaces;
			}
		
		}
		// Make the bottom floor.
		for (int c=0; c<NUM_COLS; c++)
			masterGrid[NUM_ROWS-1][c].setColorID(5);	
	}
	
	/**
	 * adds a set of targets to the masterGrid. This is likely to be called
	 * once at the start of the program.
	 */
	public void setupTargets()
	{
		targetList = new ArrayList<Target>();
		for (int i = 0; i<NUM_TARGETS; i++)
		{
			int r = (int)(Math.random()*(NUM_ROWS-4)/2)*2+1;
			int c = (int)(Math.random()*NUM_COLS);
			targetList.add(new Target(r,c));
		}
	}
	
	/**
	 * resets all the targets back to zero.
	 */
	public void resetTargets()
	{
		for (Target t:targetList)
			t.reset();
	}
	
	/**
	 * creates a bunch of "blobs" at random positions at the top of the screen.
	 * likely to be called once at the start of each simulation.
	 */
	public void setupBlobs()
	{
		blobList = new ArrayList<Blob>();
		for (int i=0; i<NUM_BLOBS; i++)
			blobList.add(new Blob(NUM_COLS));
		
	}
	
	/**
	 * creates a list of Affectors that will compose a chromosome.
	 * likely to be called several times at the start of the program, but
	 * future chromosomes will be combinations and mutations of these ones.
	 * @return List of Affectors
	 */
	public List<Affector> generateRandomAffectorList()
	{
		ArrayList<Affector> result = new ArrayList<Affector>();
		for (int i=0; i<NUM_AFFECTORS_PER_CHROMOSOME; i++)
		{
			result.add(generateRandomAffector());
		}
		return result;
		
	}
	
	/**
	 * generates a random affector in a legal location; L & R affectors go on odd rows; 
	 * F & H affectors go on even rows
	 * @return a randomly generated affector
	 */
	public Affector generateRandomAffector()
	{
		int type = (int)(Math.random()*4);
		int c = (int)(Math.random()*NUM_COLS);
		// Right and Left affectors go on the odd rows; holes and floors go on the even ones.
		int r = (int)(Math.random()*(NUM_ROWS-4)/2)*2+1+type/2;
		return new Affector(type,r,c);
	}
	
	/**
	 * creates a string representing a randomly chosen, legal affector.
	 * @return a 5-character string.
	 */
	public String generateRandomAffectorString()
	{
		return generateRandomAffector().toString();
	}
	
	/**
	 * create a list of "chromosome" strings that correspond to viable Affector 
	 * lists. Likely to be called once at the start of the program. 
	 */
	public void buildRandomChromosomeList()
	{
		chromosomeList = new ArrayList<String>();
		for (int i=0; i<NUM_CHROMOSOMES; i++)
		{
			List<Affector> trial = generateRandomAffectorList();
			chromosomeList.add(getChromosomeFromAffectorList(trial));
		}
	}
	
	/**
	 * places cells in theGrid corresponding to the items in the given 
	 * List of Affectors
	 * @param aList - the affectors we wish to show.
	 */
	public void placeAffectorListOnGrid(List<Affector> aList)
	{
		for (Affector a:aList)
		{

			if (a.getType()==3)
			{
				for (int c=Math.max(0,a.getC()-1); c<=Math.min(NUM_COLS-1,a.getC()+1); c++)
				{
					theGrid[a.getR()][c].setColorID(5);
					theGrid[a.getR()][c].setMarker("F");
					theGrid[a.getR()][c].setDisplayMarker(true);

				}
			}
			else
			{
				theGrid[a.getR()][a.getC()].setColorID(2 + a.getType());
				theGrid[a.getR()][a.getC()].setMarker(a.getLabel());
				theGrid[a.getR()][a.getC()].setDisplayMarker(true);
			}
		}
	}
	
	/**
	 * creates a list of scores that correspond to the list of chromosomes; 
	 * specifically, these are the scores earned by each chromosome.
	 * @param chromosomeList - a list of chromosome Strings
	 * @return - a list of Integers, corresponding to the chromosomes.
	 */
	public List<Integer> getScoresForAllChromosomes(List<String> chromosomeList)
	{
		List<Integer> scores = new ArrayList<Integer>();
		for (String g : chromosomeList)
			scores.add(getScoreForAffectorList(generateAffectorListFromChromosome(g)));
		return scores;
	}
	
	/**
	 * run a complete simulation for a given Affector list and return the
	 * score for this run.
	 * @param aList - the list to test
	 * @return the score for this run.
	 */
	public int getScoreForAffectorList(List<Affector> aList)
	{
		score = 0;
		currentAffectorList = (ArrayList<Affector>)aList;
		int stepCount = 0;
		boolean stillMoving;
		updateGridFromMaster();
		resetTargets();
		placeAffectorListOnGrid(aList);
		setupBlobs();
		
		do
		{
			stillMoving = doOneStep();
			stepCount++;
		} while(stillMoving && stepCount < MAX_SIMULATION_STEPS);
		return score;
	}
	
	
	
	/**
	 * simulates one step in this scenario - one motion for each blob.
	 * @return whether any of the blobs can still move.
	 */
	public boolean doOneStep()
	{
		updateGridFromMaster();
		placeAffectorListOnGrid(currentAffectorList);
		num_live_blobs = 0;
		score = 0;
		for(Blob b:blobList)
		{
			
			if (b.getR()==NUM_ROWS-2)
				score+=2;
			if (b.isDoneMoving())
				continue;
			
			num_live_blobs++;
			
			for (Affector a:currentAffectorList)
			{
				if (a.getType()<2 && a.getC()==b.getC() && a.getR()==b.getR())
				{
					b.setDirection(a.getType()*2-1);
				}			
			}
			
			if (theGrid[b.getR()+1][b.getC()].getColorID()!=5)
				b.moveDown();
			else
				b.moveLateral();
			theGrid[b.getR()][b.getC()].setColorID(1);
			for (Target t:targetList)
			{
				if (t.getR() == b.getR() && t.getC() == b.getC())
					t.touch(b.getId());
				
			}
			
			
		}
		for (Target t:targetList)
			score+=t.count();
		return num_live_blobs>0;
	}
	
	/**
	 * converts a String "chromosome" into a corresponding list of Affectors
	 * @param chromosome
	 * @return list of Affectors from this chromosome
	 */
	public List<Affector> generateAffectorListFromChromosome(String chromosome)
	{
		ArrayList<Affector> result = new ArrayList<Affector>();
		for (int i=0; i<chromosome.length(); i+=5)
			result.add(new Affector(chromosome.substring(i, i+5)));
		return result;
	}
	
	/**
	 * converts a list of Affectors into a "chromosome" String
	 * @param aList
	 * @return the "chromosome" that corresponds to this list.
	 */
	public String getChromosomeFromAffectorList(List<Affector> aList)
	{
		String result = "";
		for (Affector a:aList)
			result+=a.toString();
		return result;
	}
	
	public void paintComponent(Graphics g)
	{
		// because we have several threads doing stuff at once, we need to make sure that we keep
		//  track of when we are painting stuff, so that we don't change things while we are in the
		//  middle of the paintComponent.
		currentlyPainting = true;
		
		super.paintComponent(g);
		g.setFont(chromosomeFont);
		if (isTraining)
		{
			int y = 10;
			g.setColor(Color.white);
			for (String chromosome:chromosomeList)
			{
				g.drawString(chromosome, 0, y);
				y+=12;
			}
		}		
		else if (isSimulating)
		{
			for (Target t:targetList)
			{
				theGrid[t.getR()][t.getC()].setMarker(""+t.count());
				theGrid[t.getR()][t.getC()].setDisplayMarker(true);
			}
			
			for (int r =0; r<NUM_ROWS; r++)
				for (int c=0; c<NUM_COLS; c++)
					theGrid[r][c].drawSelf(g);
		}
		
		// ok, we're done. It's safe to change the variables again.
		currentlyPainting = false;
	}
	
	/**
	 * the mouse listener has detected a click, and it has happened on the cell in theGrid at row, col
	 * @param row
	 * @param col
	 */
	public void userClickedCell(int row, int col)
	{

	}
	
	/**
	 * Here's an example of a simple dialog box with a message.
	 */
	public void makeGameOverDialog()
	{
		JOptionPane.showMessageDialog(this, "Game Over.");
		
	}
	
	//============================ Mouse Listener Overrides ==========================

	@Override
	// mouse was just released within about 1 pixel of where it was pressed.
	public void mouseClicked(MouseEvent e)
	{
		// mouse location is at e.getX() , e.getY().
		// if you wish to convert to the rows and columns, you can integer-divide by the cell size.
		int col = e.getX()/Cell.CELL_SIZE;
		int row = e.getY()/Cell.CELL_SIZE;
		userClickedCell(row,col);
		
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		// mouse location is at e.getX() , e.getY().
		// if you wish to convert to the rows and columns, you can integer-divide by the cell size.
				
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		// mouse location is at e.getX() , e.getY().
		// if you wish to convert to the rows and columns, you can integer-divide by the cell size.
		
	}

	@Override
	// mouse just entered this window
	public void mouseEntered(MouseEvent e)
	{
		// do nothing.
		
	}

	@Override
	// mouse just left this window
	public void mouseExited(MouseEvent e)
	{
		// do nothing
		
	}
	//============================ Key Listener Overrides ==========================

	@Override
	/**
	 * user just pressed and released a key. (May also be triggered by autorepeat, if key is held down?
	 * @param e
	 */
	public void keyTyped(KeyEvent e)
	{
		char whichKey = e.getKeyChar();
		myParent.updateMessage("User just typed \""+whichKey+"\"" );
		System.out.println(whichKey);
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		// do nothing
		
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		// do nothing
		
	}
	// ============================= animation stuff ======================================
	/**
	 * if you wish to have animation, you need to call this method from the GridDemoFrame AFTER you set the window visibility.
	 */
	public void initiateAnimationLoop()
	{
		Thread aniThread = new Thread( new AnimationThread(100));
		aniThread.start();
	}
	
	/**
	 * this method should be called on a regular basis, determined by the delay set in the thread.
	 * Note: By default, this will not get called unless you uncomment the code in the GridDemoFrame's constructor
	 * that creates a thread.
	 *
	 */
	public void animationStep()
	{
		//theGrid[0][0].cycleColorIDBackward();
		if (currentlyPainting)
			return;
		if (isTraining)
		{
			if (trial_num < NUM_TRIALS)
			{
				
				// do the simulation for all the chromosomes.
				scoreList = getScoresForAllChromosomes(chromosomeList);
				sortListsByScoreDescending();

				// update the message in the lower-left corner
				myParent.updateScore(scoreList.get(0));
				
				// update the list of trails' scores by appending the best of this trial				
				bestResults.add(scoreList.get(0));
				myGraph.setList(bestResults);
				
				// replace the current set of chromosomes with a new set of children chromosomes.
				evolve();
				trial_num++;
				
				// update the message in the lower-right corner
				myParent.updateMessage("Training step "+trial_num+"/"+NUM_TRIALS);
				
			}
			else
			{
				isTraining = false;
				isSimulating = true;
			}
		}
		
		else if (isSimulating)
		{
			boolean hasMoreToDo = doOneStep();
			
			myParent.updateMessage("Num live blobs: "+num_live_blobs);
			myParent.updateScore(score);
			simulationStep++;
			if (! hasMoreToDo || simulationStep > MAX_SIMULATION_STEPS)
				isSimulating = false;
		}
		else
			myParent.updateMessage("Simulation over.");
		
		
		
		repaint();
	}
	// ------------------------------- animation thread - internal class -------------------
	public class AnimationThread implements Runnable
	{
		long start;
		long timestep;
		public AnimationThread(long t)
		{
			timestep = t;
			start = System.currentTimeMillis();
		}
		@Override
		public void run()
		{
			long difference;
			while (true)
			{
				difference = System.currentTimeMillis() - start;
				if (difference >= timestep)
				{
					animationStep();
					start = System.currentTimeMillis();
				}
				try
				{	Thread.sleep(1);
				}
				catch (InterruptedException iExp)
				{
					System.out.println(iExp.getMessage());
					break;
				}
			}
			
		}
		
	}
}
