package io.github.paveldt;

import java.util.*;

public class C_buffer {

	private Vector<NodeMetadata> highPrioBuffer;
	private Vector<NodeMetadata> lowPrioBuffer;

	public C_buffer (){
		lowPrioBuffer = new Vector<NodeMetadata>();
		highPrioBuffer = new Vector<NodeMetadata>();
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
		System.out.println(Arrays.asList(lowPrioBuffer.toArray()));
		System.out.println(Arrays.asList(highPrioBuffer.toArray()));
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

		if(useHighPriority && highPrioBuffer.size() > 0){
			nodeMetadata = highPrioBuffer.get(0);
			highPrioBuffer.remove(0);
		} else if (lowPrioBuffer.size() > 0){
			nodeMetadata = lowPrioBuffer.get(0);
			lowPrioBuffer.remove(0);
		}

		if (nodeMetadata == null) {
			System.out.println("Something went wrong, attempted to process requests when both queues were empty");
			System.out.println("Exiting system ");
			System.exit(-1);
		}

		return nodeMetadata;
	}



}
	
	
