/*
 * PONG GAME REQUIREMENTS
 * This simple "tennis like" game features two paddles and a ball, 
 * the goal is to defeat your opponent by being the first one to gain 3 point,
 *  a player gets a point once the opponent misses a ball. 
 *  The game can be played with two human players, one on the left and one on 
 *  the right. They use keyboard to start/restart game and control the paddles. 
 *  The ball and two paddles should be red and separating lines should be green. 
 *  Players score should be blue and background should be black.
 *  Keyboard requirements:
 *  + P key: start
 *  + Space key: restart
 *  + W/S key: move paddle up/down
 *  + Up/Down key: move paddle up/down
 *  
 *  Version: 0.5
 */
package vn.vanlanguni.ponggame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import dialogAndMouseDemo.CanvasPanel;
import dialogAndMouseDemo.MyDialogResult;
import dialogAndMouseDemo.SecondWindow;
import dialogAndMouseDemo.Settings;

/**
 * 
 * @author Invisible Man
 *
 */
public class PongPanel extends JPanel implements ActionListener, KeyListener, MouseMotionListener , MouseListener{
	private static final long serialVersionUID = -1097341635155021546L;

	private boolean showTitleScreen = true;
	private boolean playing;
	private boolean gameOver;

	
	boolean setting;
	

	private volatile boolean isPaused = false;
	//button play
	Point  pPlay , pSetting,pBack,pMenu,pSa;
	ImageIcon imgbtnPlay,imgbtnSetting,imgbtnBack,imgbgP,imgbtnMenu,imgbtnSa;
	int  rPlay,rSetting,rBack,rMenu,rSa;
	String nameP,nameS,nameB,namePlayer1,namePlayer2;
	boolean intersec, intersec1,intersec2,intersec3,intersec4 ;

	/** Background. */


	//private Color backgroundColor = Color.black;
	ImageIcon imgbpong ;
	
	/** State on the control keys. */
	private boolean upPressed;
	private boolean downPressed;
	private boolean wPressed;
	private boolean sPressed;

	/** The ball: position, diameter */
	Image background;
	private int ballX = 200;
	private int ballY = 200;
	private int diameter = 20;
	private int ballDeltaX = -1;
	private int ballDeltaY = 3;

	/** Player 1's paddle: position and size */
	ImageIcon imgpad1;
	ImageIcon ball1,ball2,ball3;
	private int playerOneX = 0;
	private int playerOneY = 250;
	private int playerOneWidth = 30;
	private int playerOneHeight = 80;

	/** Player 2's paddle: position and size */
	ImageIcon imgpad2;
	private int playerTwoX = 465;
	private int playerTwoY = 250;
	private int playerTwoWidth = 30;
	private int playerTwoHeight = 80;

	/** Speed of the paddle - How fast the paddle move. */
	private int paddleSpeed = 5;

	/** Player score, show on upper left and right. */
	private int playerOneScore;
	private int playerTwoScore;

	/** Construct a PongPanel. */
	public PongPanel() {
		//setBackground(backgroundColor);
		namePlayer1 = "Player 1 ";
		namePlayer2 = "Player 2 ";
		pBack = new Point(25,445);
		pPlay = new Point(222, 300);
		pSetting = new Point(25,445);
		pMenu = new Point(180,280);
		pSa = new Point(180,400);
		rMenu = 40 ;
		rSa = 35;
		rSetting = 20;
		rBack = 15;
		rPlay = 40;
		
		nameP = "image/play.png";
		nameS = "image/SettingI.png";
		nameB = "image/back.png";
		imgpad1 = new ImageIcon("paddlesimage/paddles1.PNG");
		imgpad2 = new ImageIcon("paddlesimage/paddles2.PNG");

		ball1 = new ImageIcon("image/unnamed.png");
		ball2 = new ImageIcon("image/basketball.png");
		ball3 = new ImageIcon("image/Tennis.png");

		//imgbpong = new ImageIcon("paddlesimage/Title.png");

		// listen to key presses
		setFocusable(true);
		addKeyListener(this);
		addMouseMotionListener(this);
		
		addMouseListener(this);
		//addMouseMotionListener(this);
		//addMouseListener(this);
		// call step() 60 fps
		Timer timer = new Timer(800 / 60, this);
		timer.start();
	}

	/** Implement actionPerformed */
	public void actionPerformed(ActionEvent e) {
		step();
	}

