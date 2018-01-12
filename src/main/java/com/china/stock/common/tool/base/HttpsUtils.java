package com.china.stock.common.tool.base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;

public class HttpsUtils {
	
	private static Logger log = Logger.getLogger(HttpsUtils.class);

	public static String doPost(String url, String ctype, byte[] content) throws Exception {  
        HttpsURLConnection conn = null;  
        OutputStream out = null;  
        String rsp = null;  
        try {  
            try{  
                SSLContext ctx = SSLContext.getInstance("TLS");  
                ctx.init(new KeyManager[0], new TrustManager[] {new DefaultTrustManager()}, new SecureRandom());  
                SSLContext.setDefault(ctx);  
  
                conn = getConnection(new URL(url), "POST", ctype);   
                conn.setHostnameVerifier(new HostnameVerifier() {  
                    @Override  
                    public boolean verify(String hostname, SSLSession session) {  
                        return true;  
                    }
                });  
                conn.setConnectTimeout(5000);  
                //conn.setReadTimeout(readTimeout);  
            }catch(Exception e){  
                log.error("GET_CONNECTOIN_ERROR, URL = " + url, e);  
                throw e;  
            }  
            try{  
                out = conn.getOutputStream();  
                out.write(content);  
                rsp = getResponseAsString(conn);  
            }catch(IOException e){  
                log.error("REQUEST_RESPONSE_ERROR, URL = " + url, e);  
                throw e;  
            }  
              
        }finally {  
            if (out != null) {  
                out.close();  
            }  
            if (conn != null) {  
                conn.disconnect();  
            }  
        }           
        return rsp;  
    }
	
	private static class DefaultTrustManager implements X509TrustManager {  
		  
        @Override  
        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}  
  
        @Override  
        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}  
  
        @Override  
        public X509Certificate[] getAcceptedIssuers() {  
            return null;  
        }  
    }
	
	private static HttpsURLConnection getConnection(URL url, String method, String ctype)  
            throws IOException {  
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();  
        conn.setRequestMethod(method);  
        conn.setDoInput(true);  
        conn.setDoOutput(true);  
        conn.setRequestProperty("Accept", "text/xml,text/javascript,text/html");  
        conn.setRequestProperty("User-Agent", "stargate");  
        conn.setRequestProperty("Content-Type", ctype);  
        return conn;  
	}
	
	protected static String getResponseAsString(HttpURLConnection conn) throws IOException {  
       String charset = "utf-8";
       InputStream es = conn.getErrorStream();  
       if (es == null) {  
           return getStreamAsString(conn.getInputStream(), charset);  
       } else {  
           String msg = getStreamAsString(es, charset);  
           if (StrUtils.isEmpty(msg)) {  
               throw new IOException(conn.getResponseCode() + ":" + conn.getResponseMessage());  
           } else {  
               throw new IOException(msg);  
           }  
       }  
   }
	private static String getStreamAsString(InputStream stream, String charset) throws IOException {		
       try {  
           BufferedReader reader = new BufferedReader(new InputStreamReader(stream, charset));  
           StringWriter writer = new StringWriter();  

           char[] chars = new char[256];  
           int count = 0;  
           while ((count = reader.read(chars)) > 0) {  
               writer.write(chars, 0, count);  
           }  
 
           return writer.toString();  
       } finally {  
           if (stream != null) {  
               stream.close();  
           }  
       }  
   }
}
