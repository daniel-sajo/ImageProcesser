package main;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import imgProc.*;
import showPic.ImageFrame;

public class Main {

	private ImageFrame imageWindow;

	public Main() {
		showCam(new BlobDetector());
		//showPict(new BlobDetector(), "/media/dataDisc/progik/OpenCV/java/ImgProcFramework/src/imgProc/BlobTest.jpg");
	}

	private void showCam(ImgProcInterface imgProcesser) {
		imageWindow = new ImageFrame(640, 480);
		VideoCapture capture = new VideoCapture(0);

		Mat frame = new Mat();

		if (!capture.isOpened()) {
			System.out.println("Did not connect to camera.");
		} else {

			while (true) {

				capture.read(frame);

				if (!frame.empty()) {
					// show image
					// frame = pixelRationByColor.processImage(frame,
					// panel.mouseClick_x, panel.mouseClick_y);
					frame = imgProcesser.processImage(frame);
					imageWindow.setNewImage(frame);
					imageWindow.repaint();

					// System.out.println(pixelRationByColor.ratio + "%");
				} else {
					System.out.println(" --(!) No captured frame from webcam !");
					break;
				}
			}
			capture.release();

		}

	}

	private void showPict(ImgProcInterface imgProcesser, String pictFile) {
		// open image
		Mat img;
		img = Imgcodecs.imread(pictFile);
		
		imageWindow = new ImageFrame(img.width(), img.height());
		imageWindow.setNewImage(img);
	}

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	public static void main(String[] args) {
		new Main();
	}

}
