package main;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import imgProc.*;
import imgProc.blobDetector.BlobDetector;
import imgProc.contourDetector.ContourDetector;
import imgProc.simpleProcesser.SimpleProcesser;
import showPic.ImageFrame;

public class Main {

	private ImageFrame imageWindow;

	public Main() {
		List<ImgProcInterface> processerList = new ArrayList<>();

		processerList.add(new ContourDetector());
		//processerList.add(new SimpleProcesser());

		showCam(processerList);
		
		/*
		showPict(new ContourDetector(),
		 "/media/dataDisc/progik/OpenCV/java/ImgProcFramework/src/imgProc/2015-11-07-211840.jpg");
		*/
		/*
		
		showPict(new SimpleProcesser(),
				 "/media/dataDisc/progik/OpenCV/java/ImgProcFramework/src/imgProc/2015-11-07-211840.jpg");
			*/	 
		/*
		showPict(new ContourDetector(),
				"/media/dataDisc/progik/OpenCV/java/ImgProcFramework/src/imgProc/2015-11-11 14.16.58.jpg");
				*/
	}

	private void showCam(List<ImgProcInterface> imgProcesserList) {
		Thread t = new Thread() {

			public void run() {
				List<ImageFrame> frameList = new ArrayList<>();

				for (int i = 0; i < imgProcesserList.size(); i++) {
					frameList.add(new ImageFrame(640, 480));
				}

				VideoCapture capture = new VideoCapture(0);

				Mat frame = new Mat();

				if (!capture.isOpened()) {
					System.out.println("Did not connect to camera.");
				} else {
					while (true) {
						capture.read(frame);

						if (!frame.empty()) {

							for (int i = 0; i < imgProcesserList.size(); i++) {
								frameList.get(i).setNewImage(imgProcesserList.get(i).processImage(frame));
								frameList.get(i).repaint();
							}
						} else {
							System.out.println(" --(!) No captured frame from webcam !");
							break;
						}

					}
					capture.release();
				}
			}

		};

		t.start();

	}

	private void showPict(ImgProcInterface imgProcesser, String pictFile) {
		// open image
		Mat img;
		img = Imgcodecs.imread(pictFile);

		imageWindow = new ImageFrame(img.width(), img.height());

		imageWindow.setNewImage(imgProcesser.processImage(img));
	}

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	public static void main(String[] args) {
		new Main();
	}

}
