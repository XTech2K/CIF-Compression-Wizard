import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class Menu extends JFrame
{
    /* --- Fields relating to the UI. --- */
    private JPanel pWindow;
    private JPanel pImage;
    private JPanel pOptions;
    private JPanel pButtons;
    private JPanel pSlider;
    private JPanel pCheckbox;
    private JCheckBox cAnimate;
    private JSlider sPercent;
    private JButton bLoad;
    private JButton bCompress;
    private JButton bSavePNG;
    private JButton bSaveCIF;
    private JLabel aCompressAmount;
    private JTextField tPercent;

    public static final int DEFAULT_WIDTH = 800;
    public static final int DEFAULT_HEIGHT = 600;

    /* --- Fields relating to the compression program. --- */
    private Image displayedImage;
    private int compressionPercent;
    private boolean animate;

    /* --- Reference to a Controller for the Menu to interact with the Compression program. --- */
	private Controller controller;

    public Menu()
    {
    	controller = new Controller();
    	animate = false;

        setContentPane(pWindow);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

        /* Setting up listeners for all the UI components that need them. */
        sPercent.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                JSlider s = (JSlider)e.getSource();
                if(s != null && !s.getValueIsAdjusting())
                {
                    compressionPercent = s.getValue();
                    tPercent.setText(compressionPercent + "%");
                }
            }
        });

        bLoad.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JFileChooser jfc = new JFileChooser();
                jfc.setFileFilter(new FileNameExtensionFilter("PNG & CIF", "png", "cif"));
                if(jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
                {
                    File f = jfc.getSelectedFile();
                    JOptionPane.showMessageDialog(null, "Chose to open " + f.getName() + ".");

                    //TODO: if controller.setImageFile(f) succeeds then display image (for now always try to display)

                    try
                    {
                        displayedImage = ImageIO.read(f.getAbsoluteFile());
                    }
                    catch(IOException ioe)
                    {
                        JOptionPane.showMessageDialog(null, "Could not read image from "
                                + f.getName() + ".");
                    }

                    //need to force a repaint of the window, otherwise would wait until window is dirty
                    validate();
                    repaint();
                }
            }
        });

        bCompress.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                //TODO: Call controller to compress image, receive new image, update display
            }
        });

        bSavePNG.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                //TODO: Call controller to get file for this image as a PNG
                //File f = controller.getImageAsPNG();
            }
        });

        bSaveCIF.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                //TODO: Call controller to get file for this image as a CIF
                //File f = controller.getImageAsCIF();
            }
        });
		cAnimate.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				animate = !animate;
			}
		});
	}

    /* Used to customize the drawing of the panel that the image is displayed in. */
    private void createUIComponents()
    {
        pImage = new JPanel()
        {
            @Override
            public void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                g.drawImage(displayedImage, 0, 0, getWidth(), getHeight(), null);
            }
        };
    }
}
