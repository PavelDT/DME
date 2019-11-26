package io.github.paveldt;

/**
 * @Author Pavel Todorov - pat00045 - 2634926
 * Container that holds node information and avoids type casting.
 */
public class NodeMetadata {
    public String ip;
    public int port;

    public NodeMetadata(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    /**
     * toString is a simple display function for the node data.
     */
    public String toString() {
        return ip + ":" + port;
    }

}
