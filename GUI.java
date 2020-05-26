import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.net.*;
import java.io.*;

import java.text.SimpleDateFormat;

public class GUI
{
	JEditorPane editor;
	JFrame fr;
	JScrollPane scrollPane;
	JTextField tf;

	SimpleDateFormat sdf;

	String input = "";
	String them = "";
	String text = "";

	public GUI(final String username)
	{
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		}
		catch (Exception e) {
		}

		sdf = new SimpleDateFormat("HH:mm:ss");

		fr = new JFrame(username);
		fr.setLayout(null);
		fr.setResizable(false);
		fr.setSize(300,400);

		editor = new JEditorPane("text/html", text);
		editor.setEditable(false);
		fr.add(editor);

		scrollPane = new JScrollPane(editor);
		scrollPane.setBounds(0,0,300,335);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		fr.add(scrollPane);

		tf = new JTextField();
		tf.setBounds(0,340,300,30);
		fr.add(tf);

		// fr.setAlwaysOnTop(true);
		// fr.setLocationRelativeTo(null);
		fr.setVisible(true);
        tf.requestFocusInWindow();

		tf.addKeyListener(new KeyAdapter()
		{
			public void keyPressed(KeyEvent e)
			{
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					input = tf.getText();
					if(!input.equals(""))
					{
						String time = sdf.format(new Date());
						Client.printmsg("<i>" + time + "</i> <b>" + username + ": </b>" + input);
						input = "<i>" + time + "</i> <b>You: </b>" + input;
						text = text + "<br>" + input;
						editor.setText(text);
						tf.setText("");
					}
				}
            }
        });

		fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void printanswer(String recieved)
	{
		them = recieved;
		text = text + "<br>" + them ;
		editor.setText(text);
	}
}
