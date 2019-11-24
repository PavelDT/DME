package io.github.paveldt;

import java.io.*;
import java.net.*;
import java.util.UUID;

public class C_mutex extends Thread {

	C_buffer buffer;
	int port;
	private String token = UUID.randomUUID().toString();

	// ip address and port number of the node requesting the token.
	// They will be fetched from the buffer
	String n_host;
	int n_port;

	public C_mutex (C_buffer buffer, int port) {

		this.buffer = buffer;
		this.port = port;
	}

	public void go () {

		try{
			//  >>>  Listening from the server socket on port 7001
			// from where the TOKEN will be later on returned.
			// This place the server creation outside he while loop.
			ServerSocket returnSocket = new ServerSocket(port);

			while (true) {

				// >>> Print some info on the current buffer content for debuggin purposes.
				// >>> please look at the available methods in C_buffer
				// todo -- potentially comment out - spamms too much
				// System.out.println("C:mutex   Buffer size is " + buffer.size());

				// if the buffer is not empty
				if (buffer.size() > 0) {
					// >>>   Getting the first (FIFO) node that is waiting for a TOKEN form the buffer
					//       Type conversions may be needed.
					NodeMetadata nm = buffer.get();

					// >>>  **** Granting the token
					//
					try {
						// create a new socket to connect to the noe that requested a token
						// using a new socket avoids blocking the returnSocket
						System.out.println("Sending token to: " + nm);
						Socket nodeSocket = new Socket(nm.ip, nm.port);
						PrintWriter pw = new PrintWriter(nodeSocket.getOutputStream(), true);
						// send the token represented by a unique identifier
						pw.println(token);
						// close writer
						pw.close();
					} catch (IOException e) {
						System.out.println(e);
						System.out.println("CRASH Mutex connecting to the node for granting the TOKEN" + e);
						e.printStackTrace();
					}


					//  >>>  **** Getting the token back
					try{
						// todo -- fix the buffer, it is created every single loop.
						while (true) {
							Socket serverSocket = returnSocket.accept();
							System.out.println("Listening for token returns on port " + port);

							// create an input stream and buffer reader from the server socket
							// the server socket is listening for returned tokens
							// this code will read said socket until the token is returned, thus its blocking
							InputStream is = serverSocket.getInputStream();
							BufferedReader br = new BufferedReader(new InputStreamReader(is));

							// read the socket's stream
							String data = br.readLine();
							// todo -- check that the right thing is being closed br vs is
							br.close();
							// check the node sent the correct message and that it represents a token return
							if (data != null && data.equals("--token-returned")) {
								// once the token is returned break out of the loop
								System.out.println("token returned... ");
								break;
							} else {
								System.out.println(" no data received from node returning token " + nm);
							}
						}
					} catch (IOException e) {
						System.out.println(e);
						System.out.println("CRASH Mutex waiting for the TOKEN back" + e);
						e.printStackTrace();
					}

				}// endif
			}// endwhile
		} catch (Exception e) {
			System.out.print(e);
			e.printStackTrace();
		}
	}

	@Override
	public void run (){
		go();
	}


}
