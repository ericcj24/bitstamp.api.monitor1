import java.util.ArrayList;
import java.util.Vector;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TransactionAPI{
	//{"date": "1395551812", "tid": 4151239, "price": "564.23", "amount": "0.05000000"}
	
    public static Vector<Vector<Double>> HttpGetTransactions() throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpGet = new HttpGet("https://www.bitstamp.net/api/transactions/");
            CloseableHttpResponse response1 = httpclient.execute(httpGet);

            try {
                System.out.println(response1.getStatusLine());
                HttpEntity entity1 = response1.getEntity();
                System.out.println(entity1.getContentType()+" Transaction API");
               
                ObjectMapper mapper = new ObjectMapper();
                ArrayList<Transactions> array = mapper.readValue(entity1.getContent(), new TypeReference<ArrayList<Transactions>>() { });

                /*for(int i=0; i<array.size(); i++){
                	long dateLong = Long.parseLong(array.get(i).date)*1000;
                	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                	String formattedDate =  sdf.format(dateLong);
                	System.out.println("date: "+formattedDate+ " tid " + array.get(i).tid + " price "+array.get(i).price+" amount "+array.get(i).amount);
                }*/
                
                
                Vector<Double> x = new Vector<Double>();
                Vector<Double> y = new Vector<Double>();
                for(int i=0; i<array.size(); i++){
                	x.add(Double.parseDouble(array.get(i).getDate()));
                	y.add(Double.parseDouble(array.get(i).getPrice()));
                }
                
                Vector<Vector<Double>> v = new Vector<Vector<Double>>();
                v.add(x);
                v.add(y);
                
                EntityUtils.consume(entity1);
                
                return v;
                
            } finally {
                response1.close();
            }

        } finally {
            httpclient.close();

        }
    }

}