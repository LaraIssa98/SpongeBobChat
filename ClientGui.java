import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.*;
import java.io.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.html.*;

import java.util.ArrayList;
import java.util.Arrays;


public class ClientGui extends Thread{

  final JTextPane jtd = new JTextPane();
  final JTextField chat = new JTextField();
  final JTextPane jusers = new JTextPane();
  
  private String servername;
  private int PORT;
  private String name;
  private String msgo = "";
  private Thread read;

  BufferedReader in;
  PrintWriter out;
  Socket server;

  public ClientGui() {
	  //intialize variables
    this.servername = "localhost";
    this.PORT = 12345;
    this.name = "name";

    String fontfamily = "Arial, sans-serif";
    Font font = new Font(fontfamily, Font.PLAIN, 15);

    final JFrame jfr = new JFrame("SPONGEBOB Chat");

    jfr.getContentPane().setLayout(null);
    jfr.setSize(700, 500);
    jfr.setResizable(false);
    jfr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Thread module
    jtd.setBounds(25, 25, 490, 320);
    jtd.setFont(font);

    jtd.setMargin(new Insets(6, 6, 6, 6));
    jtd.setEditable(false);
    JScrollPane jtdSP = new JScrollPane(jtd);
    jtdSP.setBounds(25, 25, 490, 320);

    jtd.setContentType("text/html");
    jtd.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);

    // Module of the list of users
    jusers.setBounds(520, 25, 156, 320);
    jusers.setEditable(true);
    jusers.setFont(font);
    jusers.setMargin(new Insets(6, 6, 6, 6));
    jusers.setEditable(false);
    JScrollPane jsplistuser = new JScrollPane(jusers);
    jsplistuser.setBounds(520, 25, 156, 320);

