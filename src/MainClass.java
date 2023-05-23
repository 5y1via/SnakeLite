import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.Timer;

import acm.graphics.GLabel;
import acm.graphics.GOval;
import acm.graphics.GPoint;
import acm.graphics.GRect;
import acm.program.GraphicsProgram;

public class MainClass extends GraphicsProgram implements ActionListener
{
    public GOval ball;

    private ArrayList<GRect> snakeBody1;
    private ArrayList<GRect> snakeBody2;

    private int snake1X, snake1Y, snake2X, snake2Y, snakeWidth, snakeHeight;

    public Timer timer = new Timer(200, this);
    // every 200ms, action performed will be called

    private boolean isSnake1Dead = false;
    private boolean isSnake2Dead = false;
    private int score1, score2;
    Scoreboard scoreLabel = new Scoreboard("SCORE \n PINK: " + score1 + "\n BLUE: " + score2, 10, 15);
    private GLabel instructions;

    /* these boolean values:
     * help with key control and snake movement
     * */
    boolean blockKey = false;
    boolean going1Up = false;
    boolean going1Down = false;
    boolean going1Left = false;
    boolean going1Right = false;

    boolean going2Up = false;
    boolean going2Down = false;
    boolean going2Left = false;
    boolean going2Right = false;

    public void run()
    {
        addKeyListeners();
        setBackground(Color.DARK_GRAY);
        setUpInstructions();
    }

    public void setUpInstructions(){
        instructions = new GLabel("Press space to start game.", 320, 240);
        instructions.setColor(Color.white);
        add(instructions);

        score1 = 0;
        score2 = 0;

        scoreLabel.setColor(Color.white);
        add(scoreLabel);
    }
    public void removeInstructions(){
        remove(instructions);
        startGame();
    }

    public void startGame(){
        // create two snakes
        snakeBody1 = new ArrayList<>();
        snake1X = 470;
        snake1Y = 250;
        snakeWidth = 10;
        snakeHeight = 10;
        for (int i = 0; i<3; i++){
            SnakePart part = new SnakePart(snake1X, snake1Y + i * 10, snakeWidth, snakeHeight);
            part.setColor(Color.black);
            part.setFillColor(Color.magenta);
            part.setFilled(true);
            snakeBody1.add(part);
            add(part);
        }

        snakeBody2 = new ArrayList<>();
        snake2X = 250;
        snake2Y = 250;
        for (int i = 0; i<3; i++){
            SnakePart part = new SnakePart(snake2X, snake2Y + i * 10, snakeWidth, snakeHeight);
            part.setColor(Color.black);
            part.setFillColor(Color.cyan);
            part.setFilled(true);
            snakeBody2.add(part);
            add(part);
        }

        // add food
        randomFood();

        // start game & timer
        going1Up = true;
        going2Up = true;
        timer.start();
    }

    public void randomFood(){
        ball = new GOval(10,10);
        ball.setFillColor(Color.PINK);
        ball.setFilled(true);
        double x = Math.random() * getGCanvas().getWidth();
        double y = Math.random() * getGCanvas().getHeight();
        ball.setLocation(x,y);
        add(ball);
        System.out.println("random food");
    }

    public void removeFood(){
        remove(ball);
        System.out.println("remove food");
    }

    public void keyPressed(KeyEvent keyPressed) // listens for user input
    {
        blockKey = true;
    }

