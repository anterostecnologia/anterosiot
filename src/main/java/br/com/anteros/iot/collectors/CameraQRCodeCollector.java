package br.com.anteros.iot.collectors;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JTextArea;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import br.com.anteros.core.utils.Assert;
import br.com.anteros.iot.Collector;
import br.com.anteros.iot.Thing;
import br.com.anteros.iot.things.CameraQRCodeReader;

public class CameraQRCodeCollector extends Collector implements Runnable {
	
	public static int CV_QR_NORTH = 0;
	public static int CV_QR_EAST = 1;
	public static int CV_QR_SOUTH = 2;
	public static int CV_QR_WEST = 3;


	protected Boolean running = false;
	protected Thread thread;

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
		System.out.println("java.library.path: " + path);
		System.out.println("NATIVE_LIBRARY_NAME: " + Core.NATIVE_LIBRARY_NAME + " , VERSION: " + Core.VERSION);
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		VideoCapture webcam = new VideoCapture(0);

		// Tamanho do display LCD
		int largura = 480;
		int altura = 320;

		System.out.println(webcam.set(Videoio.CAP_PROP_FRAME_WIDTH, largura));
		System.out.println(webcam.set(Videoio.CAP_PROP_FRAME_HEIGHT, altura));
		System.out.println(webcam.get(Videoio.CAP_PROP_FRAME_WIDTH));
		System.out.println(webcam.get(Videoio.CAP_PROP_FRAME_HEIGHT));

		if (webcam.isOpened()) {
			System.out.println("Video is captured");
		} else {
			System.out.println("Video is not captured!");
		}

		JFrame frame1 = new JFrame();
		frame1.setBounds(0, 0, largura, altura);
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JTextArea textArea = new JTextArea();
		textArea.setFont(new Font("Serif", Font.ITALIC, 30));
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setSize(largura, 100);

		My_Panel my_panel = new My_Panel();
		frame1.setContentPane(my_panel);
		frame1.add(textArea);
		frame1.setVisible(true);

		int DBG = 0;

