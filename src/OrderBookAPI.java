import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import javax.swing.JFrame;

import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import org.math.plot.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OrderBookAPI{
	//{"timestamp": "1395686143", "bids": [["572.33", "0.17000000"], ["572.32", "0.17100000"],...]}
	
	static class OrderBook {
	    private String timestamp;
	    private ArrayList<ArrayList<String>> bids;
	    private ArrayList<ArrayList<String>> asks;
	    public OrderBook() {}
	    
	    public String getTimestamp(){
	    	return timestamp;
	    }
	    public ArrayList<ArrayList<String>> getBids(){
	    	return bids;
	    }
	    public ArrayList<ArrayList<String>> getAsks(){
	    	return asks;
	    }
	}
	
	
	
    public static Vector<Vector<Double>> HttpGetOrderBook() throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpGet = new HttpGet("https://www.bitstamp.net/api/order_book/");
            CloseableHttpResponse response1 = httpclient.execute(httpGet);
            
            // The underlying HTTP connection is still held by the response object
            // to allow the response content to be streamed directly from the network socket.
            // In order to ensure correct deallocation of system resources
            // the user MUST either fully consume the response content  or abort request
            // execution by calling CloseableHttpResponse#close().

            try {
                System.out.println(response1.getStatusLine());
                HttpEntity entity1 = response1.getEntity();
                
                System.out.println(entity1.getContentType()+" OrderBook API");
               
                ObjectMapper mapper = new ObjectMapper();
                OrderBook ob = mapper.readValue(entity1.getContent(), OrderBook.class);
                long dateLong = Long.parseLong(ob.getTimestamp())*1000;
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            	String formattedDate =  sdf.format(dateLong);
                System.out.println(formattedDate);
                
                
                Vector<Double> x = new Vector<Double>();
                Vector<Double> y = new Vector<Double>();
                System.out.println("bids size is: "+ob.getBids().size());
                for(int i=0; i<ob.getBids().size(); i++){
                	//System.out.println(" price "+ob.getBids().get(i).get(0)+" amount "+ob.getBids().get(i).get(1));
                	x.add(Double.parseDouble(ob.getBids().get(i).get(0)));
                	y.add(Double.parseDouble(ob.getBids().get(i).get(0)) * Double.parseDouble(ob.getBids().get(i).get(1)));
                }
                Vector<Vector<Double>> v = new Vector<Vector<Double>>();
                v.add(x);
                v.add(y);
                /*System.out.println("bids are: " + " size is: "+ob.getAsks().size());
                for(int i=0; i<ob.getAsks().size(); i++){
                	System.out.println(" price "+ob.getAsks().get(i).get(0)+" amount "+ob.getAsks().get(i).get(1));
                }/*
                int n = array.size();
                double[] x = new double[n];
                double[] y = new double[n];
                for(int i=0; i<array.size(); i++){
                	x[i] = Double.parseDouble(array.get(i).date);
                	y[i] = Double.parseDouble(array.get(i).price);
                }
                Plot2DPanel plot = new Plot2DPanel();
                plot.addScatterPlot("my plot", x, y);
                JFrame frame = new JFrame("a plot panel");
                frame.setSize(800, 600);
                frame.setContentPane(plot);
                frame.setVisible(true);
                */
                // do something useful with the response body
                // and ensure it is fully consumed
                EntityUtils.consume(entity1);
                return v;
            } finally {
                response1.close();
            }

            /*HttpPost httpPost = new HttpPost("https://www.bitstamp.net/api/ticker/");
            List <NameValuePair> nvps = new ArrayList <NameValuePair>();
            nvps.add(new BasicNameValuePair("username", "vip"));
            nvps.add(new BasicNameValuePair("password", "secret"));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            CloseableHttpResponse response2 = httpclient.execute(httpPost);

            try {
                System.out.println(response2.getStatusLine());
                HttpEntity entity2 = response2.getEntity();
                // do something useful with the response body
                // and ensure it is fully consumed
                EntityUtils.consume(entity2);
            } finally {
                response2.close();
            }*/
        } finally {
            httpclient.close();
        }
    }

}