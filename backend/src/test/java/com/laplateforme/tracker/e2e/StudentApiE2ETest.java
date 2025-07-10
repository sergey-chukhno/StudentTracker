package com.laplateforme.tracker.e2e;

import org.junit.*;
import java.net.http.*;
import java.net.URI;
import static org.junit.Assert.*;

public class StudentApiE2ETest {
    private static Process serverProcess;

    @BeforeClass
    public static void startServer() throws Exception {
        // Start your backend server as a separate process
        // Adjust the command as needed for your project structure
        serverProcess = new ProcessBuilder("java", "-jar", "target/StudentTrackerBackend.jar")
            .directory(new java.io.File("backend"))
            .start();
        Thread.sleep(3000); // Wait for server to start
    }

    @AfterClass
    public static void stopServer() {
        if (serverProcess != null) serverProcess.destroy();
    }

    @Test
    public void testRegisterAndLogin() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        String json = "{\"username\":\"e2euser\",\"password\":\"e2epass\"}";
        HttpRequest register = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/register"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(json))
            .build();
        HttpResponse<String> regResp = client.send(register, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, regResp.statusCode());

        HttpRequest login = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/login"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(json))
            .build();
        HttpResponse<String> loginResp = client.send(login, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, loginResp.statusCode());
        assertTrue(loginResp.body().contains("token"));
    }
}
