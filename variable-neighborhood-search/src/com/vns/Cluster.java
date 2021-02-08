package com.vns;

public class Cluster {
    private int ux;
    private int uy;
    private int dx;
    private int dy;

    public Cluster(int ux, int uy, int dx, int dy) {
        this.ux = ux;
        this.uy = uy;
        this.dx = dx;
        this.dy = dy;
    }

    public int getUx() {
        return ux;
    }

    public void setUx(int ux) {
        this.ux = ux;
    }

    public int getUy() {
        return uy;
    }

    public void setUy(int uy) {
        this.uy = uy;
    }

    public int getDx() {
        return dx;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public int getDy() {
        return dy;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }
}
