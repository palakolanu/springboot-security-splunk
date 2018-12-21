package com.roja.splunk;
/*
 * Copyright 2013-2014 Splunk, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"): you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

import com.splunk.SSLSecurityProtocol;
import com.splunk.Service;
import com.splunk.ServiceArgs;

public class SplunkUtil {

	private static final ServiceArgs serviceArgs = new ServiceArgs();
	private static Service service;

	/**
	 * read splunk host info from .splunkrc file
	 */
	private static void getSplunkHostInfo() throws IOException {

		if (serviceArgs.isEmpty()) {
			// set default value
			serviceArgs.setUsername("palakolanu");
			serviceArgs.setPassword("palakolanu");
			serviceArgs.setHost("localhost");
			serviceArgs.setPort(8089);
			serviceArgs.setScheme("https");

			// update serviceArgs with customer splunk host info
			String splunkhostfile = System.getProperty("user.home") + File.separator + ".splunkrc";
			List<String> lines = Files.readAllLines(new File(splunkhostfile).toPath(), Charset.defaultCharset());
			for (String line : lines) {
				if (line.toLowerCase().contains("host=")) {
					serviceArgs.setHost(line.split("=")[1]);
				}
				if (line.toLowerCase().contains("admin=")) {
					serviceArgs.setUsername(line.split("=")[1]);
				}
				if (line.toLowerCase().contains("password=")) {
					serviceArgs.setPassword(line.split("=")[1]);
				}
				if (line.toLowerCase().contains("scheme=")) {
					serviceArgs.setScheme(line.split("=")[1]);
				}
				if (line.toLowerCase().contains("port=")) {
					serviceArgs.setPort(Integer.parseInt(line.split("=")[1]));
				}
			}
		}
		// Use TLSv1 intead of SSLv3
		serviceArgs.setSSLSecurityProtocol(SSLSecurityProtocol.TLSv1_2);
	}

	public static void resetConnection() {
		service = null;
	}

	public static Service connectToSplunk() throws IOException {

		int retry = 0;
		while (true) {
			try {

				if (service == null) {
					getSplunkHostInfo();

					service = Service.connect(serviceArgs);
					service.login();
				}

				return service;
			} catch (IOException ex) {
				retry++;
				if (retry > 5)
					throw ex;
			}
		}
	}

}