		while (webcam.isOpened()) {
			Mat image = new Mat();
			webcam.read(image);
			boolean foundQrCode = false;

			if (webcam.grab() && !image.empty()) {
				Mat traces = new Mat(image.size(), CvType.CV_8UC3);
				Mat qr_raw = Mat.zeros(100, 100, CvType.CV_8UC3);
				Mat qr = Mat.zeros(100, 100, CvType.CV_8UC3);
				Mat qr_gray = Mat.zeros(100, 100, CvType.CV_8UC1);
				Mat qr_thres = Mat.zeros(100, 100, CvType.CV_8UC1);
				Mat gray = new Mat();
				Mat edges = new Mat();

				Imgproc.cvtColor(image, gray, Imgproc.COLOR_RGB2GRAY);
				Imgproc.Canny(gray, edges, 100, 200);

				List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
				Mat hierarchy = new Mat();
				Imgproc.findContours(edges, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

				int mark = 0; // Reset all detected marker count for this frame

				Moments[] mu = new Moments[contours.size()];
				Point[] mc = new Point[contours.size()];

				for (int i = 0; i < contours.size(); i++) {
					mu[i] = Imgproc.moments(contours.get(i), false);
					mc[i] = new Point(mu[i].m10 / mu[i].m00, mu[i].m01 / mu[i].m00);
				}

				int A = 0, B = 0, C = 0, median1 = 0, median2 = 0, outlier = 0, top = 0, right = 0, bottom = 0;
				Double slope = new Double(0), dist = new Double(0);
				Alignment align = new Alignment(0);
				Integer orientation = 0;
				for (int i = 0; i < contours.size(); i++) {
					MatOfPoint2f c2f = new MatOfPoint2f(contours.get(i).toArray());
					MatOfPoint2f pointsseq = new MatOfPoint2f();
					Imgproc.approxPolyDP(c2f, pointsseq, Imgproc.arcLength(c2f, true) * 0.02, true);

					if (pointsseq.toArray().length == 4) {
						int k = i;
						int c = 0;

						while (hierarchy.get(0, k)[2] != -1) {
							k = (int) hierarchy.get(0, k)[2];
							c++;
						}
						if (hierarchy.get(0, k)[2] != -1)
							c++;

						if (c >= 5) {
							if (mark == 0)
								A = i;
							else if (mark == 1)
								B = i; // i.e., A is already found, assign current contour to B
							else if (mark == 2)
								C = i; // i.e., A and B are already found, assign current contour to C
							mark = mark + 1;
						}
					}
				}

				if (mark >= 3) { // Ensure we have (atleast 3; namely A,B,C)
									// 'Alignment Markers' discovered
					// We have found the 3 markers for the QR code; Now we need
					// to determine which of them are 'top', 'right' and
					// 'bottom' markers

					// Determining the 'top' marker
					// Vertex of the triangle NOT involved in the longest side
					// is the 'outlier'

					double AB = distance(mc[A], mc[B]);
					double BC = distance(mc[B], mc[C]);
					double CA = distance(mc[C], mc[A]);

					if (AB > BC && AB > CA) {
						outlier = C;
						median1 = A;
						median2 = B;
					} else if (CA > AB && CA > BC) {
						outlier = B;
						median1 = A;
						median2 = C;
					} else if (BC > AB && BC > CA) {
						outlier = A;
						median1 = B;
						median2 = C;
					}

					top = outlier; // The obvious choice

					dist = lineEquation(mc[median1], mc[median2], mc[outlier]); // Get
																				// the
																				// Perpendicular
																				// distance
																				// of
																				// the
																				// outlier
																				// from
																				// the
																				// longest
																				// side
					slope = lineSlope(mc[median1], mc[median2], align); // Also
																		// calculate
																		// the
																		// slope
																		// of
																		// the
																		// longest
																		// side

					// Now that we have the orientation of the line formed
					// median1 & median2 and we also have the position of the
					// outlier w.r.t. the line
					// Determine the 'right' and 'bottom' markers

					if (align.getValue() == 0) {
						bottom = median1;
						right = median2;
					} else if (slope < 0 && dist < 0) // Orientation - North
					{
						bottom = median1;
						right = median2;
						orientation = CV_QR_NORTH;
					} else if (slope > 0 && dist < 0) // Orientation - East
					{
						right = median1;
						bottom = median2;
						orientation = CV_QR_EAST;
					} else if (slope < 0 && dist > 0) // Orientation - South
					{
						right = median1;
						bottom = median2;
						orientation = CV_QR_SOUTH;
					}

					else if (slope > 0 && dist > 0) // Orientation - West
					{
						bottom = median1;
						right = median2;
						orientation = CV_QR_WEST;
					}

					// To ensure any unintended values do not sneak up when QR
					// code is not present

					if (top < contours.size() && right < contours.size() && bottom < contours.size()
							&& Imgproc.contourArea(contours.get(top)) > 10
							&& Imgproc.contourArea(contours.get(right)) > 10
							&& Imgproc.contourArea(contours.get(bottom)) > 10) {

						List<Point> L = new ArrayList<Point>();
						List<Point> M = new ArrayList<Point>();
						List<Point> O = new ArrayList<Point>();
						List<Point> tempL = new ArrayList<Point>();
						List<Point> tempM = new ArrayList<Point>();
						List<Point> tempO = new ArrayList<Point>();

						Point N = new Point();

						Mat warp_matrix = new Mat();

						getVertices(contours, top, slope, tempL);
						getVertices(contours, right, slope, tempM);
						getVertices(contours, bottom, slope, tempO);

						updateCornerOr(orientation, tempL, L); // Re-arrange
																// marker
																// corners w.r.t
																// orientation
																// of the QR
																// code
						updateCornerOr(orientation, tempM, M); // Re-arrange
																// marker
																// corners w.r.t
																// orientation
																// of the QR
																// code
						updateCornerOr(orientation, tempO, O); // Re-arrange
																// marker
																// corners w.r.t
																// orientation
																// of the QR
																// code

						boolean iflag = getIntersectionPoint(M.get(1), M.get(2), O.get(3), O.get(2), N);

						// src - Source Points basically the 4 end co-ordinates of the overlay image
						Mat src = new Mat(4, 1, CvType.CV_32FC2);

						// dst - Destination Points to transform overlay image*
						Mat dst = new Mat(4, 1, CvType.CV_32FC2);

						src.put(0, 0, L.get(0).x, L.get(0).y, M.get(1).x, M.get(1).y, N.x, N.y, O.get(3).x, O.get(3).y);
						dst.put(0, 0, 0, 0, qr.cols(), 0, qr.cols(), qr.rows(), 0, qr.rows());

						warp_matrix = Imgproc.getPerspectiveTransform(src, dst);
						Imgproc.warpPerspective(image, qr_raw, warp_matrix, new Size(qr.cols(), qr.rows()));

						Core.copyMakeBorder(qr_raw, qr, 10, 10, 10, 10, Core.BORDER_CONSTANT,
								new Scalar(255, 255, 255));

						foundQrCode = true;

						Imgproc.cvtColor(qr, qr_gray, Imgproc.COLOR_RGB2GRAY);
						Imgproc.threshold(qr_gray, qr_thres, 127, 255, Imgproc.THRESH_BINARY);

						Imgproc.drawContours(image, contours, top, new Scalar(255, 200, 0), 2);
						Imgproc.drawContours(image, contours, right, new Scalar(0, 0, 255), 2);
						Imgproc.drawContours(image, contours, bottom, new Scalar(255, 0, 100), 2);

						if (DBG == 1) {
							// Debug Prints
							// Visualizations for ease of understanding
							if (slope > 5)
								Imgproc.circle(traces, new Point(10, 20), 5, new Scalar(0, 0, 255), -1, 8, 0);
							else if (slope < -5)
								Imgproc.circle(traces, new Point(10, 20), 5, new Scalar(255, 255, 255), -1, 8, 0);

							// Draw contours on Trace image for analysis
							Imgproc.drawContours(traces, contours, top, new Scalar(255, 0, 100), 1);
							Imgproc.drawContours(traces, contours, right, new Scalar(255, 0, 100), 1);
							Imgproc.drawContours(traces, contours, bottom, new Scalar(255, 0, 100), 1);

							// Draw points (4 corners) on Trace image for each
							// Identification marker
							Imgproc.circle(traces, L.get(0), 2, new Scalar(255, 255, 0), -1, 8, 0);
							Imgproc.circle(traces, L.get(1), 2, new Scalar(0, 255, 0), -1, 8, 0);
							Imgproc.circle(traces, L.get(2), 2, new Scalar(0, 0, 255), -1, 8, 0);
							Imgproc.circle(traces, L.get(3), 2, new Scalar(128, 128, 128), -1, 8, 0);

							Imgproc.circle(traces, M.get(0), 2, new Scalar(255, 255, 0), -1, 8, 0);
							Imgproc.circle(traces, M.get(1), 2, new Scalar(0, 255, 0), -1, 8, 0);
							Imgproc.circle(traces, M.get(2), 2, new Scalar(0, 0, 255), -1, 8, 0);
							Imgproc.circle(traces, M.get(3), 2, new Scalar(128, 128, 128), -1, 8, 0);

							Imgproc.circle(traces, O.get(0), 2, new Scalar(255, 255, 0), -1, 8, 0);
							Imgproc.circle(traces, O.get(1), 2, new Scalar(0, 255, 0), -1, 8, 0);
							Imgproc.circle(traces, O.get(2), 2, new Scalar(0, 0, 255), -1, 8, 0);
							Imgproc.circle(traces, O.get(3), 2, new Scalar(128, 128, 128), -1, 8, 0);

							// Draw point of the estimated 4th Corner of
							// (entire) QR Code
							Imgproc.circle(traces, N, 2, new Scalar(255, 255, 255), -1, 8, 0);

							// Draw the lines used for estimating the 4th Corner
							// of QR Code
							Imgproc.line(traces, M.get(1), N, new Scalar(0, 0, 255), 1, 8, 0);
							Imgproc.line(traces, O.get(3), N, new Scalar(0, 0, 255), 1, 8, 0);

							// Show the Orientation of the QR Code wrt to 2D
							// Image Space
							int fontFace = Core.FONT_HERSHEY_PLAIN;

							if (orientation == CV_QR_NORTH) {
								Imgproc.putText(traces, "NORTE", new Point(20, 30), fontFace, 1, new Scalar(0, 255, 0),
										1);
							} else if (orientation == CV_QR_EAST) {
								Imgproc.putText(traces, "LESTE", new Point(20, 30), fontFace, 1, new Scalar(0, 255, 0),
										1);
							} else if (orientation == CV_QR_SOUTH) {
								Imgproc.putText(traces, "SUL", new Point(20, 30), fontFace, 1, new Scalar(0, 255, 0),
										1);
							} else if (orientation == CV_QR_WEST) {
								Imgproc.putText(traces, "OESTE", new Point(20, 30), fontFace, 1, new Scalar(0, 255, 0),
										1);
							}

							// Debug Prints
						}

					}
				}

				if (!qr_thres.empty() && foundQrCode) {
					MatOfByte mob = new MatOfByte();
					Imgcodecs.imencode(".png", qr_thres, mob);				

					try {
						BufferedImage qrCodeimage = ImageIO.read(new ByteArrayInputStream(mob.toArray()));
						LuminanceSource source = new BufferedImageLuminanceSource(qrCodeimage);
						BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

						Result result = new MultiFormatReader().decode(bitmap);
						System.out.println(result.getText());
						textArea.setText(result.getText());
						
						listener.onCollect(new SimpleResult(result.getText()), thing);

					} catch (Exception e) {
						System.out.println(e);
						textArea.setText("");
					}
				}
				try {
					my_panel.MatToBufferedImage(image);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				my_panel.repaint();
			}

		}

	}
	
	/**
	 * Retorna a distâ¢ncia entre dois pontos
	 * 
	 * @param P
	 * @param Q
	 * @return
	 */
	protected static double distance(Point P, Point Q) {
		return Math.sqrt(Math.pow(Math.abs(P.x - Q.x), 2) + Math.pow(Math.abs(P.y - Q.y), 2));
	}

	/**
	 * Distância perpendicular de um ponto J a partir da linha formada pelos pontos
	 * L e M; EquaÃ§Ã£o da linha ax + by + c = 0
	 * 
	 * @param L
	 * @param M
	 * @param J
	 * @return
	 */
	protected static double lineEquation(Point L, Point M, Point J) {
		double a, b, c, pdist;

		a = -((M.y - L.y) / (M.x - L.x));
		b = 1.0;
		c = (((M.y - L.y) / (M.x - L.x)) * L.x) - L.y;

		// Now that we have a, b, c from the equation ax + by + c, time to
		// substitute (x,y) by values from the Point J

		pdist = (a * J.x + (b * J.y) + c) / Math.sqrt((a * a) + (b * b));
		return pdist;
	}

	/**
	 * Inclinação de uma linha por dois pontos L e M sobre ele; Inclinação de uma
	 * linha, S = (x1 -x2) / (y1- y2)
	 * 
	 * @param L
	 * @param M
	 * @param alignement
	 * @return
	 */
	protected static double lineSlope(Point L, Point M, Alignment alignement) {
		Double dx, dy;
		dx = M.x - L.x;
		dy = M.y - L.y;

		if (dy != 0) {
			alignement.setValue(1);
			return (dy / dx);
		} else // Make sure we are not dividing by zero; so use 'alignement'
				// flag
		{
			alignement.setValue(0);
			return 0.0;
		}
	}

	/**
	 * Rotina para calcular 4 Cantos do Marcador no EspaÃ§o de Imagem usando
	 * Particionamento de RegiÃ£o Teoria: OpenCV Contornos armazena todos os pontos
	 * que o descrevem e esses pontos sÃ£o o perÃ­metro do polÃ­gono. A funÃ§Ã£o
	 * abaixo escolhe os pontos mais distantes do polÃ­gono, pois eles formam os
	 * vÃ©rtices desse polÃ­gono, exatamente os pontos que estamos procurando. Para
	 * escolher o ponto mais distante, o polÃ­gono Ã© dividido particionado em 4
	 * regiÃµes regiÃµes iguais usando a caixa delimitadora. O algoritmo de
	 * distÃ¢ncia Ã© aplicado entre o centro da caixa delimitadora cada ponto de
	 * contorno naquela regiÃ£o, o ponto mais distante Ã© considerado o vÃ©rtice
	 * dessa regiÃ£o. CÃ¡lculo para todas as 4 regiÃµes obtemos os 4 cantos do
	 * polÃ­gono (- quadrilÃ¡tero).
	 * 
	 * @param contours
	 * @param c_id
	 * @param slope
	 * @param quad
	 */
	protected static void getVertices(List<MatOfPoint> contours, int c_id, double slope, List<Point> quad) {
		Rect box;
		box = Imgproc.boundingRect(contours.get(c_id));

		Point M0 = new Point();
		Point M1 = new Point();
		Point M2 = new Point();
		Point M3 = new Point();
		Point A = new Point();
		Point B = new Point();
		Point C = new Point();
		Point D = new Point();
		Point W = new Point();
		Point X = new Point();
		Point Y = new Point();
		Point Z = new Point();

		A = box.tl();
		B.x = box.br().x;
		B.y = box.tl().y;
		C = box.br();
		D.x = box.tl().x;
		D.y = box.br().y;

		W.x = (A.x + B.x) / 2;
		W.y = A.y;

		X.x = B.x;
		X.y = (B.y + C.y) / 2;

		Y.x = (C.x + D.x) / 2;
		Y.y = C.y;

		Z.x = D.x;
		Z.y = (D.y + A.y) / 2;

		Double[] dmax = new Double[4];
		dmax[0] = 0.0;
		dmax[1] = 0.0;
		dmax[2] = 0.0;
		dmax[3] = 0.0;

		Double pd1 = 0.0;
		Double pd2 = 0.0;

		if (slope > 5 || slope < -5) {

			Point[] points = contours.get(c_id).toArray();
			for (int i = 0; i < points.length; i++) {
				pd1 = lineEquation(C, A, points[i]); // Position of
														// point w.r.t
														// the diagonal
														// AC
				pd2 = lineEquation(B, D, points[i]); // Position of
														// point w.r.t
														// the diagonal
														// BD

				if ((pd1 >= 0.0) && (pd2 > 0.0)) {
					dmax[1] = updateCorner(points[i], W, dmax[1], M1);
				} else if ((pd1 > 0.0) && (pd2 <= 0.0)) {
					dmax[2] = updateCorner(points[i], X, dmax[2], M2);
				} else if ((pd1 <= 0.0) && (pd2 < 0.0)) {
					dmax[3] = updateCorner(points[i], Y, dmax[3], M3);
				} else if ((pd1 < 0.0) && (pd2 >= 0.0)) {
					dmax[0] = updateCorner(points[i], Z, dmax[0], M0);
				} else
					continue;
			}
		} else {
			int halfx = (int) ((A.x + B.x) / 2);
			int halfy = (int) ((A.y + D.y) / 2);

			Point[] points = contours.get(c_id).toArray();

			for (int i = 0; i < points.length; i++) {
				if ((points[i].x < halfx) && (points[i].y <= halfy)) {
					dmax[2] = updateCorner(points[i], C, dmax[2], M0);
				} else if ((points[i].x >= halfx) && (points[i].y < halfy)) {
					dmax[3] = updateCorner(points[i], D, dmax[3], M1);
				} else if ((points[i].x > halfx) && (points[i].y >= halfy)) {
					dmax[0] = updateCorner(points[i], A, dmax[0], M2);
				} else if ((points[i].x <= halfx) && (points[i].y > halfy)) {
					dmax[1] = updateCorner(points[i], B, dmax[1], M3);
				}
			}
		}

		quad.add(M0);
		quad.add(M1);
		quad.add(M2);
		quad.add(M3);

	}

	/**
	 * Função: Compare um ponto se estiver mais distante do que anteriormente
	 * gravado distÃ¢ncia mais distante DescriÃ§Ã£o: DetecÃ§Ã£o de ponto mais
	 * distante usando ponto de referÃªncia e distÃ¢ncia de linha de base
	 * 
	 * @param P
	 * @param ref
	 */
	protected static Double updateCorner(Point P, Point ref, Double baseline, Point corner) {
		double temp_dist;
		temp_dist = distance(P, ref);

		if (temp_dist > baseline) {
			baseline = temp_dist; // The farthest distance is the new baseline
			corner.x = P.x; // P is now the farthest point
			corner.y = P.y;
		}

		return baseline;
	}

	/**
	 * Sequencia de cantos WRT para a orientaÃ§Ã£o do QR Code
	 * 
	 * @param orientation
	 * @param IN
	 * @param OUT
	 */
	protected static void updateCornerOr(int orientation, List<Point> IN, List<Point> OUT) {
		Point M0 = new Point();
		Point M1 = new Point();
		Point M2 = new Point();
		Point M3 = new Point();
		if (orientation == CV_QR_NORTH) {
			M0 = IN.get(0);
			M1 = IN.get(1);
			M2 = IN.get(2);
			M3 = IN.get(3);
		} else if (orientation == CV_QR_EAST) {
			M0 = IN.get(1);
			M1 = IN.get(2);
			M2 = IN.get(3);
			M3 = IN.get(0);
		} else if (orientation == CV_QR_SOUTH) {
			M0 = IN.get(2);
			M1 = IN.get(3);
			M2 = IN.get(0);
			M3 = IN.get(1);
		} else if (orientation == CV_QR_WEST) {
			M0 = IN.get(3);
			M1 = IN.get(0);
			M2 = IN.get(1);
			M3 = IN.get(2);
		}

		OUT.add(M0);
		OUT.add(M1);
		OUT.add(M2);
		OUT.add(M3);
	}

	/**
	 * FunÃ§Ã£o: Obter o Ponto de IntersecÃ§Ã£o das linhas formado por conjuntos de
	 * dois pontos
	 * 
	 * @param a1
	 * @param a2
	 * @param b1
	 * @param b2
	 * @return
	 */
	protected static boolean getIntersectionPoint(Point a1, Point a2, Point b1, Point b2, Point intersection) {
		Point p = a1;
		Point q = b1;
		Point r = subtract(a2, a1);
		Point s = subtract(b2, b1);

		if (cross(r, s) == 0) {
			return false;
		}

		double t = cross(subtract(q, p), s) / cross(r, s);

		Point z = new Point(r.x * t, r.y * t);
		Point point = add(p, z);
		intersection.x = point.x;
		intersection.y = point.y;
		return true;
	}

	protected static double cross(Point v1, Point v2) {
		return v1.x * v2.y - v1.y * v2.x;
	}

	public static Point add(Point p1, Point p2) {
		return new Point(p1.x + p2.x, p1.y + p2.y);
	}

	public static Point subtract(Point p1, Point p2) {
		return new Point(p1.x - p2.x, p1.y - p2.y);
	}

	public static Point multiply(Point p1, Point p2) {
		return new Point(p1.x * p2.x, p1.y * p2.y);
	}

}
