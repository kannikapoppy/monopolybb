package monopolyUI;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JLabel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import objectmodel.Asset;
import objectmodel.CellBase;
import objectmodel.City;

import services.Utils;

public class DisplayCell extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * This is the default constructor
	 */
	public DisplayCell(CellBase cell) {
		super();
		
		if (cell instanceof City)
        {
			City city = (City)cell;
			setLayout(new GridLayout(3, 1, 0, 0));
			setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
			
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

			Box horizontalBox = Box.createHorizontalBox();
			
			int numberOfHouses = city.getHousesNumber();
			for (int i=0;i<numberOfHouses;i++)
			{
				JLabel houseLabel = new JLabel();
				houseLabel.setIcon(Utils.getImageIcon("house.png"));
				horizontalBox.add(houseLabel);
			}
			
			add(horizontalBox);
        	
        }
    	else if (cell instanceof Asset)
        {
    		Asset service = (Asset)cell;
    		
    		setLayout(new GridLayout(3, 1, 0, 0));
    		setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
    		
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
    		setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
    		JLabel picLabel = new JLabel(Utils.getImageIcon(cell.getType() + ".gif"));
    		add( picLabel );
    	}
		
		initialize();
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
