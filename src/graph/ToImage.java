package graph;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class ToImage {

    public static void main(String[] args) throws Exception{
        BufferedImage im = new BufferedImage(100, 100,
                BufferedImage.TYPE_INT_RGB);

        Graphics2D g2 = im.createGraphics();
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, 100, 100);
        g2.setColor(Color.BLACK);
        
        java.awt.geom.Ellipse2D ellipse2D=new Ellipse2D.Double(10,10,20,20);
        g2.setPaint(Color.red); 
        g2.fill( ellipse2D ); 
        OutputStream out = new FileOutputStream("d:/test.jpg");
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
        encoder.encode(im);
        out.flush();
        out.close();
        im.flush();
    }
    
}
