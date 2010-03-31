package vcardmaker;

import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.swing.*;
import java.applet.Applet;

public class VCARDCreatorApplet extends Applet
		 implements ActionListener {

   JLabel text;
   JButton button;
   JPanel panel;
   BufferedImage img;
   BufferedImage img2;
   String base64EncodedImage;
   private boolean _clickMeMode = true;

   public void init(){
     setLayout(new BorderLayout(1, 2));
     setBackground(Color.white);

     text = new JLabel("I'm a Simple Program");
     button = new JButton("Click Me");
     button.addActionListener(this);
     add("Center", text);
     add("South", button);
     
     String url = "http://www.t-s-t.se/linda/gabriel.jpg";
 	 ParseHTMLPage parser = new ParseHTMLPage("username","password");
 	 img = parser.retrieveImageFromWeb( url );
 	 // base64EncodedImage = parser.convertImageToBase64EncodedString( img );
 	 // byte bytes[] = Base64.decode( base64EncodedImage );
 	 
 	 
   }

  public void start(){
     System.out.println("Applet starting.");
  }

  public void stop(){
     System.out.println("Applet stopping.");
  }

  public void destroy(){
     System.out.println("Destroy method called.");
  }

   public void actionPerformed(ActionEvent event){
        Object source = event.getSource();
        if (_clickMeMode) {
          text.setText("Button Clicked");
          button.setText("Click Again");
          _clickMeMode = false;
        } else {
          text.setText("I'm a Simple Program");
          button.setText("Click Me");
          _clickMeMode = true;
        }
   }
   
   public void paint( Graphics g ) {
	   
	   Graphics2D g2d = (Graphics2D)g;
	   g2d.drawString("hej", 10, 20);
   }
   
}
