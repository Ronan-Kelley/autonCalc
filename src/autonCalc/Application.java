package autonCalc;

import java.awt.EventQueue;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Application extends JFrame {
	public Application() {
		initUI();
	}

	public void initUI() {
		add(new Board());

		setSize(250, 200);

		setTitle("auton creation tool v0.3");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	public static void main(String[] args) {
		/*
		 * run the graphical end of things in the better way, according to stackexchange
		 */
		EventQueue.invokeLater(() -> {
			Application exe = new Application();
			exe.setVisible(true);
		});

	}

}
