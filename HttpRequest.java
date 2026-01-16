import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Handles a single HTTP request in a separate thread.
 * 
 * Key Socket Concepts Demonstrated:
 * - Socket Input/Output Streams: Used to read HTTP request and send HTTP response
 * - Buffered Reading: Efficient reading of HTTP headers line by line
 * - DataOutputStream: Allows writing different data types to the output stream
 * - Proper HTTP Protocol: Follows HTTP/1.0 response format
 * 
 * HTTP Response Format:
 * Status Line: HTTP/1.0 200 OK
 * Headers: Content-Type: text/html
 *          Content-Length: 1234
 * Blank Line: (CRLF only)
 * Body: (file content)
 */
final class HttpRequest implements Runnable {
    // HTTP uses CRLF (Carriage Return Line Feed) as line separator
    final static String CRLF = "\r\n";
    final static int BUFFER_SIZE = 1024;
    
    private Socket socket;
    private String clientIP;
    
    /**
     * Constructor - receives the client socket from the server
     * 
     * Socket represents the connection to the client and allows us to:
     * - Read the HTTP request from the client (InputStream)
     * - Write the HTTP response back to the client (OutputStream)
     */
    public HttpRequest(Socket socket, String clientIP) {
        this.socket = socket;
        this.clientIP = clientIP;
    }
    
    /**
     * Implement the run() method of the Runnable interface.
     * This method is executed when the thread is started.
     * 
     * Threading allows multiple clients to be served concurrently:
     * Main thread -> accepts new connections
     * Worker threads -> process requests independently
     */
    public void run() {
        try {
            processRequest();
        } catch (Exception e) {
            System.err.println("[" + clientIP + "] Error processing request: " + e.getMessage());
        }
    }

