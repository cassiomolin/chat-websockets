# Example of chat application with WebSockets

[![MIT Licensed](https://img.shields.io/badge/license-MIT-blue.svg)](https://raw.githubusercontent.com/cassiomolin/chat/master/LICENSE.txt)

Example using:

- **Undertow:** Servlet container
- **Weld:** CDI reference implementation
- **Jackson:** JSON parser for Java
- **WebSockets:** Using the JSR-356 implementation provided by Undertow

This example demonstrates a simple chat application with WebSockets. On server side the WebSockets support is provided by Undertow (that implements the JSR 356) and on client side the WebSockets support is provided by the HTML5 WebSockets API.

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

- [ ] Add screenshots
- [ ] Improve documentation with payload types
- [ ] Improve documentation with authentication details
- [ ] Write tests with Java WebSocket client API and Arquillian
