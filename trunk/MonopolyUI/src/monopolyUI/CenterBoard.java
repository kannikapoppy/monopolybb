package monopolyUI;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.Box;
import java.awt.GridBagConstraints;
import java.util.List;

import javax.swing.JLabel;

import objectmodel.Player;
import services.Utils;
import java.awt.Insets;

public class CenterBoard extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * This is the default constructor
	 */
	public CenterBoard(List<Player> players) {
		super();
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		Box playersBox = Box.createVerticalBox();
		GridBagConstraints gbc_verticalBox = new GridBagConstraints();
		gbc_verticalBox.insets = new Insets(0, 0, 5, 5);
		gbc_verticalBox.gridx = 0;
		gbc_verticalBox.gridy = 0;
		add(playersBox, gbc_verticalBox);
		
		for (Player player : players)
		{
			JLabel userLbl = new JLabel(player.getName() + ", " + player.getMoney() + "$");
			String colorImageName = "Box" + player.getPlayerColor().toString() + "32.png";
			userLbl.setIcon(Utils.getImageIcon(colorImageName));
			playersBox.add(userLbl);
		}
		
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 200);
	}

}