    /**
     * Parse the HTTP request and send an appropriate response
     * 
     * HTTP Request Format:
     * GET /index.html HTTP/1.1
     * Host: localhost:5555
     * Connection: keep-alive
     * ... (more headers)
     * (blank line)
     * 
     * We only need to parse the request line and determine the filename.
     */
    private void processRequest() throws Exception {
        // Use try-with-resources to ensure streams are properly closed
        // even if an exception occurs
        try (
            InputStream inputStream = socket.getInputStream();
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            BufferedReader requestReader = new BufferedReader(new InputStreamReader(inputStream))
        ) {
            // Step 1: Read the HTTP request line
            // Format: GET /filename HTTP/1.1
            String requestLine = requestReader.readLine();
            
            if (requestLine == null) {
                return;  // Client disconnected without sending a request
            }
            
            // Step 2: Parse the request line to extract the filename
            StringTokenizer tokens = new StringTokenizer(requestLine);
            String method = tokens.nextToken();       // Should be "GET"
            String fileName = tokens.nextToken();     // The requested file path
            String httpVersion = tokens.nextToken();  // Should be "HTTP/1.1" or "HTTP/1.0"
            
            // Log the request
            System.out.println("[" + clientIP + "] " + requestLine);
            
            // Step 3: Read HTTP headers (and discard them for this simple server)
            // Headers end with a blank line (just CRLF)
            String headerLine;
            while ((headerLine = requestReader.readLine()) != null && !headerLine.isEmpty()) {
                // In a real server, you would parse these headers (Host, User-Agent, etc.)
                // For this simple example, we just consume them
            }
            
            // Step 4: Map the request to a file
            // Map requests to the www directory
            // This is a security measure to prevent directory traversal attacks
            if (fileName.equals("/")) {
                fileName = "/index.html";  // Default file
            }
            fileName = "./www" + fileName;
            
            // Step 5: Try to open the requested file
            File file = new File(fileName);
            boolean fileExists = file.exists() && file.isFile();
            
            // Step 6: Construct and send the HTTP response
            if (fileExists) {
                sendSuccessResponse(outputStream, file);
            } else {
                sendErrorResponse(outputStream);
            }
            
        } finally {
            // The try-with-resources statement automatically closes all resources,
            // but we also explicitly close the socket
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("[" + clientIP + "] Error closing socket: " + e.getMessage());
            }
        }
    }

    /**
     * Send a successful HTTP 200 response with the requested file
     * 
     * HTTP Response Format:
     * Status Line: HTTP/1.0 200 OK\r\n
     * Headers: Content-Type: text/html\r\n
     *          Content-Length: 1234\r\n
     * Blank Line: \r\n
     * Body: (file content)
     */
    private void sendSuccessResponse(DataOutputStream outputStream, File file) throws IOException {
        long fileSize = file.length();
        String contentType = getContentType(file.getName());
        
        // Send status line
        outputStream.writeBytes("HTTP/1.0 200 OK" + CRLF);
        
        // Send response headers
        outputStream.writeBytes("Content-Type: " + contentType + CRLF);
        outputStream.writeBytes("Content-Length: " + fileSize + CRLF);
        
        // Blank line indicates end of headers
        outputStream.writeBytes(CRLF);
        
        // Send the file content
        sendFileBytes(file, outputStream);
        
        System.out.println("[" + clientIP + "] Sent: 200 OK (" + fileSize + " bytes)");
    }

    /**
     * Send an HTTP 404 Not Found error response
     */
    private void sendErrorResponse(DataOutputStream outputStream) throws IOException {
        String errorBody = "<HTML>" +
            "<HEAD><TITLE>404 Not Found</TITLE></HEAD>" +
            "<BODY>" +
            "<H1>404 Not Found</H1>" +
            "<P>The requested resource was not found on this server.</P>" +
            "</BODY>" +
            "</HTML>";
        
        // Send status line
        outputStream.writeBytes("HTTP/1.0 404 Not Found" + CRLF);
        
        // Send response headers
        outputStream.writeBytes("Content-Type: text/html" + CRLF);
        outputStream.writeBytes("Content-Length: " + errorBody.length() + CRLF);
        
        // Blank line indicates end of headers
        outputStream.writeBytes(CRLF);
        
        // Send error message as body
        outputStream.writeBytes(errorBody);
        
        System.out.println("[" + clientIP + "] Sent: 404 Not Found");
    }

    /**
     * Read a file and send its bytes to the client via the socket's output stream
     * 
     * This demonstrates efficient file reading:
     * - Use a buffer (1KB) to read chunks of the file
     * - Send each chunk over the network
     * - Repeat until entire file is sent
     * 
     * This approach is memory-efficient and works for files of any size.
     */
    private void sendFileBytes(File file, OutputStream outputStream) throws IOException {
        try (FileInputStream fileStream = new FileInputStream(file)) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            
            // Read from file and write to output stream in chunks
            while ((bytesRead = fileStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
    }

    /**
     * Determine the MIME type (Content-Type) based on file extension
     * 
     * This header tells the browser how to interpret the file:
     * - text/html: Display as HTML page
     * - image/jpeg: Display as JPEG image
     * - image/png: Display as PNG image
     * - text/css: Treat as CSS stylesheet
     * - application/octet-stream: Generic binary file (download)
     */
    private static String getContentType(String fileName) {
        fileName = fileName.toLowerCase();
        
        if (fileName.endsWith(".html") || fileName.endsWith(".htm")) {
            return "text/html";
        }
        if (fileName.endsWith(".css")) {
            return "text/css";
        }
        if (fileName.endsWith(".js")) {
            return "application/javascript";
        }
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "image/jpeg";
        }
        if (fileName.endsWith(".png")) {
            return "image/png";
        }
        if (fileName.endsWith(".gif")) {
            return "image/gif";
        }
        if (fileName.endsWith(".ico")) {
            return "image/x-icon";
        }
        if (fileName.endsWith(".txt")) {
            return "text/plain";
        }
        if (fileName.endsWith(".pdf")) {
            return "application/pdf";
        }
        if (fileName.endsWith(".json")) {
            return "application/json";
        }
        
        // Default to generic binary type
        return "application/octet-stream";
    }
}