    public void keyReleased(KeyEvent e){ // reacts to user input
        if (blockKey){
            blockKey = false;
            switch (e.getKeyCode()) {
                case KeyEvent.VK_SPACE:
                    removeInstructions();
                    break;
                case KeyEvent.VK_UP:
                    going1Up = true;
                    going1Down = false;
                    going1Left = false;
                    going1Right = false;
                    break;
                case KeyEvent.VK_DOWN:
                    going1Up = false;
                    going1Down = true;
                    going1Left = false;
                    going1Right = false;
                    break;
                case KeyEvent.VK_LEFT:
                    going1Up = false;
                    going1Down = false;
                    going1Left = true;
                    going1Right = false;
                    break;
                case KeyEvent.VK_RIGHT:
                    going1Up = false;
                    going1Down = false;
                    going1Left = false;
                    going1Right = true;
                    break;
                case KeyEvent.VK_W:
                    going2Up = true;
                    going2Down = false;
                    going2Left = false;
                    going2Right = false;
                    break;
                case KeyEvent.VK_S:
                    going2Up = false;
                    going2Down = true;
                    going2Left = false;
                    going2Right = false;
                    break;
                case KeyEvent.VK_A:
                    going2Up = false;
                    going2Down = false;
                    going2Left = true;
                    going2Right = false;
                    break;
                case KeyEvent.VK_D:
                    going2Up = false;
                    going2Down = false;
                    going2Left = false;
                    going2Right = true;
                    break;
            }
        }
    }
    @Override
    public void actionPerformed(ActionEvent arg0) {
        // snake#1
        if (!(isSnake1Dead)){
            // all the times something happens
            if (hitsBall(snakeBody1)) {
                removeFood();
                randomFood();
                growSnake(snakeBody1);
                score1++;
                updateScoreboard();
            }
            if (hitsWall(snakeBody1) || hitsEitherSnake(snakeBody1, snakeBody2)){
                System.out.println("SNAKE#1 DIES");
                isSnake1Dead = true;
                snakeDies(snakeBody1);
            }

            // all the times nothing happens (snake moves)
            if (going1Up){
                moveUp(snakeBody1);
                System.out.println("1 moving Up)");
            }
            else if (going1Down){
                moveDown(snakeBody1);
                System.out.println("1 moving Down)");
            }
            else if (going1Left){
                moveLeft(snakeBody1);
                System.out.println("1 moving Left)");
            }
            else if (going1Right){
                moveRight(snakeBody1);
                System.out.println("1 moving Right)");
            }
            redrawSnake(snakeBody1); // redraw
        }

        // snake#2
        if (!(isSnake2Dead)){
            // all the times something happens
            if (hitsBall(snakeBody2)) {
                removeFood();
                randomFood();
                growSnake(snakeBody2);
                score2++;
                updateScoreboard();
            }
            if (hitsWall(snakeBody2) || hitsEitherSnake(snakeBody2, snakeBody1)){
                System.out.println("SNAKE#2 DIES");
                isSnake2Dead = true;
                snakeDies(snakeBody2);
            }

            // all the times nothing happens (snake moves)
            if (going2Up){
                moveUp(snakeBody2);
                System.out.println("2 moving Up)");
            }
            else if (going2Down){
                moveDown(snakeBody2);
                System.out.println("2 moving Down)");
            }
            else if (going2Left){
                moveLeft(snakeBody2);
                System.out.println("2 moving Left)");
            }
            else if (going2Right){
                moveRight(snakeBody2);
                System.out.println("2 moving Right)");
            }
            redrawSnake(snakeBody2); // redraw
        }
        // otherwise:
        if (isSnake1Dead && isSnake2Dead){
            gameOver();
        }
    }

    private void updateScoreboard(){
        remove(scoreLabel);
        scoreLabel = new Scoreboard("SCORE \n PINK: " + score1 + "\n BLUE: " + score2, 10, 15);
        scoreLabel.setColor(Color.white);
        add(scoreLabel);

    }
    private void redrawSnake(ArrayList<GRect> snakeBody)
    {
        for (int i = snakeBody.size()-1; i>=1; i--){
            int prevX = (int) snakeBody.get(i-1).getX();
            int prevY = (int) snakeBody.get(i-1).getY();

            snakeBody.get(i).setLocation(prevX, prevY);
            add(snakeBody.get(i));
        }

        System.out.println("X is " + snakeBody.get(1).getX());
        System.out.println("Y is " + snakeBody.get(1).getY());

        /*ArrayList<GRect> copy = new ArrayList<>(snakeBody);
        for (int i = 1; i < snakeBody.size(); i++){
            GRect part = copy.get(i);
            GRect changePart = copy.get(i - 1);
            snakeBody.get(i).setLocation(changePart.getX(), changePart.getY());
            double xDistance = changePart.getX() - part.getX();
            double yDistance = changePart.getY() - part.getY();
            snakeBody.get(i).move(xDistance, yDistance);

            /*snakeBody.get(i).setLocation(copy.get(i-1).getX(), copy.get(i-1).getY());
            snakeBody.get(i).move(copy.get(i-1).getX()-copy.get(i).getX(), copy.get(i-1).getY()-copy.get(i).getY());
        }*/
    }

