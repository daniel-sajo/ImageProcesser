package imgProc.contourDetector;

import java.util.ArrayList;
import java.util.Collections;
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
		Imgproc.threshold(newImg, newImg, thresh, maxval, Imgproc.THRESH_BINARY);

		// http://docs.opencv.org/2.4/modules/imgproc/doc/structural_analysis_and_shape_descriptors.html?highlight=findcontours#findcontours
		List<MatOfPoint> contours = new ArrayList<>();
		Mat hierarchy = new Mat();
		Imgproc.findContours(newImg.clone(), contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

		Mat grayScaleImgCopy = newImg.clone();
		Imgproc.cvtColor(newImg, grayScaleImgCopy, Imgproc.COLOR_GRAY2BGR);

		contours = SortContoursBySize(contours);		
		
		int indexMax = Math.min(3, contours.size());
		for (int i = 0; i < indexMax; i++) {
			Scalar color = new Scalar(50*(i+1)%255, 60*(i+1)%255, 80*(i+1)%255);
			Imgproc.drawContours(grayScaleImgCopy, contours, i, color, 4);
			
			Mat line = new Mat();
			Imgproc.fitLine(contours.get(i), line, Imgproc.CV_DIST_L2, 0, 0.01, 0.01);
				
			double vx, vy, x, y;
			vx = line.get(0, 0)[0];
			vy = line.get(1, 0)[0];
			x = line.get(2, 0)[0];
			y = line.get(3, 0)[0];
			
			int lefty = (int) ((-x*vy/vx) + y);
			int righty = (int) (((grayScaleImgCopy.height()-x)*vy/vx)+y);
			
			Point p1 = new Point(grayScaleImgCopy.height()-1, righty);
			Point p2 = new Point(0, lefty);

			
			Imgproc.line(grayScaleImgCopy, p1, p2, color, 7);
			
			double alpha = computeDegreeOfLine(p1, p2);
			
			Imgproc.putText(grayScaleImgCopy, new Double(alpha).toString(), p2, 2, 0.9, new Scalar(0,0,150));
			//System.out.println(alpha);
			
		}
		
		return grayScaleImgCopy;
		//return img;
	}
	
	private List<MatOfPoint> selectBiggestContour(List<MatOfPoint> contours){
		
		double max = Imgproc.contourArea(contours.get(0));;
		MatOfPoint maxContour = contours.get(0);
		
		for(int i=1; i< contours.size(); i++){
			double currentSize = Imgproc.contourArea(contours.get(i));
			if(currentSize > max)
				maxContour = contours.get(i);
		}
		
		ArrayList<MatOfPoint> retList = new ArrayList<MatOfPoint>();
		retList.add(maxContour);
		
		return retList;
	}
	
	private List<MatOfPoint> SortContoursBySize(List<MatOfPoint> contours){
		
		ArrayList<ContourElement> contourElementList = new ArrayList<>();
		
		for(int i=1; i< contours.size(); i++){
			double currentSize = Imgproc.contourArea(contours.get(i));
			ContourElement ce = new ContourElement();
			ce.size = currentSize;
			ce.contour = contours.get(i);
			
			contourElementList.add(ce);
		}
		
		Collections.sort(contourElementList);
		
		ArrayList<MatOfPoint> retList = new ArrayList<>();
		for(int i=0; i<contourElementList.size(); i++){
			retList.add(contourElementList.get(i).contour);
		}
		
		return retList;
	}
	
	private class ContourElement implements Comparable<ContourElement>{
		double size;
		MatOfPoint contour;
		
		@Override
		public int compareTo(ContourElement o) {
			// ascending
			// return new Double(this.size).compareTo(new Double( ((ContourElement)o).size ));
			
			// descending
			return new Double(o.size).compareTo(new Double( this.size ));
		}
	}
	
	private double computeDegreeOfLine(Point p1, Point p2){
		Point d = new Point(p1.x-p2.x, p1.y-p2.y);
		
		double alpha = Math.atan2(d.y, d.x);
		
		return alpha*(-180.0);
	}
}
