package imgProc.blobDetector;

import java.util.List;

import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.imgproc.Imgproc;

import imgProc.ImgProcInterface;

public class BlobDetector implements ImgProcInterface {

	@Override
	public Mat processImage(Mat img) {

		Mat ret_img = img.clone();

		Imgproc.cvtColor(img, ret_img, Imgproc.COLOR_RGB2GRAY);

		// Set up the detector with default parameters.
		FeatureDetector detector = FeatureDetector.create(FeatureDetector.PYRAMID_SIMPLEBLOB);
		/*
		 * GRID_SIMPLEBLOB PYRAMID_SIMPLEBLOB DYNAMIC_SIMPLEBLOB SIMPLEBLOB
		 */
		MatOfKeyPoint keypoints = new MatOfKeyPoint();

		// settings for detector
		String filename = "/media/dataDisc/progik/OpenCV/java/ImgProcFramework/src/imgProc/blobDetector/blobdetect_settings.yml";

		detector.read(filename);
		// detector.write(filename);

		detector.detect(ret_img, keypoints);

		Features2d.drawKeypoints(img, keypoints, ret_img);

		showBlobs(keypoints.toList(), img);

		// floodfill blob area

		// Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2HLS);
		for (KeyPoint kp : keypoints.toList()) {
			floodFillBlobPoints((int) kp.pt.x, (int) kp.pt.y, new byte[] { -127, -127, -127 },
					new byte[] { 120, 91, 50 }, img);
			cycleNum = 0;

			try {
				Thread.sleep(500l);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// Imgproc.cvtColor(img, img, Imgproc.COLOR_HLS2BGR);
		return img;

		// TODO more than one blob can be found on the images. Is it a problem?
	}

	private void showBlobs(List<KeyPoint> keypoints, Mat img) {
		List<KeyPoint> keyList = keypoints;

		for (KeyPoint kp : keyList) {
			/*
			 * System.out.println(kp.size); System.out.println(kp.pt.x + " " +
			 * kp.pt.y); System.out.println(kp.angle);
			 * 
			 * System.out.println();
			 */

			org.opencv.imgproc.Imgproc.circle(img, kp.pt, (int) kp.size, new Scalar(0, 0, 140));

		}

	}

	int cycleNum = 0;

	private void floodFillBlobPoints(int cor_x, int cor_y, byte[] targetColor, byte[] replacementColor, Mat img) {
		// TODO img boundaries

		if (cycleNum++ > 700)
			return;

		int epsilon = 70;

		// https://en.wikipedia.org/wiki/Flood_fill#Stack-based_recursive_implementation_.28four-way.29

		byte[] currColor = new byte[3];
		img.get(cor_y, cor_x, currColor);

		// 1. If target-color is equal to replacement-color, return.
		if (compareColors(targetColor, replacementColor, 0))
			return;
		// 2. If the color of node is not equal to target-color, return.
		if ( !compareColors(targetColor, currColor, epsilon) )
			return;
		/*
		 * if( !(currColor[1] < 30) ) return;
		 */

		System.out.println("current color: B" + currColor[0] + " G" + currColor[1] + " R" + currColor[2]);

		// 3. Set the color of node to replacement-color.
		img.put(cor_y, cor_x, replacementColor);

		int jumpDist = 12;

		// 4. Perform Flood-fill (one step to the west of node, target-color,
		// replacement-color).
		floodFillBlobPoints(cor_x - jumpDist, cor_y, targetColor, replacementColor, img);
		// Perform Flood-fill (one step to the east of node, target-color,
		// replacement-color).
		floodFillBlobPoints(cor_x + jumpDist, cor_y, targetColor, replacementColor, img);
		// Perform Flood-fill (one step to the north of node, target-color,
		// replacement-color).
		floodFillBlobPoints(cor_x, cor_y - jumpDist, targetColor, replacementColor, img);
		// Perform Flood-fill (one step to the south of node, target-color,
		// replacement-color).
		floodFillBlobPoints(cor_x, cor_y + jumpDist, targetColor, replacementColor, img);
		// 5. Return.
		return;
	}

	private boolean compareColors(byte[] color1, byte[] color2, int treshold) {

		if ((Math.abs(color1[0] - color2[0]) < treshold) && (Math.abs(color1[1] - color2[1]) < treshold)
				&& (Math.abs(color1[2] - color2[2]) < treshold)) {
			System.out.println("t");
			return true;
		}

		else
			return false;
	}

}
