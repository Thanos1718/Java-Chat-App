package Chat;

import java.io.EOFException;
import java.io.IOException;

import javax.swing.JFrame;

public class ClientTest {

	public static void main(String[] args) throws IOException,ClassNotFoundException,EOFException {
		Client c=new Client("127.0.0.1");
		c.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		c.startClient();
	}

}
