package echo;

import java.nio.ByteBuffer;

import org.xnio.channels.StreamSourceChannel;

import io.undertow.Undertow;
import io.undertow.util.Headers;
import io.undertow.util.StatusCodes;

public class EchoServer {

    public static void main(String[] args) {
        Undertow.builder().addHttpListener(8080, "localhost").setHandler(ex -> {
            if (ex.getRequestMethod().equalToString("GET")) {
                String text = ex.getQueryParameters().get("text").poll();
                ex.setStatusCode(StatusCodes.OK);
                ex.getRequestHeaders().put(Headers.CONTENT_TYPE, "text/plain; charset=UTF-8");
                ex.getResponseSender().send(text);

            } else if (ex.getRequestMethod().equalToString("POST")) {
                ByteBuffer buf = ByteBuffer.allocate((int) ex.getRequestContentLength());
                try (StreamSourceChannel channel = ex.getRequestChannel()) {
                    channel.read(buf);
                }
                ex.setStatusCode(StatusCodes.OK);
                ex.getRequestHeaders().put(Headers.CONTENT_TYPE, "text/plain; charset=UTF-8");
                ex.getResponseSender().send(new String(buf.array()));

            } else {
                ex.setStatusCode(StatusCodes.METHOD_NOT_ALLOWED);
                ex.getRequestHeaders().put(Headers.CONTENT_TYPE, "text/plain; charset=UTF-8");
                ex.getResponseSender().send(StatusCodes.METHOD_NOT_ALLOWED_STRING);
            }
        }).build().start();
    }
}
