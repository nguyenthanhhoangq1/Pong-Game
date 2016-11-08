package dialogAndMouseDemo;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class CanvasPanel extends JPanel implements MouseMotionListener, MouseListener {
	private static final int WIDTH = 500;
	private static final int HEIGHT = 500;
	Color buttonColor = Color.BLUE;
	Rectangle rect;
	Rectangle rect2;
	ImageIcon btnIcon = new ImageIcon("images/button.png");
	boolean hover;
	boolean pressed;
	boolean dragged;
	int x, y, w, h;
	int dx, dy;

	public CanvasPanel() {
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		addMouseMotionListener(this);
		addMouseListener(this);
		setFocusable(true);
		w = 100;
		h = 30;
		x = WIDTH / 2 - w / 2;
		y = HEIGHT / 2 - h / 2;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		// g.setColor(buttonColor);
		// g.fillRect(x, y, w, h);
		rect = new Rectangle(x, y, w, h);
		if (hover) {
			if (pressed) {
				g.drawImage(btnIcon.getImage(), x, y, x + w, y + h, 0, 214, 371, 214 + 106, null);
				g.setColor(Color.RED);
			} else {
				g.drawImage(btnIcon.getImage(), x, y, x + w, y + h, 0, 0, 371, 108, null);
				g.setColor(Color.WHITE);
			}
		} else {
			g.drawImage(btnIcon.getImage(), x, y, x + w, y + h, 0, 108, 371, 108 + 106, null);
			g.setColor(Color.WHITE);
		}
		g.setFont(new Font("Tahoma", Font.BOLD, 15));
		g.drawString("Open", x + 30, y + 19);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (dragged && CanvasPanel.this.getBounds().contains(e.getPoint())) {
			x = e.getX() - dx;
			y = e.getY() - dy;
			repaint();
			System.out.format("Mouse x: %d , Mouse y: %d, dx: %d, dx: %d",
					e.getX(), e.getY(), dx, dy);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// System.out.format("x=%d - y=%d", e.getX(), e.getY());
		if (rect.contains(e.getX(), e.getY())) {
			buttonColor = Color.RED;
			hover = true;
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		} else {
			buttonColor = Color.BLUE;
			hover = false;
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (rect.contains(e.getPoint())) {
			SecondWindow w = new SecondWindow();
			w.setLocationRelativeTo(CanvasPanel.this);
			w.setVisible(true);
			Settings s = w.getSetings();
			System.out.println("After open window");
			
			// Stop and wait for user input
			
			if (w.dialogResult == MyDialogResult.YES) {
				System.out.printf("User settings: \n Username1: %s \n Username2: %s",
						s.getUserName1(), s.getUserName2());
			} else {
				System.out.println("User chose to cancel");
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {

	}

	@Override
	public void mouseExited(MouseEvent arg0) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		pressed = true;
		if (rect.contains(e.getX(), e.getY())) {
			dx = e.getX() - x;
			dy = e.getY() - y;
			dragged = true;
		}
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		pressed = false;
		dragged = false;
		repaint();
	}
}
