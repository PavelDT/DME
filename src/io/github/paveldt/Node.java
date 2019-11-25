package io.github.paveldt;

import io.github.paveldt.util.IPManager;

import java.net.*;
import java.io.*;
import java.util.*;

public class Node {

	private ServerSocket nodeTokenServer;
	private Socket nodeTokenSocket;


	private String coordinatorIP;
	private int coordinatorPort = 7000;
	private int coordinatorTokenReturnPort = 7001;

	private String nodeIP;
	private int nodePort;
	private String priority;



	public Node (String coordinatorIP, String nam, int port, int sec, String prio) {

		this.coordinatorIP = coordinatorIP;
		nodeIP = nam;
		nodePort = port;
		priority = prio;

		System.out.println("Node " + nodeIP + ":" + nodePort + " of DME is active ....");



		// NODE sends n_host and n_port  through a socket s to the coordinator

		// c_host:c_req_port
		// and immediately opens a server socket through which will receive
		// a TOKEN (actually just a synchronization).


		try{
			// create the server socket that is responsible for accepting token returns
			nodeTokenServer = new ServerSocket(nodePort);

			while (true) {

				// todo --
				// >>>  sleep a random number of seconds linked to the initialisation sec value

				// REQUEST TOKEN
				// >>>
				// **** Send to the coordinator a token request.
				// send your ip address and port number
				Socket nodeSocket = new Socket(coordinatorIP, coordinatorPort);
				// writer that will send node's ip and port for a token request
				PrintWriter pw = new PrintWriter(nodeSocket.getOutputStream(), true);
				// send the token
				pw.println(priority + "---" + nodeIP + "---" + nodePort);
				// close the writer
				pw.close();

				//WAIT FOR TOKEN FROM COORDINATOR
				// >>>
				// **** Wait for the token
				// this is just a synchronization
				// Print suitable messages

				// create the node's server socket
				while (true) {
					nodeTokenSocket = nodeTokenServer.accept();
					// create a reader to read the token
					BufferedReader br = new BufferedReader(new InputStreamReader(nodeTokenSocket.getInputStream()));
					// read a single line
					String data = br.readLine();
					// close the reader
					br.close();

					if (data != null) {
						System.out.println("Token received " + data);
						// break infinate loop once the token has been received
						break;
					} else {
						// todo -- this is for debugging purposes
						System.out.println("Nothing received ");
					}
				}



				//>>>
				// Sleep half a second, say
				// This is the critical section
				Thread.sleep(500);


				// >>>
				// **** Return the token
				// this is just establishing a synch connection to the coordinator's ip and return port.
				// Print suitable messages - also considering communication failures
				Socket returnSocket = new Socket(coordinatorIP, coordinatorTokenReturnPort);
				// create a writer to send the token back to the coordinator
				PrintWriter returnWriter = new PrintWriter(returnSocket.getOutputStream(), true);
				// send the token back
				// mutex is waiting for "--token-returned"
				returnWriter.println("--token-returned");
				// close the writer
				returnWriter.close();
				System.out.println("Token returned ");

			}

		} catch (IOException e) {
			System.out.println(e);
			e.printStackTrace();
			System.exit(1);

		} catch (InterruptedException ie) {
			System.out.println(ie);
			ie.printStackTrace();
			System.exit(1);
		}
	}



	public static void main (String args[]) {


		// port and millisec (average waiting time) are specific of a node
		if ((args.length < 2) || (args.length > 4)){
			System.out.println("Usage: Node [port number] [millisecs] [priority] [coordinatorIP]");
			System.out.println("eg1: 7100 100 LOW");
			System.out.println("eg2: 7100 100 LOW 127.0.0.1");
			System.exit(1);
		}

		// get the IP address and the port number of the node

		String nodeIP = IPManager.getLocalIP();
		int nodePort = Integer.parseInt(args[0]);
		// todo -- work out what this is for.
		int milis = Integer.parseInt(args[1]);

		String prio = args[2].toUpperCase() ;
		String coordinatorIP = "127.0.0.1";
		if (args.length == 4) {
			coordinatorIP = IPManager.decideCoordinatorIP(args[3]);
		}

		System.out.println ("node port is " + nodePort + " nodeIP is " + nodeIP + " coordinatorIP is " + coordinatorIP);

		Node n = new Node(coordinatorIP, nodeIP, nodePort, milis, prio);

	}


}

