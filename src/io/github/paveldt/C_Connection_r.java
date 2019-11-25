package io.github.paveldt;

import java.net.*;
import java.io.*;
import java.util.Arrays;


// Reacts to a node request.
// Receives and records the node request in the buffer.
//
public class C_Connection_r extends Thread {

	// class variables
	C_buffer buffer;
	Socket socket;
	InputStream in;
	BufferedReader bin;

	public C_Connection_r(Socket socket, C_buffer buffer) {
		this.socket = socket;
		this.buffer = buffer;
	}

	@Override
	public void run() {

		System.out.println("C:connection IN    dealing with request from socket "+ socket);
		try {

			// >>> read the request, i.e. node ip and port from the socket s
			// >>> save it in a request object and save the object in the buffer (see C_buffer's methods).

			in = socket.getInputStream();
			bin = new BufferedReader(new InputStreamReader(in));

			// data sent will look like "LOW---127.0.0.1---7100"
			// LOW (or HIGH) is the request priority
			// 127.0.0.1 is the ip of the node requesting the token and
			// 7100 is the port
			String data[] = bin.readLine().split("---");
			// the first part indicates the priority of the request
			String priority = data[0];
			// the second part of the data will be ip of the node requesting to process critical section while holding token
			String ip = data[1];
			// the third part of the data will be the port of the node requesting to process critical section while holding token
			int port = Integer.parseInt(data[2]);
			NodeMetadata nm = new NodeMetadata(ip, port);
			// write the request to the buffer
			buffer.add(priority, nm);


			socket.close();
			System.out.println("C:connection OUT    received and recorded request from " + priority + nm.ip + ":" + nm.port + "  (socket closed)");


		}
		catch (IOException e) {
			System.out.println(e);
			e.printStackTrace();
			System.exit(1);
		}

		buffer.show();
	} // end of run() method

} // end of class
