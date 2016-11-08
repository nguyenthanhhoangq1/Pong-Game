package dialogAndMouseDemo;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class CanvasPanelBallIntersect extends JPanel implements MouseMotionListener, MouseListener {
	private static final int WIDTH = 500;
	private static final int HEIGHT = 500;
	boolean hover;
	boolean pressed;
	boolean dragged;
	boolean intersec;
	int dx, dy;
	Point p1, p2;
	int r1, r2;
	Color c1, c2;

	public CanvasPanelBallIntersect() {
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		addMouseMotionListener(this);
		addMouseListener(this);
		setFocusable(true);
		p1 = new Point(50, 50);
		p2 = new Point(WIDTH/2-r2, HEIGHT/2-r2);
		r1 = 30;
		r2 = 40;
		c1 = Color.ORANGE;
		c2 = Color.BLUE;
		Timer t = new Timer(1000/60, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				repaint();
			}
		});
		t.start();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(c2);
		g.fillOval(p2.x - r2, p2.y - r2, r2 * 2, r2 * 2);
		g.setColor(c1);
		g.fillOval(p1.x - r1, p1.y - r1, r1 * 2, r1 * 2);
		g.setColor(Color.GRAY);
		if (dragged) {
			if (intersec) {
				g.drawString("Two circles are intersecting", p1.x + 30, p1.y + 30);
			} else {
				g.drawString("Move me over the blue circle", p1.x + 30, p1.y + 30);
			}
		} else {
			g.drawString("Drag me", p1.x + 30, p1.y + 30);
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (dragged) {
			p1.x = e.getX() - dx;
			p1.y = e.getY() - dy;
			if (getPointDistance(p1, p2) <= r1 + r2) {
				intersec = true;
				c2 = Color.GREEN;
			} else {
				intersec = false;
				c2 = Color.BLUE;
			}
		}
	}

	public double getPointDistance(Point p1, Point p2) {
		return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (getPointDistance(e.getPoint(), p1) <= r1) {
			c1 = Color.YELLOW;
			hover = true;
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		} else {
			c1 = Color.ORANGE;
			hover = false;
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {

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
		if (getPointDistance(e.getPoint(), p1) <= r1) {
			dx = e.getX() - p1.x;
			dy = e.getY() - p1.y;
			c1 = Color.RED;
			dragged = true;
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		pressed = false;
		dragged = false;
		c1 = Color.ORANGE;
	}
}
