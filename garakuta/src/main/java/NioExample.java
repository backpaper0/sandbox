
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

public class NioExample {

    static AtomicBoolean loop = new AtomicBoolean(true);

    public static void main(final String[] args) throws Exception {
        try (final Selector sel = Selector.open();
                ServerSocketChannel ssc = ServerSocketChannel.open()) {
            ssc.configureBlocking(false);
            ssc.socket().setReuseAddress(true);
            ssc.bind(new InetSocketAddress(9000));
            ssc.register(sel, SelectionKey.OP_ACCEPT, new AcceptHandler());
            int counter = 0;
            System.out.println("server start");
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        System.in.read();
                    } catch (final IOException e) {
                        e.printStackTrace();
                    }
                    loop.set(false);
                    sel.wakeup();
                }
            }).start();
            while (loop.get()) {
                System.out.printf("count: %d%n", ++counter);
                final int select = sel.select(10000);
                if (select > -1) {
                    for (final Iterator<SelectionKey> it = sel.selectedKeys()
                            .iterator(); it.hasNext();) {
                        final SelectionKey key = it.next();
                        it.remove();
                        final Handler h = (Handler) key.attachment();
                        h.handle(key);
                    }
                }
            }
        }
        System.out.println("server end");
    }

    interface Handler {

        void handle(SelectionKey key) throws IOException;
    }

    static class AcceptHandler implements Handler {

        @Override
        public void handle(final SelectionKey key) throws IOException {
            final ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
            System.out.printf("accept: %s%n", ssc);
            final SocketChannel sc = ssc.accept();
            sc.configureBlocking(false);
            sc.register(key.selector(), SelectionKey.OP_READ, new IOHandler());
        }
    }

    static class IOHandler implements Handler {

        Queue<ByteBuffer> bs = new LinkedList<>();

        @Override
        public void handle(final SelectionKey key) throws IOException {
            final SocketChannel sc = (SocketChannel) key.channel();
            int i = -1;
            if (key.isReadable()) {
                System.out.printf("read: %s%n", sc);
                final ByteBuffer b = ByteBuffer.allocate(5);
                i = sc.read(b);
                if (i > -1) {
                    b.flip();
                    bs.offer(b);
                    if ((key.interestOps() & SelectionKey.OP_WRITE) != SelectionKey.OP_WRITE) {
                        key.interestOps(key.interestOps()
                                | SelectionKey.OP_WRITE);
                    }
                } else {
                    System.out.println("read end");
                    //key.cancel();
                    key.interestOps(key.interestOps() ^ SelectionKey.OP_READ);
                }
            }
            if (key.isWritable()) {
                System.out.printf("write: %s%n", sc);
                ByteBuffer b;
                while (null != (b = bs.poll())) {
                    System.out.write(b.array(), 0, b.limit());
                }
                System.out.flush();
                System.out.println("write end");
                //key.cancel();
                key.interestOps(key.interestOps() ^ SelectionKey.OP_WRITE);
            }
        }
    }
}
