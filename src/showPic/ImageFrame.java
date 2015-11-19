package showPic;

import javax.swing.JFrame;

import org.opencv.core.Mat;

public class ImageFrame extends JFrame{

	private ImagePanel imagePanel;
	
	public ImageFrame(int width, int height){
		imagePanel = new ImagePanel(width, height);
		
		this.add(imagePanel);
		this.pack();
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		
	}

	public void setNewImage(Mat frame) {
		imagePanel.setNewImage(frame);
		this.repaint();
	}
	
	/**
	 * method for testing bufferedImage -> mat conversion
	 * @param frame
	 */
	public void setNewImageTst(Mat frame) {
		imagePanel.setNewImageTst(frame);
		this.repaint();
	}

	
}
