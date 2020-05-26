import java.net.*;
import java.util.*;
import java.io.*;

class RunServer
{
	public static void main(String args[])
	{
		try (Socket s = new Socket("127.0.0.1", 1500))
		{
			//Server exists. Do nothing.
		}
		catch (IOException ex)
		{
			Server s = new Server();
			s.start();
		}
	}
}
