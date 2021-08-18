package Chat;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

import javax.swing.*;

public class Client extends JFrame {

	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String serverIp;
	private Socket connection;
	private String msg="";
	private String server_name;
	private int colot=0;
	public Client(String host) {
		super("Client side");
		userText=new JTextField(30);
		userText.setEditable(false);
		add(userText,BorderLayout.NORTH);
		serverIp=host;
		userText.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e) {
						sendMessage(e.getActionCommand());
						userText.setText("");
					}
				});
		chatWindow=new JTextArea();
		add(new JScrollPane(chatWindow));
		setSize(300,400);
		chatWindow.setBackground(Color.gray);
		chatWindow.setEditable(false);
		setVisible(true);
	}
	public void startClient() throws IOException
	{
		try 
		{
			while(true)
			{
		    setupConnection();
		    setsources();
		    whileChatting();
			}
		}
		catch(EOFException e)
		{
			showMessage("\n Client ended chat");
		}
		catch(IOException e)
		{
			showMessage("\n Input output error");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		finally
		{
			closeResources();
		}
	}
	private void whileChatting() throws IOException, ClassNotFoundException {
			isAbleToChat(true);
			try
			{
				do {
					String user_msg=(String)input.readObject();
					showMessage("\n "+server_name+" : "+ user_msg);
				}
				while(!msg.toLowerCase().equals("end"));
			}
			catch(ClassNotFoundException e)
			{
				showMessage("\n Class not found exception");
			}
		
	}
	private void setupConnection() throws IOException {
		try
		{
			showMessage("\n Attempting to connect");
			connection=new Socket(InetAddress.getByName(serverIp),8082);
			server_name=connection.getLocalAddress().getHostName();
			showMessage("\n Connected with :"+server_name);
		}
		catch(IOException e)
		{
			showMessage("\n Error while connecting with server");
		}
	}
	private void isAbleToChat(boolean is)
	{
		SwingUtilities.invokeLater(
				new Runnable()
				{
					public void run()
					{
						userText.setEditable(is);
					}
				});
	}
	private void showMessage(String msg) {
		SwingUtilities.invokeLater(
				new Runnable()
				{
					public void run()
					{
						userText.setCaretColor(Color.blue);
						chatWindow.append(msg);
					}
				});
	}
	private void closeResources() {
		try
		{
			isAbleToChat(false);
			connection.close();
			output.close();
			input.close();
		}
		catch(IOException e)
		{
			showMessage("\n Some error occured!");
		}
	}
	private void setsources() throws IOException {
		try
		{
		output=new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input=new ObjectInputStream(connection.getInputStream());
		showMessage("\nClient is connected");
		}
		catch(IOException e)
		{
			showMessage("\n Error occured while setting up resources");
		}
	}
	
	protected void sendMessage(String actionCommand) {
		try
		{
			output.writeObject(actionCommand);
			output.flush();
			showMessage("\n Me"+" : "+actionCommand);
		}
		catch(IOException e)
		{
			showMessage("\n Cannot send message!!");
		}
	}

}
