import javax.swing.JOptionPane;
import java.net.*;
import java.util.*;
import java.io.*;

class Client extends Thread
{
	static DataInputStream objDIS;
	static DataOutputStream objDOS;

	public void run()
	{
		try
		{
			// Conenct with server
			Socket objSocket = new Socket("127.0.0.1",1500);

			System.out.println("Connection established");

			objDIS = new DataInputStream(objSocket.getInputStream());  //Input
			objDOS = new DataOutputStream(objSocket.getOutputStream()); //Ouput

			String name;

			do
			{
				name = JOptionPane.showInputDialog("Please enter your username:");
				if (name == null || name.isEmpty())
					JOptionPane.showMessageDialog(null, "The username you entered is not correct.\n\nPlease try again.");
			} while(name == null || name.isEmpty());

			printmsg("Username=" + name);

			GUI gui = new GUI(name);

			while(true)
			{
				//Send message recieved from server to GUI
				String msgFromServer = objDIS.readUTF();
				gui.printanswer(msgFromServer);
			}
		}
		catch(Exception e)
		{
			System.out.println("Server not found.");
			System.exit(-1);
		}
	}

	static void printmsg(String msgFromClient)
	{
		try
		{
			//Send message recieved by GUI to server
			objDOS.writeUTF(msgFromClient);
		}
		catch (Exception e)
		{}
	}
}

