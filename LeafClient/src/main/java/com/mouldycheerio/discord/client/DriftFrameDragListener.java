package com.mouldycheerio.discord.client;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;

public class DriftFrameDragListener extends MouseAdapter {

    private final JFrame frame;
    private Point mouseDownCompCoords = null;
    private double xv = 0;
    private double yv = 0;
    private Point centreLocation;

    public DriftFrameDragListener(JFrame frame) {
        this.frame = frame;
        centreLocation = frame.getLocation();
        makeLoop();
    }

    public void makeLoop() {
        Thread thread = new Thread(new Runnable() {

            public void run() {
                long alpha = 0;
                while (true) {
                    alpha++;
                    frame.setLocation((int) centreLocation.getX(), (int)(centreLocation.getY() + Math.sin(alpha/40.0) * 20));
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }) {
        };
        thread.start();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseDownCompCoords = null;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseDownCompCoords = e.getPoint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point currCoords = e.getLocationOnScreen();
        frame.setLocation(currCoords.x - mouseDownCompCoords.x, currCoords.y - mouseDownCompCoords.y);
        centreLocation = frame.getLocation();
    }
}
