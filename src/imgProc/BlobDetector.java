package imgProc;

import java.util.List;

import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Scalar;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.imgproc.Imgproc;

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
		String filename = "/media/dataDisc/progik/OpenCV/java/ImgProcFramework/src/imgProc/blobdetect_settings.yml";

		detector.read(filename);
		// detector.write(filename);

		detector.detect(ret_img, keypoints);

		Features2d.drawKeypoints(ret_img, keypoints, ret_img);

		showBlobs(keypoints.toList(), ret_img);

		return ret_img;
	}

	private void showBlobs(List<KeyPoint> keypoints, Mat img) {
		List<KeyPoint> keyList = keypoints;

		for (KeyPoint kp : keyList) {
			System.out.println(kp.size);
			System.out.println(kp.pt.x + " " + kp.pt.y);
			System.out.println(kp.angle);

			System.out.println();

			org.opencv.imgproc.Imgproc.circle(img, kp.pt, (int) kp.size, new Scalar(0, 0, 140));

		}

	}

}
