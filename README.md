# Example with Undertow, Weld, Jersey and WebSockets

Example application using:

- **Undertow:** Servlet container
- **Weld:** CDI reference implementation
- **Jackson:** JSON parser for Java
- **WebSockets:** Using the JSR-356 implementation provided by Undertow
- 
## How to build and run this application?

Follow these steps to build and run this application:

1. Open a command line window or terminal.
1. Navigate to the root directory of the project, where the `pom.xml` resides.
1. Compile the project: `mvn clean compile`.
1. Package the application: `mvn package`.
1. Change into the `target` directory: `cd target`
1. You should see a file with the following or a similar name: `chat-1.0.jar`.
1. Execute the JAR: `java -jar chat-1.0.jar`.
1. A page to test the application will be available at `http://localhost:8080/index.html`. The chat endpoint will be available at `http://localhost:8080/chat`.

## TODO

- [ ] Improve documentation (write about payload types)
- [ ] Write tests with Java WebSocket client API and Arquillian
