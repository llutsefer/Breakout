package com.example.breakout_game;


import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Ball extends GraphicsItem {
    private Point2D moveVector = new Point2D(1, -1).normalize();

    private double velocity = 300;
    protected double oldX;
    protected double oldY;

    public Ball() {
        oldX = 0;
        oldY = 0;
        x = -100;
        y = -100;
        width = height = canvasHeight * .015;
    }

    @Override
    public void draw(GraphicsContext graphicsContext) {
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillOval(x, y, width, height);
    }

    public void setPosition(Point2D point) {
        this.x = point.getX() - width/2;
        this.y = point.getY() - height/2;
    }

    public void updatePosition(double diff) {
        x += moveVector.getX() * velocity * diff;
        y += moveVector.getY() * velocity * diff;
    }
    protected void setMoveVector(Point2D moveVector) {
        this.moveVector = moveVector;
    }
    public Point2D getMoveVector() {
        return moveVector;
    }

    public void bounceHorizontally() {
        x = oldX;
        y = oldY;
        moveVector = new Point2D(moveVector.getX()*-1, moveVector.getY());
    }

    public void bounceVertically(){
        x = oldX;
        y = oldY;
        moveVector = new Point2D(moveVector.getX(), moveVector.getY()*-1);
    }
    public void bounceFromPaddle(double v){
        x = oldX;
        y = oldY;
        v = Math.max(v, Math.toRadians(35));
        moveVector = new Point2D(Math.cos(v), -Math.sin(v)).normalize();
    }

    public ArrayList<Point2D> getExtremePoints(){
        double radius = width/2;
        ArrayList<Point2D> extremePoints = new ArrayList<>();
        extremePoints.add(new Point2D(x-radius, y+radius));
        extremePoints.add(new Point2D(x+radius, y+radius));
        extremePoints.add(new Point2D(x+radius, y-radius));
        extremePoints.add(new Point2D(x-radius, y-radius));
        return  extremePoints;
    }
    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

}
