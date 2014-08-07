package com.wagonlift.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.Certificate;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.wagonlift.constants.Constants;

public class HTMLUtils {

	Logger logger = Logger.getLogger(HTMLUtils.class);

	/**
	 * Call a given REST URL, based on http method provided
	 * 
	 * @param httpGetPost
	 *            predefined constant
	 * @param url
	 *            url to call
	 * @param params
	 *            params to include for POST calling
	 * @return response from server
	 */
	public String callUrl(String httpGetPost, String url,
			Map<String, String> params) {

		BufferedReader br = null;
		HttpClient httpClient = new HttpClient();
		try {

			HttpMethod httpMethod = null;

			if (StringUtils.equals(httpGetPost, Constants.HTTP_METHOD_GET)) {

				GetMethod getMethod = new GetMethod(url);

				httpMethod = getMethod;

			} else if (StringUtils.equals(httpGetPost,
					Constants.HTTP_METHOD_POST)) {

				// initialise post method
				PostMethod postMethod = new PostMethod(url);

				if (params != null) {
					// populate post method with parameters
					Set<String> keySet = params.keySet();
					for (String key : keySet) {
						postMethod.addParameter(key, params.get(key));
					}
				}

				httpMethod = postMethod;

			} else {
				return null;
			}

			// execute http method
			int statusCode = httpClient.executeMethod(httpMethod);
			// check http status returned
			if (statusCode != HttpStatus.SC_OK) {
				logger.error("Method failed: " + httpMethod.getStatusLine());
			}
			InputStream rstream = null;
			// Get the response body
			rstream = httpMethod.getResponseBodyAsStream();
			// Process the response
			br = new BufferedReader(new InputStreamReader(rstream));

			String respString = "";
			String line;
			while ((line = br.readLine()) != null) {
				respString = respString + line;
			}
			logger.debug("# => " + statusCode + " => " + url + " => "
					+ respString);

			br.close();

			return respString;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
    static String urlEncodeUTF8(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedOperationException(e);
        }
    }
    static String urlEncodeUTF8(Map<?,?> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<?,?> entry : map.entrySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(String.format("%s=%s",
                urlEncodeUTF8(entry.getKey().toString()),
                urlEncodeUTF8(entry.getValue().toString())
            ));
        }
        return sb.toString();       
    }
	
	public static String callHttpsURL(String httpsURL, Map<String, String> params, String httpMethod) {

		URL url;
		try {
			
			if(params!=null && StringUtils.equalsIgnoreCase(httpMethod, Constants.HTTP_METHOD_GET)){
				url = new URL(httpsURL+"?"+urlEncodeUTF8(params));
			}else{
				url = new URL(httpsURL);
			}
			
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
			System.out.println(httpMethod);
			if (StringUtils.equals(httpMethod, Constants.HTTP_METHOD_POST)){
				con.setRequestMethod("POST");
				con.setDoOutput(true);
				if(params!=null){
					DataOutputStream wr = new DataOutputStream(con.getOutputStream());
					wr.writeBytes(urlEncodeUTF8(params));
					wr.flush();
					wr.close();
				}
			}
			// dump all the content
			StringBuffer output = print_content(con);
			if(output!=null){
				return output.toString();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public static void print_https_cert(HttpsURLConnection con) {

		if (con != null) {

			try {

				System.out.println("Response Code : " + con.getResponseCode());
				System.out.println("Cipher Suite : " + con.getCipherSuite());
				System.out.println("\n");

				Certificate[] certs = con.getServerCertificates();
				for (Certificate cert : certs) {
					System.out.println("Cert Type : " + cert.getType());
					System.out.println("Cert Hash Code : " + cert.hashCode());
					System.out.println("Cert Public Key Algorithm : "
							+ cert.getPublicKey().getAlgorithm());
					System.out.println("Cert Public Key Format : "
							+ cert.getPublicKey().getFormat());
					System.out.println("\n");
				}

			} catch (SSLPeerUnverifiedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	private static StringBuffer print_content(HttpsURLConnection con) {
		StringBuffer output = null;
		if (con != null) {
			try {
				output = new StringBuffer();
				BufferedReader br = new BufferedReader(new InputStreamReader(
						con.getInputStream()));
				String line;
				while ((line = br.readLine()) != null) {
					output.append(line);
				}
				br.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println(output);
		return output;
	}
		
}