	/** Repeated task */
	public void step() {

		if (playing) {
			
			/* Playing mode */

			// move player 1
			// Move up if after moving, paddle is not outside the screen
			if (wPressed && playerOneY > 0) {
				playerOneY -= paddleSpeed;
			}
			// Move down if after moving paddle is not outside the screen
			if (sPressed && playerOneY + playerOneHeight < getHeight()) {
				playerOneY += paddleSpeed;
			}

			// move player 2
			// Move up if after moving paddle is not outside the screen
			if (upPressed && playerTwoY > 0) {
				playerTwoY -= paddleSpeed;
			}
			// Move down if after moving paddle is not outside the screen
			if (downPressed && playerTwoY + playerTwoHeight < getHeight()) {
				playerTwoY += paddleSpeed;
			}

			/*
			 * where will the ball be after it moves? calculate 4 corners: Left,
			 * Right, Top, Bottom of the ball used to determine whether the ball
			 * was out yet
			 */
			int nextBallLeft = ballX + ballDeltaX;
			int nextBallRight = ballX + diameter + ballDeltaX;
			// FIXME Something not quite right here
			int nextBallTop = ballY + ballDeltaY;
			int nextBallBottom = ballY + diameter + ballDeltaY;

			// Player 1's paddle position
			int playerOneRight = playerOneX + playerOneWidth;
			int playerOneTop = playerOneY;
			int playerOneBottom = playerOneY + playerOneHeight;

			// Player 2's paddle position
			int playerTwoLeft = playerTwoX;
			int playerTwoTop = playerTwoY;
			int playerTwoBottom = playerTwoY + playerTwoHeight;

			// ball bounces off top and bottom of screen
			if (nextBallTop < 0 || nextBallBottom > getHeight()) {
				Sound.play("Sound/soundl.wav");
				ballDeltaY *= -1;
			}

			// will the ball go off the left side?
			if (nextBallLeft < playerOneRight-13) {
				// is it going to miss the paddle?
				if (nextBallTop > playerOneBottom || nextBallBottom < playerOneTop) {
					Sound.play("Sound/soundtb.wav");
					playerTwoScore++;

					// Player 2 Win, restart the game
					if (playerTwoScore == 3) {
						playing = false;
						gameOver = true;
						Sound.play("Sound/win.wav");
					}
					ballX = 250;
					ballY = 250;
					ballDeltaX *= -1;
				} else {
					// If the ball hitting the paddle, it will bounce back
					// FIXME Something wrong here
					Sound.play("Sound/soundpaddles.wav");
					ballDeltaX *= -1;
				}
			}

			// will the ball go off the right side?
			if (nextBallRight > playerTwoLeft+13) {
				// is it going to miss the paddle?
				if (nextBallTop > playerTwoBottom || nextBallBottom < playerTwoTop) {
					Sound.play("Sound/soundtb.wav");
					playerOneScore++;

					// Player 1 Win, restart the game
					if (playerOneScore == 3) {
						
						playing = false;
						gameOver = true;
						Sound.play("Sound/win.wav");
					}
					ballX = 250;
					ballY = 250;
					ballDeltaX *= -1;
				} else {

					// If the ball hitting the paddle, it will bounce back
					// FIXME Something wrong here
					Sound.play("Sound/soundpaddles.wav");
					ballDeltaX *= -1;
				}
			}

			// move the ball
			ballX += ballDeltaX;
			ballY += ballDeltaY;

		}

		// stuff has moved, tell this JPanel to repaint itself
		repaint();
	}

