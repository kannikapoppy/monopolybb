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

import services.Utils;

public class DisplayCell extends JPanel {

	private static final long serialVersionUID = 1L;
	private Image m_bgImage = null;
	private Box playersBox = Box.createHorizontalBox();
	private Box secondaryPlayersBox;

	/**
	 * This is the default constructor
	 */
	public DisplayCell(CellBase cell) {
		super();

		setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
		
		if (cell instanceof City)
        {
			City city = (City)cell;
			setLayout(new GridLayout(4, 1, 0, 0));
			
			JLabel assetNameLbl = new JLabel(city.getName());
			assetNameLbl.setIcon(Utils.getImageIcon(city.getCountry().getName() + ".png"));
			add(assetNameLbl);
			
			String ownerStr;
			if (city.getOwner() == null)
				ownerStr = "No Owner";
			else
				ownerStr = city.getOwner().getName();
			
			JLabel assetOwnerLbl = new JLabel("Owner: " + ownerStr);
			add(assetOwnerLbl);

			Box housesBox = Box.createHorizontalBox();
			
			int numberOfHouses = 2;//city.getHousesNumber();
			for (int i=0;i<numberOfHouses;i++)
			{
				JLabel houseLabel = new JLabel();
				houseLabel.setIcon(Utils.getImageIcon("house.png"));
				housesBox.add(houseLabel);
			}
			
			add(housesBox);        	
        }
    	else if (cell instanceof Asset)
        {
    		Asset service = (Asset)cell;
    		
    		setLayout(new GridLayout(4, 1, 0, 0));
    		
    		JLabel assetNameLbl = new JLabel(service.getName());
    		assetNameLbl.setIcon(Utils.getImageIcon(service.getGroup().getName() + ".png"));
    		add(assetNameLbl);
    		
    		String ownerStr;
    		if (service.getOwner() == null)
    			ownerStr = "No Owner";
    		else
    			ownerStr = service.getOwner().getName();
    		
    		JLabel assetOwnerLbl = new JLabel("Owner: " + ownerStr);
    		add(assetOwnerLbl);

    		JLabel assetTypeLbl = new JLabel("Type: " + service.getGroup().getName());
    		add(assetTypeLbl);
        }
    	else
    	{
    		setLayout(new BorderLayout());
    		
    		m_bgImage = Utils.getImage(cell.getType() + ".gif");
    		clearComponents();
    	}
		
		add(playersBox, BorderLayout.SOUTH);
		
		if (cell.getType().compareTo("Jail") == 0)
		{			
			secondaryPlayersBox = Box.createHorizontalBox();
			
			add(secondaryPlayersBox, BorderLayout.CENTER);
		}
		
		initialize();
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
	}
	
	private void clearComponents()
	{
	    for (Component component : getComponents())
	    {
	    	// do not remove the players panel from the cell layout
	    	//if (component != m_cellPlayersPanel)
	    	//{
	    	  remove(component);
	    	//}
	    }
	}
	
	@Override
	public void paintComponent(Graphics g) {
	    if (m_bgImage !=null) 
	    {
	    	g.drawImage(m_bgImage, 0, 0, getWidth(), getHeight(), this);
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
