package sk.hlavco.hladacBytov.resources;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class Run {

    public static void main(String[] args) {

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                try {
                    Process proc = Runtime.getRuntime().exec("java -jar HladacBytov.jar");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        timer.schedule(task, 1000,180000);
    }
}
