# NETTY-WEBSOCKET For FUN!

![Java](https://img.shields.io/badge/Java-11%2B-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.4.5-brightgreen)
![Netty](https://img.shields.io/badge/Netty-4.1.59-blue)
![License](https://img.shields.io/badge/License-MIT-yellow)

A lightweight WebSocket server framework built on **Netty** with **Spring Boot** integration. Provides annotation-based request routing similar to Spring MVC.

## Features

- Annotation-based WebSocket controller (`@WebSocketController`, `@WebSocketRequestMapping`)
- Exception handling with `@WebSocketControllerAdvice`
- Channel and Channel Group management
- Multi-server support via Redis Pub/Sub
- Idle connection detection

## Requirements

- Java 11+
- Maven 3.6+
- Redis (optional, for multi-server deployment)

## Quick Start

```bash
git clone https://github.com/modorigoon/netty-websocket.git
cd netty-websocket
mvn clean package
java -jar service/target/*.jar
```

The WebSocket server will start on `ws://localhost:8090/ws`

## Project Structure

```
netty-websocket/
├── server/          # Core WebSocket framework (reusable library)
└── service/         # Example Spring Boot application
```

## Configuration

Configure in `service/src/main/resources/application.yml`:

```yaml
websocket:
  netty:
    socket-path: /ws           # WebSocket endpoint path
    port: 8090                 # Server port
    max-content-length: 65536  # Max message size (bytes)
    boss-thread: 1             # Accept thread count
    worker-thread: 4           # I/O thread count
  pubsub:
    provider: redis            # Pub/Sub provider
    host: localhost
    port: 6379
    channel: pubsub            # Redis channel name
```

## WebSocket Request & Response

### Request
```json
{
    "mapper": "MAPPER_NAME",
    "body": {
        "key": "value"
    }
}
```

### Response
```json
{
    "status": "OK",
    "identifier": "target identifier",
    "message": "message",
    "body": {},
    "time": "2024-01-01T12:00:00"
}
```

**Status codes**: `OK`, `ERROR`, `BAD_REQUEST`

## Annotations

### @WebSocketController
Marks a class as WebSocket request handler (similar to `@RestController`)

```java
@WebSocketController
public class GroupController {
    // handler methods
}
```

### @WebSocketRequestMapping
Maps a method to handle specific WebSocket requests (similar to `@RequestMapping`)

```java
@WebSocketController
public class GroupController {

    /**
     * Request: { "mapper": "join", "body": { "groupName": "room1" } }
     */
    @WebSocketRequestMapping(value = "join")
    public void join(JoinRequest req, ChannelHandlerContext ctx,
                     CompletableFuture<ResponseEntity> future) {
        // handle join request
        future.complete(new ResponseEntity("room1", "Joined!", null));
    }

    /**
     * Request: { "mapper": "leave", "body": null }
     */
    @WebSocketRequestMapping(value = "leave")
    public void leave(ChannelHandlerContext ctx,
                      CompletableFuture<ResponseEntity> future) {
        // handle leave request
        future.complete(new ResponseEntity(ResponseEntity.ResponseStatus.OK));
    }
}
```

### @WebSocketControllerAdvice & @WebSocketExceptionHandler
Global exception handling (similar to `@ControllerAdvice`)

```java
@WebSocketControllerAdvice
public class WebSocketExceptionAdvice {

    @WebSocketExceptionHandler(throwables = { GroupProcessingException.class })
    public ResponseEntity handleGroupException(GroupProcessingException e) {
        return new ResponseEntity(ResponseEntity.ResponseStatus.ERROR, e.getMessage());
    }
}
```

## WebSocketTemplate

Provides helper methods for WebSocket operations.

### Single Channel Operations
| Method | Description |
|--------|-------------|
| `send(dest, message, data)` | Send message to a single channel |
| `join(name, channel)` | Register a channel with an identifier |
| `leave(channel)` | Unregister a channel |
| `isRegistered(name)` | Check if an identifier is registered |

### Channel Group Operations
| Method | Description |
|--------|-------------|
| `sendToGroup(name, message, data)` | Broadcast message to a group |
| `joinGroup(name, channel)` | Add channel to a group |
| `leaveGroup(name, channel)` | Remove channel from a group |

## Multi-Server Architecture

For horizontal scaling, use Redis Pub/Sub to synchronize messages across server instances.

```
                    ┌─────────────────┐
                    │  Load Balancer  │
                    └────────┬────────┘
                             │
           ┌─────────────────┼─────────────────┐
           │                 │                 │
    ┌──────▼──────┐   ┌──────▼──────┐   ┌──────▼──────┐
    │  Server 1   │   │  Server 2   │   │  Server 3   │
    └──────┬──────┘   └──────┬──────┘   └──────┬──────┘
           │                 │                 │
           └─────────────────┼─────────────────┘
                             │
                    ┌────────▼────────┐
                    │  Redis Pub/Sub  │
                    └─────────────────┘
```

Implement custom `Publisher` or `Subscriber` interfaces for other message brokers.

## License

MIT License
