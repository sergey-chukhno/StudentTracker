package com.laplateforme.tracker;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import com.laplateforme.tracker.handler.StudentsHandler;

public class StudentTrackerServer {
  public static void main(String[] args) throws IOException {
    int port = 8080;
    HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
    server.createContext("/students", new StudentsHandler());
    server.setExecutor(null); // creates a default executor
    System.out.println("[INFO] HTTP server started on port " + port);
    server.start();
  }
}