import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
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
    private Image image;
    private Image scaledImage;
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
		setMinimumSize(new Dimension(800, 600));

        /* Setting up listeners for all the UI components that need them. */
        sPercent.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                JSlider s = (JSlider)e.getSource();
                if(s != null)
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
            	String userHomeDir = System.getProperty("user.home");
                JFileChooser jfc = new JFileChooser(userHomeDir + "/Pictures");
                jfc.setFileFilter(new FileNameExtensionFilter("PNG & CIF", "png", "cif"));
                if(jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
                {
                    File f = jfc.getSelectedFile();
                    JOptionPane.showMessageDialog(null, "Chose to open " + f.getName() + ".");

                    //TODO: if controller.setImageFile(f) succeeds then display image (for now always try to display)

                    try
                    {
						image = ImageIO.read(f.getAbsoluteFile());
                    }
                    catch(IOException ioe)
                    {
                        JOptionPane.showMessageDialog(null, "Could not read image from "
                                + f.getName() + ".");
                    }

                    //keep a version of the image that is scaled to its containing JPanel
					scaledImage = image.getScaledInstance(pImage.getWidth(), pImage.getHeight(), Image.SCALE_SMOOTH);

                    //need to force a repaint of the window, otherwise would wait until window is dirty
                    revalidate();
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

		pImage.addComponentListener(new ComponentAdapter()
		{
			@Override
			public void componentResized(ComponentEvent e)
			{
				super.componentResized(e);
				//maintain an image that is scaled to its containing JPanel
				if(image != null)
				{
					scaledImage = image.getScaledInstance(e.getComponent().getWidth(),
														  e.getComponent().getHeight(),
														  Image.SCALE_SMOOTH);
				}
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
                g.drawImage(scaledImage, 0, 0, this);
            }
        };
    }
}