	/** Paint the game screen. */
	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		imgbtnPlay = new ImageIcon(nameP);
		imgbtnSetting = new ImageIcon(nameS);
		imgbgP = new ImageIcon("background/backgp.jpg");
		if (showTitleScreen) {
			//Sound.play("Sound/playingsoundloop.wav");
			/* Show welcome screen */
			
			
			Image imgbpong = new ImageIcon(
					"background/backgm.jpg")
					.getImage();
			// Draw game title and start message
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 36));
			//g.drawString("Pong Game", 130, 100);
			g.drawImage( imgbpong,0,0,500,500,null);
			
			g.drawImage(imgbtnPlay.getImage(),pPlay.x - rPlay, pPlay.y - rPlay, rPlay * 2, rPlay * 2,null);
			g.drawImage(imgbtnSetting.getImage(),pSetting.x - rSetting, pSetting.y - rSetting, rSetting * 2, rSetting * 2,null	);
			if (intersec4) {
				g.setColor(Color.white);
				g.setFont(new Font(Font.SANS_SERIF, Font.TRUETYPE_FONT, 20));
				g.drawString("Setting", pSetting.x +30 , pSetting.y +10);
			}
			// FIXME Wellcome message below show smaller than game title
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
			//g.drawString("Press 'P' to play.", 140, 300);
			
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
			//g.drawString("Press 'S' to Setting.", 135, 400);
		} else if (playing) {
			
			
			/* Game is playing */
			Image background = new ImageIcon("background/background.png").getImage();
			g.drawImage(background, 0, 0, 500, 500, null);
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 36));
			g.setColor(Color.blue);
			g.drawString(namePlayer1, 30, 50);
			g.setColor(Color.red);
			g.drawString(namePlayer2, 320, 50);
			// set the coordinate limit
			int playerOneRight = playerOneX + playerOneWidth;
			int playerTwoLeft = playerTwoX;

			// draw dashed line down center
			//for (int lineY = 0; lineY < getHeight(); lineY += 50) {
				//g.drawLine(250, lineY, 250, lineY + 25);
			//}

			// draw "goal lines" on each side
			// g.setColor(Color.GREEN);
			// g.drawLine(playerOneRight, 0, playerOneRight, getHeight());
			// g.drawLine(playerTwoLeft, 0, playerTwoLeft, getHeight());

			// draw the scores
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 36));
			g.setColor(Color.blue);
			g.drawString(String.valueOf(playerOneScore), 215, 270); // Player 1
																	// score
			g.setColor(Color.red);
			g.drawString(String.valueOf(playerTwoScore), 265, 270); // Player 2
																	// score
			
			// draw the ball
			g.setColor(Color.RED);
			g.drawImage(ball1.getImage(),ballX, ballY, diameter, diameter,null);
			//g.fillOval(ballX, ballY, diameter, diameter);

			// draw the paddles

			g.drawImage(imgpad1.getImage(), playerOneX, playerOneY, playerOneWidth, playerTwoHeight, null);
			// g.fillRect(playerOneX, playerOneY, playerOneWidth,
			// playerOneHeight);
			g.drawImage(imgpad2.getImage(), playerTwoX, playerTwoY, playerTwoWidth, playerTwoHeight, null);
			

			// g.fillRect(playerTwoX, playerTwoY, playerTwoWidth,
			// playerTwoHeight);


		} 
		if (setting){
			imgbtnBack = new ImageIcon(nameB);
			g.drawImage(imgbgP.getImage(),0,0,500,500,null);
			//g.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
			//g.drawString("Press 'C' to Menu.", 135, 200);
			g.drawImage(imgbtnBack.getImage(),pBack.x - rBack, pBack.y - rBack, rBack * 2, rBack * 2,null);
			//g.drawString("Soccer", 80, 230);
			if (intersec2) {
				g.setColor(Color.white);
				g.setFont(new Font(Font.SANS_SERIF, Font.TRUETYPE_FONT, 20));
				g.drawString("Back", pSetting.x +20 , pSetting.y +5);
			}
			
			//g.drawString("basketball", 80, 280);
			//g.drawString("Tennis", 80, 330);
			////JLabel lbl1 =new JLabel("soccer");
			//JLabel lbl3 =new JLabel("Tennis");
			//JLabel lbl2 =new JLabel("Basketball");
			//add(lbl1);
			//add(lbl2);
			//add(lbl3);
			//lbl1.setBounds(150, 200, 90,50 );
			//lbl2.setBounds(150,250, 90,50 );
			//lbl3.setBounds(150, 300, 90,50 );
			//g.drawImage(ball1.getImage(),210, 200, 50, 50,null);
			//g.drawImage(ball2.getImage(),210,250,50, 50,null);
			//g.drawImage(ball3.getImage(),210, 300, 50, 50,null);
			
		}
		  else if (gameOver) {
			imgbtnMenu = new ImageIcon("image/menugo.png");
			imgbtnSa = new ImageIcon("image/restartgo.png");
			/* Show End game screen with winner name and score */

			// Draw scores
			// TODO Set Blue color
			g.drawImage(imgbgP.getImage(),0,0,500,500,null);
			g.setColor(Color.BLUE);
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 36));
			g.drawString(namePlayer1, 30, 50);
			g.setColor(Color.RED);
			g.drawString(namePlayer2, 320, 50);
			g.setColor(Color.BLUE);
			g.drawString(String.valueOf(playerOneScore), 80, 100);
			g.setColor(Color.RED);
			g.drawString(String.valueOf(playerTwoScore), 380, 100);
			g.drawImage(imgbtnMenu.getImage(), pMenu.x - rMenu, pMenu.y - rMenu, rMenu * 2, rMenu * 2,null);
			g.drawImage(imgbtnSa.getImage(), pSa.x - rSa, pSa.y - rSa, rSa * 2, rSa * 2,null);
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 36));
			if (playerOneScore > playerTwoScore) {
				g.setColor(Color.BLUE);
				g.drawString ("The Winner is :"+namePlayer1 , 15, 200);
			} else {
				g.setColor(Color.RED);
				g.drawString("The Winner is :"+namePlayer2, 15, 200);
			}
			if (intersec) {
				g.setColor(Color.blue);
				g.setFont(new Font(Font.SANS_SERIF, Font.TRUETYPE_FONT, 20));
				g.drawString("Click to back a menu", pMenu.x +35 , pMenu.y +25);
				
			}
			if (intersec1) {
				g.setColor(Color.blue);
				g.setFont(new Font(Font.SANS_SERIF, Font.TRUETYPE_FONT, 20));
				g.drawString("Click to restart a game", pSa.x +35 , pSa.y +25);
			}
			// Draw the winner name
			

			// Draw Restart message
			//g.setColor(Color.BLUE);
			//g.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
			// TODO Draw a restart message
			//g.drawString("Press 'Space' to restart game", 95, 335);
		}
		
	}
	
	
	public void keyTyped(KeyEvent e) {
		
	}

	public void keyPressed(KeyEvent e) {
		
		if (playing) {
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				upPressed = true;
			} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				downPressed = true;
			} else if (e.getKeyCode() == KeyEvent.VK_W) {
				wPressed = true;
			} else if (e.getKeyCode() == KeyEvent.VK_S) {
				sPressed = true;
			}else if ( e.getKeyCode() == KeyEvent.VK_ESCAPE){
				isPaused = true ;
			}
		} else if (gameOver && e.getKeyCode() == KeyEvent.VK_SPACE) {
			gameOver = false;
			showTitleScreen = true;
			playerOneY = 250;
			playerTwoY = 250;
			ballX = 250;
			ballY = 250;
			playerOneScore = 0;
			playerTwoScore = 0;
		}else if (setting && e.getKeyCode() == KeyEvent.VK_N){
			SecondWindow w = new SecondWindow();
			w.setLocationRelativeTo(PongPanel.this);
			w.setVisible(true);
			Settings s = w.getSetings();
			System.out.println("After open window");
			
			// Stop and wait for user input
			
			if (w.dialogResult == MyDialogResult.YES) {
				System.out.printf("User settings: \n Username1: %s \n Username2: %s",
						s.getUserName1(), s.getUserName2());
				namePlayer1 = s.getUserName1();
				namePlayer2 =s.getUserName2();
			} else {
				System.out.println("User chose to cancel");
			}
		}
	}

	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			upPressed = false;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			downPressed = false;
		} else if (e.getKeyCode() == KeyEvent.VK_W) {
			wPressed = false;
		} else if (e.getKeyCode() == KeyEvent.VK_S) {
			sPressed = false;
		}
	}
	public void pauseGame (){
	
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		if (getPointDistance(arg0.getPoint(),pPlay) <= rPlay) {
			//intersec = true;
			nameP = "image/playwm.png";
		} else {
			//intersec = false;
			nameP = "image/play.png";
		}
		if (getPointDistance(arg0.getPoint(), pSetting)<=rSetting){
			intersec4 = true ;
		}
		else {
			intersec4 = false;
		}
		if (getPointDistance(arg0.getPoint(), pBack)<=rBack){
			intersec2 =true ;
			
		}
		else {
			intersec2 = false;
			}
		if (getPointDistance(arg0.getPoint(), pMenu)<=rMenu){
			intersec =true ;
			
		}
		else {
			intersec = false;
			}
		if (getPointDistance(arg0.getPoint(), pSa)<=rSa){
			intersec1 =true ;
			
		}
		else {
			intersec1 = false;
			}
	}
		public double getPointDistance(Point p1, Point p2) {
			return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
		}

		/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
		 */
		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			if (showTitleScreen){
			if (getPointDistance(e.getPoint(),pPlay) <= rPlay){
				Sound.play("Sound/click.wav");
				showTitleScreen = false;
				gameOver = false ;
				setting = false;
				playing = true;
			}
			}
			if (showTitleScreen){
				if (getPointDistance(e.getPoint(), pSetting)<=rSetting){
				
				Sound.play("Sound/click.wav");
				setting = true;
				showTitleScreen = false;
				playing = false ;
				gameOver = false ;
				}
			}
			
			else if (setting){
				if (getPointDistance(e.getPoint(), pBack)<=rBack){
				
				Sound.play("Sound/click.wav");
				showTitleScreen = true;
				playing = false ;
				setting = false;
				gameOver = false;
			}
		}
			if (gameOver){
				if ( getPointDistance(e.getPoint(), pMenu)<=rMenu){
					Sound.play("Sound/click.wav");
					gameOver = false;
					showTitleScreen = true;
					playerOneY = 250;
					playerTwoY = 250;
					ballX = 250;
					ballY = 250;
					playerOneScore = 0;
					playerTwoScore = 0;
					
				}
				else if (getPointDistance(e.getPoint(), pSa)<=rSa){
					gameOver = false;
					playerOneY = 250;
					playerTwoY = 250;
					ballX = 250;
					ballY = 250;
					playerOneScore = 0;
					playerTwoScore = 0;
					playing = true;
					
				}
			}
			
	}	
	
}
