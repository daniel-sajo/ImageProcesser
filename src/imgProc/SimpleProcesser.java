package imgProc;

import org.opencv.core.Mat;

public class SimpleProcesser implements ImgProcInterface{

	@Override
	public Mat processImage(Mat m) {
		return m;
	}

}
