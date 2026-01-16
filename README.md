# Java Web Server - Socket Programming Example

A professional, educational implementation of a multi-threaded web server in Java that demonstrates fundamental socket programming concepts and HTTP protocol implementation.

## Overview

This project shows students how web servers work at the socket level, implementing a fully functional HTTP/1.0 server that serves static files with proper request parsing, response generation, and multi-threaded client handling.

## Features

- **Socket Programming**: ServerSocket and Socket classes for TCP/IP communication
- **Multi-Threading**: Concurrent request handling using Java threads
- **HTTP Protocol**: Full HTTP/1.0 request/response implementation
- **File Serving**: Static HTML, CSS, JavaScript, images, and more
- **Error Handling**: Proper 404 responses and exception management
- **Professional Web Content**: 4 complete pages with modern design
- **Responsive Design**: Mobile-friendly web interface
- **Request Logging**: Per-client tracking with IP addresses

## Project Structure

```
WEBSERV/
├── WebServer.java              Main server class (socket creation & threading)
├── HttpRequest.java            Request handler (HTTP parsing & response)
│
├── www/                        Professional web content (served by server)
│   ├── index.html              Home page
│   ├── css/style.css           Modern responsive stylesheet
│   ├── js/main.js              Interactive JavaScript
│   └── pages/
│       ├── about.html          About the project
│       ├── socket-info.html    Socket programming guide
│       └── examples.html       Code examples
│
├── README.txt                  Comprehensive documentation
└── QUICKSTART.txt              Quick reference guide
```

## Getting Started

### Prerequisites
- Java JDK 8 or higher
- Any modern web browser
- Terminal/Command line

### Compile

```bash
cd /path/to/WEBSERV
javac *.java
```

### Run

```bash
java WebServer 5555
```

The server will start listening on `http://localhost:5555/`

### Access the Server

Open your browser and navigate to:
- **Home**: `http://localhost:5555/`
- **About**: `http://localhost:5555/pages/about.html`
- **Socket Guide**: `http://localhost:5555/pages/socket-info.html`
- **Code Examples**: `http://localhost:5555/pages/examples.html`

### Stop the Server

Press `Ctrl+C` in the terminal

## Usage

### Basic Commands

```bash
# Compile
javac *.java

# Run on default port 5555
java WebServer 5555

# Run on custom port
java WebServer 8080

# Test with curl
curl http://localhost:5555/
curl -v http://localhost:5555/pages/about.html
```

## Key Concepts Demonstrated

### Socket Programming
- **ServerSocket**: Listening for incoming TCP connections
- **Socket**: Representing client connections
- **Streams**: Input/output communication channels
- **Blocking Operations**: How `accept()` waits for clients

### Multi-Threading
- Spawning new threads for concurrent request handling
- Main thread accepts connections while worker threads serve clients
- Unlimited concurrent clients (limited only by system resources)

### HTTP Protocol
- Request parsing (method, filename, headers)
- Response generation (status line, headers, body)
- MIME type detection
- Proper header formatting with CRLF

### File I/O
- Efficient buffered reading (1KB chunks)
- Large file handling without excessive memory usage
- Security: directory traversal prevention

## Architecture

```
Client Request
    ↓
ServerSocket.accept() [BLOCKING]
    ↓
Socket returned
    ↓
Create HttpRequest object
    ↓
Spawn new Thread
    ↓
Main thread returns to accept()
    ↓
Worker thread processes request
    ↓
Read HTTP request from Socket
    ↓
Parse request line
    ↓
Locate and serve file
    ↓
Send HTTP response
    ↓
Close socket
```

## Supported File Types

| Extension | MIME Type |
|-----------|-----------|
| .html, .htm | text/html |
| .css | text/css |
| .js | application/javascript |
| .jpg, .jpeg | image/jpeg |
| .png | image/png |
| .gif | image/gif |
| .ico | image/x-icon |
| .txt | text/plain |
| .pdf | application/pdf |
| .json | application/json |
| (others) | application/octet-stream |

## Web Content

The server includes professional web content demonstrating:

### Home Page
- Project overview and key features
- How the server works (6-step flow)
- Quick start guide
- Learning path

### About Page
- Project explanation
- Key concepts
- Why socket programming matters
- Real-world applications

### Socket Info Page
- What is a socket?
- Client-server model
- Key programming concepts
- HTTP protocol details
- Threading and concurrency
- Common pitfalls and solutions

### Examples Page
- Code snippets with explanations
- Creating ServerSocket
- Handling requests
- Sending responses
- MIME type detection
- Error handling
- Learning exercises

## Code Quality

- **Clean Code**: Well-organized, readable implementation
- **Comprehensive Comments**: 50+ lines explaining socket concepts
- **Error Handling**: Proper exception handling for various error conditions
- **Best Practices**: Try-with-resources, proper resource cleanup
- **Input Validation**: Port validation, file existence checking
- **Security**: Directory traversal prevention

## Learning Outcomes

Students will understand:

**Network Programming**
- How TCP/IP sockets enable network communication
- Client-server communication model
- Blocking vs. non-blocking operations

**Protocol Implementation**
- HTTP request/response format
- Header parsing and generation
- Status codes and error responses
- MIME types

**Software Design**
- Multi-threading for concurrent processing
- Resource management
- Exception handling
- Logging and debugging

**Professional Practices**
- Code organization
- Documentation
- Testing
- Security considerations

## Troubleshooting

**Port Already in Use**
```bash
# Use a different port
java WebServer 8080
```

**Permission Denied (ports < 1024)**
- Use ports 1024 and above (no admin required)
- Or run with administrator privileges

**Files Not Found**
- Ensure `www/` directory exists in current directory
- Check file paths: `http://localhost:5555/pages/about.html`

**Compilation Errors**
- Make sure you're in the WEBSERV directory
- Run: `javac *.java`

## Testing

### Browser Testing
Simply visit the URLs in your web browser

### Command Line Testing
```bash
# Basic request
curl http://localhost:5555/

# Show headers
curl -v http://localhost:5555/

# Test specific page
curl http://localhost:5555/pages/examples.html
```

## Documentation

Detailed documentation is available in:
- **README.txt** - Comprehensive guide for students
- **QUICKSTART.txt** - Quick reference guide

## Requirements

- Java 8+
- No external dependencies (pure Java standard library)

## Port Configuration

- Default: 5555
- Valid range: 1024-65535
- Use ports 1024+ without admin privileges

## Future Enhancements

Students can extend this project with:
- POST request handling
- URL query parameters
- Directory listing
- HTTP caching headers
- Content compression
- Authentication
- Statistics page
- Performance monitoring

## References

- [Java Socket API](https://docs.oracle.com/javase/8/docs/api/java/net/Socket.html)
- [HTTP/1.1 Specification](https://www.rfc-editor.org/info/rfc2616)
- [Java Threading](https://docs.oracle.com/javase/tutorial/essential/concurrency/)

## Author

Educational example for teaching socket programming and network concepts.

## License

Free to use for educational purposes.

---

**Ready to learn?** Start the server and explore the web pages to see socket programming in action!

```bash
java WebServer 5555
```

Then open `http://localhost:5555/` in your browser.
