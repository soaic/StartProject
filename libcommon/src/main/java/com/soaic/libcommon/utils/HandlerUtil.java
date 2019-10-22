package com.soaic.libcommon.utils;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by xnie on 16/4/6.
 */
public class HandlerUtil {

    public static void postOnUiThread(Runnable action) {
        new Handler(Looper.getMainLooper()).post(action);
    }

    public static void postOnUiThread(Runnable action, long startedMillis, long minDuration) {
        long delay = minDuration - (System.currentTimeMillis() - startedMillis);
        if (delay <= 0) {
            postOnUiThread(action);
        } else {
            postOnUiThread(action, delay);
        }
    }

    public static void postOnUiThread(Runnable action, long delayMillis) {
        if (delayMillis > 0) {
            new Handler(Looper.getMainLooper()).postDelayed(action, delayMillis);
        } else {
            postOnUiThread(action);
        }
    }

    // runnable 不阻塞当前线程。
    public static void runOnUiThread(Runnable action) {
        Looper mainLooper = Looper.getMainLooper();
        if (Thread.currentThread() == mainLooper.getThread()) {
            // already UI thread, run it directly
            action.run();
        } else {
            // post the action on UI thread
            postOnUiThread(action);
        }
    }

    // runnable 阻塞当前线程。
    public static void runOnUiThreadSync(Runnable runnable) {
        Looper mainLooper = Looper.getMainLooper();
        if (Thread.currentThread() == mainLooper.getThread()) {
            runnable.run();
        } else {
            SyncRunnable sr = new SyncRunnable(runnable);
            new Handler(Looper.getMainLooper()).post(sr);
            sr.waitForComplete();
        }
    }

    private static final class SyncRunnable implements Runnable {
        private final Runnable mTarget;
        private boolean mComplete;

        public SyncRunnable(Runnable target) {
            mTarget = target;
        }

        public void run() {
            mTarget.run();
            synchronized (this) {
                mComplete = true;
                notifyAll();
            }
        }

        public void waitForComplete() {
            synchronized (this) {
                while (!mComplete) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }
}
