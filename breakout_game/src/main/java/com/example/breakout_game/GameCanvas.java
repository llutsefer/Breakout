package com.example.breakout_game;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.Random;

import static com.example.breakout_game.GraphicsItem.canvasHeight;
import static com.example.breakout_game.GraphicsItem.canvasWidth;

public class GameCanvas extends Canvas {
    private GraphicsContext graphicsContext;
    private Paddle paddle;
    private Ball ball;
    private boolean gameRunning = false;
    static private Point2D [][] grid = new Point2D[20][10];
    private ArrayList<Brick> bricks = new ArrayList<>();

    private AnimationTimer animationTimer = new AnimationTimer() {
        private long lastUpdate;
        @Override
        public void handle(long now) {
            double diff = (now - lastUpdate) / 1_000_000_000.;
            lastUpdate = now;
            ball.updatePosition(diff);
            if(gameEndedInDefeat()){
                graphicsContext.clearRect(0, 0, getWidth(), getHeight());
            }else if (shouldBallBounceHorizontally()){
                ball.bounceHorizontally();
            } else if(shouldBallBounceVertically()) {
                ball.bounceVertically();
            }else if(shouldBallBounceFromPaddle()){
                double vectorOfBall = Math.toRadians(Math.abs(ball.getX() - paddle.getX())) ;
                ball.bounceFromPaddle(vectorOfBall);
            } else if (shouldBallBounceVerticallyFromBrick()) {
                ball.bounceVertically();
            } else if (shouldBallBounceHorizontallyFromBrick()) {
                ball.bounceHorizontally();
            }
            boolean mustWeChangeTheOldValues = true;
            for (Brick brick: bricks) {
                if(brick.mustCrash(ball.getExtremePoints()) != Brick.CrushType.NoCrush){
                        mustWeChangeTheOldValues = false;
                }
            }
            if(ball.getY()>=0+ball.getHeight() && ball.getX()>=0+ball.getWidth() && ball.getX()<=getWidth()-ball.getWidth() && mustWeChangeTheOldValues){
                ball.oldX = ball.getX();
                ball.oldY = ball.getY();
            }
            mustWeChangeTheOldValues = true;
            ball.setVelocity(ball.getVelocity()*1.00003);
            draw();
        }

        @Override
        public void start() {
            super.start();
            lastUpdate = System.nanoTime();
        }
    };

    public GameCanvas() {
        super(640, 700);

        this.setOnMouseMoved(mouseEvent -> {
            paddle.setPosition(mouseEvent.getX());
            if(!gameRunning)
                ball.setPosition(new Point2D(mouseEvent.getX(), paddle.getY() - ball.getWidth() / 2));
            draw();
        });

        this.setOnMouseClicked(mouseEvent -> {
            gameRunning = true;
            animationTimer.start();
        });
    }

    public static double getCanvasHeight() {
        return canvasHeight;
    }

    public static double getCanvasWidth() {
        return canvasWidth;
    }

    public void initialize() {
        graphicsContext = this.getGraphicsContext2D();
        GraphicsItem.setCanvasSize(getWidth(), getHeight());
        paddle = new Paddle();
        ball = new Ball();
        loadLevel();
    }

    public void draw() {
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillRect(0, 0, getWidth(), getHeight());
        paddle.draw(graphicsContext);
        ball.draw(graphicsContext);
        for (Brick brick:bricks) {
            brick.draw(graphicsContext);
        }
        if (gameEndedInDefeat()) {
            graphicsContext.setFill(Color.BLACK);
            graphicsContext.fillRect(0, 0, getWidth(), getHeight());
            graphicsContext.setFill(Color.RED);
            graphicsContext.setFont(Font.font("Arial", FontWeight.BOLD, 48));
            graphicsContext.fillText("Game Over", getWidth() / 2 - 100, getHeight() / 2);
        }
        if(gameEndedInVictory()){
            graphicsContext.setFill(Color.BLACK);
            graphicsContext.fillRect(0, 0, getWidth(), getHeight());
            graphicsContext.setFill(Color.WHITE);
            graphicsContext.setFont(Font.font("Arial", FontWeight.BOLD, 48));
            graphicsContext.fillText("You Won!", getWidth() / 2 - 100, getHeight() / 2);
        }
    }

    private void loadLevel(){
        setGrid();
        Color c;
        for(int i = 2;i<8;i++) {
            c = generateRandomColor();
            for(int j = 0;j<10;j++){
                bricks.add(new Brick(i, j, c));
            }
        }
    }

    private void setGrid(){
        double x;
        double y;
        double helpValueX = canvasWidth/10;
        double helpValueY = canvasHeight/20;
        for(int i = 0;i<20;i++){
            for (int j = 0;j<10;j++){
                grid[i][j] = new Point2D(helpValueX*j,helpValueY*i);
            }
        }
    }

    static public Point2D getBrickPosition(int i, int j){
        return grid[i][j];
    }

    private Color generateRandomColor(){
        Random rand = new Random();
        double red, green, blue;
        do {
            red = rand.nextDouble();
            green = rand.nextDouble();
            blue = rand.nextDouble();
        } while (red + green + blue < 0.5);
        Color color = new Color(red, green, blue, 1.0);
        return color;
    }

    private boolean shouldBallBounceHorizontally(){
        return ball.getX() <= 0 || ball.getX() >= getWidth() - ball.getWidth();
    }
    private boolean shouldBallBounceHorizontallyFromBrick() {
        for (Brick brick : bricks) {
            if (brick.mustCrash(ball.getExtremePoints()) == Brick.CrushType.HorizontalCrush) {
                brick.crush();
                bricks.remove(brick);
                return true;
            }
        }
        return false;
    }
    private boolean shouldBallBounceVertically(){
        return ball.getY() <= 0;
    }
    private boolean shouldBallBounceVerticallyFromBrick(){
        for (Brick brick:bricks) {
            if(brick.mustCrash(ball.getExtremePoints())== Brick.CrushType.VerticalCrush){
                brick.crush();
                bricks.remove(brick);
                return true;
            }
        }
        return false;
    }
    private boolean shouldBallBounceFromPaddle(){
        return ball.getY() >= paddle.getY() - ball.getHeight() && ball.getX() >= paddle.getX() && ball.getX() <= paddle.getX() + paddle.getWidth();
    }

    private boolean gameEndedInDefeat(){
        return ball.getY() >= getHeight() - ball.getHeight();
    }

    private boolean gameEndedInVictory(){
        return bricks.isEmpty();
    }
}