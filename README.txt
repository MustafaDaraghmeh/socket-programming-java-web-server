================================================================================
                    JAVA WEB SERVER - SOCKET PROGRAMMING EXAMPLE
================================================================================

OVERVIEW
--------
This is an educational web server implementation demonstrating fundamental 
socket programming concepts in Java. It implements a simple HTTP/1.0 server 
that can serve static HTML and image files.

Key Educational Topics:
  • ServerSocket and Socket classes (TCP/IP communication)
  • Input/Output streams (reading and writing over network)
  • Multi-threading for concurrent client handling
  • HTTP protocol implementation (request/response format)
  • Resource management with try-with-resources
  • Error handling and logging

================================================================================
COMPILATION
================================================================================

Compile all Java files:
  javac *.java

Note: Java compiler warnings about imports can be safely ignored.

================================================================================
EXECUTION
================================================================================

Run the server with a port number:
  java WebServer <port>

Examples:
  java WebServer 5555
  java WebServer 8080
  java WebServer 3000

Port Requirements:
  • Ports below 1024 require administrator/root privileges
  • Use ports 1024-65535 for normal user execution
  • Common port: 8080 (if 5555 is taken)

================================================================================
ACCESSING THE SERVER
================================================================================

From the same computer (localhost):
  1. Start the server (e.g., java WebServer 5555)
  2. Open a web browser
  3. Enter the URL: http://localhost:5555/

From a different computer:
  1. Identify the server's IP address (use 'ipconfig' on Windows or 'ifconfig' on Linux)
  2. Replace localhost with the server's IP address
  3. Example: http://192.168.1.100:5555/

================================================================================
FEATURES
================================================================================

HTTP Response Features:
  ✓ HTTP/1.0 compliant status lines and headers
  ✓ Content-Type detection (HTML, CSS, JavaScript, images, PDF, etc.)
  ✓ Content-Length header for proper file transfer
  ✓ 404 Not Found error responses with HTML error page
  ✓ Proper HTTP header formatting with CRLF line endings

Multi-threading:
  ✓ Each client request handled in a separate thread
  ✓ Server remains responsive to new connection requests
  ✓ Multiple clients can be served simultaneously
  ✓ Graceful shutdown with Ctrl+C

Resource Management:
  ✓ Try-with-resources for automatic stream cleanup
  ✓ Proper exception handling and error logging
  ✓ Per-client IP address logging for debugging

================================================================================
ARCHITECTURE
================================================================================

WebServer.java - Main Server Class
  • Creates a ServerSocket and listens for client connections
  • Accepts incoming TCP connections using socket.accept()
  • Spawns a new thread for each client request
  • Explains socket concepts with detailed comments

HttpRequest.java - Request Handler Class
  • Runs in its own thread (implements Runnable)
  • Reads HTTP request from client socket
  • Parses request line to extract filename
  • Serves file or returns 404 error
  • Demonstrates stream I/O and HTTP protocol

Flow:
  1. WebServer creates ServerSocket on specified port
  2. WebServer calls socket.accept() → blocks until client connects
  3. Client connects → ServerSocket returns a Socket object
  4. WebServer creates new HttpRequest(Socket) → handles that client
  5. WebServer spawns a new Thread for HttpRequest.run()
  6. WebServer immediately returns to accept() for next client
  7. HttpRequest thread reads HTTP request from socket
  8. HttpRequest thread sends HTTP response and closes socket

This allows the server to handle multiple clients concurrently!

================================================================================
KEY SOCKET PROGRAMMING CONCEPTS
================================================================================

Understood by studying this server:

1. ServerSocket
   - Listens for incoming TCP connections on a port
   - socket.accept() is a BLOCKING operation
   - Returns a Socket object when a client connects

2. Socket
   - Represents one client connection
   - Has InputStream (read from client) and OutputStream (write to client)
   - Each client gets a separate Socket object

3. Multi-Threading
   - Main thread accepts connections and spawns worker threads
   - Each worker thread handles one client request
   - Allows unlimited concurrent clients
   - Main thread can immediately accept the next connection

