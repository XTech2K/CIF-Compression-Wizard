import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;

public class Menu extends JFrame
{
	/* --- Fields relating to the UI. --- */
	private JPanel pWindow;
	private JPanel pImage;
	private JPanel pOptions;
	private JPanel pButtons;
	private JPanel pSlider;
	private JSlider sPercent;
	private JButton bLoad;
	private JButton bCompress;
	private JButton bSavePNG;
	private JLabel aCompressAmount;
	private JTextField tPercent;
	private JButton bAnimate;
	private JTextField tfStatus;

	public static final int DEFAULT_WIDTH = 800;
	public static final int DEFAULT_HEIGHT = 800;

	private int drawx, drawy;    //coordinate pair to start drawing the image at (used to center the image in the panel)
	private long lastResizeTime;    //keeps track of how often complex resizing code is called

	/* --- Fields relating to the compression program. --- */
	private Image image;
	private Image scaledImage;
	private String filename;
	private int compressionPercent;

	/* --- Reference to a Controller for the Menu to interact with the Compression program. --- */
	private Controller controller;

	public Menu()
	{
		controller = new Controller();

		setContentPane(pWindow);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		setMinimumSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		//setResizable(false);

		/* --- Setting up listeners for all the UI components that need them. --- */

		sPercent.addChangeListener(e -> {
			JSlider s = (JSlider) e.getSource();
			if(s != null)
			{
				compressionPercent = s.getValue();
				tPercent.setText(compressionPercent + "%");
			}
		});

		bLoad.addActionListener(e -> {
			String userHomeDir = System.getProperty("user.home");
			JFileChooser jfc = new JFileChooser(userHomeDir + "/Pictures");
			jfc.setFileFilter(new FileNameExtensionFilter("PNG or JPEG", "png", "jpg", "jpeg"));
			if(jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
			{
				//TODO: doesn't show due to load hogging the Swing thread
				//tfStatus.setText("Loading image, please wait...");

				File f = jfc.getSelectedFile();

				if(!controller.setImageFile(f))
				{
					JOptionPane.showMessageDialog(null, "Could not read image from "
							+ f.getName() + ".");
					return;
				}

				filename = f.getName();

				image = controller.getBaseImage();

				scaleImage();

				tfStatus.setText("Finished loading file: " + filename);

				//TODO: Debugging - remove.
				//System.out.println("image width, height = " + image.getWidth(null) + "," + image.getHeight(null));
				//System.out.println("scaled image width, height = " + scaledImage.getWidth(null) + "," + scaledImage.getHeight(null));

				//need to force a repaint of the window, otherwise it would wait until window is dirty (which takes too long)
				revalidate();
				repaint();
			}
		});

		bCompress.addActionListener(e -> {
			//TODO: doesn't show due to compression hogging the Swing thread
			//tfStatus.setText("Compressing image, please wait...");

			if(!controller.compressImage(compressionPercent, false))
			{
				//compression failed?
				JOptionPane.showMessageDialog(null, "Failed to compress image.");
				return;
			}

			//store the new compressed image
			image = controller.getCompressedImage();

			//update scaledImage so that subsequent repaints show the new, compressed image
			scaleImage();

			tfStatus.setText("Compression finished - now at " + compressionPercent + "% compression.");

			//need to force a repaint of the window, otherwise it would wait until window is dirty (which takes too long)
			revalidate();
			repaint();
		});

		bSavePNG.addActionListener(e -> {
			//if we don't have an image loaded, don't allow a save!
			if(image == null) return;

			String userHomeDir = System.getProperty("user.home");
			JFileChooser jfc = new JFileChooser(userHomeDir + "/Pictures");
			jfc.setFileFilter(new FileNameExtensionFilter("PNG", "png"));

			//create a default filename for the new image based on the current filename
			String file = filename.substring(0, filename.lastIndexOf(".")) + "-compressed.png";
			jfc.setSelectedFile(new File(file));

			if(jfc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
			{
				File f = jfc.getSelectedFile();
				if(!controller.saveImageAsPNG(f))
				{
					//image could not be saved?
					JOptionPane.showMessageDialog(null, "Failed to save image.");
					return;
				}

				tfStatus.setText("Finished saving image to file: " + f.getName());
			}
		});

		bAnimate.addActionListener(e -> {
			//if we don't have an image loaded, don't allow animation!
			if(image == null) return;

			String userHomeDir = System.getProperty("user.home");
			JFileChooser jfc = new JFileChooser(userHomeDir + "/Pictures");
			jfc.setFileFilter(new FileNameExtensionFilter("gif", "gif"));

			//create a default filename for the new image based on the current filename
			String file = filename.substring(0, filename.lastIndexOf(".")) + "-compression.gif";
			jfc.setSelectedFile(new File(file));

			if(jfc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
			{
				File f = jfc.getSelectedFile();

				//TODO: doesn't show due to compression hogging the Swing thread
				//tfStatus.setText("Compressing image, please wait...");

				if(!controller.compressImage(compressionPercent, true))
				{
					//compression failed?
					JOptionPane.showMessageDialog(null, "Failed to compress image.");
					tfStatus.setText("Compression failed.");
					return;
				}

				//TODO: doesn't show due to animation hogging the Swing thread
				//tfStatus.setText("Creating animation, please wait...");

				if(!controller.saveAnimationAsGIF(f))
				{
					//animation failed
					JOptionPane.showMessageDialog(null, "Failed to save animation.");
					tfStatus.setText("Could not save animation.");
					return;
				}

				image = controller.getCompressedImage();

				scaleImage();

				tfStatus.setText("Finished saving animation to file: " + f.getName());

				//need to force a repaint of the window, otherwise it would wait until window is dirty (which takes too long)
				revalidate();
				repaint();
			}
		});

		pImage.addComponentListener(new ComponentAdapter()
		{
			@Override
			public void componentResized(ComponentEvent e)
			{
				super.componentResized(e);

				//this method is called continuously during a resize, which can cause severe slowdowns
				//only perform these operations at most 20 times a second, rather than 50+ times
				long curtime = System.currentTimeMillis();
				if(curtime - lastResizeTime < 50)
				{
					return;
				}

				//been long enough, reset lastResizeTime and scale the image
				lastResizeTime = curtime;
				scaleImage();
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

	private void scaleImage()
	{
		if(image == null) return;

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

		/*
		 * Start drawing at either the midpoint of the difference between the image and the panel,
		 * or at 0,0 if the panel is not larger than the image
		 */
		drawx = pwid > iwid ? (pwid - iwid) / 2 : 0;
		drawy = phi > ihi ? (phi - ihi) / 2 : 0;
	}
}
