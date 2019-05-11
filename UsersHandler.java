import java.util.Scanner;

class UsersHandler implements Runnable {

  private Server server;
  private Users user;

  public UsersHandler(Server server2, Users user) {
    this.server = server2;
    this.user = user;
    this.server.SendUsers();
  }

  public void run() {
    String message;

    // when there is a new message, broadcast to all
    Scanner s = new Scanner(this.user.getInputStream());
    while (s.hasNextLine()) {
      message = s.nextLine();

      // showing smiley faces
      message = message.replace(":)", "<img src='https://static.xx.fbcdn.net/images/emoji.php/v9/f4c/1/16/1f642.png'>");
      message = message.replace(":D", "<img src='https://static.xx.fbcdn.net/images/emoji.php/v9/fce/1/16/1f600.png'>");
      message = message.replace(":d", "<img src='https://static.xx.fbcdn.net/images/emoji.php/v9/fce/1/16/1f600.png'>");
      message = message.replace(":(", "<img src='http://2.bp.blogspot.com/-rnfZUujszZI/UZEFYJ269-I/AAAAAAAADnw/BbB-v_QWo1w/s1600/facebook-frown-emoticon.png'>");
      message = message.replace("-_-", "<img src='http://3.bp.blogspot.com/-wn2wPLAukW8/U1vy7Ol5aEI/AAAAAAAAGq0/f7C6-otIDY0/s1600/squinting-emoticon.png'>");
      message = message.replace(";)", "<img src='http://1.bp.blogspot.com/-lX5leyrnSb4/Tv5TjIVEKfI/AAAAAAAAAi0/GR6QxObL5kM/s400/wink%2Bemoticon.png'>");
      message = message.replace(":P", "<img src='https://static.xx.fbcdn.net/images/emoji.php/v9/f9f/1/16/1f61b.png'>");
      message = message.replace(":p", "<img src='https://static.xx.fbcdn.net/images/emoji.php/v9/f9f/1/16/1f61b.png'>");
      message = message.replace(":o", "<img src='http://1.bp.blogspot.com/-MB8OSM9zcmM/TvitChHcRRI/AAAAAAAAAiE/kdA6RbnbzFU/s400/surprised%2Bemoticon.png'>");
      message = message.replace(":O", "<img src='http://1.bp.blogspot.com/-MB8OSM9zcmM/TvitChHcRRI/AAAAAAAAAiE/kdA6RbnbzFU/s400/surprised%2Bemoticon.png'>");

      // Private message management
      //if we @ at the begining of the message so its for a specific user
      if (message.charAt(0) == '@'){
        if(message.contains(" ")){
          System.out.println("private msg: " + message);
          int firstSpace = message.indexOf(" ");
          String privatemg= message.substring(1, firstSpace);
          //send the msg
          server.sendmessages(message.substring(firstSpace+1,message.length()),user,privatemg);
        }

      // Change management and change colors if the msg started with #
       
      }else if (message.charAt(0) == '#'){
        user.changecolor(message);
        // update color for all other users
        this.server.SendUsers();
      }else{
        // update user list
        server.SendMessages(message, user);
      }
    }
    // end of Thread
    server.CloseConnwithUser(user);
    this.server.SendUsers();
    s.close();
  }
}