4. I/O Streams
   - Read HTTP request from socket's InputStream
   - Write HTTP response to socket's OutputStream
   - Buffered reading for efficiency
   - Always close streams to release resources

5. HTTP Protocol
   - Request: Method + Path + Headers + Body
   - Response: Status + Headers + Body
   - CRLF (\r\n) separates headers and ends response
   - MIME types tell the browser how to display content

================================================================================
SUPPORTED FILE TYPES
================================================================================

The server automatically detects and serves:
  .html, .htm     → text/html
  .css            → text/css
  .js             → application/javascript
  .jpg, .jpeg     → image/jpeg
  .png            → image/png
  .gif            → image/gif
  .ico            → image/x-icon
  .txt            → text/plain
  .pdf            → application/pdf
  .json           → application/json
  (others)        → application/octet-stream (download)

================================================================================
EXAMPLE REQUESTS
================================================================================

Valid requests your server can handle:
  GET /index.html HTTP/1.1       → Serves index.html
  GET /test1.html HTTP/1.1       → Serves test1.html
  GET /files/file1.html HTTP/1.1 → Serves files/file1.html
  GET / HTTP/1.1                 → Serves index.html (default)
  GET /notfound.html HTTP/1.1    → Returns 404 error

================================================================================
ERROR HANDLING
================================================================================

The server gracefully handles:
  ✓ Port already in use (BindException)
  ✓ Invalid port number
  ✓ File not found (404 response)
  ✓ Client disconnections
  ✓ Malformed requests
  ✓ IO exceptions during file transfer

All errors are logged with client IP address for debugging.

================================================================================
TESTING THE SERVER
================================================================================

Method 1: Web Browser (Easiest)
  1. Start server: java WebServer 5555
  2. Open browser: http://localhost:5555/
  3. Try different pages: http://localhost:5555/pages/about.html
  4. Test error: http://localhost:5555/notfound.html

Method 2: Command Line (curl)
  curl http://localhost:5555/
  curl -v http://localhost:5555/  (verbose, shows headers)

Method 3: telnet (raw HTTP)
  telnet localhost 5555
  GET /index.html HTTP/1.0

================================================================================
STOPPING THE SERVER
================================================================================

To stop the server:
  Press Ctrl+C in the terminal running the server
  The server will close gracefully and print "WebServer stopped"

================================================================================
EXTENSIONS & LEARNING EXERCISES
================================================================================

For students to extend this project:

1. Add POST request handling (not just GET)
2. Add request logging to a file with timestamps
3. Implement URL query parameters (e.g., ?name=value)
4. Add directory listing when /directory/ is requested
5. Implement HTTP caching headers (Last-Modified, ETag)
6. Add gzip content compression
7. Create a status/statistics page (/status)
8. Implement connection keep-alive (HTTP/1.1 feature)
9. Add basic authentication
10. Measure and log request processing time

================================================================================
TROUBLESHOOTING
================================================================================

Problem: "Address already in use"
  → Another process is using this port
  → Try a different port number
  → Or wait a few minutes and try again

Problem: "Permission denied" on port < 1024
  → Need administrator/root privileges for low port numbers
  → Use ports 1024 and above instead

Problem: Files not found
  → Make sure files are in the same directory as WebServer.class
  → Use /files/file1.html to access files in subdirectories

Problem: Browser shows garbled content
  → May be a binary file (image, PDF)
  → Check Content-Type is correct
  → Try opening file directly in browser

================================================================================
REFERENCES & RESOURCES
================================================================================

Java Socket Programming:
  • https://docs.oracle.com/javase/tutorial/networking/sockets/index.html
  • ServerSocket: https://docs.oracle.com/javase/8/docs/api/java/net/ServerSocket.html
  • Socket: https://docs.oracle.com/javase/8/docs/api/java/net/Socket.html

HTTP Protocol:
  • RFC 2616 - HTTP/1.1 Specification
  • https://www.w3.org/Protocols/rfc2616/rfc2616-sec5.html

Threading in Java:
  • https://docs.oracle.com/javase/tutorial/essential/concurrency/index.html
  • Runnable interface: https://docs.oracle.com/javase/8/docs/api/java/lang/Runnable.html

================================================================================
