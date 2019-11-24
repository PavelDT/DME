package io.github.paveldt;

import java.util.*;

public class C_buffer {

	private Vector<NodeMetadata> buffer;


	public C_buffer (){
		buffer = new Vector<NodeMetadata>();
	}


	public int size(){

		return buffer.size();
	}

	//todo -- use this to create new items in the buffer instead of doing it manually in receiver.
	public synchronized void saveRequest (String[] r){

		String ip = r[0];
		int port = Integer.parseInt(r[1]);
		NodeMetadata nm = new NodeMetadata(ip, port);
	}


	public void show(){
		System.out.print("[");
		for (NodeMetadata nm : buffer) {
			System.out.print(nm.ip + ":" + nm.port + ", " );
		}
		System.out.print("]\n");
	}


	public void add(NodeMetadata nodeMetadata){
		buffer.add(nodeMetadata);
	}

	/**
	 * Fetches and removes first item from buffer
	 */
	synchronized public NodeMetadata get(){

		NodeMetadata nodeMetadata = null;

		if (buffer.size() > 0){
			nodeMetadata = buffer.get(0);
			buffer.remove(0);
		}
		return nodeMetadata;
	}



}
	
	
