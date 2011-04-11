package monopolyUI;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import main.*;
import services.*;
import objectmodel.*;

public class Board extends JPanel {

	private static final int LINE_SIZE = 9;
	
	/**
	 * The game board representation
	 */
	private MonopolyGame monopolyGame;

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

    public Board() {
		super();
		PlayerManager playerManager = new PlayerManager();
		// Create the logic
		monopolyGame = new MonopolyGame();
		// Create the event Handler & register to the events 
		EventHandler eventHandler = new EventHandler();
		eventHandler.registerEvents(monopolyGame);
		
		monopolyGame.initGame(playerManager.CreatePlayers());
		
		
        initUI();
	}

    private void initUI() {
        //init layout
        this.setLayout(new GridBagLayout());

        List<JComponent> components = new LinkedList<JComponent>();
        for (int i=0 ; i < LINE_SIZE * 4 ; i++) {
            components.add(createCellPanel(monopolyGame.getGameBoard().getCellBase().get(i)));
        }

        Iterator<JComponent> componentIterator = components.iterator();

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
        JPanel innerPanel = createInnerPanel("CENTER");
        this.add(innerPanel,
            new GridBagConstraints(1,
                    1,
                    6,
                    6,
                    2, 2,
                    GridBagConstraints.CENTER,
                    GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
	}

    private JPanel createCellPanel(CellBase cell) {
    	return new DisplayCell(cell);
    }
    
    private JPanel createInnerPanel(String text) {
        JPanel panel = new JPanel(new BorderLayout()) ;
        panel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.RED));
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        panel.add(label, BorderLayout.CENTER);
        return panel;
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
}
