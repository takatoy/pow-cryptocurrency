package wafflecore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.BindException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class WaffleHttpServer {
    HttpServer server;

    public WaffleHttpServer() {
        try {
            server = HttpServer.create(new InetSocketAddress(8080), 0);
        } catch (BindException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        try {
            server.createContext("/", new WaffleHttpHandler());
            server.setExecutor(null);
            server.start();
            System.out.println("Server running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        server.stop(0);
    }
}

class WaffleHttpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange arg0) throws IOException {
        String path = arg0.getRequestURI().getPath();
        if (path.equals("/")) {
            path = "/index.html";
        }
        File file = new File(".", path);
        arg0.getResponseHeaders().add("Content-Type", "application/json");
        arg0.sendResponseHeaders(200, file.length());
        InputStream is = new FileInputStream(file);
        OutputStream os = arg0.getResponseBody();

        byte[] bytes = new byte[1024];
        int len = 0;
        while((len = is.read(bytes)) >= 0){
            os.write(bytes, 0, len);
        }
        os.flush();

        os.close();
        is.close();
    }
}
