package GUi;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Pinball {
    //定义常量
    private final int TABLE_WIDTH = 800;
    private final int TABLE_HEIGHT = 800;

    private final int RACKET_WIDTH = 160;
    private final int RACKET_HEIGHT = 30;
    private final int RACKET_SPEED = 35;
    private int racketX = TABLE_WIDTH / 2;
    private int racketY = TABLE_HEIGHT - 100;

    private final int BALL_SIZE = 30;
    private int ballX = 40;
    private int ballY = 40;
    private int ballSpeedX = 2;
    private int ballSpeedY = 4;

    private boolean isGame = true;
    private final int WORD_SIZE = 50;

    //定义一个时间刷新器
    private Timer timer;
    //定义画布
    private class Mycavas extends JPanel {

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            if (isGame){
                //游戏中
                //绘制小球和球拍
                g.setColor(Color.GRAY);
                g.fillOval(ballX, ballY, BALL_SIZE, BALL_SIZE);

                g.setColor(Color.ORANGE);
                g.fillRect(racketX, racketY, RACKET_WIDTH, RACKET_HEIGHT);

            }else{
                //游戏结束
                g.setColor(Color.RED);
                g.setFont(new Font("in game", Font.BOLD, WORD_SIZE));
                g.drawString("辣鸡，输了吧", (TABLE_WIDTH - 4 * WORD_SIZE)/2 , (TABLE_HEIGHT - WORD_SIZE)/2);
            }
        }
    }

    Mycavas drawArea = new Mycavas();
    public void init(){
        //组装部件，完成操作逻辑
        Frame frame = new JFrame();
        KeyListener keylistener = new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_LEFT){
                    if (racketX > 0){
                        racketX -= RACKET_SPEED;
                    }
                }else if(keyCode == KeyEvent.VK_RIGHT){
                    if (racketX < TABLE_WIDTH - RACKET_WIDTH){
                        racketX += RACKET_SPEED;
                    }
                }
            }
        };

        frame.addKeyListener(keylistener);
        drawArea.addKeyListener(keylistener);

        ActionListener ballAction = new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                //每次绘图修正小球速度
                if (ballX <= 0 || ballX >= TABLE_WIDTH - BALL_SIZE){
                    ballSpeedX = -ballSpeedX;
                }
                if (ballY <= 0 || (ballY >= racketY - BALL_SIZE && ballX >= racketX && ballX <= racketX + RACKET_WIDTH)){
                    ballSpeedY = -ballSpeedY;
                }
                //小球超出界限游戏结束
                if (ballY >= racketY - BALL_SIZE * 2 && (ballX <= racketX - BALL_SIZE || ballX >= racketX + RACKET_WIDTH)){
                    timer.stop();
                    isGame = false;
                    drawArea.repaint();

                }

                ballX += ballSpeedX;
                ballY += ballSpeedY;
                drawArea.repaint();
            }
        };

        //添加关闭按钮
        frame.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                System.exit(0);
            }
        });
        timer = new Timer(10, ballAction);
        timer.start();
        drawArea.setPreferredSize(new Dimension(TABLE_WIDTH, TABLE_HEIGHT));
        frame.add(drawArea);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args){
        new Pinball().init();
    }
}
