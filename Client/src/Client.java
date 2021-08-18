

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
		chatWindow.setEditable(false);
		chatWindow.setBackground(Color.gray);
		add(new JScrollPane(chatWindow));
		setSize(500,400);
		setVisible(true);
		System.out.println("This is constructor statt");
	}
	public void startClient() throws IOException
	{
		//System.out.println("Client start statrt");
		try 
		{
			//System.out.println("Client statrt");
			//System.out.println("Client statrt");
		    setupConnection();
		    setsources();
		    whileChatting();
			
		}
		catch(EOFException e)
		{
			showMessage("\n You ended chat");
		}
		catch(IOException e)
		{
			showMessage("\n Input output error");
		} catch (ClassNotFoundException e) {
			showMessage("\n Error");
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
					System.out.println("Client chatting");
					String user_msg=(String)input.readObject();
					showMessage("\n"+server_name+" : "+ user_msg);
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
			System.out.println("Client setup connection");
			showMessage("\n Attempting to connect");
			connection=new Socket(InetAddress.getByName(serverIp),8082);
			server_name=connection.getInetAddress().getLocalHost().getHostName();
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
		System.out.println("Client setresources");
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
			showMessage("\n Me : "+actionCommand);
		}
		catch(IOException e)
		{
			showMessage("\n Cannot send message!!");
		}
	}

}
