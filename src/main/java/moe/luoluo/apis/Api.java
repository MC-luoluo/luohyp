package moe.luoluo.apis;

import moe.luoluo.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.StandardCharsets;


public class Api {
    static final Logger logger = LoggerFactory.getLogger(Api.class);

    public static String request(URI uri) throws IOException {

        Proxy proxy = null;
        if (!Config.INSTANCE.getProxy().isEmpty()) {
            proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(Config.INSTANCE.getProxy(), Integer.parseInt(Config.INSTANCE.getProxyPort())));
        }
        URL url = uri.toURL();
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            URLConnection connection;
            if (proxy != null) {
                connection = url.openConnection(proxy);
            } else {
                connection = url.openConnection();
            }
            connection.setConnectTimeout(10000);
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("Connection", "keep-alive");
//            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:131.0) Gecko/20100101 Firefox/131.0");
            connection.connect();

            String line;
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (FileNotFoundException e) {
            return "NotFound";
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                logger.error("An error occurred", e2);
            }
        }
        return result.toString();
    }
}
