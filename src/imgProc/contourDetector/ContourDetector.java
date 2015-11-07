package imgProc.contourDetector;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import imgProc.ImgProcInterface;

public class ContourDetector implements ImgProcInterface {

	@Override
	public Mat processImage(Mat img) {
		Mat newImg = img.clone();

		Imgproc.cvtColor(img, newImg, Imgproc.COLOR_RGB2GRAY);

		// http://docs.opencv.org/2.4/doc/tutorials/imgproc/threshold/threshold.html
		double thresh = 100;
		double maxval = 255;
		Imgproc.threshold(newImg, newImg, thresh, maxval, Imgproc.THRESH_BINARY_INV);

		// http://docs.opencv.org/2.4/modules/imgproc/doc/structural_analysis_and_shape_descriptors.html?highlight=findcontours#findcontours
		List<MatOfPoint> contours = new ArrayList<>();
		Mat hierarchy = new Mat();
		Imgproc.findContours(newImg.clone(), contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

		Mat grayScaleImgCopy = newImg.clone();
		Imgproc.cvtColor(newImg, grayScaleImgCopy, Imgproc.COLOR_GRAY2BGR);

		for (int i = 0; i < contours.size(); i++) {
			Scalar color = new Scalar(i*20%255, i*10%255, i*5%255);
			Imgproc.drawContours(grayScaleImgCopy, contours, i, color, 7);
			
			Mat line = new Mat();
			Imgproc.fitLine(contours.get(i), line, Imgproc.CV_DIST_L2, 0, 0.01, 0.01);
				
			double vx, vy, x, y;
			vx = line.get(0, 0)[0];
			vy = line.get(1, 0)[0];
			x = line.get(2, 0)[0];
			y = line.get(3, 0)[0];
			
			int lefty = (int) ((-x*vy/vx) + y);
			int righty = (int) (((grayScaleImgCopy.height()-x)*vy/vx)+y);

			Imgproc.line(grayScaleImgCopy, new Point(grayScaleImgCopy.height()-1, righty), new Point(0, lefty), new Scalar(50,50,50));
			
			//Imgproc.fillPoly(grayScaleImgCopy, contours, color);
			
		}
		// Imgproc.fillPoly(img, contours, new Scalar(210, 120, 6));

		return grayScaleImgCopy;
		// return img;
	}
	
	

}
