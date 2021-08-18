package Chat;
import javax.swing.*;
import java.io.*;
class ServerTest
{
	public static void main(String []args) throws IOException
	{
		Server s=new Server();
		s.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		s.startRunning();
	}
}