package gov.lbl.glamm.server.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;

import javax.imageio.ImageIO;

import sun.misc.BASE64Encoder;



public abstract class GlammUtils {
	
	private GlammUtils() {}
	
	//********************************************************************************
	
	public static String genConstrainedImageLink(final String imgUrlString, final int maxDim) {

		String html = "";
		HttpURLConnection connection = null;

		try {
			URL imgUrl = new URL(imgUrlString);
			String userInfo = imgUrl.getUserInfo();
			BufferedImage img = null;

			if(userInfo == null || userInfo.isEmpty())
				img = ImageIO.read(imgUrl);
			else {
				// we have userInfo, so we have to explicitly request basic authentication
				connection = (HttpURLConnection) imgUrl.openConnection();
				connection.setRequestMethod("GET");
				
				BASE64Encoder enc = new sun.misc.BASE64Encoder();
				String encodedAuthorization = enc.encode( userInfo.getBytes() );
				connection.setRequestProperty("Authorization", "Basic "+ encodedAuthorization);
				
				InputStream istream = connection.getInputStream();
				img = ImageIO.read(istream);
				istream.close();
			}

			if(img != null) {
				
				int imgWidth 	= img.getWidth();
				int imgHeight 	= img.getHeight();
				int width 		= imgWidth;
				int height 		= imgHeight;


				if(imgWidth > maxDim || imgHeight > maxDim) {
					if(imgWidth < imgHeight) {
						// set height to maxDim, scale width
						height = maxDim;
						width = (int) (height * (float) imgWidth / (float) imgHeight);
					}
					else {
						// set width to maxDim, scale height
						width = maxDim;
						height = (int) (width * (float) imgHeight / (float) imgWidth);
					}
				}

				html = "<br><img width=" + width + " height=" + height + " src=\"" + imgUrlString + "\"/>";
			}
		} catch(IOException e) {
			html = "";  // fail silently - don't return a link;
		} finally {
			if(connection != null)
				connection.disconnect();
		}

		return html;
	}

	//********************************************************************************

	public static String joinArray ( Object[] a ) {
		return joinArray( a, ",", true );
	}
	
	//********************************************************************************

	public static String joinArray ( Object[] a, String separator, boolean shouldQuote ) {
		
		String	result 	= "";
		String	quote 	= shouldQuote ? "\"" : "";
		
		if ( a != null && a.length > 0 ) {
			for ( int i = 0; i < a.length - 1; i++ ) {
				result += quote + a[i].toString() + quote + separator;
			}

			result += quote + a[a.length - 1].toString() + quote;
		}
		
		
		return result;
	}
		
	//********************************************************************************
	
	public static String joinCollection(Collection<String> a) {
		return joinCollection(a, ",", true);
	}
	
	//********************************************************************************
	
	public static String joinCollection(Collection<String> a, String separator, boolean shouldQuote) {
		String result = "";
		String quote = shouldQuote ? "\"" : "";
		
		if(a != null && a.size() > 0) {
			int i = 0;
			for(Object ai : a) {
				if(i < a.size() - 1) 
					result += quote + ai + quote + separator;
				else
					result += quote + ai + quote;
				i++;
			}
		}
	
		return result;
	}
	
	//********************************************************************************


}