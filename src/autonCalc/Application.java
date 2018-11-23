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

		setSize(1000, 700);

		setTitle("auton creation tool v0.3");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	public static void main(String[] args) {
		/*
		 * run the graphical end of things in the better way, according to stackexchange
		 */

		System.out.println("program launched");

		EventQueue.invokeLater(() -> {
			Application exe = new Application();
			// uncomment to start maximized (may be necessary to fix some graphical glitches
			// on smaller monitors, breaks layout on larger ones until minimized)
			// exe.setExtendedState(JFrame.MAXIMIZED_BOTH);
			exe.setVisible(true);
		});

	}

}