    jusers.setContentType("text/html");
    jusers.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);

    // Field message user input
    chat.setBounds(0, 350, 400, 50);
    chat.setFont(font);
    chat.setMargin(new Insets(6, 6, 6, 6));
    final JScrollPane chatSP = new JScrollPane(chat);
    
    chatSP.setBounds(25, 350, 650, 50);

    // button send
    final JButton jsbtn = new JButton("Send");
    jsbtn.setBackground(Color.YELLOW);
    jsbtn.setFont(font);
    jsbtn.setBounds(575, 410, 100, 35);

    // button Disconnect
    final JButton jsbtndeco = new JButton("Disconnect");
    jsbtndeco.setBackground(Color.YELLOW);
    jsbtndeco.setFont(font);
    jsbtndeco.setBounds(25, 410, 130, 35);

    chat.addKeyListener(new KeyAdapter() {
      // send message on Enter
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          sendMessage();
        }

        // Get last message typed
        if (e.getKeyCode() == KeyEvent.VK_UP) {
          String currentMessage = chat.getText().trim();
          chat.setText(msgo);
          msgo = currentMessage;
        }

      }
    });

    // Click on send button
    jsbtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        sendMessage();
      }
    });

    // Connection view
    final JTextField jtfName = new JTextField(this.name);
    final JTextField jtfport = new JTextField(Integer.toString(this.PORT));
    final JTextField jtfAddr = new JTextField(this.servername);
    final JButton jcbtn = new JButton("Connect");
    jcbtn.setBackground(Color.YELLOW);


    // check if those field are not empty using textlistener
    jtfName.getDocument().addDocumentListener(new TextListener(jtfName, jtfport, jtfAddr, jcbtn));
    jtfport.getDocument().addDocumentListener(new TextListener(jtfName, jtfport, jtfAddr, jcbtn));
    jtfAddr.getDocument().addDocumentListener(new TextListener(jtfName, jtfport, jtfAddr, jcbtn));

    // position of the Modules
    jcbtn.setFont(font);
    jtfAddr.setBounds(25, 380, 135, 40);
    jtfName.setBounds(375, 380, 135, 40);
    jtfport.setBounds(200, 380, 135, 40);
    jcbtn.setBounds(575, 380, 100, 40);

    // default color of Thread Modules and User List
    jtd.setBackground(Color.YELLOW);
    jusers.setBackground(new Color(32,178,170));

    // adding elements
    jfr.add(jcbtn);
    jfr.add(jtdSP);
    jfr.add(jsplistuser);
    jfr.add(jtfName);
    jfr.add(jtfport);
    jfr.add(jtfAddr);
    jfr.setVisible(true);


    //chat info
    appendToPane(jtd, "<h4>Hey there, for a better chatting experience you can use the commands stated below </h4>"
        +"<ul>"
        +"<li><b>@nickname </b> to send a private message to the user 'nickname' where only he/she can see it</li>"
        +"<li><b> # d3961b </b> to change the color of his username</li>"
        +"<li><b> ;) </b> smiley faces </li>"
        +"</ul><br/>");

    // On connect
    jcbtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        try {
          name = jtfName.getText();
          String port = jtfport.getText();
          servername = jtfAddr.getText();
          PORT = Integer.parseInt(port);
//intializing sockets
             server = new Socket(servername, PORT);

//BufferedReader and PRintWriter are for reading and writing for other clients 
          in = new BufferedReader(new InputStreamReader(server.getInputStream()));
          out = new PrintWriter(server.getOutputStream(), true);

          // send name to server
          out.println(name);

          // create new Read Thread 
          read = new Read();
          read.start();
          jfr.remove(jtfName);
          jfr.remove(jtfport);
          jfr.remove(jtfAddr);
          jfr.remove(jcbtn);
          jfr.add(jsbtn);
          jfr.add(chatSP);
          jfr.add(jsbtndeco);
          jfr.revalidate();
          jfr.repaint();
          jtd.setBackground(Color.YELLOW);
          jusers.setBackground(new Color(32,178,170));
        } catch (Exception ex) {
          appendToPane(jtd, "<span>connection error</span>");
          JOptionPane.showMessageDialog(jfr, ex.getMessage());
        }
      }

    });

    // when disconnecting you interrupt the thread create to close the connection and add again the info needed to reconnect like port number and name and ip address
    jsbtndeco.addActionListener(new ActionListener()  {
      public void actionPerformed(ActionEvent ae) {
        jfr.add(jtfName);
        jfr.add(jtfport);
        jfr.add(jtfAddr);
        jfr.add(jcbtn);
        jfr.remove(jsbtn);
        jfr.remove(chatSP);
        jfr.remove(jsbtndeco);
        jfr.revalidate();
        jfr.repaint();
        read.interrupt();
        jusers.setText(null);
        jtd.setBackground(Color.YELLOW);
       // jusers.setBackground(Color.ORANGE);
        jusers.setBackground(new Color(32,178,170));
        appendToPane(jtd, "<span>Connection closed.</span>");
        out.close();
      }
    });

  }
	// sending messages
	public void sendMessage() {
		try {
			String message = chat.getText().trim();
			if (message.equals("")) {
				return;
			}
			this.msgo = message;
			out.println(message);
			//getting the input is heard by the respective Listener for that component
			chat.requestFocus();
			chat.setText(null);
		} catch (Exception ex) {

			System.exit(0);
		}
	}

	public static void main(String[] args) throws Exception {
  ClientGui client = new ClientGui();
}

  // read new incoming messages
  class Read extends Thread {
    public void run() {
      String message;
      //at each thread read the message and preview it on the screen as long as the thread is not being interrupted and the chat is still going on
      while(!Thread.currentThread().isInterrupted()){
        try {
          message = in.readLine();
          if(message != null){
         //sending the html to pane
              appendToPane(jtd, message);
            }
          }
      
        catch (IOException ex) {
          System.err.println("Failed to parse incoming message");
        }
      }
    }
  }

  // send html to pane
  private void appendToPane(JTextPane tp, String msg){
    HTMLDocument doc = (HTMLDocument)tp.getDocument();
    HTMLEditorKit editorKit = (HTMLEditorKit)tp.getEditorKit();
    try {
      editorKit.insertHTML(doc, doc.getLength(), msg, 0, 0, null);
      tp.setCaretPosition(doc.getLength());
    } catch(Exception e){
      e.printStackTrace();
    }
  }}