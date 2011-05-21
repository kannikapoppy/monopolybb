package monopolyUI;

import java.lang.reflect.InvocationTargetException;
import javax.swing.*;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import objectmodel.Asset;
import objectmodel.CellBase;
import objectmodel.City;
import objectmodel.GameBoard;
import src.client.PlayerDetails;
import src.client.Server;
import src.monopolyUI.GetEventsTask;

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
	
        private int playerId;
        private java.util.Timer getAllEventsTimer;
        java.util.List<PlayerDetails> players;

        private CenterBoard innerBoard = null;
	
	/**
	 * mapping between logical cells to visual cells
	 */
	private java.util.Hashtable<CellBase, DisplayCell> cellBaseToDisplayCell = new java.util.Hashtable<CellBase, DisplayCell> ();
        private java.util.Hashtable<Integer, CellBase> cellIDToCellBase = new java.util.Hashtable<Integer, CellBase> ();

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
	public Board(String gameName, String playerName) {
		super();
                this.playerId = playerId;
		getAllEventsTimer = Server.getInstance().startPolling("Game Players Timer",
                        new GetEventsTask(gameName, this, playerName), 0, 1);
	}
    
    public void initUI(final java.util.List<PlayerDetails> players)
    {
        this.players = players;

        // init layout
        this.setLayout(new GridBagLayout());

        String boardXML = Server.getInstance().getGameBoardXML();
        JAXBContext jContext;
        GameBoard gameBoard = null;
        try
        {
            jContext = JAXBContext.newInstance("objectmodel", objectmodel.ObjectFactory.class.getClassLoader());
            Unmarshaller unmarshaller = jContext.createUnmarshaller() ;
            InputStream is = new ByteArrayInputStream(boardXML.getBytes("UTF-8"));
            gameBoard = (GameBoard)unmarshaller.unmarshal(is);
        }
        catch (JAXBException ex)
        {
            Logger.getLogger(GetEventsTask.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (UnsupportedEncodingException e)
        {
            Logger.getLogger(GetEventsTask.class.getName()).log(Level.SEVERE, null, e);
        }

        final GameBoard finalBoard = gameBoard;
        final Board board = this;
        try {
            // create visual cells
            SwingUtilities.invokeAndWait(new Runnable() {

                public void run() {
                    java.util.List<DisplayCell> components = new LinkedList<DisplayCell>();
                    for (int i = 0; i < LINE_SIZE * 4; i++) {
                        CellBase logicCell = finalBoard.getCellBase().get(i);
                        // create display cell for logical cell
                        DisplayCell cell = new DisplayCell(logicCell);
                        // add it to the mapping
                        cellBaseToDisplayCell.put(logicCell, cell);
                        cellIDToCellBase.put(i, logicCell);
                        components.add(cell);
                    }
                    // set the initial position of all players to the cell 0 (GO)
                    for (PlayerDetails player : players) {
                        components.get(0).AddPlayerToPlayersList(player);
                    }
                    java.util.Iterator<DisplayCell> componentIterator = components.iterator();
                    //Add Panels for Each of the four sides
                    for (int sideIndex = 0; sideIndex < 4; sideIndex++) {
                        for (int lineIndex = 0; lineIndex < LINE_SIZE; lineIndex++) {
                            JComponent component = componentIterator.next();
                            switch (sideIndex) {
                                case 0:
                                    //top line
                                    addComponent(lineIndex, 0, component);
                                    break;
                                case 1:
                                    //right line
                                    addComponent(LINE_SIZE, lineIndex, component);
                                    break;
                                case 2:
                                    //bottom line - and in reverse order
                                    addComponent(LINE_SIZE - lineIndex, LINE_SIZE, component);
                                    break;
                                case 3:
                                    //left line - and in reverse order
                                    addComponent(0, LINE_SIZE - lineIndex, component);
                                    break;
                            }
                        }
                    }
                    // Main Inner Area Notice Starts at (1,1) and takes up 8X8
                    innerBoard = new CenterBoard(players);
                    board.add(innerBoard, new GridBagConstraints(1, 1, 8, 8, 2, 2, GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(0, 0, 0, 0), 0, 0));
                    board.validate();
                    board.repaint();
                }
            });
        } catch (InterruptedException ex) {
            Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    public void SetPlayingUser(String playerName)
    {
    	PlayerDetails player = GetPlayerDetails(playerName);
        innerBoard.SetPlayingUser(player);

    	validate();
	repaint();
    }

    /**
     * visually moves the player on the board
     * @param player - player to move
     * @param origin - from cell
     * @param destination - to cell
     */
    public void MovePlayer(String playerName, int originCellID, int destinationCellID)
    {
	final PlayerDetails player = GetPlayerDetails(playerName);
        final CellBase origin = GetCellBase(originCellID);
        final CellBase destination = GetCellBase(destinationCellID);

        try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    public void run()
                    {
                        // remove the player from origin cell
                        DisplayCell originDisplayCell = cellBaseToDisplayCell.get(origin);
                        
                        if (origin.getType().compareTo("Jail") == 0 &&
                                        player.isInJail())
                        {
                            originDisplayCell.RemovePlayerFromSecondaryPlayersList(player);
                            player.setInJail(false);
                        }
                        else
                        {
                            originDisplayCell.RemovePlayerFromPlayersList(player);
                        }

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

    public void SimulateDiceThrow(int dice1, int dice2)
    {
    	if (innerBoard == null)
            System.out.println("innerBoard is null");

        innerBoard.SimulateDiceThrow(dice1, dice2);
    }

    public void EnableDiceThrow()
    {
    	innerBoard.EnableDiceThrow();
    }

    public void UpdatePlayerLost(String playerName, int currentPositionID)
    {
    	final PlayerDetails p = GetPlayerDetails(playerName);
        p.setLost();

        final CellBase currentPosition = GetCellBase(currentPositionID);

        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run()
                    {
                        innerBoard.UpdatePlayerDisplay(p);

                        // remove the player from the board.
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
                            {
                                if (((Asset)cell).getOwner() == p)
                                {
                                    ((Asset)cell).setOwner(null);
                                    cellBaseToDisplayCell.get(cell).SetOwner(null);
                                }
                            }
                            if (cell instanceof City)
                            {
                                if (((City)cell).getOwner() == p)
                                {
                                    ((City)cell).setOwner(null);
                                    cellBaseToDisplayCell.get(cell).SetOwner(null);
                                }
                            }
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

    public void setPlayerInJailLogicly(String playerName) {
        PlayerDetails player = GetPlayerDetails(playerName);
        player.setInJail(true);
    }

    public void SetAssetOwner(String playerName, Integer boardSquareID) {
        PlayerDetails player = GetPlayerDetails(playerName);
        CellBase currentPosition = GetCellBase(boardSquareID);
        if (currentPosition instanceof Asset)
        {
            ((Asset)currentPosition).setOwner(player);
        }
        else
        {
            ((City)currentPosition).setOwner(player);
        }

        DisplayCell cellDiaplsyCell = cellBaseToDisplayCell.get(currentPosition);
    	cellDiaplsyCell.SetOwner(player);
    }

    public void SetHouseBuilt(String playerName, Integer boardSquareID) {
        PlayerDetails player = GetPlayerDetails(playerName);
        final City city = (City)GetCellBase(boardSquareID);
        city.IncrementNumberOfHouses();

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

    private PlayerDetails GetPlayerDetails(String playerName) {
        PlayerDetails retValue = null;
        for (PlayerDetails p : players)
        {
            if (p.getName().compareTo(playerName) == 0)
            {
                retValue = p;
                break;
            }
        }
        return retValue;
    }

    public CellBase GetCellBase(int currentPositionID) {
        return cellIDToCellBase.get(currentPositionID);
    }

    public void TransferMoneyFromUserToUser(String from, String to, int amountPaid) {
        PlayerDetails fromPlayer = GetPlayerDetails(from);
        PlayerDetails toPlayer = GetPlayerDetails(to);
        fromPlayer.SubtractAmount(amountPaid);
        toPlayer.AddAmount(amountPaid);
        innerBoard.UpdatePlayerDisplay(fromPlayer);
        innerBoard.UpdatePlayerDisplay(toPlayer);
    }

    public void TransferMoneyToBank(String from, int amountPaid) {
        PlayerDetails fromPlayer = GetPlayerDetails(from);
        fromPlayer.SubtractAmount(amountPaid);
        innerBoard.UpdatePlayerDisplay(fromPlayer);
    }

    public void TransferMoneyFromBank(String to, int amountPaid) {
        PlayerDetails fromPlayer = GetPlayerDetails(to);
        fromPlayer.AddAmount(amountPaid);
        innerBoard.UpdatePlayerDisplay(fromPlayer);
    }


}
