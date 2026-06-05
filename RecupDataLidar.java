import java.lang.*;
import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

/*  ==================================Résumé======================================
    Ce programme permet de récuperer les données du LIDAR RPLIDAR A2M8
    Il execute pour cela un programme contenu dans la variable path 
    qui renvoie ensuite une chaine de caractère contenant trois informations : 
    - Distance 
    - Angle
    - Facteur de qualité (47 si la valeur est bonne, 0 sinon)
    Cette chaine de caractère est ensuite traitée pour en ressortir uniquement
    les valeurs utiles.
    Enfin, la distacne de l'obstacle le plus proche contenue dans une fenetre
    délimitée par deux angles est envoyé via un socket en TCP-IP vers le programme 
    LabVIEW.
    ==============================================================================
*/



class RecupDataLidar  {

    public static int UNKNOWN = 12000000;
    public static int distance = UNKNOWN;
    public static int distanceObstacle = UNKNOWN;
    public static int angleObstacle = 0;
     public static int ObstaclePlusProche = distanceObstacle;
    public static byte distanceEnvoyée = 0;
    public static int angle = 0;
    private static String path = "C:\\Users\\mesba\\Documents\\INSA\\3A\\S6\\SAE Fauteuil\\Logiciel lidar\\ultra_simple.exe";
    private static String portCom = "COM3";
    private static Socket socket;
    private static ProcessBuilder lidar;
    private static DataOutputStream dataOutputStream;


    public static void main (String args[]) {

        //Création du process pour le programme du LIDAR
        lidar = new ProcessBuilder(path, portCom);
        
        // L'ajout d'une interface graphique peut se faire
        
        try {
            // Connexion au serveur au serveur en TCP-IP
            InetAddress serveur = InetAddress.getByName("192.168.1.27");
            socket = new Socket(serveur, 6340);

            // Création du stream de sortie pour le serveur
            //PrintStream out = new PrintStream(socket.getOutputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());

            // Démarage du lidar (A voir pour le mettre dans une classe à part)
            Process process = lidar.start();
            String line;
            // Création du stream d'entrée venant du process
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line2 = in.readLine();

            // Attente de la première valeur du Lidar pour éliminer les chaines de caractères
            // liées au démarrage du  Lidar
            while (line2.charAt(0) != 'S') {
                line2 = in.readLine();
                System.out.println("debut : "+line2);
            }

            // Thread de temps réel
            new Thread(() -> {
            while (socket.isConnected()) {
                try {
                    
                    dataOutputStream.writeInt(ObstaclePlusProche);
                    System.out.print("Donnees envoyees ");
                    System.out.println(ObstaclePlusProche);
                    Thread.sleep(100); // 100ms delay
                } catch (Exception e) {
                    System.out.println("Thread interrupted: " + e.getMessage());
                    break;
                }
            }
            }).start();

            // Thread de fermeture d'application
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println( "\n[Shutdown Hook] Évènement intercepté : La JVM s'arrête !");
            try {
                if(socket != null && socket.isConnected()) socket.close();
                if (process != null && process.isAlive()) process.destroy();
                System.out.println("[Shutdown Hook] Nettoyage terminé.");}
            catch (IOException ex) {ex.printStackTrace();}
            }));

            // Boucle de fonctionement du Lidar
            while (true) {
                line = in.readLine();
                String regex = "[ThetaDisQ:.Côté]";
                String[] retour = line.split(regex);
                String complet = "";
                for (String s : retour) {
                    complet = complet + s;          //Concatenation des valeurs
                }
                
                String[] finale = complet.split("[ S]");        //Séparation des valeurs

                distance = (int)Float.parseFloat(finale[6])/100;
                angle = (int)Float.parseFloat(finale[4])/100;

                //Mesure valide
                if (finale[8].equals("47"))
                {
                    //Zone de mesure
                    if((angle < 45 || angle > 315))
                    {
                        //Retiens l"élément le plus proche
                        if(distanceObstacle > distance)
                        {
                            distanceObstacle = distance;
                        }
                    }
                    else
                    {
                        //En dehors de la zone de mesure
                        //MaJ données à envoyer
                        if(distanceObstacle != UNKNOWN)
                        {
                            ObstaclePlusProche = distanceObstacle;
                            if (ObstaclePlusProche > 1000) ObstaclePlusProche = 1000;
                            distanceObstacle = UNKNOWN;
                        }
                    }
                }
            
            }
            
    } catch (IOException ex) {
        ex.printStackTrace();
    
    }
   }
}