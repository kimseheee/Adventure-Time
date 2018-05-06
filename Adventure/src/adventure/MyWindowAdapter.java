package adventure;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class MyWindowAdapter extends WindowAdapter
{
	public void windowClosing(WindowEvent e) {
		Window window = e.getWindow();
		window.setVisible(false);
		window.dispose();
		System.exit(0);
	}
}