    private void growSnake(ArrayList<GRect> snakeBody)
    {
        GRect part = new GRect(snakeBody.get(snakeBody.size()-1).getX(), snakeBody.get(snakeBody.size()-1).getY() + 1, 10, 10);
        part.setColor(Color.black);
        if (snakeBody == snakeBody1){
            part.setFillColor(Color.magenta);
        }
        else {
            part.setFillColor(Color.cyan);
        }
        part.setFilled(true);
        snakeBody.add(part);
        add(part);
    }

    private void moveUp(ArrayList<GRect> snakeBody)
    {
        snakeBody.get(0).move(0, -10);
        snakeBody.get(0).setLocation(snakeBody.get(0).getX(), snakeBody.get(0).getY() - 10);
    }
    private void moveDown(ArrayList<GRect> snakeBody)
    {
        snakeBody.get(0).move(0, 10);
        snakeBody.get(0).setLocation(snakeBody.get(0).getX(), snakeBody.get(0).getY() + 10);
    }
    private void moveLeft(ArrayList<GRect> snakeBody)
    {
        snakeBody.get(0).move(-10, 0);
        snakeBody.get(0).setLocation(snakeBody.get(0).getX()-10, snakeBody.get(0).getY());
    }
    private void moveRight(ArrayList<GRect> snakeBody)
    {
        snakeBody.get(0).move(10, 0);
        snakeBody.get(0).setLocation(snakeBody.get(0).getX() + 10, snakeBody.get(0).getY());
    }

    public boolean hitsBall(ArrayList<GRect> snakeBody){
        if (snakeBody.get(0).getBounds().intersects(ball.getBounds())){
            System.out.println("HITS ball");
            return true;
        }
        else {
            System.out.println("misses ball");
            return false;
        }
    }

    public boolean hitsEitherSnake(ArrayList<GRect> snakeBody, ArrayList<GRect> otherSnakeBody){
        boolean check = false;
        for (int i = 1; i < otherSnakeBody.size(); i++){
            if (snakeBody.get(0).getLocation().equals(otherSnakeBody.get(i).getLocation())){
                check = true;
                break;
            }
        }
        if (check){
            System.out.println("HITS eitherSnake");
        }
        else{
            System.out.println("misses eitherSnake");
        }
        return check;
    }

    public boolean hitsWall(ArrayList<GRect> snakeBody){
        if (snakeBody.get(0).getX() < 0 || snakeBody.get(0).getX() > getGCanvas().getWidth() || snakeBody.get(0).getY() < 0 || snakeBody.get(0).getY() > getGCanvas().getHeight()){
            System.out.println("HITS wall");
            return true;
        }
        else{
            System.out.println("misses wall");
            return false;
        }
    }

    public void snakeDies(ArrayList<GRect> snakeBody){
        System.out.println(snakeBody + " is dead.");
        if (snakeBody == snakeBody1){
            going1Up = false;
            going1Down = false;
            going1Left = false;
            going1Right = false;
        }
        else {
            going2Up = false;
            going2Down = false;
            going2Left = false;
            going2Right = false;
        }
    }

    public void gameOver(){
        System.out.println("Game over.");

        GRect scoreboard = new GRect(160, getGCanvas().getHeight()/2 - 50, 400, 100);
        scoreboard.setColor(Color.black);
        scoreboard.setFillColor(Color.gray);
        scoreboard.setFilled(true);
        add(scoreboard);

        GLabel scoreResults = new GLabel("Final Score: " + "\nPINK: " + score1+ "\n BLUE: " + score2, 250, getGCanvas().getHeight()/2);
        scoreResults.setColor(Color.white);
        add(scoreResults);

        String text = "";
        if (score1>score2){
            text = "PINK WINS";
        }
        else if (score2>score1){
            text = "BLUE WINS";
        }
        else {
            text = "DRAW";
        }
        GLabel results = new GLabel(text, getGCanvas().getWidth()/2 - 20, getGCanvas().getHeight()/2 + 20);
        results.setColor(Color.white);
        add(results);
    }

    public static void main(String[] args)
    {
        new MainClass().start();
    }
}