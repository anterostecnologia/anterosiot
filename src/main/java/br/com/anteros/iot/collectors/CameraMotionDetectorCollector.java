package br.com.anteros.iot.collectors;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.video.BackgroundSubtractorMOG2;
import org.opencv.videoio.VideoCapture;

import com.diozero.util.SleepUtil;

import br.com.anteros.core.utils.Assert;
import br.com.anteros.iot.Collector;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.things.CameraMotionDetector;

public class CameraMotionDetectorCollector extends Collector implements Runnable {

	protected Boolean running = false;
	protected Thread thread;
	
	public CameraMotionDetectorCollector(CollectorListener listener, Thing thing) {
		super(listener, thing);
	}

	public CameraMotionDetectorCollector() {
	}

	@Override
	public void startCollect() {
		Assert.notNull(listener);
		this.running = true;
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void stopCollect() {
		this.running = false;
		
	}

	@Override
	public void run() {
		int count = 0;
		
		while(running) {
			SleepUtil.sleepMillis(1000);
			count++;
			listener.onCollect(new SimpleResult("Mensagem coletada da camera " + count), thing);
			
			System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
			
			VideoCapture camera = new VideoCapture(0);
//			VideoCapture ip_camera = new VideoCapture("http://10.0.8.99/video.cgi?x.mjpeg");
//	          BackgroundSubtractorMOG2 backgroundSubtractorMOG = new BackgroundSubtractorMOG2();

			if (!camera.isOpened()) {
				System.out.println("ERRO! Falha ao estabelecer uma conexão com a câmera. Verifique se o dispositivo já está em uso!");
			}
			

			Mat frame = new Mat();
			
			while (camera.isOpened()) {
				camera.read(frame);

				if (!frame.empty()) {

                    Mat fgMask=new Mat();
//                    backgroundSubtractorMOG.apply(frame, fgMask, 0.1);
				
				}
				
			}
			camera.release();
			
		}
	}

	@Override
	public boolean isSupportedThing(Thing thing) {
		return thing instanceof CameraMotionDetector;
	}

}
