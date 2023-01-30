package com.evolven.common;

public class Spinner implements Runnable {
    private volatile boolean showProgress = false;
    private String title = "Processing";
    private long delay = 100;
    public  Spinner(long delay, String title) {
        this.title = title;
        this.delay = delay;
    }
    public  Spinner() {}
    public  Spinner(long delay) {
        this.delay = delay;
    }

    public void start() {
        if (showProgress) return;
        showProgress = true;
        new Thread(this).start();
    }
    public void run() {
        String anim= "|/-\\";
        int x = 0;
        while (showProgress) {
            try {
                System.out.print("\r " + title + " " + anim.charAt(x++ % anim.length()));
                Thread.sleep(100);
            }
            catch (Exception e) {
                stop();
                return;
            };
        }
    }
    public void stop() {
        showProgress = false;
        System.out.print(StringUtils.repeat('\r', title.length() + 1 ));
    }

}
