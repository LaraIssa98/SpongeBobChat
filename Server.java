import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.awt.Color;

public class Server {

  
  private List<Users> cl1;
  private ServerSocket server;

  public static void main(String[] args) throws IOException {
    new Server().run();
  }

  public Server() {
  //creating an array of clients since the chat has multiusers
    this.cl1 = new ArrayList<Users>();
  }

  public void run() throws IOException {
	  //open socket connection at the specified port
    server = new ServerSocket(12345) {
      protected void finalize() throws IOException {
        this.close();
      }
    };

    while (true) {
      // accepts a new client
 
      Socket client = server.accept();
     	Scanner s = new Scanner ( client.getInputStream() );
      // get name of newUser
      String Name = s.nextLine();
   
      // create new User
      Users user = new Users(client, Name);

      // add newUser message to list
      this.cl1.add(user);

      // Welcome msg
      user.getOutStream().println("<b>Welcome to this Chat </b> " + user.toString() );

      // create a new thread for newUser incoming messages handling
      new Thread(new UsersHandler(this, user)).start();
    }
  }

  // delete a user from the list
  public void CloseConnwithUser(Users user){
    this.cl1.remove(user);
  }
  // send list of clients to all Users
  public void SendUsers(){
	    for (Users client : this.cl1) {
	      client.getOutStream().println(this.cl1);
	    }
	  }

  // send incoming msg to all Users
  public void SendMessages(String msg, Users userSender) {
	  
    for (Users client : this.cl1) {
      client.getOutStream().println(userSender.toString() + "<span>: " + msg+"</span>");
    }
  }
 
  // send message to a User (String)
  public void sendmessages(String st1, Users userS, String user){
    boolean b1 = false;
    for (Users client : this.cl1) {
    	
      if (client != userS/*not sending it to himself again*/ && client.getNickname().equals(user) ) {
        b1 = true;
        //print to whom this msg is
        userS.getOutStream().println(userS.toString() + " to " + client.toString() +": " + st1);
        //print the message to this specific user
        client.getOutStream().println("(<b>Private</b>)" + userS.toString() + "<span>: " + st1+"</span>");
      }
    }
    if (!b1) {
    	//if the user isn't found
      userS.getOutStream().println(userS.toString() + " -> (<b>no one is found here</b>): " + st1);
    }
  }
}


class ColorInt {
	//proposing different colors which the user can use to change his chat window color
    public static String[] mColors = {
            "#3079ab", // dark blue
            "#e15258", // red
            "#f9845b", // orange
            "#7d669e", // purple
            "#53bbb4", // aqua
            "#51b46d", // green
            "#e0ab18", // mustard
            "#f092b0", // pink
            "#e8d174", // yellow
            "#e39e54", // orange
            "#d64d4d", // red
            "#4d7358", // green
    };

    public static String getColor(int i) {
        return mColors[i % mColors.length];
    }
}
