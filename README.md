# Gestion et traitement des données d'un Lidar RPLIDAR A2M8

[![Version](https://img.shields.io/badge/Version-1.1.0-blue.svg)](https://github.com/[ton-github]/[ton-repo])
[![License: MIT](https://img.shields.io/badge/License-Noé_MESBAH-yellow.svg)](https://opensource.org/licenses/MIT)

> Ce programme permet de récupérer les données d'un Lidar RPLIDAR A2M8 pour les traiter et envoyer la distance de l'obstacle le plus proche dans un secteur particulier via un connexion en TCP-IP

---

##  Fonctionnalités

Exprime ici ce que ton projet est capable de faire sous forme de liste :
*  **Lancement et lecture de process** : Gestion et lecture des sorties d'un programme .exe lié au Lidar.
*  **Traitement** : Traitement des données brut en données exploitables puis calcul de la distance la plus proche à cahque tour du Lidar.
*  **connexion** : Connexion en TCP-IP et envoie d'un entier de 32 bit non signé sous la forme de 4 octets.

##  Technologies Utilisées

Une liste des langages, frameworks, outils ou matériels :
* **Langages :** Java uniquement
* **Matériel / Environnement :** RPLIDAR A2M8 et cible FPGA/temps réel (NI myRio)
* **Protocoles / Outils :** connexion TCP-IP 

---

##  Installation & Démarrage

Avant de commencer, assurez-vous d'avoir installé :
* **Java Development Kit (JDK)** : Version 21 ou supérieure. Vous pouvez vérifier votre version de Java en en tapant dans un terminal :
```bash
java -version
```
* **Système d'exploitation** : Windows (requis pour l'exécution du module `.exe`).

