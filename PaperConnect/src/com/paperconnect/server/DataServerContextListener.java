package com.paperconnect.server;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 
 * Class for listening for the creation and destruction of the Servlet
 *
 */
public class DataServerContextListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		//Initialize the DataServer during the initialization of the Servlet
		DataServer.init();
	}

}
