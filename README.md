# DME Readme Instructions

DME is a distributed mutual-exclusion system whereby you can write to only node node  at a time. The project serves as a demonstration of how various mechanisms in Java can be utilised to:
- Ensure exclusive access to resources - in this case the buffers used to store messages.
- Handle errors - implemented via timeouts
- Self-heal critical errors - Nodes can be killed and re-join the DME system on restart.

## How to start the Coordinator:

1. cd to the DME directory
2. java -cp . io.github.paveldt.Coordinator

How to start a node
1 cd to the DME directory
2 java -cp . io.github.paveldt.Node [node-port] [milisec] [priority] [optional coordinator ip]

Example with parameters:

```
java -cp . io.github.paveldt.Coordinator
java -cp . io.github.paveldt.Node 9100 100 LOW 127.0.0.1
```
