package dialogAndMouseDemo;

import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MainGui extends JFrame {
	JPanel pnl;
	
	public MainGui() {
		setTitle("Demo");
		pnl = new CanvasPanel();
		//pnl = new CanvasPanelBallIntersect();
		setContentPane(pnl);
		pack();
		setLocationRelativeTo(null);
		
	}
	
	public static void main(String[] args) {
		MainGui f = new MainGui();
		f.setDefaultCloseOperation(EXIT_ON_CLOSE);
		f.setVisible(true);
	}

}
