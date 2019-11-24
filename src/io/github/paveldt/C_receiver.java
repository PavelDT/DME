package io.github.paveldt;

import java.io.IOException;
import java.net.*;

public class C_receiver extends Thread {

	// Buffer holding node metadata token processing requests
	private C_buffer buffer;
	// receiver listening port
	private int port;

	// receiver server and client sockets
	private ServerSocket receiverServer;
	private Socket receiverSocket;

	// node request processing thread
	private C_Connection_r connection;


	public C_receiver (C_buffer buffer, int port){
		this.buffer = buffer;
		this.port = port;
	}

	@Override
	public void run () {

		// >>> create the socket the server will listen to
		try {
			// creates server socket that will lsiten for node metadata
			receiverServer = new ServerSocket(port);
			while (true) {
				// >>> get a new connection
				receiverSocket = receiverServer.accept();
				connection = new C_Connection_r(receiverSocket, buffer);

				System.out.println ("C:receiver    Coordinator has received a request ...") ;

				// >>> create a separate thread to service the request, a C_Connection_r thread
				connection.start();
			}


		} catch (IOException e) {
			System.out.println("Exception when creating a connection "+e);
			e.printStackTrace();
		}
	}//end run

}
	

