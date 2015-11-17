package dice.eu.fleximonkey;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import com.extl.jade.user.Condition;
import com.extl.jade.user.FilterCondition;
import com.extl.jade.user.ListResult;
import com.extl.jade.user.QueryLimit;
import com.extl.jade.user.ResourceKey;
import com.extl.jade.user.ResourceType;
import com.extl.jade.user.SearchFilter;
import com.extl.jade.user.Server;
import com.extl.jade.user.UserAPI;
import com.extl.jade.user.UserService;

public class FCOCheckKey {
	public ArrayList<String> listString = new ArrayList<String>();

	public void listvmkeys(String cloudusername, String cloudpassword,
			String cloudapiurl, String cloudUUID, String serverUUID) {

		@SuppressWarnings("unused")
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

			// Create an FQL filter and a filter condition
			SearchFilter sf = new SearchFilter();
			FilterCondition fc = new FilterCondition();

			// set the condition type
			fc.setCondition(Condition.IS_EQUAL_TO);

			// the field to be matched
			fc.setField("resourceUUID");

			// and a list of values
			fc.getValue().add(serverUUID);

			// Add the filter condition to the query
			sf.getFilterConditions().add(fc);

			// Set a limit to the number of results
			QueryLimit lim = new QueryLimit();
			lim.setMaxRecords(1000);
			lim.setLoadChildren(true);

			ListResult result = service.listResources(sf, lim,
					ResourceType.SERVER);
			// Call the service to execute the query

			for (Object o : result.getList()) {
				Server s = ((Server) o);
				LoggerWrapper.myLogger.info("Listing VM keyys for:" + s.getResourceUUID());

				List<ResourceKey> resourcekey = s.getResourceKey();
				for (Object oR : resourcekey) {
					ResourceKey r = ((ResourceKey) oR);
					listString.add(r.getName().toString());
				}
				LoggerWrapper.myLogger.info( "Listing VM keyys for:" + listString);
			}
		} catch (Exception e) {
			LoggerWrapper.myLogger.severe("Unable to list keys error: " + e.toString());

		}

	}

	public ArrayList<String> getList() {
		return listString;
	}
}
