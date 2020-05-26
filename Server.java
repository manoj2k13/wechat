import java.net.*;
import java.util.*;
import java.io.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Server extends Thread
{
	int i=0;

	public void run()
	{
		try
		{
			ServerSocket serverSocket = new ServerSocket(1500);
			System.out.println("Server started.");

			while(true)
			{
				Socket clientSocket = serverSocket.accept();

				ChatHandler clientHandler = new ChatHandler(clientSocket, i);
				clientHandler.start();

				System.out.println("\nClient connected: " + clientSocket.getRemoteSocketAddress().toString());

				i++;
			}
		}
		catch(Exception e)
		{
		}
	}
}

class ChatHandler extends Thread
{
	protected Socket s;

	protected DataInputStream i;
	protected DataOutputStream o;

	String username = "";
	int id;

	public ChatHandler(Socket s, int id) throws IOException
	{
		this.s = s;
		this.id = id;
		i = new DataInputStream(new BufferedInputStream(s.getInputStream()));
		o = new DataOutputStream(new BufferedOutputStream(s.getOutputStream()));
	}

	protected static Vector handlers = new Vector();

	public void run()
	{
		try
		{
			handlers.add(this);  //Add current connection to dynamic array.
			while(true)
			{
				//Read message from client
				String msg = i.readUTF();

				// Check if a username has been sent
				Pattern pat = Pattern.compile("Username=(.*)");
				Matcher mat = pat.matcher(msg);
				if (mat.find())
				{
					this.username = mat.group(1);
					System.out.println("\tUsername: " + this.username + "\n");
				}
				else
				{
					// Check if the message is meant for a group of people
					pat = Pattern.compile("<i>(.*)</i> <b>(.*): </b>(.*): (.*)");
					mat = pat.matcher(msg);
					if (mat.find())
					{
						msg = "<i>" + mat.group(1) + "</i> <b>" + mat.group(2) + ": </b>" + mat.group(4);

						// More than one person -> Multicast
						if (mat.group(3).contains(","))
							msg = "<font color = \"blue\">" + msg + "</font>";
						else
							msg = "<font color = \"green\">" + msg + "</font>";

						// Unicast or Multicast
						sendmessage(msg, this.id, mat.group(3));
					}
					else
					{
						// Broadcast
						msg = "<font color = \"red\">" + msg + "</font>";
						sendmessage(msg, this.id, "");
					}
				}
			}
		}
		catch (IOException ex)
		{
			// ex.printStackTrace();
		}
		finally
		{
			handlers.remove(this);
			try
			{
				s.close();
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}
	}

	public static void sendmessage(String msg, int id, String ppl)
	{
		synchronized(handlers)
		{
			Iterator e=handlers.iterator();
			while(e.hasNext())
			{
				ChatHandler c = (ChatHandler) e.next();
				try
				{
					synchronized(c.o)
					{
						// Check if client is not the sender
						if(c.id != id)
						{
							// Broadcast: Send to everyone
							if (ppl == "")
							{
								c.o.writeUTF(msg);
							}
							else
							{
								// Send only to the correct person
								if (ppl.contains(c.username))
								{
									c.o.writeUTF(msg);
								}
							}
						}
					}
					c.o.flush();
				}
				catch(IOException ex)
				{
					c.stop();
				}
			}
		}
	}
}
