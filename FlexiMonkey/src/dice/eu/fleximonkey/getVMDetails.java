package dice.eu.fleximonkey;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import com.extl.jade.user.Condition;
import com.extl.jade.user.FilterCondition;
import com.extl.jade.user.ListResult;
import com.extl.jade.user.QueryLimit;
import com.extl.jade.user.ResourceType;
import com.extl.jade.user.SearchFilter;
import com.extl.jade.user.Server;
import com.extl.jade.user.UserAPI;
import com.extl.jade.user.UserService;

public class getVMDetails {
     
    public static void main(String[] args) throws MalformedURLException {

    	System.setProperty("jsse.enableSNIExtension", "false");
    	 // Create a trust manager that does not validate certificate chains
	    TrustManager[] trustAllCerts = new TrustManager[] { 
	      new X509TrustManager() {
	        public X509Certificate[] getAcceptedIssuers() { 
	          return new X509Certificate[0]; 
	        }
	        public void checkClientTrusted(X509Certificate[] certs, String authType) {}
	        public void checkServerTrusted(X509Certificate[] certs, String authType) {}
	    }};

	    // Ignore differences between given hostname and certificate hostname
	    HostnameVerifier hv = new HostnameVerifier() {
	      public boolean verify(String hostname, SSLSession session) { return true; }
	    };

	    // Install the all-trusting trust manager
	    try {
	      SSLContext sc = SSLContext.getInstance("SSL");
	      sc.init(null, trustAllCerts, new SecureRandom());
	      HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	      HttpsURLConnection.setDefaultHostnameVerifier(hv);
	    } catch (Exception e) {}
	    
	    String userEmailAddress = "dwhigham@flexiant.com";
    	String customerUUID ="";
    	String password = "";
    	String UserURL = "https://cp.sd1.flexiant.net/soap/user/current/?wsdl";
    	String serverUUID="45904f7e-4be9-3ba0-a86c-8a8ddf55f7db";
    	
        UserService service;
         
        // Get the service WSDL from the server

        URL url = null;
        url = new URL(com.extl.jade.user.UserAPI.class.getResource("."), UserURL);
        // Get the UserAPI
        UserAPI api = new UserAPI(url,
                new QName("http://extility.flexiant.net", "UserAPI"));
                 
        // and set the service port on the service
        service = api.getUserServicePort();
         
        // Get the binding provider
        BindingProvider portBP = (BindingProvider) service;
         
        // and set the service endpoint
        portBP.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
        		UserURL);
         
        // and the caller's authentication details and password
        portBP.getRequestContext().put(BindingProvider.USERNAME_PROPERTY,
                userEmailAddress + "/" + customerUUID);
        portBP.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY,
                password);
     
        try {
             
            // List all servers in the running and starting states
             
            // Create an FQL filter and a filter condition
            SearchFilter sf = new SearchFilter();
            FilterCondition fc = new FilterCondition();
  
            // set the condition type
            fc.setCondition(Condition.IS_EQUAL_TO);
             
            fc.setField("resourceUUID");
            
            // and a list of values
            fc.getValue().add(serverUUID);
             


            
            // Add the filter condition to the query
            sf.getFilterConditions().add(fc);
       
            // Set a limit to the number of results
            QueryLimit lim = new QueryLimit();
            lim.setMaxRecords(1000);
            lim.setLoadChildren(true);
          
            ListResult result = service.listResources(sf, lim, ResourceType.SERVER);
            // Call the service to execute the query
         
    
            // Iterate through the results           
	           for(Object o : result.getList()) {
            	  Server s = ((Server)o);           
                //Place results in string values

	         	String cpus = String.valueOf(s.getCpu());
	         	String ram = String.valueOf(s.getRam());
	         	System.out.println("Number of CPUs: " +cpus +" " + "Amount of RAM: " + ram);
	           }

        } catch (Exception e) {
            
            e.printStackTrace();
        }
    }
}