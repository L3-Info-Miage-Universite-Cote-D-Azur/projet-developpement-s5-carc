# Livraison 5

- # En terme de fonctionnalitées livrées et issues faites
    - Modification du fonctionnement des tuiles (Issue #54 et #63)
        - Passage d'une tuile ayant 5 zones à une tuile composée de 13 zones (Issue #57)
    - Ajout des couleurs sur la console (Issue #55)
        - Ajout des couleurs en fonction d'un document de configuration (Issue #52)
    - Retirer meeple placé (Issue #54)
        - Possibilité de retirer les meeples lorsque les zones fermées seront traités (Issue #49)
    - Client Serveur (Issue #56)
        - Code Server (Issue #51, #59, #62 et #63)
        - Code Client (Issue #53, #62)


- # En terme de tests (en charge : Lucas Blanc, Nathan Rihet, Loïc Le Contel, Mike Chiappe)
    - MeepleTest
        - Si le meeple existe
    - VectorTest
        - position, x, y
        - addition de vecteur
    - PlayerTest
        - Récupérer Id du joueur
        - Initialiser une partie
    - GameTest
        - Si le jeu démarre
        - jeu ne rencontre pas d'erreurs
        - S'il y a bien un gagnant
    - GameTurn
        - Chemin vers les ressources modifié

- # En terme d'organisation de code
    - Suppression des classes : SimplePlayerAI, AIPlayerBase, PlayerBase
    - Ajout des classes : AI, SimpleAI, LoggerConfig, Logger, Bounds, GameDrawUtils, GameScoreUtils, ImageDatabase,
      ExcelNode, ExcelRow, RemoveMeepleCommand, CommandFactory, CommandId, TileChunkExcelConfig, TileExcelConfig,
      Direction, Player, IPlayerListener, ChunkId, IGameListener, ClientHelloMessage, ServerHelloMessage,
      GameCommandMessage, GameCommandRequestMessage, GameDataMessage, GameResultMessage, JoinMatchmakingMessage,
      LeaveMatchmakingMessage, MatchmakingDataMessage, MatchmakingLeftMessage, Message, MessageFactory, MessageId,
      Crc32, Packet, MessageHandler, TcpClientReadHandler, TcpServerAcceptHandler, TcpClientConnection,
      TcpClientConnectionManagern TcpServerSocket, Server

- # En terme d'organisation du travail (qui a fait quoi)
    - Nous avons réfléchie au découpage des tuiles sur papier. (issue #57)
    - Sebastien Aglae s'est occupé d'ajouter les meeples sur l'affichage
    - Sebastien Aglae s'est occupé du code de départ pour le serveur Issue #51
    - Nathan Rihet s'est occupé de la première version de l'issue #55
    - Nathan Rihet a modifié le fichier JSON et correction de l'utilisation du JSON dans le code
    - Nathan s'est occupé de MeepleTest, VectorTest, PlayerTest (Issue #58)
    - Lucas Blanc a créé la moitié des tile.txt
    - Lucas Blanc a créé la classe RemoveMeepleCommand
    - Lucas Blanc s'est occupé des tests de PlayerTest (Issue #58)
    - Pour l'issue #49 Lucas a ajouté une méthode à Player et à IGameListener et sur Main
    - Loïc Le Contel a créé l'autre moitié des tile.txt
    - Loïc Le Contel s'est occupé de modifier GameTurnTest et GameTest (Issue #58)
    - Mike Chiappe a modifié le fonctionnement des tiles (Issue #57)
    - Mike Chiappe a créé le code serveur (Issue #51)
    - Mike Chiappe a ajouté le code permettant la connexion au serveur (Issue #59)
    - Mike Chiappe a ajouté le code pour communiquer avec le serveur (Issue #62)
    - Mike Chiappe a ajouté le code permettant de simuler le jeu sur le serveur (Issue #61)
    - Mike Chiappe a ajouté un parser Excel pour lire les fichiers tile.txt
    - Mike Chiappe a ajouté la javadoc sur tout le code dans la partie logic
    - Loïc Le Contel s'est occupé de remplir iteration05.md et de la user story
    - Loïc Le Contel s'occupe de la gestion du projet et de driver les collaborateurs
