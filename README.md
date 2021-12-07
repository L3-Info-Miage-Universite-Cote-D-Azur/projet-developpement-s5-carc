# Instructions pour le lancement du jeu Carcassonne

## 1. Installer Maven

Si maven est déjà installé sur votre machine, il n'est pas nécessaire de faire cette étape. Passez à la suivante (étape
2) !

Cette étape consiste à installer [Maven](https://maven.apache.org/download.cgi#Files) qui est un outil de gestion et de
compréhension de projet.
> [Télécharger maven ici](https://maven.apache.org/download.cgi#Files)

1. Choisissez un endroit que vous n'allez pas supprimer. Ici nous utiliserons ce chemin "C:\Program Files\"
2. Créer un dossier dans le chemin que vous avez choisi et nommez-le 'Maven' "C:\Program Files\Maven\"
3. Créer un dossier dans le dossier Maven et nommez-le en fonction de la version utilisée, ici 3.8.2 "C:\Program
   Files\Maven\3.8.2\"
4. Ouvrer le zip "apache-maven-<version>-bin.zip" et glissez le contenu dans "C:\Program Files\Maven\3.8.2\"
5. Télécharger Maven de la colonne "Link", ligne "Binary zip archive". Le nom du fichier doit être sous cette forme "
   apache-maven-<version>-bin.zip"
6. Maintenant, vous avez ce chemin "C:\Program Files\Maven\3.8.2\bin\"
7. Sous Windows, cherchez dans Windows "Variables"
8. Ouvrer "Modifier les variables d'environnement système"
9. Cliquer sur "Variables d'environnement ..."
10. Dans variables système, chercher la variable 'Path'
11. Appuyer sur 'Modifier' puis sur 'Nouveau'
12. Entrer 'C:\Program Files\Maven\3.8.2\bin'
13. Valider

## 2. Compiler le projet

Pour prendre en compte les nouvelles modifications du projet, il est nécessaire de compiler avant de lancer le projet.

1. Pour compiler le projet, executer 'Construction de carcassonne.bat'

## 3. Lancer le projet

1. Pour lancer le projet, executer le serveur 'Serveur carcassonne.bat' (Le serveur supporte plus partie les apres les
   autres).
2. Ensuite, executer 'Client carcassonne.bat' autant de fois que vous le souhaiter (entre 2 et 5)

* Actuellement, 2 clients sous supporter.
* Le serveur ne s'arrete pas à la fin de la partie
* Les clients s'arrete à la fin de la partie
