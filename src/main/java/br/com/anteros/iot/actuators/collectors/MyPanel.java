package br.com.anteros.iot.actuators.collectors;


import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;  
class My_Panel extends JPanel{  
    private BufferedImage image;  
    // Create a constructor method  


    public My_Panel(){  
        super();   
    }  
    /**  
     * Converts/writes a Mat into a BufferedImage.  
     *   
     * @param matrix Mat of type CV_8UC3 or CV_8UC1  
     * @return BufferedImage of type TYPE_3BYTE_BGR or TYPE_BYTE_GRAY  
     * @throws IOException 
     */  
    public boolean MatToBufferedImage(Mat matBGR) throws IOException{  
		image = matToBufferedImage(matBGR);
        return true;  
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
    
    public void setImage(BufferedImage image) {
    	this.image = image;
    }
    public void paintComponent(Graphics g){  
        super.paintComponent(g);   
        if (this.image==null) return;  
        g.drawImage(this.image,0,0,this.image.getWidth(),this.image.getHeight(), null);  
        //g.drawString("This is my custom Panel!",10,20);  
    }  
}  
