package io.github.paveldt.util;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Author Pavel Todorov - pat00045 - 2634926
 * Utility class for node and coordinator IP selection
 */
public class IPManager {
    public static String getLocalIP() {
        // innitialize to loopback address
        String nodeIP = "127.0.0.1";

        // get the IP address of the node

        try{
            InetAddress localIP = InetAddress.getLocalHost();
            if(localIP instanceof Inet6Address) {
                System.out.println("IPv6 not supported - falling back to using 127.0.0.1 ");
            } else if (localIP instanceof Inet4Address) {
                nodeIP = localIP.getHostAddress();
            }

        } catch(UnknownHostException e){
            System.out.println("Failed to detect IP - using 127.0.0.1 instead");
            e.printStackTrace();
        }
        return nodeIP;
    }

    /**
     * Check if coordinator IP is v4 and a correct IP
     * if not return 127.0.0.1
     */
    public static String decideCoordinatorIP(String coordinatorIP) {

        try{

            InetAddress coordinatorIPv4 = InetAddress.getByName(coordinatorIP);
            if(coordinatorIPv4 instanceof  Inet4Address) {
                System.out.println("Using coordinator IP " + coordinatorIP);
                return coordinatorIP;
            }
        } catch (UnknownHostException e) {
            System.out.println("Error - Unknown host. Using coordinator IP 127.0.0.1 ");
            e.printStackTrace();
        }
        System.out.println("Using coordinator IP 127.0.0.1 ");
        return  "127.0.0.1";
    }
}
