# Livraison 6

- # En terme de fonctionnalitées livrées et issues faites
    - Client Serveur
        - Système de matchmaking (Issue #56)
    - Rotation des tuiles
        - Possibilité de faire tourner les tuiles avant de les placer #64
    - Zones fermées
        - Ajout des zones et zones fermées #48
    - Restriction de placement des meeples
        - Un seul meeple par tuile (par joueur) #65
    - Calcul de point pour zones fermées
        - Lors de la fermeture d'une zone des points sont ajouté ua joueur. #66


- # En terme de tests
    - carcassonne-server
        - SlaveCommandTest
        - MatchTest
    - carcassonne-common
        - CommandExecuorTest
        - CommandTest
        - ByteInputStreamTest
        - ByteOutputStreamTest
        - PacketTest
        - ChunkFactoryTest
        - Crc32Util
        - MessageTest
        - ResizableByteBufferTest
        - GameStateTest
        - GameStateFactoryTest
        - RotateTielDrawnCommandTest
        - CommandTypeTest
        - CommandFactoryTest
        - GameBoardTest
        - TestUtils
    - carcassonne-client
        - ClientTest

- # En terme d'organisation de code
    - Classes supprimées :
        - Math : Direction
        - Game : GameTurn
    - Nouvelle classes :
        - Tile: AbbeyChunk, ChunkArea, ChunkFactory, FieldChunk, RoadChunk, RoadEndChunk, TownChunk, TileRotation
        - State : GameState, GameStateType, GameStateFactory, GameStartState, GameOverState, GameTurnInitState,
          GameTurnPlaceTileState, GameTurnExtraActionState, GameTurnEndingState, GameTurnWaitingMasterDataState
        - Command : MasterNextTurnDataCommand, RotateTileDrawnCommand

- # En terme d'organisation du travail (qui a fait quoi)
    - Mike Chiappe et Loïc Le Contel ont réfléchie au fonctionnement et à la manière de mettre en place les zones sur
      les tuiles. (issue #48)
    - Loïc Le Contel s'est occupé de modifier le console coloring #55
    - Loïc Le Contel a ajouté la restriction de placement pour les meeples #65
    - Loïc Le Contel a ajouté un test sur GameBoardTest
    - Sebastien Aglae s'est occupé d'ajouter les chunks sur l'affichage
    - Sebastien Aglae s'est occupé d'ajouter les zones sur l'affichage
    - Sebastien Aglae s'est occupé d'ajouter les zones fermées sur l'affichage
    - Sebastien Aglae s'est occupé d'ajouter les bords de zones sur l'affichage
    - Sebastien Aglae a travaillé sur un test qui permettait de tester des cas (lance un vrai jeu)
    - Nathan Rihet et Lucas Blanc se sont occupé d'implémenter la rotation des tuiles #64
    - Nathan Rihet a corrigé des tests
    - Nathan a crée une commande pour que l'IA effectue la rotation de la tuile
    - Lucas Blanc s'est occupé de faire des tests
    - Mike Chiappe a ajouté le système de zones et zones fermées
    - Mike Chiappe s'est occupé du calcul des points lors de la fermeture de zone
    - Mike Chiappe a ajouté des test pour carcassonne-common et carcassonne-client
    - Loïc Le Contel s'est occupé de remplir iteration06.md et de la user story
    - Loïc Le Contel s'occupe de la gestion du projet et de driver les collaborateurs
