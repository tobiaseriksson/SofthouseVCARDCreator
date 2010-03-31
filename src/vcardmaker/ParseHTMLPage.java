/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vcardmaker;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;
import javax.imageio.*;
import javax.net.ssl.*;

import sun.misc.BASE64Encoder;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
/**
 *
 * @author tobiase
 */
public class ParseHTMLPage {
    
    private boolean debug = false;
    private String photoBaseURL;
    private String url;
    private String htmlPage;
    private String dummyPhotoBase64Encoded = "";
    private TrustManager[] trustAllCerts;
    
    public ParseHTMLPage( String _photBaseURL, String _url ) {
    	photoBaseURL = _photBaseURL;
    	url = _url;
    }
    
    public void init() {
    	byte bytes[] = loadFile( "gaston.gif" );
    	dummyPhotoBase64Encoded = Base64.encodeBytes( bytes ); 
    	
    	// Create a trust manager that does not validate certificate chains
        trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
                }
            }
        };
     // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } 
        catch (Exception e) {
        	e.printStackTrace();
        }


    }
    
    public void debug() {
        debug = true;
    }    
    
    public void getPage() {
        try { 
            // URL yahoo = new URL("http://baltic.softhouse.se/wiki/doku.php?id=personal&u=toe&p=joh316"); 
            URL yahoo = new URL(url); 
            BufferedReader in = new BufferedReader( new InputStreamReader(yahoo.openStream()) ); 
            String inputLine;     
            while ((inputLine = in.readLine()) != null) { 
                // Process each line.
                if( debug ) System.out.println(inputLine); 
                htmlPage+=inputLine+"\n";
            } 
            in.close(); 
    
        } catch (MalformedURLException me) { 
            System.out.println(me); 
    
        } catch (IOException ioe) { 
            System.out.println(ioe); 
        } 
    }
    
    public LinkedList<VCard> parsePageForVCards() {
        LinkedList<VCard> listOfVCARDs = new LinkedList<VCard>();
        Scanner scanner = new Scanner( htmlPage );
        String line;
        boolean personellTagFound = false;
        boolean personellTableFound = false;
        try {
          //first use a Scanner to get each line
          while ( scanner.hasNextLine() ){
            line = scanner.nextLine();
            if( debug ) System.out.println( "line="+line );
            
            if( line.compareToIgnoreCase("<h1><a name=\"personal\" id=\"personal\">Personal</a></h1>") == 0 ) {
                personellTagFound = true;
            }            
            if( line.startsWith("<table") ) {
                personellTableFound = true;
            }
            if( personellTableFound && personellTagFound ) {
                VCard newVCARD = processLine( line );
                if( newVCARD != null ) {
                    System.out.println( "New Employee found!");
                    listOfVCARDs.add( newVCARD );
                }                
            }
            if( line.startsWith("</table>") && personellTableFound && personellTagFound ) {
            	personellTableFound = false;
            	personellTagFound = false;
            }

          }
        }        
        catch( Exception exc ) {
            System.out.println( "Exception; "+exc.toString() );
            exc.printStackTrace();
        }
        finally {
          //ensure the underlying stream is always closed
          scanner.close();          
        } 
        return listOfVCARDs;
    }
        
    private VCard processLine(String aLine) {    
        VCard newVcard = null;
        try {
         Scanner s = new Scanner(aLine);
         s.findInLine("<td.*?>(.*?)<.td><td.*?>(.*?)<.td><td.*?>(.*?)<.td><td.*?>(.*?)<.td><td.*?>(.*?)<.td><td.*?>(.*?)<.td><td.*?>(.*?)<.td><td.*?>(.*?)<.td>"); 
         // <td.*?>(.*?)<\/td>"); // <td.*?>(.*?)<\/td><td.*?>(.*?)<\/td><td.*?>(.*?)<\/td><td.*?>(.*?)<\/td><td.*?>(.*?)<\/td>/)");
         MatchResult result;
         try {
            result = s.match();
         }
         catch( IllegalStateException ise ) {
             return newVcard;
         }
         if( true ) {
             for (int i=1; i<=result.groupCount(); i++)
                 System.out.println(i+" = "+result.group(i));
             s.close();                 
            }
          newVcard = new VCard();
          newVcard.fullName = result.group(2);
          if( newVcard.fullName.indexOf("href") >= 0 ) {
        	  newVcard.fullName = parseOutContentFromATag( newVcard.fullName );
          }
          newVcard.fullName = newVcard.fullName.replaceFirst("^\\s", "");          
          newVcard.workFixedPhoneNumber= cleanPhoneNumer(result.group(3));
          newVcard.workMobilePhoneNumber = cleanPhoneNumer(result.group(4));
          newVcard.homeNumber = cleanPhoneNumer(result.group(5));
          
          String base64EncodedImage = "";
          String photoFilename = parseOutImageFilenameFromATag( result.group( 1 ) );
          if( photoFilename == null ) {
        	  base64EncodedImage = dummyPhotoBase64Encoded;
          } else {
	          String photoURL = photoBaseURL+photoFilename;
	          byte bytes[] = ParseHTMLPage.getImage( photoURL );
	      	  base64EncodedImage = Base64.encodeBytes( bytes );
          }
          String lines[] = base64EncodedImage.split("\\n");
          base64EncodedImage="";
          for( int i=0;i<lines.length;i++) {
        	  if( lines[i].startsWith(" ") ) base64EncodedImage=base64EncodedImage+lines[i]+"\n";
        	  else base64EncodedImage=base64EncodedImage+" "+lines[i]+"\n";
        	  // System.out.println("line;"+i+" of "+lines.length);
          }
      	  newVcard.photoBase64Encoded = base64EncodedImage;
        }
        catch( Exception exc ) {
            System.out.println("Exception; "+exc.toString() );
            exc.printStackTrace();
        }
        return newVcard;
    } 
    
    /**
     * Remove all spaces and clean up the phonenumber so that there are no +XX(0)... but rather just +46... 
     * @param phonenumber
     * @return
     */
    private String cleanPhoneNumer( String phonenumber ) {
    	String tmp = phonenumber.replaceAll( " \\(0\\)", "" );
    	tmp = tmp.replaceAll("\\s", "");
    	return tmp;
    }
    
    private String parseOutImageFilenameFromATag( String str ) {        
         Scanner s = new Scanner(str);
         MatchResult result;
         s.findInLine("media=(.*?)\"");          
         try {
            result = s.match();
         }
         catch( IllegalStateException ise ) {
             return null;
         }
         return result.group(1);
    }
    
    private String parseOutContentFromATag( String str ) {        
        Scanner s = new Scanner(str);
        MatchResult result;
        s.findInLine(">(.*?)<");          
        try {
           result = s.match();
        }
        catch( IllegalStateException ise ) {
            return null;
        }
        return result.group(1);
   }
    
    public BufferedImage retrieveImageFromWeb( String _URLString ) {
    	BufferedImage img = null;
        try {
            URL url = new URL( _URLString );
            img = ImageIO.read( url );
        }
        catch( Exception exc ) {
            System.out.println("Unable to retrieve image; "+_URLString );
            System.out.println( "Exception; "+exc.toString() );
        }
        return img;
    }
    
    public static byte[] getImage(String _URLString)  {
    	ByteArrayOutputStream os = new ByteArrayOutputStream();
    	try {
        URL url = new URL(_URLString );
        InputStream in = url.openStream();
        // OutputStream out = new BufferedOutputStream(new FileOutputStream("temp.jpg"));
        for (int b; (b = in.read()) != -1; ) {
            // out.write(b);
            os.write(b);
        }
        // out.close();
        in.close();
        os.close();
    	}
    	catch( Exception exc ) {
    		System.out.println("URL; "+_URLString );
    		exc.printStackTrace();
    	}
    	return os.toByteArray();
    }

    private byte[] loadFile( String path ) {
    	byte bytes[] = new byte[1024*4];
    	ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
        	FileInputStream fis = new FileInputStream( path );
            while( fis.available() > 0 ) {
            	fis.read(bytes);
            	os.write(bytes);            	
            }
            fis.close();
        }
        catch( Exception exc ) {
            System.out.println("Unable to load file; "+ path );
            exc.printStackTrace();
        }
        return os.toByteArray();        
    }
    
    
    
    public static void verifyImageRetrieval() {
    	String url = "http://www.t-s-t.se/linda/gabriel.jpg";
    	ParseHTMLPage parser = new ParseHTMLPage("username","password");
    	BufferedImage img = parser.retrieveImageFromWeb( url );    	
    	try { ImageIO.write(img,"jpg",new File("temp2.jpg")); }
    	catch (Exception e) { e.printStackTrace(); }

    }
    
    public static void retrieveVCardsFromSofthouseIntranet() {
    	
    	// String url = "https://baltic.softhouse.se/wiki/doku.php?id=personal&u=toe&p=##lindae03";
    	String url = "https://intra.softhouse.se/karlskronawiki/-/wiki/Karlskrona/Personal?id=personal&u=toe&p=##lindae03";
    	String imgBaseURL = "https://baltic.softhouse.se/wiki/lib/exe/fetch.php?cache=cache&u=my_user_name&p=my_password&media=";
    	ParseHTMLPage parser = new ParseHTMLPage( imgBaseURL, url );
    	parser.init();
    	// parser.debug();
    	parser.getPage();
    	LinkedList<VCard> vcards = parser.parsePageForVCards();
    	ListIterator<VCard> vcardsIterator = vcards.listIterator();
    	try {
    		String vcardString = "";
			FileWriter fw = new FileWriter( "softhouse_baltic.vcf" );			
			VCard vc = null;
	    	while( vcardsIterator.hasNext() ) {
	    		vc = vcardsIterator.next();
	    		vc.workTitle = "Consultant";
	    		vc.companyName = "Softhouse";
	            vc.companyAddress = "Campus Gräsvik 3A, S-371 75 Karlskrona, Sweden";
	            vc.webaddress = "www.softhouse.se";
	    		vcardString = vc.createVCard();
	    		fw.write( vcardString  );
	    	}
	    	fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    	
    }
    
    
    public static void main(String[] args) {
    	ParseHTMLPage.retrieveVCardsFromSofthouseIntranet();
	}
    
}
