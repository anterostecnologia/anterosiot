package br.com.anteros.iot.collectors;


import java.awt.Graphics;
import java.awt.image.BufferedImage;
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
//    	MatOfByte mob = new MatOfByte();
//		Imgcodecs.imencode(".png", matBGR, mob);
//		image = ImageIO.read(new ByteArrayInputStream(mob.toArray()));
//        return true;      	
    	MatOfByte mob = new MatOfByte();
		Imgcodecs.imencode(".png", matBGR, mob);
    	byte[] bytes = mob.toArray();
    	ImageIO.setUseCache(false);
		image = ImageIO.read(new ByteArrayInputStream(bytes));
        return true;  
    }  
    public void paintComponent(Graphics g){  
        super.paintComponent(g);   
        if (this.image==null) return;  
        g.drawImage(this.image,0,0,this.image.getWidth(),this.image.getHeight(), null);  
        //g.drawString("This is my custom Panel!",10,20);  
    }  
}  
