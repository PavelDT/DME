package io.github.paveldt;

import java.net.*;

public class Coordinator {

	public static void main (String args[]){

		int mutexPort = 7001;
		int receiverPort = 7000;

		Coordinator c = new Coordinator ();

		try {
			InetAddress c_addr = InetAddress.getLocalHost();
			String c_name = c_addr.getHostName();
			System.out.println ("Coordinator address is "+c_addr);
			System.out.println ("Coordinator host name is "+c_name+"\n\n");
		}
		catch (Exception e) {
			System.err.println(e);
			System.err.println("Error in coordinator");
		}


		// allows defining port at launch time
		if (args.length == 1) {
			// no point in trycatching this - just allow the coordinator to crash, it has not really done anything yet
			mutexPort = Integer.parseInt(args[0]);
		}

		// Create and run a C_receiver and a C_mutex object sharing a C_buffer object
		// Create buffer that will be shared between mutex and receiver
		C_buffer buffer = new C_buffer();
		// create mutex and pass in buffer
		C_mutex mutex = new C_mutex(buffer, mutexPort);
		// create teh receiver and pass in same buffer
		C_receiver receiver = new C_receiver(buffer, receiverPort);

		// start receiver thread
		receiver.start();
		System.out.println("Receiver started ");
		// start mutex thread\
		mutex.start();
		System.out.println("Mutex started ");

	}

}
