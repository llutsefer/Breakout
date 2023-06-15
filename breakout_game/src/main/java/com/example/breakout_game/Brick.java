package com.example.breakout_game;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Brick extends GraphicsItem{

    public enum CrushType {
        NoCrush,
        HorizontalCrush,
        VerticalCrush
    }

    private int x;
    private int y;
    private Color color;

    public Brick(int x, int y, Color color) {
            this.x = x;
            this.y = y;
            this.color = color;
        width = canvasWidth / 10;
        height = canvasHeight / 20;
    }

    @Override
    public void draw(GraphicsContext graphicsContext) {
        graphicsContext.setFill(color);
        graphicsContext.fillRect(GameCanvas.getBrickPosition(x, y).getX(), GameCanvas.getBrickPosition(x, y).getY(), canvasHeight * .08, canvasHeight * .04);
    }


    public CrushType mustCrush(ArrayList<Point2D> ballPositions) {
        if ( (GameCanvas.getBrickPosition(x, y).getX() - width <= ballPositions.get(0).getX())
                && (GameCanvas.getBrickPosition(x, y).getX() + width  >=  ballPositions.get(0).getX())
                && ((int) ballPositions.get(0).getY() == GameCanvas.getBrickPosition(x, y).getY() + height)) {
            return CrushType.VerticalCrush;
        } else if ( (GameCanvas.getBrickPosition(x, y).getX() <=  ballPositions.get(1).getX())
                && (GameCanvas.getBrickPosition(x, y).getX() + width >=  ballPositions.get(1).getX())
                && ((int) ballPositions.get(1).getY() == GameCanvas.getBrickPosition(x, y).getY() + height)) {
            return CrushType.VerticalCrush;
        } else if (( ballPositions.get(2).getX() >= GameCanvas.getBrickPosition(x, y).getX() )
                && ( ballPositions.get(2).getX() <= GameCanvas.getBrickPosition(x, y).getX() + width)
                && ((int) ballPositions.get(2).getY() == GameCanvas.getBrickPosition(x, y).getY() )) {
            return CrushType.VerticalCrush;
        }else if (( ballPositions.get(3).getX() >= GameCanvas.getBrickPosition(x, y).getX() )
                && ( ballPositions.get(3).getX() <= GameCanvas.getBrickPosition(x, y).getX() + width )
                && ((int) ballPositions.get(3).getY() == GameCanvas.getBrickPosition(x, y).getY() )) {
            return CrushType.VerticalCrush;
        }
        else if (((int) ballPositions.get(0).getX() == GameCanvas.getBrickPosition(x, y).getX() + width)
                &&  GameCanvas.getBrickPosition(x, y).getY() + height >= (int) ballPositions.get(3).getY()
                &&  GameCanvas.getBrickPosition(x, y).getY() <= (int) ballPositions.get(0).getY()) {
            return CrushType.HorizontalCrush;
        } else if ( ((int) ballPositions.get(1).getX() == GameCanvas.getBrickPosition(x, y).getX())
                &&  GameCanvas.getBrickPosition(x, y).getY() + height >= (int) ballPositions.get(3).getY()
                &&  GameCanvas.getBrickPosition(x, y).getY()  <= (int) ballPositions.get(0).getY()) {
            return CrushType.HorizontalCrush;
        }
        return CrushType.NoCrush;
    }

    public void crush() {
        color = Color.TRANSPARENT;
    }
}
