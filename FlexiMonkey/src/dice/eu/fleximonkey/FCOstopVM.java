package dice.eu.fleximonkey;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import com.extl.jade.user.Job;
import com.extl.jade.user.ResourceMetadata;
import com.extl.jade.user.ServerStatus;
import com.extl.jade.user.UserAPI;
import com.extl.jade.user.UserService;

public class FCOstopVM {
	private static final Logger log = Logger.getLogger( FCOstopVM.class.getName() );

	@SuppressWarnings("deprecation")
	public void stopvm(String vmuuid, String cloudusername,
			String cloudpassword, String cloudapiurl, String cloudUUID) {

		UserService service;
		Job stopServer = null;

		System.setProperty("jsse.enableSNIExtension", "false");
		URL url = null;
		try {
			url = new URL(com.extl.jade.user.UserAPI.class.getResource("."),
					cloudapiurl);
		} catch (MalformedURLException e1) {
			 log.log( Level.SEVERE, "Unable to get FCO WSDL");
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
		// Get date time for job creation
		try {
			GregorianCalendar gregorianCalendar = new GregorianCalendar();
			DatatypeFactory datatypeFactory = null;
			datatypeFactory = DatatypeFactory.newInstance();
			XMLGregorianCalendar now = datatypeFactory
					.newXMLGregorianCalendar(gregorianCalendar);

			Date date = new Date();
			datatypeFactory = null;
			datatypeFactory = DatatypeFactory.newInstance();

			now = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
			@SuppressWarnings("deprecation")
			int mins = date.getMinutes();
			int sec = date.getSeconds();
			int hours = date.getHours();
			sec += 10;
			if (sec >= 60) {
				sec -= 60;
				mins += 1;
			}
			if (mins == 60) {
				mins = 59;
			}

			now.setTime(hours, mins, sec);

			// Send stop command
			// Can also be scheduled for time in future.
			log.log( Level.INFO, "Sending stop command for:  " + vmuuid);
			stopServer = service.changeServerStatus(vmuuid,
					ServerStatus.STOPPED, true, new ResourceMetadata(), now);
			// waits till job completes or fails
			service.waitForJob(stopServer.getResourceUUID(), false);
			log.log( Level.INFO, "Server stopped:  " + vmuuid);

		} catch (Exception e) {
			log.log( Level.SEVERE, "Unable to send stop job or server is already stopped");

		}
	}

}