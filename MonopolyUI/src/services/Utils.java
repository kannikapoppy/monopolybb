package services;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class Utils {

    public static final String IMAGES_FOLDER = "/images/";

    //show problem with runnable and parameters and how final overcome it!
    public static void showExample (final String title, final JComponent component) {

        //all swing code runs in the EDT so we need to initialize the main container (JFrame) to run inside of the EDT as well
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame(title);
                //what do we want to happen when the user click on the "X" button
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.getContentPane().setLayout(new BorderLayout());
                frame.getContentPane().add(component, BorderLayout.CENTER);

                //set size
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                Dimension frameSize = new Dimension();
                frameSize.setSize(screenSize.width * 0.5, screenSize.height * 0.5);
                frame.setSize(frameSize);

                frame.setLocationRelativeTo(null);

                frame.setVisible(true);
            }
        });
    }

    public static Image getImage (String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }

        URL imageURL = Utils.class.getResource(IMAGES_FOLDER + name);
        if (imageURL == null) {
            return null;
        }

        try {
            return ImageIO.read(imageURL);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ImageIcon getImageIcon (String name) {
        Image image = getImage(name);
        if (image == null) {
            return null;
        }
        return new ImageIcon(image);
    }
    
    public static void ShowError(Component parentComponent, final String msg)
	{
		JOptionPane.showMessageDialog(parentComponent, msg); 
	}

    public static void setNativeLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }
}
