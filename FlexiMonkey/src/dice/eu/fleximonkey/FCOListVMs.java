package dice.eu.fleximonkey;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import com.extl.jade.user.Condition;
import com.extl.jade.user.FilterCondition;
import com.extl.jade.user.ListResult;
import com.extl.jade.user.QueryLimit;
import com.extl.jade.user.ResourceType;
import com.extl.jade.user.SearchFilter;
import com.extl.jade.user.Server;
import com.extl.jade.user.ServerStatus;
import com.extl.jade.user.UserAPI;
import com.extl.jade.user.UserService;

public class FCOListVMs {

	public void listvms(String cloudusername, String cloudpassword,
			String cloudapiurl, String cloudUUID) {
		
		LoggerWrapper loggerWrapper = null;
		try {
			loggerWrapper = LoggerWrapper.getInstance();
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//tag used to not shutdown server
		String resourceKeyName = "noshutdown";
		

		UserService service;

		System.setProperty("jsse.enableSNIExtension", "false");
		URL url = null;
		try {
			url = new URL(com.extl.jade.user.UserAPI.class.getResource("."),
					cloudapiurl);
		} catch (MalformedURLException e1) {
			LoggerWrapper.myLogger.severe("Unable to get FCO WSDL");
		}

		// Get the UserAPI
		UserAPI api = new UserAPI(url, new QName(
				"http://extility.flexiant.net", "UserAPI"));
		// and set the service port on the service
		service = api.getUserServicePort();

		// Get the binding provider
		BindingProvider portBP = (BindingProvider) service;

		// and set the service endpoint
		portBP.getRequestContext().put(
				BindingProvider.ENDPOINT_ADDRESS_PROPERTY, cloudapiurl);

		// and the caller's authentication details and password
		portBP.getRequestContext().put(BindingProvider.USERNAME_PROPERTY,
				cloudusername + "/" + cloudUUID);
		portBP.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY,
				cloudpassword);

		try {
			LoggerWrapper.myLogger.info("Attempting to list servers");

			// List all servers in the running and starting states

			// Create an FQL filter and a filter condition
			SearchFilter sf = new SearchFilter();
			FilterCondition fc = new FilterCondition();

			// set the condition type
			fc.setCondition(Condition.IS_EQUAL_TO);

			// the field to be matched
			fc.setField("status");

			// and a list of values
			fc.getValue().add(ServerStatus.RUNNING.name());

			// Add the filter condition to the query
			sf.getFilterConditions().add(fc);
			// Set a limit to the number of results
			QueryLimit lim = new QueryLimit();
			lim.setMaxRecords(1000);

			ListResult result = service.listResources(sf, lim,
					ResourceType.SERVER);
			// Call the service to execute the query
			List<String> list = new ArrayList<String>();
			Random randomGenerator;
			randomGenerator = new Random();
			for (Object o : result.getList()) {
				Server s = ((Server) o);
				String serverUUID = s.getResourceUUID().toString();
				//check if no_shutdowntag is added
				FCOCheckKey checkkey = new FCOCheckKey();
				checkkey.listvmkeys(cloudusername, cloudpassword, cloudapiurl, cloudUUID, serverUUID);
				
				 ArrayList<String> checkedlist = new ArrayList<String>();
				 checkedlist = checkkey.getList();
				if(checkedlist.contains(resourceKeyName))
				{
					LoggerWrapper.myLogger.info("Key noshutdown found, not adding to list");
				}
					else
				{
				//If key not found then add to list to be considered for shutdown
	        	list.add(s.getResourceUUID());
			}
				LoggerWrapper.myLogger.info("Looking for VM to be stopped for customer: " + s.getCustomerUUID());
				LoggerWrapper.myLogger.info("Server "+ s.getResourceUUID() + " is in state " + s.getStatus());

			}
			// Error checking if no server is running
			int index = randomGenerator.nextInt(list.size());
			String vmuuid = list.get(index);
			LoggerWrapper.myLogger.info("Server random from list " + vmuuid);

			FCOstopVM stopvm = new FCOstopVM();
			stopvm.stopvm(vmuuid, cloudusername, cloudpassword, cloudapiurl,
					cloudUUID);

		} catch (Exception e) {
			LoggerWrapper.myLogger.severe("Unable to list servers " + e.toString());

		}
	}
}