package monopolyUI;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JLabel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;

import objectmodel.Asset;
import objectmodel.CellBase;
import objectmodel.City;
import objectmodel.Player;
import objectmodel.PlayerColor;

import services.Utils;

/**
 * this class displays a single cell
 * @author Benda & Eizenman
 *
 */
public class DisplayCell extends JPanel {

	private static final long serialVersionUID = 1L;
	/**
	 * used to draw images as the cell
	 */
	private Image m_bgImage = null;
	/**
	 * a box which will hold players in the current location. will be located at the bottom
	 * of the cell
	 */
	private Box playersBox = Box.createHorizontalBox();
	/**
	 * a box which will hold players in the current location. will be located at the center
	 * of the cell. especially here for jail
	 */
	private Box secondaryPlayersBox;
	/**
	 * label of the owner
	 */
	private JLabel assetOwnerLbl;
	/**
	 * box that will hold houses icons to display when houses are built
	 */
	private Box housesBox = null;

	/**
	 * This is the default constructor
	 */
	public DisplayCell(CellBase cell) {
		super();

		setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
		
		if (cell instanceof City)
        {
			//this is a city lets display it
			
			City city = (City)cell;
			setLayout(new GridLayout(4, 1, 0, 0));
			
			// display name
			JLabel assetNameLbl = new JLabel(city.getName());
			assetNameLbl.setIcon(Utils.getImageIcon(city.getCountry().getName() + ".png"));
			add(assetNameLbl);
			
			// display owner
			assetOwnerLbl = new JLabel();
    		add(assetOwnerLbl);
			SetOwner(city.getOwner());

			// display houses
			housesBox = Box.createHorizontalBox();
			
			int numberOfHouses = city.getHousesNumber();
			for (int i=0;i<numberOfHouses;i++)
			{
				BuildHouse();
			}
			
			add(housesBox);  
			
			// set the tool tip of the city
			setToolTipText(String.format("<html><body>Price: %d<br>House Price: %d<br>Land Toll: %d<br>One House Toll: %d<br>Two House Toll: %d<br>Three House Toll: %d</body></html>", 
					city.getPrice(), city.getSingleHousePrice(), city.getLandToll(), 
					city.getHouseToll().getOne(), city.getHouseToll().getTwo(), 
					city.getHouseToll().getThree())); 
        }
    	else if (cell instanceof Asset)
        {
    		//this is a service. lets display it.
    		
    		Asset service = (Asset)cell;
    		
    		setLayout(new GridLayout(4, 1, 0, 0));
    		
    		// display name
    		JLabel assetNameLbl = new JLabel(service.getName());
    		assetNameLbl.setIcon(Utils.getImageIcon(service.getGroup().getName() + ".png"));
    		add(assetNameLbl);
    		
    		// display owner
    		assetOwnerLbl = new JLabel();
    		add(assetOwnerLbl);
			SetOwner(service.getOwner());
    		
    		// display type
			JLabel assetTypeLbl = new JLabel("Type: " + service.getGroup().getName());
    		add(assetTypeLbl);
    		
    		// set the tool tip
    		setToolTipText(String.format("<html><body>Price: %d<br>Land Toll: %d<br>Group Toll: %d</body></html>", 
    				service.getPrice(), service.getLandToll(), service.getGroupToll()));
        }
    	else
    	{
    		setLayout(new BorderLayout());
    		
    		// set the background image later to be drawn
    		m_bgImage = Utils.getImage(cell.getType() + ".gif");
    		clearComponents();
    	}
		
		add(playersBox, BorderLayout.SOUTH);
		
		if (cell.getType().compareTo("Jail") == 0)
		{			
			// if this is the jail cell we need to prepare place for prisioners
			
			secondaryPlayersBox = Box.createHorizontalBox();
			
			add(secondaryPlayersBox, BorderLayout.CENTER);
		}
		
		initialize();
	}
	
	public void BuildHouse()
	{
		// add house label to the box of houses
		JLabel houseLabel = new JLabel();
		houseLabel.setIcon(Utils.getImageIcon("house.png"));
		housesBox.add(houseLabel);
	}
	
	/**
	 * sets owner data in the label.
	 * @param p
	 */
	public void SetOwner(Player p)
	{
		String ownerStr;
		if (p == null)
		{
			ownerStr = "No Owner";
			assetOwnerLbl.setForeground(Color.BLACK);
		}
		else
		{
			ownerStr = p.getName();
			assetOwnerLbl.setForeground(PlayerColorToSystemColor(p.getPlayerColor()));
		}
		
		assetOwnerLbl.setText("Owner: " + ownerStr);
	}
	
	private Color PlayerColorToSystemColor(PlayerColor playerColor)
	{
		switch (playerColor)
		{
			case Blue:
				return Color.BLUE;
			case Green:
				return Color.GREEN;
			case Orange:
				return Color.ORANGE;
			case Red:
				return Color.RED;
			case Gray:
				return Color.GRAY;
			case Yellow:
				return Color.YELLOW;
			default:
				return Color.BLACK;
		}
	}
	
	public void AddPlayerToPlayersList(Player p)
	{
		internalAddPlayer(p, playersBox);
	}
	
	public void AddPlayerToSecondaryPlayersList(Player p)
	{
		internalAddPlayer(p, secondaryPlayersBox);
	}
	
	private void internalAddPlayer(Player p, Box box)
	{
		JLabel userLbl = new JLabel();
		String colorImageName = "Box" + p.getPlayerColor().toString() + "16.png";
		userLbl.setIcon(Utils.getImageIcon(colorImageName));
		userLbl.setName(p.getName());
		box.add(userLbl);
		
		box.validate();
		box.repaint();
	}
	
	public void RemovePlayerFromPlayersList(Player p)
	{
		internalRemovePlayer(p, playersBox);
	}
	
	public void RemovePlayerFromSecondaryPlayersList(Player p)
	{
		internalRemovePlayer(p, secondaryPlayersBox);
	}
	
	private void internalRemovePlayer(Player p, Box box)
	{
		for (Component comp : box.getComponents())
		{
			if (comp.getName().compareTo(p.getName()) == 0)
			{
				box.remove(comp);
				break;
			}
		}
		
		box.validate();
		box.repaint();
	}
	
	private void clearComponents()
	{
	    for (Component component : getComponents())
	    {
	    	//do not remove the players panel from the cell layout
	    	if (component != playersBox && component != secondaryPlayersBox)
	    	{
	    		remove(component);
	    	}
	    }
	}
	
	@Override
	public void paintComponent(Graphics g) {
	    if (m_bgImage !=null) 
	    {
	    	g.drawImage(m_bgImage, 0, 0, getWidth(), getHeight(), this);
	    }
	    else
	    {
	    	super.paintComponent(g);
	    }
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(400, 200);
	}

}
