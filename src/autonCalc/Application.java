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

		setTitle("auton creation tool alpha v0.1");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	public static void main(String[] args) {

		EventQueue.invokeLater(() -> {
			Application exe = new Application();
			exe.setVisible(true);
		});

	}

}
