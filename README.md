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

## WebSocket authentication overview

At protocol level, both HTTP and WS don't ensure any kind of security. HTTPS and WSS must be used instead. However, for demonstration purpose, this application uses HTTP and WS.

According to the [RFC 6455][], the WebSockets protocol don't define any particular way to authenticate a client:

> [**10.5.  WebSocket Client Authentication**][RFC 6455 10.5]
>
>   This protocol doesn't prescribe any particular way that servers can
>   authenticate clients during the WebSocket handshake.  The WebSocket
>   server can use any client authentication mechanism available to a
>   generic HTTP server, such as cookies, HTTP authentication, or TLS
>   authentication.

Bear in mind that HTTP and WebSockets are different channels of communication and it must be taken account when designing an authentication mechanist. See the quote below of an [article][Auth0 article] from the Auth0 blog:

> It is a common misconception that a user who is authenticated in the hosting web application, is also authenticated in the socket stream. These are two completely different channels.

### HTTP Basic Authentication

One of the obvious choices to protect a WebSocket endpoint is the [HTTP Basic Authentication][RFC 7617], sending the username and password encoded as Base64 in the `Authorization` header of the handshake request. 

However, the [HTML5 WebSocket API][] doesn't allow the developer to send arbitrary headers in the handshake.

At the time of writing, looks like Chrome and Firefox can negotiate HTTP Basic Authentication with the server, when the credentials are sent in the WebSocket URL, as following:

```javascript
String username = ...
String password = ...
var websocket = new WebSocket("ws://" + username + ":" + password + "@localhost:8080"); 
```

The browser will encode `username:password` as Base64 and will send it in the `Authorization` header of the handshake request. 

```
Authorization: Basic dXNlcm5hbWU6cGFzc3dvcmQ=
```

However looks like no other browsers support it.

### Token-Based Authentication

Alternatively to HTTP Basic Authentication, short lived tokens (and valid only once) can be used to authenticate a WebSocket handshake. It's the approach used in this application and require two steps.

First the client exchanges their username and password for an access token:

```js
var credentials = {
    username: document.getElementById("username").value,
    password: document.getElementById("password").value
};

var request = new XMLHttpRequest();
request.open("POST", "http://localhost:8080/auth");
request.setRequestHeader("Content-Type", "application/json");
request.onreadystatechange = function () {
    if (request.readyState === XMLHttpRequest.DONE && request.status == 200) {
        var webSocketAccessToken = JSON.parse(request.responseText);
        openSocket(webSocketAccessToken.token);          
    }
    ...
};
request.send(JSON.stringify(credentials));
```

Then the client open a WebSocket connection sending the access token in query string:

```js
function openSocket(accessToken) {
    var websocket = new WebSocket("ws://localhost:8080/chat?accessToken=" + accessToken); 
    ...
}
```

The server must ensure that the token is valid only for a short period of time and cannot be reused. 

On server side, the authentication and token validation are handled by the following classes:

- [`AccessTokenFilter`][AccessTokenFilter]
- [`AuthenticationServlet`][AuthenticationServlet]
- [`Authenticator`][Authenticator]

For example purposes, the user credentials are hardcoded and only the following are accepted by the application:

Username | Password 
-------- | ----------
joe      | secret 
john     | secret 
jane     | secret 

## Building and running this application

Follow these steps to build and run this application:

1. Open a command line window or terminal.
1. Navigate to the root directory of the project, where the `pom.xml` resides.
1. Compile the project: `mvn clean compile`.
1. Package the application: `mvn package`.
1. Change into the `target` directory: `cd target`
1. You should see a file with the following or a similar name: `chat-1.0.jar`.
1. Execute the JAR: `java -jar chat-1.0.jar`.
1. A page to test the application will be available at `http://localhost:8080`.
   - The authentication endpoint will be available at `http://localhost:8080/auth`.
   - The chat endpoint will be available at `ws://localhost:8080/chat`.

## Using the chat

Browse to `http://localhost:8080` and authenticate using the credentials listed above:

<img src="src/main/doc/authentication.png" width="500">

Once authenticated, the chat will be displayed and the online contacts will be seen on the left. Open multiple tabs/windows and authenticate with different users. Write a message and click _Send_:

<img src="src/main/doc/joe window.png" width="500">
<img src="src/main/doc/jane window.png" width="500">

[RFC 6455]: https://tools.ietf.org/html/rfc6455
[RFC 6455 10.5]: https://tools.ietf.org/html/rfc6455#section-10.5
[Auth0 article]: https://auth0.com/blog/auth-with-socket-io/
[HTML5 WebSocket API]: https://developer.mozilla.org/en-US/docs/Web/API/WebSocket
[RFC 7617]: https://tools.ietf.org/html/rfc7617
[AccessTokenFilter]: /src/main/java/com/cassiomolin/example/chat/security/AccessTokenFilter.java`
[AuthenticationServlet]: /src/main/java/com/cassiomolin/example/chat/security/AuthenticationServlet.java
[Authenticator]: /src/main/java/com/cassiomolin/example/chat/security/Authenticator.java
