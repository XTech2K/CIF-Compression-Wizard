import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
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
    private JPanel pSpacer;

    public static final int DEFAULT_WIDTH = 800;
    public static final int DEFAULT_HEIGHT = 800;

    private int drawx, drawy;	//coordinate pair to start drawing the image at (used to center the image in the panel)
    private long lastResizeTime;	//keeps track of how often complex resizing code is called

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
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setMinimumSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        //setResizable(false);

        /* --- Setting up listeners for all the UI components that need them. --- */

        sPercent.addChangeListener(e -> {
                JSlider s = (JSlider)e.getSource();
                if(s != null)
                    {
                        compressionPercent = s.getValue();
                        tPercent.setText(compressionPercent + "%");
                    }
            });

        bLoad.addActionListener(e -> {
                String userHomeDir = System.getProperty("user.home");
                JFileChooser jfc = new JFileChooser(userHomeDir + "/Pictures");
                jfc.setFileFilter(new FileNameExtensionFilter("PNG & CIF", "png", "cif"));
                if(jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
                    {
                        File f = jfc.getSelectedFile();

                        //TODO: if controller.setImageFile(f) succeeds then display image (for now always try to display)

                        try
                            {
                                image = ImageIO.read(f.getAbsoluteFile());
                            }
                        catch(IOException ioe)
                            {
                                JOptionPane.showMessageDialog(null, "Could not read image from "
                                                              + f.getName() + ".");
                                return;
                            }

                        //keep a version of the image that is scaled to its containing JPanel
                        //scale by the larger of the two dimensions
                        if(image.getWidth(null) > image.getHeight(null))
                            {
                                scaledImage = image.getScaledInstance(pImage.getWidth(), -1, Image.SCALE_SMOOTH);
                            }
                        else
                            {
                                scaledImage = image.getScaledInstance(-1, pImage.getHeight(), Image.SCALE_SMOOTH);
                            }

                        int pwid = pImage.getWidth();
                        int iwid = scaledImage.getWidth(this);
                        int phi = pImage.getHeight();
                        int ihi = scaledImage.getHeight(this);
                        drawx = pwid > iwid ? (pwid - iwid) / 2 : 0;	//start drawing at either the midpoint of the difference
                        drawy = phi > ihi ? (phi - ihi) / 2 : 0;		//between size of the image and size of the panel
                        //or start at 0 if panel is not larger than image

                        //TODO: Debugging - remove.
                        System.out.println("image width, height = " + image.getWidth(null) + "," + image.getHeight(null));
                        System.out.println("scaled image width, height = " + scaledImage.getWidth(null) + "," + scaledImage.getHeight(null));

                        //need to force a repaint of the window, otherwise would wait until window is dirty
                        revalidate();
                        repaint();
                    }
            });

        bCompress.addActionListener(e -> {
                //TODO: Relies on Controller.compressImage
                if(!controller.compressImage(compressionPercent, animate))
                    {
                        //compression failed?
                        JOptionPane.showMessageDialog(null, "Failed to compress image.");
                    }
            });

        bSavePNG.addActionListener(e -> {
                String userHomeDir = System.getProperty("user.home");
                JFileChooser jfc = new JFileChooser(userHomeDir + "/Pictures");
                if(jfc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
                    {
                        //TODO: Relies on Controller.saveImageAsPNG
                        File f = jfc.getSelectedFile();
                        if(!controller.saveImageAsPNG(f))
                            {
                                //image could not be saved?
                                JOptionPane.showMessageDialog(null, "Failed to save image.");
                            }
                    }
            });

        bSaveCIF.addActionListener(e -> {
                String userHomeDir = System.getProperty("user.home");
                JFileChooser jfc = new JFileChooser(userHomeDir + "/Pictures");
                if(jfc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
                    {
                        //TODO: Relies on Controller.saveImageAsCIF
                        File f = jfc.getSelectedFile();
                        if(!controller.saveImageAsCIF(f))
                            {
                                //image could not be saved?
                                JOptionPane.showMessageDialog(null, "Failed to save image.");
                            }
                    }
            });

        cAnimate.addActionListener(e -> animate = !animate);

        pImage.addComponentListener(new ComponentAdapter()
            {
                @Override
                public void componentResized(ComponentEvent e)
                {
                    super.componentResized(e);

                    //this method is called continuously during a resize, which can cause severe slowdowns
                    //only perform these operations every tenth of a second, rather than several dozens of times per second
                    long curtime = System.currentTimeMillis();
                    if(curtime - lastResizeTime < 100) { return; }

                    //been long enough, reset lastResizeTime and continue with function
                    lastResizeTime = curtime;

                    if(image != null)
                        {
                            if(image.getWidth(null) > image.getHeight(null))
                                {
                                    scaledImage = image.getScaledInstance(pImage.getWidth(), -1, Image.SCALE_SMOOTH);
                                }
                            else
                                {
                                    scaledImage = image.getScaledInstance(-1, pImage.getHeight(), Image.SCALE_SMOOTH);
                                }

                            int pwid = pImage.getWidth();
                            int iwid = scaledImage.getWidth(null);
                            int phi = pImage.getHeight();
                            int ihi = scaledImage.getHeight(null);
                            drawx = pwid > iwid ? (pwid - iwid) / 2 : 0;	//start drawing at either the midpoint of the difference
                            drawy = phi > ihi ? (phi - ihi) / 2 : 0;		//between size of the image and size of the panel
                            //or start at 0 if panel is not larger than image

                            //TODO: Debugging - remove
                            System.out.println("scaled image width, height = " + scaledImage.getWidth(null) + "," + scaledImage.getHeight(null));
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
                    g.drawImage(scaledImage, drawx, drawy, this);
                }
            };
    }
}
