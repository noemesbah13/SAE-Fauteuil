import java.net.*;
import java.io.*;


public class Serveur {
    static int port = 9632;
    public static void main(String[] args) {
        
    try {

        ServerSocket socketServeur = new ServerSocket(port);
        System.out.println("Lancement du serveur");
            Socket socketClient = socketServeur.accept();
            System.out.println("Connexion avec le Client:"+socketClient.getInetAddress());
        while (true) {
            BufferedReader in = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
            String message = in.readLine();
            System.out.println("Message reçu du client: "+message);
        }
    }

    catch (Exception e) {
        e.printStackTrace();
    }

}
}
