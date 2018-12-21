package com.roja.splunk;

import java.util.HashMap;
import java.util.Map;

import com.splunk.Args;
import com.splunk.HttpService;
import com.splunk.Receiver;
import com.splunk.SSLSecurityProtocol;
import com.splunk.Service;

public class DemoSplunk {

	public static void main(String[] args) {

		HttpService.setSslSecurityProtocol(SSLSecurityProtocol.TLSv1_2);

		Map<String, Object> connectionArgs = new HashMap<String, Object>();

		connectionArgs.put("host", "localhost");
		connectionArgs.put("username", "palakolanu");
		connectionArgs.put("password", "palakolanu");
		connectionArgs.put("port", 8089);
		connectionArgs.put("scheme", "https");

		Service splunkService = Service.connect(connectionArgs);

		// Set Source information
		Args logArgs = new Args();
		logArgs.put("sourcetype", "fromroja");

		// Set Index
		Receiver receiver = splunkService.getReceiver();
		receiver.log("main", logArgs, "HelloSplunk Event I am from Eclipse Log");

	}

}
