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

public class Board extends JPanel {

	private static final int LINE_SIZE = 9;
	private CenterBoard innerBoard = null;
	
	/**
	 * The game board representation
	 */
	private MonopolyGame monopolyGame;
	private Hashtable<CellBase, DisplayCell> cellBaseToDisplayCell = new Hashtable<CellBase, DisplayCell> ();

	public static void main(String[] args) {
      //all swing code runs in the EDT so we need to initialize the main container (JFrame) to run inside of the EDT as well
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Monopoly By Benda And Eizenman");
                //what do we want to happen when the user click on the "X" button
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.getContentPane().setLayout(new BorderLayout());
                
                // TODO: add a panel which displays an image of monopoly, just for the beauty

                //set size
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                Dimension frameSize = new Dimension();
                frameSize.setSize(screenSize.width * 0.5, screenSize.height * 0.5);
                frame.setSize(frameSize);

                frame.setLocationRelativeTo(null);
                
                frame.setJMenuBar(new MonopolyMenu());

                frame.setVisible(true);
            }
        });
    }

    public Board(List<Player> players) throws Exception {
		super();
		// Create the logic
		monopolyGame = new MonopolyGame();
		// Create the event Handler & register to the events 
		EventHandler eventHandler = new EventHandler(this);
		eventHandler.registerEvents(monopolyGame);
		
		if (!monopolyGame.initGame(players))
		{
			throw new Exception("Error Initializing");
		}
		
		initUI(players);
	}
    
    public void StartGame() throws Exception
    {
    	try
    	{
    		if (!monopolyGame.startGame(false))
    		{
    			throw new Exception("Unable To Start Game");
    		}
    	}
    	catch (Exception ex)
    	{
    		throw ex;
    	}
    }

    private void initUI(List<Player> players) {
        // init layout
        this.setLayout(new GridBagLayout());

        List<DisplayCell> components = new LinkedList<DisplayCell>();
        for (int i=0 ; i < LINE_SIZE * 4 ; i++) {
        	CellBase logicCell = monopolyGame.getGameBoard().getCellBase().get(i);
        	DisplayCell cell = createCellPanel(logicCell);
        	cellBaseToDisplayCell.put(logicCell, cell);
        	components.add(cell);
        }
        
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

        // Main Inner Area Notice Starts at (1,1) and takes up 11x11
        innerBoard = new CenterBoard(players);
        this.add(innerBoard,
            new GridBagConstraints(1,
                    1,
                    6,
                    6,
                    2, 2,
                    GridBagConstraints.CENTER,
                    GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
	}

    private DisplayCell createCellPanel(CellBase cell) {
    	return new DisplayCell(cell);
    }
    
    /*private JPanel createInnerPanel(String text) {
        JPanel panel = new JPanel(new BorderLayout()) ;
        panel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.RED));
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }*/

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

    public void MovePlayer(final Player player, final CellBase origin, final CellBase destination)
    {
    	try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() 
			    { 
					DisplayCell originDisplayCell = cellBaseToDisplayCell.get(origin);
			    	originDisplayCell.RemovePlayerFromPlayersList(player);
			    	
			    	DisplayCell destinationDisplayCell = cellBaseToDisplayCell.get(destination);
			    	if (destination.getType().compareTo("Jail") == 0 &&
			    			player.isInJail())
			    	{
			    		destinationDisplayCell.AddPlayerToSecondaryPlayersList(player);
			    	}
			    	else
			    	{
			    		destinationDisplayCell.AddPlayerToPlayersList(player);
			    	}
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

    public void GetPlayerOutOfJail(final Player player)
    {
    	try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() 
			    { 
					CellBase prisonCell = monopolyGame.getGameBoard().getCellBase().get(player.getBoardPosition());
					
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
    	innerBoard.UpdateBalance(p);
    }
    
    public void UpdatePlayerLost(final Player p)
    {
    	try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() 
			    { 
					innerBoard.MarkPlayerAsLost(p);
			    	for (CellBase cell : cellBaseToDisplayCell.keySet())
			    	{	
			    		if (cell instanceof Asset)
			    			cellBaseToDisplayCell.get(cell).SetOwner(((Asset)cell).getOwner());
			    		if (cell instanceof City)
			    			cellBaseToDisplayCell.get(cell).SetOwner(((City)cell).getOwner());
			    	}
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
