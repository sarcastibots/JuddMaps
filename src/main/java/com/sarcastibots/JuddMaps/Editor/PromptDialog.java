package com.sarcastibots.JuddMaps.Editor;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Not finished!
 **/
public class PromptDialog {

    /* Note: NOT threadsafe. */
    static String ans = null;

    static void tell(String info) {
	String[] texts = new String[1];
	texts[0] = info;
	String[] btns = new String[1];
	btns[0] = "OK";
	
	PromptDialog.prompt(texts, btns);
    }

    static void prompt(String[] texts, String[] btns) {
	final JDialog d = new JDialog();
	d.setLocationRelativeTo(null);

	// set up text display area
	JPanel textPanel = new JPanel( );
	textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
	for ( String text: texts) {
	    JLabel l = new JLabel(text);
	    textPanel.add(l);
	}

	// set up buttons
	JPanel bpane = new JPanel(new FlowLayout());
	for ( String btnText: btns) {
	    JButton b1 = new JButton(btnText);
	    b1.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    ans = ((JButton)e.getSource()).getText();
		    d.dispose();
		}
	    });
	    bpane.add(b1);
	}

	JPanel cp = (JPanel)d.getContentPane();
	cp.setLayout( new BorderLayout() );
	cp.add(textPanel, BorderLayout.CENTER);
	cp.add(bpane, BorderLayout.SOUTH);

	d.pack();

	d.setVisible(true);
    }

}



