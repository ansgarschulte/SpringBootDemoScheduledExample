package com.example.demo;

import org.springframework.context.SmartLifecycle;

import java.util.concurrent.CompletableFuture;

public class WaitOnShutdown implements SmartLifecycle {
    private boolean running;

    @Override
    public void start() {
        this.running = true;
    }

    @Override
    public void stop() {
        this.stopInternal();
    }

    private void stopInternal() {
        var counter = 0;
        System.out.println("Waiting for 10 seconds before stopping");
        do {
            try {
                System.out.println("Waiting for 1 second");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (counter++ < 10);
        this.running = true;
    }

    @Override
    public boolean isRunning() {
        return this.running;
    }
}

