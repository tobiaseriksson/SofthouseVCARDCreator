package vcardmaker;

/*
 *
 * A free Java sample program 
 * to POST to a HTTPS secure SSL website
 *
 * @author William Alexander
 * free for use as long as this comment is included 
 * in the program as it is
 * 
 * More Free Java programs available for download 
 * at http://www.java-samples.com
 *
 */
import java.io.*;
import java.net.*;
import java.security.Security.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.net.ssl.*;
import com.sun.*;

public class sslpost {

	public sslpost() {
		
	}	
	
	public String excutePost(String targetURL, String urlParameters) {
		URL url;
		HttpURLConnection connection = null;
		try { // Create connection
			url = new URL(targetURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setInstanceFollowRedirects(true);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			// Send request
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
			// Get Response
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			StringBuffer response = new StringBuffer();
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	/**
	 * Logins into a page using a HTTP-POST request
	 * And returns the resulting page (HTML) as a String 
	 * @param targetURL
	 * @param urlParameters
	 * @return
	 */
	public String loginWithSSL(String targetURL, String urlParameters) {
		URL url;
		HttpURLConnection connection = null;
		try { // Create connection
			System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
			java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
			url = new URL(targetURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setInstanceFollowRedirects(true);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			// Send request
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
			StringBuffer response = new StringBuffer();
			
			/*
			 *  Get Response header
			 *  			
			response.append("Response message:");
			for (int i=0; ; i++) {
		        String headerName = connection.getHeaderFieldKey(i);
		        String headerValue = connection.getHeaderField(i);
		        
		        if (headerName == null && headerValue == null) {
		            // No more headers
		            break;
		        } else {
		        	if (headerName == null) {
		            // The header value contains the server's HTTP version
		        	} else {
		        		response.append( headerName + " = " + headerValue + "\n" );		        	
		        	}
		        }
		    }
			response.append("Response message end:");
			*/
			if (connection.getHeaderField("Location") != null )  {
				System.out.println( "location="+connection.getHeaderField("Location") );
			}
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}
	
	public String readPage( String targetURL ) {
		URL url;
		HttpURLConnection connection = null;
		try { // Create connection
			System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
			java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
			url = new URL(targetURL);
			connection = (HttpURLConnection) url.openConnection();
			// connection.setRequestMethod("GET");
			connection.setInstanceFollowRedirects(true);
			/*
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			*/
			
			/*
			// Send request
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
			*/
			
			StringBuffer response = new StringBuffer();
			
			/*
			 *  Get Response header
			 *  			
			response.append("Response message:");
			for (int i=0; ; i++) {
		        String headerName = connection.getHeaderFieldKey(i);
		        String headerValue = connection.getHeaderField(i);
		        
		        if (headerName == null && headerValue == null) {
		            // No more headers
		            break;
		        } else {
		        	if (headerName == null) {
		            // The header value contains the server's HTTP version
		        	} else {
		        		response.append( headerName + " = " + headerValue + "\n" );		        	
		        	}
		        }
		    }
			response.append("Response message end:");
			*/
			if (connection.getHeaderField("Location") != null )  {
				System.out.println( "location="+connection.getHeaderField("Location") );
			}
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}
	
	public String getSessionID( String html ) {		
		String sessionId = "";
		String[] htmlLineByLine = html.split("\r\n|\r|\n");
		String theHREFPattern = "href..engine.wsc.cmd.logout.session"; 
		for( String line : htmlLineByLine ) {	
			// System.out.println(":"+line);
			// if( line.matches( "href" ) == true ) { matches does not work!!! WHY ?
			if( line.indexOf( "href") > 0 ) {
				// System.out.println( "found: "+line );
				Pattern regex = Pattern.compile("session=(\\w+)"); 
				Matcher m = regex.matcher( line ); 
				if (m.find()) { 
				    sessionId = m.group(1);
				    break;
				}
			}
		}
		return sessionId;
	}
	
	
	public String getPktString( String html ) {		
		String pkt = "";
		String[] htmlLineByLine = html.split("\r\n|\r|\n");		
		for( String line : htmlLineByLine ) {	
			 System.out.println(":"+line);
			// if( line.matches( "href" ) == true ) { matches does not work!!! WHY ?
			if( line.indexOf( "href") > 0 ) {
				 System.out.println( "found: "+line );
				Pattern regex = Pattern.compile("pkt=([-:%\\w]+)"); 
				Matcher m = regex.matcher( line ); 
				if (m.find()) { 
				    pkt = m.group(1);
				    break;
				}
			}
		}
		return pkt;
	}
	
	
	public void addToListOfIDs( String html, LinkedList<String> listOfIDs ) {		
		//LinkedList<String> listOfIDs = new LinkedList<String>();
		String[] htmlLineByLine = html.split("\r\n|\r|\n");		
		for( String line : htmlLineByLine ) {	
			// System.out.println(":"+line);
			// if( line.matches( "href" ) == true ) { matches does not work!!! WHY ?
			if( line.indexOf( "href") > 0 ) {
				// System.out.println( "found: "+line );
				// 
				Pattern regex = Pattern.compile("s_id=(\\d+)&"); 
				Matcher m = regex.matcher( line ); 
				if (m.find()) { 
					listOfIDs.add( m.group(1) );
				}
			}
		}
		// return listOfIDs;
	}
	
	public int getTotalNumberOfEmployees( String html ) {		
		int totalNumberOfEmployees = 0;
		String[] htmlLineByLine = html.split("\r\n|\r|\n");
		for( String line : htmlLineByLine ) {	
			if( line.indexOf( "href") > 0 ) {
				Pattern regex = Pattern.compile("sc=(\\d+)&"); 
				Matcher m = regex.matcher( line ); 
				if (m.find()) { 
					totalNumberOfEmployees = Integer.valueOf(m.group(1));
					break;
				}
			}
		}
		return totalNumberOfEmployees;
	}
	
	
	public VCard parseHTMLForVCardInformation( String html ) {
		VCard vcard = new VCard();
		//Pattern findFullnamePattern = Pattern.compile("txtheader\">([\\wåäöÅÄÖ]+)\\s+([\\wåäöÅÄÖ]+)");
		Pattern findFullnamePattern = Pattern.compile("txtheader\">([\\s\\d\\wåäöÅÄÖé]+)\\suppgifter<\\/div");
		Matcher findFullnameMatcher = null;
		
		// <td class="maintxt">Mobilnr&nbsp;&nbsp;</td><td class="maintxt">0768-640324</td>
		// Pattern findMobilephoneNumberPattern = Pattern.compile("Mobilnr.*>([\\s\\d-]+)");
		Pattern findMobilephoneNumberPattern = Pattern.compile("Mobilnr&nbsp;&nbsp;<.td><td class=\"maintxt\">([\\s\\d-+]+)");
		Matcher findMobilephoneNumberMatcher = null;

		
		// class=\"maintxt\">E-post&nbsp;&nbsp;</td><td class=\"maintxt\"><a href=\"mailto:bjorn.eriksson@softhouse.se\" style=\"font-weight:normal\">bjorn.eriksson@softhouse.se</a></td>";
		//Pattern findEMailPattern = Pattern.compile("E-post.*mailto:(.+)\"");
		Pattern findEMailPattern = Pattern.compile("E-post.*mailto:([a-zA-Z0-9._+\\-@]+)");
		Matcher findEMailMatcher = null;

		// <tr><td class=\"maintxt\">Besöksadress&nbsp;&nbsp;</td><td class=\"maintxt\">Ölandsgatan 42</td></tr><tr><td class=\"maintxt\">Postadress&nbsp;&nbsp;</td><td class=\"maintxt\">Ölandsgatan 42</td></tr><tr><td class=\"maintxt\">Postnr&nbsp;&nbsp;</td><td class=\"maintxt\">116 63</td></tr><tr><td class=\"maintxt\">Postort&nbsp;&nbsp;</td><td class=\"maintxt\">STOCKHOLM</td></tr><tr><td class=\"maintxt\">Land&nbsp;&nbsp;</td><td class=\"maintxt\">Sverige</td></tr><tr><td class=\"maintxt\">E-post&nbsp;&nbsp;</td><td class=\"maintxt\">";
		Pattern findStreetPattern = Pattern.compile("Besöksadress&nbsp;&nbsp;<.td><td class=\"maintxt\">([-+\\s\\d\\wåäöÅÄÖé]+)");
		Matcher findStreetMatcher = null;
		Pattern findZipPattern = Pattern.compile("Postnr&nbsp;&nbsp;<.td><td class=\"maintxt\">([-+\\s\\d\\wåäöÅÄÖé]+)");
		Matcher findZipMatcher = null;
		Pattern findCityPattern = Pattern.compile("Postort&nbsp;&nbsp;<.td><td class=\"maintxt\">([-+\\s\\d\\wåäöÅÄÖé]+)");
		Matcher findCityMatcher = null;
		Pattern findCountryPattern = Pattern.compile("Land&nbsp;&nbsp;<.td><td class=\"maintxt\">([-+\\s\\d\\wåäöÅÄÖé]+)");
		Matcher findCountryMatcher = null;
		
		
		String[] htmlLineByLine = html.split("\r\n|\r|\n");		
		for( String line : htmlLineByLine ) {
			// System.out.println( ":"+line );
			findFullnameMatcher = findFullnamePattern.matcher( line );
			if (findFullnameMatcher.find()) { 
				vcard.fullName = findFullnameMatcher.group(1);
				System.out.println( "name:"+vcard.fullName );
			}
			findMobilephoneNumberMatcher = findMobilephoneNumberPattern.matcher(line);
			if (findMobilephoneNumberMatcher.find()) {				
				vcard.workMobilePhoneNumber = findMobilephoneNumberMatcher.group(1).replaceAll("[\\s-]", "").replaceAll("^0", "+46");
				System.out.println( "mobile:"+vcard.workMobilePhoneNumber );
			}
			findEMailMatcher = findEMailPattern.matcher(line);
			if (findEMailMatcher.find()) { 
				vcard.workEmail = findEMailMatcher.group(1);
				System.out.println( "email:"+vcard.workEmail );
			}
			findStreetMatcher = findStreetPattern.matcher(line);
			if (findStreetMatcher.find()) { 
				vcard.companyAddress += findStreetMatcher.group(1)+";";
				System.out.println( "address:"+vcard.companyAddress );
			}
			findZipMatcher = findZipPattern.matcher(line);
			if (findZipMatcher.find()) { 
				vcard.companyAddress += findZipMatcher.group(1)+";";
				System.out.println( "address:"+vcard.companyAddress );
			}
			findCityMatcher = findCityPattern.matcher(line);
			if (findCityMatcher.find()) { 
				vcard.companyAddress += findCityMatcher.group(1)+";";
				System.out.println( "address:"+vcard.companyAddress );
			}
			findCountryMatcher = findCountryPattern.matcher(line);
			if (findCountryMatcher.find()) { 
				vcard.companyAddress += findCountryMatcher.group(1)+";";
				System.out.println( "address:"+vcard.companyAddress );
			}
			
		}		 			
		return vcard;
	}
	
	public void testVCardParsing() {
		sslpost vcardCreator = new sslpost();
		String html = "<div class=\"txtheader\">Björn 2 Eriksson uppgifter</div>\n";
		html += "<td class=\"maintxt\">Mobilnr&nbsp;&nbsp;</td><td class=\"maintxt\">0768-640324</td>\n";
		html += "<td class=\"maintxt\">Mobilnr&nbsp;&nbsp;</td><td class=\"maintxt\">+45768 - 64 03 24</td>\n";
		html += "class=\"maintxt\">E-post&nbsp;&nbsp;</td><td class=\"maintxt\"><a href=\"mailto:bjorn.eriksson@softhouse.se\" style=\"font-weight:normal\">bjorn.eriksson@softhouse.se</a></td>\n";
		html += " <td class=\"maintxt\">Mobilnr&nbsp;&nbsp;</td><td class=\"maintxt\">0706-643916</td></tr><tr><td class=\"maintxt\">Faxnr&nbsp;&nbsp;</td><td class=\"maintxt\">040-664 39 19</td></tr><tr><td valign=\"top\" class=\"maintxt\">Tips</td><td class=\"maintxt\">Koppla</td></tr>";
		html += "<tr><td class=\"maintxt\">Besöksadress&nbsp;&nbsp;</td><td class=\"maintxt\">Ölandsgatan 42</td></tr><tr><td class=\"maintxt\">Postadress&nbsp;&nbsp;</td><td class=\"maintxt\">Ölandsgatan 42</td></tr><tr><td class=\"maintxt\">Postnr&nbsp;&nbsp;</td><td class=\"maintxt\">116 63</td></tr><tr><td class=\"maintxt\">Postort&nbsp;&nbsp;</td><td class=\"maintxt\">STOCKHOLM</td></tr><tr><td class=\"maintxt\">Land&nbsp;&nbsp;</td><td class=\"maintxt\">Sverige</td></tr><tr><td class=\"maintxt\">E-post&nbsp;&nbsp;</td><td class=\"maintxt\">";
		VCard vcard = vcardCreator.parseHTMLForVCardInformation(html);
		System.out.println( "vcard "+vcard.fullName );
		
		html = ";<a href=\"_sok_internlista.whtml?session=1a9e80b5d097d89ad052d4e459bb7608&sc=101&st=76&pkt=%02SEARCH%04000%3D7855701710537787374%04048%3Dj%04060%3D2%03\">4</a>";
		System.out.println( "pkt "+vcardCreator.getPktString(html) );
	}
	
	public byte[] getImage(String _URLString)  {
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
	
	
	public static void main(String[] args) {
		sslpost vcardCreator = new sslpost();
		 // vcardCreator.testVCardParsing();
		 // if( true ) return;
		
		String urlString = "https://tss.telenor.se/engine.wsc";
		String urlParameters="";
		try {
			urlParameters =
				"p_ank=" + URLEncoder.encode("0768832453", "UTF-8") +        
				"&p_pswd=" + URLEncoder.encode("VNaxL9IY", "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//
		// Login
		//
		System.out.println( "Login in..." );
		String html = vcardCreator.loginWithSSL( urlString, urlParameters );
		String sessionId = vcardCreator.getSessionID(html);		
		System.out.println( "Found session-id = "+sessionId );
		
		//
		// Retrieve total number of employees
		//
		System.out.println( "Retrieving total number of records..." );
		urlString = "https://tss.telenor.se/_sok_internlista.whtml?session="+sessionId;
		html = vcardCreator.readPage( urlString );
		int totalNumberOfEmplyees = vcardCreator.getTotalNumberOfEmployees(html);
		System.out.println( "totally "+totalNumberOfEmplyees+" records found" );
		String pktString = vcardCreator.getPktString(html);
		System.out.println( "Found pkt-string = "+pktString );
		
		//
		// Go through each page and retrieve the IDs
		//
		System.out.println( "Retrieving the IDs..." );
		LinkedList<String> ids = new LinkedList<String>();
		int pageNumber = 0;
		int index = (pageNumber*25)+1;
		while( index < totalNumberOfEmplyees ) {
			System.out.println( "Page "+pageNumber+"..." );
			urlString = "https://tss.telenor.se/_sok_internlista.whtml?session="+sessionId+"&sc="+totalNumberOfEmplyees+"&st="+index+"&pkt="+pktString;
			System.out.println( urlString );
			html = vcardCreator.readPage( urlString );
			vcardCreator.addToListOfIDs(html, ids);
			pageNumber++;
			index = (pageNumber*25)+1;
		}
		
		// if( true ) return;
		
		//
		// Retrieve each employee/record
		//
		System.out.println( "Retrieve each employee/record..." );
		LinkedList<VCard>  vcards = new LinkedList<VCard>();
		Iterator<String> employeeItr = ids.iterator();
		String userId = "";
		while( employeeItr.hasNext() ) {					
			userId = employeeItr.next();
			System.out.println( "Retrieve "+userId+"..." );
			urlString = "https://tss.telenor.se/v_sok_card.whtml?session="+sessionId+"&search=N&menu=1&s_id="+userId+"&use_more_info=1";
			html = vcardCreator.readPage( urlString );
			VCard vcard = vcardCreator.parseHTMLForVCardInformation(html);
			// Get image as well
			String base64EncodedImage = "";
            String photoURL = "https://tss.telenor.se/engine_retrieve.wsc?session="+sessionId+"&id=74&default=img/empty_photo.gif&s_id="+userId;
            byte bytes[] = vcardCreator.getImage( photoURL );
         	base64EncodedImage = Base64.encodeBytes( bytes );
	        
	        String lines[] = base64EncodedImage.split("\\n");
	        base64EncodedImage="";
	        for( int i=0;i<lines.length;i++) {
	        	  if( lines[i].startsWith(" ") ) base64EncodedImage=base64EncodedImage+lines[i]+"\n";
	        	  else base64EncodedImage=base64EncodedImage+" "+lines[i]+"\n";
	        }
	      	vcard.photoBase64Encoded = base64EncodedImage;
	      	vcard.uid = userId;
			vcards.add( vcard );
		}
		
		
		System.out.println( "Writing vcard file to disk..." );
		//
		//
		//
		
		Iterator<VCard> vcardItr = vcards.iterator();
		VCard vcard = null;
		try {
	    		String vcardString = "";
				FileWriter fw = new FileWriter( "softhouse.vcf" );							
		    	while( vcardItr.hasNext() ) {
		    		vcard = vcardItr.next();
		    		System.out.println( "name:"+vcard.fullName );
		    		if( vcard.fullName.trim().length() <= 0 || vcard.workMobilePhoneNumber.trim().length() <= 0 ) {
		    			continue;
		    		}
		    		vcard.workTitle = "Consultant";
		    		vcard.companyName = "Softhouse";		    		
		    		vcard.webaddress = "www.softhouse.se";
		    		vcardString = vcard.createVCard();
		    		fw.write( vcardString  );
		    	}
		    	fw.close();
		} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}								
		System.out.println( "Done..." );
	}
	

	public void readList(String urlString ) {
		try {
			
		} catch (Exception e) {
			System.out.println("Something bad just happened.");
			System.out.println(e);
			e.printStackTrace();
		}
	}
}
