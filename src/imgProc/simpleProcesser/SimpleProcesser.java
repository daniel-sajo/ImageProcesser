package imgProc.simpleProcesser;

import org.opencv.core.Mat;

import imgProc.ImgProcInterface;

public class SimpleProcesser implements ImgProcInterface{

	@Override
	public Mat processImage(Mat m) {
		return m;
	}

}
