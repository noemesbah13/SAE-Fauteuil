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

    public static int distance = 12000000;
    public static int distanceObstacle = 12000000;
    public static int angleObstacle = 0;
    public static int angle = 0;
    private static String path = "C:\\Users\\mesba\\Documents\\INSA\\3A\\S6\\SAE Fauteuil\\Logiciel lidar\\ultra_simple.exe";
    private static String portCom = "COM3";


    public static void main (String args[]) {

        //Création du process pour la programme du LIDAR
        ProcessBuilder lidar = new ProcessBuilder(path, portCom);

        // L'ajout d'une interface graphique peut se faire
        
        try {
            // Connexion au serveur au serveur en TCP-IP
            InetAddress serveur = InetAddress.getByName("10.4.15.53");
            Socket socket = new Socket(serveur, 9632);

            // Création du stream de sortie pour le serveur
            PrintStream out = new PrintStream(socket.getOutputStream());

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
                /*System.out.println("distance : "+finale[6]);
                System.out.println("angle : "+finale[4]);
                System.out.println("Q : "+finale[8]);*/
                
                distance = Integer.parseInt(finale[6]);
                angle = Integer.parseInt(finale[4]);


                // analyse des données
                /* */ 
                //System.out.println("Q : "+finale[8]);
                if (finale[8].equals("47") && distanceObstacle > distance && (angle < 4500 || angle > 31500)){
                    //System.out.println("Good value ");
                    distanceObstacle = distance;
                    angleObstacle = angle;
                
                }
                if (angle>=35900 || angle<=100){

                    // Obstacle devant le fauteuil
                    if (distanceObstacle != 12000000){
                        out.println(distanceObstacle/1000);
                        
                        /*if (distanceObstacle < 70000 && distanceObstacle > 50000){
                            out.println(7);              
                            System.out.println("1");
                        }
                        //100 cm
                        else if (distanceObstacle < 50000 && distanceObstacle > 20000){
                            out.println();              // Obstacle à une distance moyenne
                            System.out.println("2");
                        }
                        //75 cm
                        else if (distanceObstacle < 75000 && distanceObstacle > 5000){
                            out.println();              // Obstacle à une distance moyenne
                            System.out.println("2");
                        }
                        // 50 cm
                        else if (distanceObstacle < 5000){
                            out.println(4);             // Obstacle très proche
                            System.out.println("4");
                        }
                        else {
                            
                        }*/
                    }
                    distanceObstacle = 12000000;
                }
             //
            }

    } catch (IOException ex) {
        ex.printStackTrace();
    
    }
   }
}