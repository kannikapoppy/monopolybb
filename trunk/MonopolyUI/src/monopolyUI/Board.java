package monopolyUI;

import javax.swing.*;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import main.*;
import services.*;
import objectmodel.*;
import objectmodel.Dice.DiceThrowResult;

/**
 * This class represent the entire visual board
 * @author Benda & Eizenman
 *
 */
public class Board extends JPanel {

	private static final int LINE_SIZE = 9;
	private static final String WINDOW_TITLE = "Monopoly By Benda And Eizenman";
	private static final String ERROR_INITIALIZING_MSG = "Error Initializing";
	private static final String UNABLE_TO_START_GAME_MSG = "Unable To Start Game";
	private static final String PLAYER_LOST_MSG_SUFFIX = " lost!!!";
	private static final String PLAYER_LOST_MSG_TITLE = "Another one bites the dust !!!";
	private CenterBoard innerBoard = null;
	private EventHandler eventHandler = null;
	
	/**
	 * The game board representation
	 */
	private MonopolyGame monopolyGame;
	/**
	 * mapping between logical cells to visual cells
	 */
	private Hashtable<CellBase, DisplayCell> cellBaseToDisplayCell = new Hashtable<CellBase, DisplayCell> ();

	/**
	 * entry point of the program
	 * @param args
	 */
	public static void main(String[] args) {
      //all swing code runs in the EDT so we need to initialize the main container (JFrame) to run inside of the EDT as well
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame(WINDOW_TITLE);
                //what do we want to happen when the user click on the "X" button
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.getContentPane().setLayout(new BorderLayout());

                //set size
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                Dimension frameSize = new Dimension();
                frameSize.setSize(screenSize.width * 0.5, screenSize.height * 0.5);
                frame.setSize(frameSize);

                frame.setLocationRelativeTo(null);
                
                // setting the menu
                frame.setJMenuBar(new MonopolyMenu());

                frame.setVisible(true);
            }
        });
    }

    /**
     * ctor
     * @param players - players of the game
     * @throws Exception - upon error might throw exception
     */
	public Board(List<Player> players) throws Exception {
		super();
		// Create the logic
		monopolyGame = new MonopolyGame();
		// Create the event Handler & register to the events 
		eventHandler = new EventHandler(this);
		eventHandler.registerEvents();
		
		if (!monopolyGame.initGame(players))
		{
			throw new Exception(ERROR_INITIALIZING_MSG);
		}
		
		initUI(players);
	}
    
    public MonopolyGame getMonopolyGame()
    {
    	return monopolyGame;
    }

    public void StartGame() throws Exception
    {
    	try
    	{
    		if (!monopolyGame.startGame(false))
    		{
    			throw new Exception(UNABLE_TO_START_GAME_MSG);
    		}
    	}
    	catch (Exception ex)
    	{
    		throw ex;
    	}
    }

    /**
     * when finishing a game by starting a new game in the menu
     */
    public void FinishGame()
    {
    	eventHandler.unRegisterEvents();
    }
    
    private void initUI(List<Player> players) {
        // init layout
        this.setLayout(new GridBagLayout());

        // create visual cells
        List<DisplayCell> components = new LinkedList<DisplayCell>();
        for (int i=0 ; i < LINE_SIZE * 4 ; i++) {
        	CellBase logicCell = monopolyGame.getGameBoard().getCellBase().get(i);
        	// create display cell for logical cell
        	DisplayCell cell = new DisplayCell(logicCell);
        	// add it to the mapping
        	cellBaseToDisplayCell.put(logicCell, cell);
        	components.add(cell);
        }
        
    	// set the initial position of all players to the cell 0 (GO)
        for (Player player : players)
    	{
    		components.get(0).AddPlayerToPlayersList(player);
    	}

        Iterator<DisplayCell> componentIterator = components.iterator();

        //Add Panels for Each of the four sides
        for (int sideIndex = 0; sideIndex < 4; sideIndex++) {
            for (int lineIndex = 0; lineIndex < LINE_SIZE; lineIndex++) {
                JComponent component = componentIterator.next();
                switch(sideIndex)
                {
                    case 0:
                        //top line
                        addComponent (lineIndex, 0, component);
                        break;
                    case 1:
                        //right line
                        addComponent (LINE_SIZE, lineIndex, component);
                        break;
                    case 2:
                        //bottom line - and in reverse order
                        addComponent (LINE_SIZE - lineIndex, LINE_SIZE, component);
                        break;
                    case 3:
                        //left line - and in reverse order
                        addComponent (0, LINE_SIZE - lineIndex, component);
                        break;
                }
            }
        }

        // Main Inner Area Notice Starts at (1,1) and takes up 8X8
        innerBoard = new CenterBoard(monopolyGame);
        this.add(innerBoard,
            new GridBagConstraints(1,
                    1,
                    8,
                    8,
                    2, 2,
                    GridBagConstraints.CENTER,
                    GridBagConstraints.CENTER,
                    new Insets(0, 0, 0, 0), 0, 0));
	}

    private void addComponent(int gridX, int gridY, JComponent component) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = gridX;
        c.gridy = gridY;
        c.weightx = 1;
        c.weighty = 1;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.BOTH;
        c.ipadx = 3;
        c.ipady = 3;
        this.add(component, c);
    }

    /**
     * Visually marks player p as the current player by pointing an arrow to it 
     * @param p - player who turn is now
     */
    public void SetPlayingUser(final Player p)
    {
    	innerBoard.SetPlayingUser(p);
    	
    	validate();
		repaint();
    }
    
    /**
     * visually moves the player on the board
     * @param player - player to move
     * @param origin - from cell
     * @param destination - to cell
     */
    public void MovePlayer(final Player player, final CellBase origin, final CellBase destination)
    {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() 
			    { 
					// remove the player from origin cell
					DisplayCell originDisplayCell = cellBaseToDisplayCell.get(origin);
			    	originDisplayCell.RemovePlayerFromPlayersList(player);
			    	
			    	// add the user to his new cell
			    	DisplayCell destinationDisplayCell = cellBaseToDisplayCell.get(destination);
			    	if (destination.getType().compareTo("Jail") == 0 &&
			    			player.isInJail())
			    	{
			    		// if the user is in jail we add him to a box in the middle of
			    		// the cell so it would like he is in jail.
			    		destinationDisplayCell.AddPlayerToSecondaryPlayersList(player);
			    	}
			    	else
			    	{
			    		destinationDisplayCell.AddPlayerToPlayersList(player);
			    	}
			    	
			    	validate();
			    	repaint();
			    }
			});
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    /**
     * very good. player is getting out of jail
     * @param player
     */
    public void GetPlayerOutOfJail(final Player player)
    {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() 
			    { 
					CellBase prisonCell = monopolyGame.getGameBoard().getCellBase().get(player.getBoardPosition());
					
					// we remove the player from the center cell box and add him to bottom box
					DisplayCell prisonDisplayCell = cellBaseToDisplayCell.get(prisonCell);
					prisonDisplayCell.RemovePlayerFromSecondaryPlayersList(player);
					prisonDisplayCell.AddPlayerToPlayersList(player);
			    }
			});
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void SimulateDiceThrow(DiceThrowResult diceThrow)
	{
    	innerBoard.SimulateDiceThrow(diceThrow);
	}
    
    public void EnableDiceThrow()
    {
    	innerBoard.EnableDiceThrow();
    }
    
    public void SetCellOwner(CellBase cell, Player owner)
    {
    	DisplayCell cellDiaplsyCell = cellBaseToDisplayCell.get(cell);
    	cellDiaplsyCell.SetOwner(owner);
    }
    
    public void BuildHouse(final City city)
    {
    	SwingUtilities.invokeLater(new Runnable() {
			public void run() 
			{
		    	DisplayCell cellDiaplsyCell = cellBaseToDisplayCell.get(city);
		    	cellDiaplsyCell.BuildHouse();
		    	cellDiaplsyCell.validate();
		    	cellDiaplsyCell.repaint();
			}
    	});
    }

    public void UpdateBalance(Player p)
    {
    	innerBoard.UpdatePlayerDisplay(p);
    }
    
    public void UpdateBalanceForAllPlayers()
    {
    	for (Player p : monopolyGame.getPlayers())
    	{
    		innerBoard.UpdatePlayerDisplay(p);
    	}
    }
    
    public void UpdatePlayerLost(final Player p)
    {
    	try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() 
			    { 
					innerBoard.UpdatePlayerDisplay(p);
					
					// remove the player from the board.
					CellBase currentPosition = monopolyGame.getGameBoard().getCellBase().get(p.getBoardPosition());
					DisplayCell currentPositionDisplay = cellBaseToDisplayCell.get(currentPosition);
					if (p.isInJail())
					{
						currentPositionDisplay.RemovePlayerFromSecondaryPlayersList(p);
					}
					else
					{
						currentPositionDisplay.RemovePlayerFromPlayersList(p);
					}
					
					// clear all cells which were owned by the loser, so visual will be coordinated
					// with logics
					for (CellBase cell : cellBaseToDisplayCell.keySet())
			    	{	
			    		if (cell instanceof Asset)
			    			cellBaseToDisplayCell.get(cell).SetOwner(((Asset)cell).getOwner());
			    		if (cell instanceof City)
			    			cellBaseToDisplayCell.get(cell).SetOwner(((City)cell).getOwner());
			    	}
			    	
			    	// show a message that the user has lost
					JOptionPane.showMessageDialog(null, 
							p.getName() + PLAYER_LOST_MSG_SUFFIX,
							PLAYER_LOST_MSG_TITLE, JOptionPane.OK_OPTION);
			    }
			});
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}
