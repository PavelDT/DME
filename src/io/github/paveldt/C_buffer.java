package io.github.paveldt;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author Pavel Todorov - pat00045 - 2634926
 * Buffer holding node token requests
 */
public class C_buffer {

	private Vector<NodeMetadata> highPrioBuffer;
	private Vector<NodeMetadata> lowPrioBuffer;
	private AtomicInteger priorityBalancer;

	public C_buffer (){
		lowPrioBuffer = new Vector<NodeMetadata>();
		highPrioBuffer = new Vector<NodeMetadata>();
		priorityBalancer = new AtomicInteger(0);
	}

    /**
	 * returns the combined size of both high and low priority buffers
	 */
	public int size(){

		return lowPrioBuffer.size() + highPrioBuffer.size();
	}

	//todo -- use this to create new items in the buffer instead of doing it manually in receiver.
	public synchronized void saveRequest (String[] r){

		String ip = r[0];
		int port = Integer.parseInt(r[1]);
		NodeMetadata nm = new NodeMetadata(ip, port);
	}


	public void show(){

		// cannot use a foreach loop - causes concurrent modification exception
		System.out.println("Both buffers ");
		System.out.println("LOW " + Arrays.asList(lowPrioBuffer.toArray()));
		System.out.println("HIGH " + Arrays.asList(highPrioBuffer.toArray()));
	}


	public void add(String priority, NodeMetadata nodeMetadata){
		if(priority.equals("LOW")) {
			lowPrioBuffer.add(nodeMetadata);
		} else if (priority.equals("HIGH")) {
			highPrioBuffer.add(nodeMetadata);
		} else {
			System.out.println("Unrecognised priority ");
		}
	}

	/**
	 * Fetches and removes first item from buffer
	 */
	synchronized public NodeMetadata get(){

		NodeMetadata nodeMetadata = null;
		// by default fetch from high priority buffer
		boolean useHighPriority = true;

		// check if a low priority request should be executed
		// todo -- this can be improved by accounting for the number of node
		//         rather than just using a hard coded 10
		if (priorityBalancer.get() % 10 == 0 && lowPrioBuffer.size() > 0) {
			useHighPriority = false;
		} else if (highPrioBuffer.size() == 0 && lowPrioBuffer.size() > 0) {
			useHighPriority = false;
		}

		if (useHighPriority) {
			nodeMetadata = highPrioBuffer.get(0);
			highPrioBuffer.remove(0);
			System.out.println("Processing from HIGH priority buffer ");
		} else {
			nodeMetadata = lowPrioBuffer.get(0);
			lowPrioBuffer.remove(0);
			System.out.println("Processing from LOW priority buffer ");
		}

		// every time a request is fetched increment the balancer
		// every 10th request should be a low priority request to prevent starvation
		priorityBalancer.incrementAndGet();

		if (nodeMetadata == null) {
			System.out.println("Something went wrong, attempted to process requests when both buffers were empty");
			System.out.println("Exiting system ");
			System.exit(-1);
		}

		return nodeMetadata;
	}



}
	
	
