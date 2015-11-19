package showPic;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.event.MouseInputListener;

import org.opencv.core.Mat;

@SuppressWarnings("serial")
public class ImagePanel extends JPanel {
	
	private int width;
	private int height;

	private BufferedImage buf_image;
	int mouseClick_x, mouseClick_y;

	ImagePanel(int width, int height) {
		this.width = width;
		this.height = height;
		
		Dimension dim = new Dimension(width,height);
		this.setSize( dim );
		this.setPreferredSize( dim );
	}

	protected void setNewImage(Mat imgMatrix) {
		buf_image = MatToBufImg.toBufferedImage(imgMatrix);
	}
	
	/**
	 * method for testing BufferedImage -> Mat conversion
	 * @param imgMatrix
	 */
	protected void setNewImageTst(Mat imgMatrix) {
		
		BufferedImage bi = MatToBufImg.toBufferedImage(imgMatrix);
		Mat m = MatToBufImg.toMat(bi);
		
		buf_image = MatToBufImg.toBufferedImage(m);
	}
	

	// Override default paint
	public void paintComponent(Graphics g) {
		int width, height;
		if (buf_image != null) {
			width = buf_image.getWidth();
			height = buf_image.getHeight();
		} else
			width = height = 0;

		g.drawImage(buf_image, 0, 0, width, height, this);
	}

}
