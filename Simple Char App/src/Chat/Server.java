package Chat;
import java.util.*;
import javax.swing.*;
import java.net.*;
import java.awt.*;
import java.io.*;
import java.awt.event.*;
class Server extends JFrame 
{
	private JTextField userText;
	private JTextArea chatWindow;
	private JFrame mainWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private ServerSocket server;
	private Socket connection;
	private String client_name;
	
	//setting up environment
	Server() throws IOException
	{
		super("My Chat App");
		mainWindow=new JFrame();
		userText=new JTextField(20);
		userText.setEditable(false);
		userText.setSelectionColor(Color.CYAN);
		userText.addActionListener(new ActionListener()
				{

					@Override
					public void actionPerformed(ActionEvent e) {
						sendMessage(e.getActionCommand());
						userText.setText("");
					}
			
				});
		add(userText,BorderLayout.NORTH);
		chatWindow=new JTextArea();
		chatWindow.setEditable(false);
		chatWindow.setSelectionColor(Color.blue);
		chatWindow.setEditable(false);
		chatWindow.setBackground(Color.gray);
		add(new JScrollPane(chatWindow));
		setSize(400,400);
		setVisible(true);
	}
//	public void actionPerformed(ActionEvenet e)
//	{
//		String msg=userText.getText();
//		sendMessage(msg);
//		userText.setText("");
//	}
	//setting up all the methods for chatting
	public void startRunning()
	{
		try
		{
			server=new ServerSocket(8082,101);
			while(true)
			{
				try
				{
					waitForConnection();
					setUpStreams();
					whileChatting();
				}
				catch(EOFException e)
				{
					showMessage("\nServer ended Connction!");
				}
				finally
				{
					closeSources();
				}
			}
		}
		catch(IOException e)
		{System.out.println("Error r");
			e.printStackTrace();
		}
	}

	//Waiting for connection
	private void waitForConnection()
	{
		try
		{
			showMessage("\n Waiting for connection");
			connection=server.accept();
			client_name=connection.getInetAddress().getHostName();
			showMessage("\n Connected to :"+ client_name);
		}
		catch(Exception e)
		{
			System.out.println("Error r");
			e.printStackTrace();
		}
	}
	//initilaize the streams and stuff
	private void setUpStreams() throws IOException
	{
		System.out.println("Error r");
		output=new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input=new ObjectInputStream(connection.getInputStream());
		showMessage("\n Server is setup");
	}

	//closing all the resources 
	private void closeSources()
	{
		isAbleToType(false);
		try 
		{
		input.close();
		output.close();
		connection.close();
		server.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	// send Message to client
	 private void sendMessage(String msg)
	 {
	 	try
	 	{
	 		output.writeObject(msg);
	 		output.flush();
	 		showMessage("\n Me : " + msg);
	 	}
	 	catch(IOException e)
	 	{
	 		chatWindow.append("\nUnable to send the message");
	 	}
	 }

	 // set textarea to able to type or not
	private void isAbleToType(boolean is)
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

//Show message came from client or sent
	private void showMessage(String msg)
	{
		if(msg==null)return;
		SwingUtilities.invokeLater(
			new Runnable()
			{
				public void run()
				{
					chatWindow.append(msg);
				}
			}
		);
	}

	//to able to chat while connection is on
	private void whileChatting() throws IOException
	{
		String msg="You are connected!";
		showMessage("\n"+msg);
		isAbleToType(true);
		do{
			try
			{
				msg=(String)input.readObject();
				showMessage("\n"+client_name+" : "+msg);
			}
			catch(ClassNotFoundException e)
			{
				System.out.println("Error r");
				showMessage("\n Something went wrong");
			}

		}while(!msg.toLowerCase().equals("end"));
	}
}