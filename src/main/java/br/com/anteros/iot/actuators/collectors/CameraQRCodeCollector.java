package br.com.anteros.iot.actuators.collectors;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JTextArea;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import br.com.anteros.core.log.Logger;
import br.com.anteros.core.log.LoggerProvider;
import br.com.anteros.core.utils.Assert;
import br.com.anteros.iot.Collector;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.things.CameraQRCodeReader;

public class CameraQRCodeCollector extends Collector implements Runnable {

	public static int CV_QR_NORTH = 0;
	public static int CV_QR_EAST = 1;
	public static int CV_QR_SOUTH = 2;
	public static int CV_QR_WEST = 3;
	
	private static final Logger LOG = LoggerProvider.getInstance().getLogger(CameraQRCodeCollector.class.getName());
	

	protected Boolean running = false;
	protected Thread thread;
	protected String lastValue;

//	public static void main(String[] args) {
//		new Thread(new CameraQRCodeCollector()).start();
//	}

	public CameraQRCodeCollector(CollectorListener listener, Thing thing) {
		super(listener, thing);
	}

	public CameraQRCodeCollector() { 
	}

	@Override
	public void startCollect() {
		Assert.notNull(listener);
		this.running = true;
		thread = new Thread(this);
		thread.setName("Câmera QRCode");
		thread.start();
	}

	@Override
	public void stopCollect() {
		this.running = false;
	}

	@Override
	public boolean isSupportedThing(Thing thing) {
		return thing instanceof CameraQRCodeReader;
	}

	@Override
	public void run() {

		String path = System.getProperty("java.library.path");
		LOG.info("java.library.path: " + path);
		LOG.info("NATIVE_LIBRARY_NAME: " + Core.NATIVE_LIBRARY_NAME + " , VERSION: " + Core.VERSION);
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		VideoCapture webcam = new VideoCapture(0);

		webcam.set(Videoio.CAP_PROP_FRAME_WIDTH, 480);
		webcam.set(Videoio.CAP_PROP_FRAME_HEIGHT, 800);
		webcam.set(Videoio.CAP_PROP_MONOCHROME, 1);
		// Tamanho do display LCD
		int largura = 480;
		int altura = 800;

		if (webcam.isOpened()) {
			LOG.info("Video aberto");
		} else {
			LOG.error("Video error");
		}

		JFrame frame1 = new JFrame();
		frame1.setBounds(0, 0, largura, altura);
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//		JTextArea textArea = new JTextArea();
//		textArea.setFont(new Font("Serif", Font.ITALIC, 30));
//		textArea.setLineWrap(true);
//		textArea.setWrapStyleWord(true);
//		textArea.setSize(largura, 100);

		My_Panel my_panel = new My_Panel();
		frame1.setContentPane(my_panel);
//		frame1.add(textArea);
		frame1.setVisible(true);

		frame1.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					running = false;
				}

			}

			@Override
			public void keyReleased(KeyEvent e) {
				

			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					running = false;
				}

			}
		});

//		long startTime = System.currentTimeMillis();
		running = true;

//		try {
//			Thread.sleep(3000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}

		Mat image = new Mat();
		if (webcam.isOpened()) {
			webcam.read(image);			
		}

//		BufferedImage bufferedImage = null;
//		int count = 0;

		boolean retrieve;
		while (running) {
			retrieve = false;
			if (webcam.isOpened()) {
				retrieve = webcam.read(image);		
//				retrieve = webcam.retrieve(image);
			}
			Core.flip(image, image, 1);
			if (retrieve && !image.empty()) {
//				if (count == 0) {
//					try {
//						bufferedImage = matToBufferedImage(image);
//						Result result = decodeQRCode(bufferedImage);
//						if (result != null) {
//							drawResultPoints(bufferedImage, 1, result);
//							my_panel.setImage(bufferedImage);
//
//							long difference = System.currentTimeMillis() - startTime;
//
//							if (difference >= ((CameraQRCodeReader) thing).getIntervalToReadSameQrCode()){
//								lastValue = result.getText();
//								textArea.setText(lastValue);
//								LOG.info(new Date() + "  " + lastValue);
//							    listener.onCollect(new SimpleResult(lastValue), thing);
//								startTime = System.currentTimeMillis();
//							}
//
//						} else {
//							my_panel.MatToBufferedImage(image);
//						}
//						my_panel.repaint();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				} else {
//					if (count > 10) {
//						count = 0;
//					}
					try {
						my_panel.MatToBufferedImage(image);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					my_panel.repaint();
//				}
//				count++;
			}

		}
		Thread.interrupted();
	}

	public static BufferedImage matToBufferedImage(Mat mat) {

		if (mat.height() > 0 && mat.width() > 0) {
			BufferedImage image = new BufferedImage(mat.width(), mat.height(), BufferedImage.TYPE_3BYTE_BGR);
			WritableRaster raster = image.getRaster();
			DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
			byte[] data = dataBuffer.getData();
			mat.get(0, 0, data);
			return image;
		}

		return null;
	}

//	private void drawResultPoints(BufferedImage barcode, float scaleFactor, Result rawResult) {
//		ResultPoint[] points = rawResult.getResultPoints();
//		if (points != null && points.length > 0) {
//			Graphics graphics = barcode.getGraphics();
//			graphics.setColor(Color.GREEN);
//			for (ResultPoint point : points) {
//				if (point != null) {
//					graphics.fillRect((int) point.getX() - 10, (int) point.getY() - 10, 20, 20);
//				}
//
//			}
//		}
//	}

//	private static Result decodeQRCode(BufferedImage bufferedImage) throws IOException {
//		LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
//		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
//
//		try {
//			Result result = new MultiFormatReader().decode(bitmap);
//			return result;
//		} catch (NotFoundException e) {
//			return null;
//		}
//	}

	@Override
	public boolean isRunning() {
		return running ? true : false;
	}

	

}
