import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class TextListener implements DocumentListener {
	JTextField jtf1;
	JTextField jtf2;
	JTextField jtf3;
	JButton jcbtn;

	public TextListener(JTextField jtf1, JTextField jtf2, JTextField jtf3, JButton jcbtn) {
		this.jtf1 = jtf1;
		this.jtf2 = jtf2;
		this.jtf3 = jtf3;
		this.jcbtn = jcbtn;
	}

	public void changedUpdate(DocumentEvent e) {
	}
	
	public void removeUpdate(DocumentEvent e) {
		if (jtf1.getText().trim().equals("") || jtf2.getText().trim().equals("") || jtf3.getText().trim().equals("")) {
			//disable jcbtn
			jcbtn.setEnabled(false);
		} else {
			//enable jcbtn
			jcbtn.setEnabled(true);
		}
	}

	public void insertUpdate(DocumentEvent e) {
		if (jtf1.getText().trim().equals("") || jtf2.getText().trim().equals("") || jtf3.getText().trim().equals("")) {
			//disable jcbtn
			jcbtn.setEnabled(false);
		} else {
			//disable jcbtn
			jcbtn.setEnabled(true);
		}
	}

	}
