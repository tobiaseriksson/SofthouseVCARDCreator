/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vcardmaker;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 *
 * @author tobiase
 */
public class VCard {
    public String fullName = "";
    public String workMobilePhoneNumber = "";
    public String workFixedPhoneNumber = "";
    public String workEmail = "";
    public String homeNumber = "";
    public String photoBase64Encoded = "";
    public String companyName = "";
    public String companyAddress = "";
    public String workTitle = "";
	public String webaddress = "";
	public String uid = "";
    
    public VCard() {
    }
    
    public String createVCard() {
    	String vcardString = "";
    	try {
    	String name[] = fullName.split("\\s");    	
    	if( name.length <= 1 ) {
    		name = new String[2];
    		name[0] = fullName;
    		name[1] = "";
    	} 
    		    
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    	StringBuilder dateAsString = new StringBuilder( dateFormat.format( new Date() ) );
    	photoBase64Encoded = photoBase64Encoded.replaceAll("^", " " );
    	vcardString = 
    		"BEGIN:VCARD\n"+
    		"VERSION:3.0\n"+
    		"REV:"+dateAsString+"\n"+
    		"UID:"+uid+"\n"+
    		"FN:"+fullName+"\n"+
    		"N:"+name[1]+";"+name[0]+";;;\n"+
    		"TITLE:"+workTitle+"\n"+
    		"TEL;WORK;VOICE;CELL;PREF:"+workMobilePhoneNumber+"\n"+
    		"TEL;WORK;VOICE:"+workFixedPhoneNumber+"\n"+
    		"EMAIL;TYPE=INTERNET;WORK:"+workEmail+"\n"+
    		"ORG:"+companyName+"\n" +
    		"ADR;TYPE=WORK,POSTAL:"+companyAddress+"\n"+
       		"PHOTO;TYPE=JPEG;ENCODING=BASE64:\n" +
    		photoBase64Encoded + "\n\n" + 
    		"END:VCARD\n";
    	}
    	catch( Exception exc ) {
    		
    		exc.printStackTrace();
    	}
    	
    	return vcardString;
    }
    
    public static boolean ValidationOfVCardOK() {
    	// String url = "http://seriewikin.serieframjandet.se/images/d/d9/Gaston_huvud.jpg";
    	String url = "http://www.t-s-t.se/linda/gabriel.jpg";
    	ParseHTMLPage parser = new ParseHTMLPage("username","password");
    	//BufferedImage img = parser.retrieveImageFromWeb( url );
    	//String base64EncodedImage = parser.convertImageToBase64EncodedString( img );
    	// byte bytes[] = parser.retriveFileFromWeb( url );
    	byte bytes[] = ParseHTMLPage.getImage( url );
    	String base64EncodedImage = Base64.encodeBytes( bytes );
    	FileOutputStream fos;
		try {
			fos = new FileOutputStream("tobiasbild.jpg");
			fos.write( bytes );
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	// System.out.println("Base64Encoded Image:\n"+base64EncodedImage );
    	
    	VCard vcard = new VCard();
    	vcard.fullName = "Tobias Eriksson";
    	vcard.homeNumber = "045715677";
    	vcard.companyName = "Softhouse";
    	vcard.companyAddress = "Campus Gräsvik 3A, S-371 75 Karlskrona, Sweden";
    	vcard.workEmail = "tobias.eriksson@softhouse.se";
    	vcard.workTitle = "Software Engineer";
    	vcard.workMobilePhoneNumber = "+46703213051";
    	vcard.photoBase64Encoded = base64EncodedImage;
    	System.out.println( vcard.createVCard() );
    	return true;
    }
    
    public String toString() {
    	String result = "";
    	result = "name: "+fullName+"\n";
    	result += "mobile: "+workMobilePhoneNumber+"\n";
    	result += "email: "+workEmail+"\n";
    	return result;
    }
    
    
    public static void main(String args[]) {
    	VCard.ValidationOfVCardOK();    	
    }
}
