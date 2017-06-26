# Example of chat application with WebSockets

[![MIT Licensed](https://img.shields.io/badge/license-MIT-blue.svg)](https://raw.githubusercontent.com/cassiomolin/chat/master/LICENSE.txt)

Chat example using:

- **Undertow:** Servlet container
- **Weld:** CDI reference implementation
- **Jackson:** JSON parser for Java
- **WebSockets:** Using the JSR 356 implementation provided by Undertow

This example demonstrates a simple chat with WebSockets:

- On server side the WebSockets support is provided by Undertow (that implements the JSR 356).
- On client side the WebSockets support is provided by the HTML5 WebSockets API.

## Authentication overview

The [RFC 6455][] says the following regard WebSocket client authentication:

> [**10.5.  WebSocket Client Authentication**][RFC 6455 10.5]
>
>   This protocol doesn't prescribe any particular way that servers can
>   authenticate clients during the WebSocket handshake.  The WebSocket
>   server can use any client authentication mechanism available to a
>   generic HTTP server, such as cookies, HTTP authentication, or TLS
>   authentication.

HTTP Basic Authentication has been chosen to secure the `/chat` endpoint. When the credentials are sent in the WebSocket URL, as following:

```javascript
var ws = new WebSocket("ws://username:password@example.com"); 
```

The browser sends `username:password` encoded as Base64 in the handshake request and the HTTP Basic Authentication is negotiated with the server:

```
Authorization: Basic dXNlcm5hbWU6cGFzc3dvcmQ=
```

The [`AuthenticationFilter`](src/main/java/com/cassiomolin/example/chat/security/AuthenticationFilter.java) filter extracts the credentials from the `Authorization` header, validates them and accepts or refuses the request.

User credentials are hardcoded and only the following are accepted by the application:

 Username | Password 
--------- |----------
 joe      | secret 
 jane     | secret 
 john     | secret 

## Building and running this application

Follow these steps to build and run this application:

1. Open a command line window or terminal.
1. Navigate to the root directory of the project, where the `pom.xml` resides.
1. Compile the project: `mvn clean compile`.
1. Package the application: `mvn package`.
1. Change into the `target` directory: `cd target`
1. You should see a file with the following or a similar name: `chat-1.0.jar`.
1. Execute the JAR: `java -jar chat-1.0.jar`.
1. A page to test the application will be available at `http://localhost:8080/index.html`. The chat endpoint will be available at `http://localhost:8080/chat`.

## Using the chat

Browse to `http://localhost:8080/index.html` and authenticate using the credentials listed above:

<img src="src/main/doc/screenshot-01.png" width="500">

Once authenticated, the chat will be displayed and the online contacts will be seen on the left. Open multiple tabs/windows and authenticate with different users. Write a message and click _Send_:

<img src="src/main/doc/screenshot-03.png" width="500">
<img src="src/main/doc/screenshot-04.png" width="500">

[RFC 6455]: https://tools.ietf.org/html/rfc6455
[RFC 6455 10.5]: https://tools.ietf.org/html/rfc6455#section-10.5
