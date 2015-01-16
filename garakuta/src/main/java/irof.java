import java.awt.BorderLayout;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class irof implements Runnable, Cloneable {

    private static final Random 気まぐれ = new Random();

    private static final ScheduledExecutorService 増やすよ = Executors
        .newSingleThreadScheduledExecutor();

    private static irof マスターいろふ;

    private static byte[] アイコンのデータ;

    //EDTからしかアクセスしない
    private static final ArrayList<JFrame> いろふ族 = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        アイコンのデータ = decodeBase64(BASE64でエンコードしたアイコンのデータ);

        マスターいろふ = new irof();
        マスターいろふ.動くろふ();
    }

    @Override
    public void run() {
        if (増やすよ.isShutdown()) {
            return;
        }

        if (SwingUtilities.isEventDispatchThread()) {
            表示しろふ();
            増えろふ();

        } else if (Thread.currentThread().isInterrupted() == false) {
            動くろふ();
        }
    }

    //main、増殖スケジュール
    private void 動くろふ() {
        SwingUtilities.invokeLater(this);
    }

    //EDT
    private void 増えろふ() {
        irof 新しいろふ = clone();
        long delay = 気まぐれ.nextInt(3);
        TimeUnit unit = TimeUnit.SECONDS;
        増やすよ.schedule(新しいろふ, delay, unit);
    }

    //EDT
    private void 表示しろふ() {
        JLabel ラベろふ = new JLabel(new ImageIcon(アイコンのデータ));
        final JFrame いろフレーム = new JFrame();
        if (this != マスターいろふ) {
            いろふ族.add(いろフレーム);
        }
        いろフレーム.add(ラベろふ, BorderLayout.CENTER);
        いろフレーム.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        いろフレーム.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosed(WindowEvent e) {
                if (irof.this == マスターいろふ) {
                    終わろふ();
                } else {
                    いろふ族.remove(いろフレーム);
                }
            }
        });
        いろフレーム.pack();
        Rectangle ディスプレイのサイズ的な何か =
            GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration()
                .getBounds();
        int x = 気まぐれ.nextInt(ディスプレイのサイズ的な何か.width);
        int y = 気まぐれ.nextInt(ディスプレイのサイズ的な何か.height);
        いろフレーム.setLocation(x, y);
        いろフレーム.setVisible(true);
    }

    @Override
    protected irof clone() {
        try {
            return (irof) super.clone();

        } catch (CloneNotSupportedException e) {
            //例外が起きたらおかしい
            throw new Error(e);
        }
    }

    //EDT
    protected void 終わろふ() {
        for (JFrame いろフレーム : いろふ族) {
            いろフレーム.dispose();
        }
        いろふ族.clear();
        増やすよ.shutdownNow();
    }

    private static byte[] decodeBase64(String text) {
        char[] cs = text.toCharArray();
        int q = cs.length / 4;
        byte[] bs = new byte[q * 3];
        int index = 0;
        for (int i = 0; i < q; i++) {
            int a =
                indexOf(cs[i * 4]) << 18
                    | indexOf(cs[i * 4 + 1]) << 12
                    | indexOf(cs[i * 4 + 2]) << 6
                    | indexOf(cs[i * 4 + 3]);
            bs[index++] = (byte) (a >> 16);
            bs[index++] = (byte) (a >> 8);
            bs[index++] = (byte) a;
        }
        while (bs[--index] == 0) {
        }
        return Arrays.copyOf(bs, index + 1);
    }

    private static int indexOf(char c) {
        if (c == '+') {
            return 62;
        } else if (c == '/') {
            return 63;
        } else if ('A' <= c && c <= 'Z') {
            return c - 65;
        } else if ('a' <= c && c <= 'z') {
            return c - 71;
        } else if ('0' <= c && c <= '9') {
            return c + 4;
        }
        return 0;
    }

    private static final String BASE64でエンコードしたアイコンのデータ =
        "R0lGODlhkgCSAPcAAAAAAIAAAACAAICAAAAAgIAAgACAgICAgMDAwP8AAAD/AP//AAAA//8A/wD//////wAAAAAAAAAAAAAAAAAA"
            + "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAMwAAZgAAmQAAzAAA"
            + "/wAzAAAzMwAzZgAzmQAzzAAz/wBmAABmMwBmZgBmmQBmzABm/wCZAACZMwCZZgCZmQCZzACZ/wDMAADMMwDMZgDMmQDMzADM/wD/"
            + "AAD/MwD/ZgD/mQD/zAD//zMAADMAMzMAZjMAmTMAzDMA/zMzADMzMzMzZjMzmTMzzDMz/zNmADNmMzNmZjNmmTNmzDNm/zOZADOZ"
            + "MzOZZjOZmTOZzDOZ/zPMADPMMzPMZjPMmTPMzDPM/zP/ADP/MzP/ZjP/mTP/zDP//2YAAGYAM2YAZmYAmWYAzGYA/2YzAGYzM2Yz"
            + "ZmYzmWYzzGYz/2ZmAGZmM2ZmZmZmmWZmzGZm/2aZAGaZM2aZZmaZmWaZzGaZ/2bMAGbMM2bMZmbMmWbMzGbM/2b/AGb/M2b/Zmb/"
            + "mWb/zGb//5kAAJkAM5kAZpkAmZkAzJkA/5kzAJkzM5kzZpkzmZkzzJkz/5lmAJlmM5lmZplmmZlmzJlm/5mZAJmZM5mZZpmZmZmZ"
            + "zJmZ/5nMAJnMM5nMZpnMmZnMzJnM/5n/AJn/M5n/Zpn/mZn/zJn//8wAAMwAM8wAZswAmcwAzMwA/8wzAMwzM8wzZswzmcwzzMwz"
            + "/8xmAMxmM8xmZsxmmcxmzMxm/8yZAMyZM8yZZsyZmcyZzMyZ/8zMAMzMM8zMZszMmczMzMzM/8z/AMz/M8z/Zsz/mcz/zMz///8A"
            + "AP8AM/8AZv8Amf8AzP8A//8zAP8zM/8zZv8zmf8zzP8z//9mAP9mM/9mZv9mmf9mzP9m//+ZAP+ZM/+ZZv+Zmf+ZzP+Z///MAP/M"
            + "M//MZv/Mmf/MzP/M////AP//M///Zv//mf//zP///ywAAAAAkgCSAAAI/wAfCBxIsKDBgwgTKlzIsKHDhxAjSpxIsaLFixgzatzI"
            + "saPHjyBDihxJsqTJkyhTqlzJsmVDADBjypxJs2ZNlzgf2tzJs6fPnCx9Ch1KlCZQkUWTKiV6lGNSjUtvNsUoNKXSqRV3YhXYcytE"
            + "m14J8gzLUCpZsWDPGkyrtiDbtmbbro17dqbchXTJ2r2r0ChcmXzL7q0bM/BLwGoR44V5d3BYxQkd64W8VfJcy24ZHwagU3PfwpNB"
            + "R/aLEHNm0Z9RH6SMlfVl11xJr5Y923Rsz15hn8b9WvVu37957yYsvHRxtLYf5A3O2TjwpropJr99/Hfq6kCjS8eOvHPz0c+Pav83"
            + "PHA6zvHkabcOT748epfmDcc/zz79fPj15d/H/7198Mb5AfheTvvlph5x3CE4YHZaObQcSGPp11VvD6Jl0YT+RdWgc7Bp6F9tGvbH"
            + "XIgHfjiicLqRiKGJUIWnYoUsXugiYj/FaNWMAdqoUooL6mgSjzkSWBV0HRaIUohCPldiSy8uiZRpRn7U5JAllejkSFM+VWWRPVKV"
            + "ZVQ/cplgiySCx9SWrl3p5VXbrYilemq2OZSUMHr0lnJRXkclhHlmtWGcm+2J5o4R1ilYjT46yKZEgibq3ZmPRuionIgGeuekMrpp"
            + "5qWYZrThYpx2Sl+oovJnaKk3norqkaquehKprq7/BGusqfZJa0ez3homoLq+aWuvZHYJLK6tDkvnr8ZmKmyynvLKLLHLPntRsSQh"
            + "q+CY1UqaaK5PVqrjp6zOuS24aEL6Lbm+DsZtYOiGtBy1f8FLqYj/OdoutAk6K6C+E6HHr1za4ksvhwPbGPBG8/0L8L3KFkyhqAev"
            + "ia3CfDHc73vWxksxwYcGyaLFjOaX8b4j4wmkx+du/DBzw65raaO6ykuwpsDKDGLELat8Ira02gyetHryDDTCOg8dkc9Gh1xy0qBG"
            + "y/TRRT/dMcpSX7x01RQ6jHWzV2/tntNeNy102JGOTbbJ5p69s5Zqf0l1zm6/jWqWaiO3VN1Be4v3zC7vNE2d1n6XHbjEgA8+deGG"
            + "65m4n3IvHvXej9cdueRdh4104JO3XbnXl/vdOeSZk/053qPjFBAAOw==";
}