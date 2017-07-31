# Example of chat application with WebSockets

[![Build Status](https://travis-ci.org/cassiomolin/chat-websockets.svg?branch=master)](https://travis-ci.org/cassiomolin/chat-websockets)
[![MIT Licensed](https://img.shields.io/badge/license-MIT-blue.svg)](https://raw.githubusercontent.com/cassiomolin/chat-websockets/master/LICENSE.txt)

Chat example using:

- **Undertow:** Servlet container that also provides WebSockets support (JSR 356 implementation).
- **Weld:** CDI reference implementation.
- **Jackson:** JSON parser for Java.

This example demonstrates a simple chat with WebSockets:

- On server side the WebSockets support is provided by Undertow (that implements the JSR 356).
- On client side the WebSockets support is provided by the HTML5 WebSockets API.

## WebSocket frames

Once WebSocket frames can carry text or binary data without any headers, the application defines its own protocol to exchange messages between client and server. The frames are encoded as JSON and look like:

```json
{
  "type": "...",
  "payload": { 
    
  }
}
```

The `type` property indicates the type of data that will be found in the `payload` property. For more details, refer to the [`WebSocketMessage`](src/main/java/com/cassiomolin/example/chat/model/WebSocketMessage.java) class.

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

It's also important to bear in mind that HTTP and WebSockets are different channels of communication and it must be taken into account when designing an authentication mechanism. See the quote below of an [article][Auth0 article] from the Auth0 blog:

> The socket server will not automagically know about the logged-in user, thus anyone could join any stream. [...]
>
> It is a common misconception that a user who is authenticated in the hosting web application, is also authenticated in the socket stream. These are two completely different channels.

### HTTP Basic Authentication

One of the obvious choices to protect a WebSocket endpoint is the [HTTP Basic Authentication][RFC 7617], sending the username and password encoded as Base64 in the `Authorization` header of the handshake request. 

However, the [HTML5 WebSocket API][] doesn't allow the developer to send arbitrary headers in the handshake.

At the time of writing, looks like only Chrome and Firefox can negotiate HTTP Basic Authentication with the server, when the credentials are sent in the WebSocket URL, as following:

```javascript
String username = ...
String password = ...
var websocket = new WebSocket("ws://" + username + ":" + password + "@localhost:8080"); 
```

The browser will encode `username:password` as Base64 and will send it in the `Authorization` header of the handshake request: 

```
Authorization: Basic dXNlcm5hbWU6cGFzc3dvcmQ=
```

While just a couple of browsers support it, we should consider another approach.

### Short lived tokens

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

Then the client opens a WebSocket connection sending the access token in query string to negotiate the authentication with the server:

```js
function openSocket(accessToken) {
    var websocket = new WebSocket("ws://localhost:8080/chat?accessToken=" + accessToken); 
    ...
}
```

A similar approach is used by [Slack Real Time Messaging API][].


The server ensures that the token is valid for a short period of time and cannot be reused. The following classes handle authentication and token validation on server side:

- [`AuthenticationServlet`](src/main/java/com/cassiomolin/example/chat/security/AuthenticationServlet.java)
- [`AccessTokenFilter`](src/main/java/com/cassiomolin/example/chat/security/AccessTokenFilter.java)
- [`Authenticator`](src/main/java/com/cassiomolin/example/chat/security/Authenticator.java)

[`Authenticator`](src/main/java/com/cassiomolin/example/chat/security/Authenticator.java) relies on [Guava Cache][] to keep the track of the tokens and expire them after a few seconds.

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
   1. The authentication endpoint will be available at `http://localhost:8080/auth`.
   1. The chat endpoint will be available at `ws://localhost:8080/chat`.

### Quick words on Undertow and uber-jars

This application is packed as an [uber-jar](https://stackoverflow.com/q/11947037/1426227), making it easy to run, so you don't need to be bothered by installing a servlet container such as Tomcat and then deploy the application on it. Just execute `java -jar <jar-file>` and the application will be up and running. 

This application uses [Undertow](http://undertow.io/), a lighweight Servlet container designed to be fully embeddable. It's used as the default web server in the [WildFly Application Server](http://wildfly.org/).

The uber-jar is created with the [Apache Maven Shade Plugin](https://maven.apache.org/plugins/maven-shade-plugin/), that provides the capability to create an executable jar including its dependencies.

## Using the chat

Browse to `http://localhost:8080` and authenticate using the credentials listed above:

<img src="src/main/doc/authentication.png" width="500">

Once authenticated, the chat will be displayed and the online contacts will be seen on the left. Open multiple tabs/windows and authenticate with different users. Write a message and hit <kbd>Enter</kbd> or click the _Send_ button to chat:

<img src="src/main/doc/joe window.png" width="500">
<img src="src/main/doc/jane window.png" width="500">

## Todo 

- [ ] Write tests with Arquillian


[RFC 6455]: https://tools.ietf.org/html/rfc6455
[RFC 6455 10.5]: https://tools.ietf.org/html/rfc6455#section-10.5
[Auth0 article]: https://auth0.com/blog/auth-with-socket-io/
[HTML5 WebSocket API]: https://developer.mozilla.org/en-US/docs/Web/API/WebSocket
[RFC 7617]: https://tools.ietf.org/html/rfc7617
[Slack Real Time Messaging API]: https://api.slack.com/methods/rtm.connect
[Guava Cache]: https://github.com/google/guava/wiki/CachesExplained
