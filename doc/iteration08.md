# Livraison 8

- # En terme de fonctionnalitées livrées et issues faites
    - Ajouter les tuiles Tunnel et Portail Magique (issue #87)
        - Ajout des tuiles tunnel #88
        - Ajout des tuiles portail magique #86
    - Ajouter les tuiles marquée par le Dragon (issue #89)
        - Ajout des tuiles marquée par le dragon #90

- # En terme de tests (Mike Chiappe, Sebastien Aglae)
  - carcassonne-server
    - ServerTest
    - MatchTest
    - MatchmakingTest
    - ClientConnectionTest
  - carcassonne-common
    - DragonTest
    - PlaceMeepleCommandTest
    - TileTest
    - RemoveMeepleCommandTest
    - SkipMeeplePlacementCommandTest
    - GameStateTest
    - PlaceFairyCommandTest
    - VectorTest
    - FairyTest
    - GameBoardTest
    - TestUtils
    - PlaceTileDrawnCommandTest
    - RotateTileDrawnCommandTest
    - GameStateTest
    - MoveDragonCommandTest
    - GameTest
    - MessageTest
  - carcassonne-client
    - ClientTest
    - LoggerTest
    - TcpClientSocketTest
    - BoundTest
    - ImageDatabaseTest
    - PolygonTest
    - AuthenticationServiceTest
    - BattleServiceTest
    - GameControllerServiceTest
    - MessageDispatcherTest
    - ServerConnectiontest
    - AITest
    - TilePositionTest
    - TargetListTest
    - HeuristicEvaluatorTest
    - HeuristicAITest
    
- # En terme d'organisation de code
    - Classes supprimées :
        - runConfiguration.xml, SimpleAI.java, HeuristicAI.java, HeuristicMeeplePlacementEvaluator.java, MasterCommandExecutionNotifier.java, GameLogger.java, ChunkPositionConstant.java, CommandExecutorListerner.java,
        - MasterNextTurnDataCommand.java, VolcanoArea.java, CommandExecutorTest.java, MasterNextTurnDataCommandTest.java, SlaveCommandExecutionNotifier.java, 
    - Nouvelle classes :
        - README.md
        - HeuristicAI.java, FairyPlacementEvaluator.java, MeeplePlacementEvaluator.java, MeepleRemovalEvaluator.java, StatsConfig.java, ClientGameListener.java, ChunkPositionConstants.java, GameBoardView.java, ClientTestUtils.java, AITest.java,
        - HeuristicAITest.java, TilePositionTest.java, DragonMovementEvaluatorTest.java, FairyPlacementEvaluatorTest.java, HeuristicEvaluatorTest.java, MeeplePlacementEvaluatorTest.java, TilePositionEvaluatorTest.java, TargetListTest.java, LoggerTest.java,
        - MessageDispatcherTest.java, ServerConnectionTest.java, AuthenticationServiceTest.java, BattleServiceTest.java, GameControllerServiceTest.java, GameStatisticsTest.java, BoundTest.java, ImageDatabaseTest.java, PolygonTest.java, PlaceFairyCommand.java, 
        - RemoveMeepleCommand.java, Fairy.java, GameMasterNextTurnDataMessage.java, ReflectionUtils.java, 
        - TILE_AW.txt, TILE_AX.txt, TILE_AY.txt, TILE_AZ.txt, TILE_BA.txt, TILE_BB.txt
        - MoveDragonCommandTest.java, PlaceFairyCommandTest.java, DragonTest.java, FairyTest.java, AreaTest.java, ClientConnectionDeathChecker.java, OfflinePlayerAI.java, MatchTest.java, MatchmakingTest.java, ClientConnectionTest.java 

- # En terme d'organisation du travail (qui a fait quoi)
    - Mike Chiappe a fait en sorte que le dragon apparaisse selon certaines conditions
    - Mike Chiappe a amélioré l'IA
    - Mike Chiappe a réglé les bugs que SonarQ nous présentait
    - Mike Chiappe s'est occupé de faire des tests
    - Mike Chiappe a ajouté un timer sur la connexion des clients
    - Sebastien Aglae a créé le Readme
    - Sebastien Aglae s'est occupé de faire des tests
    - Sebastien Aglae a nettoyé le code grâce à SonarQ
    - Sebastien Aglae a fait de l'optimisation syntaxique
    - Nathan Rihet a implémenté les dernières tuiles
    - Nathan Rihet a commencé le rapport
    - Nathan Rihet a fait la trame du diaporama pour la soutenance
    - Lucas Blanc a ajouté les tuiles Tunnel, Portail magique, marquées par le dragon
    - Loïc Le Contel a corrigé les fichiers tile.txt
    - Loïc Le Contel s'est occupé de créer les User Story pour la livraison 08
    - Loïc Le Contel s'est occupé de remplir iteration08.md
    - Loïc Le Contel s'occupe de la gestion du projet et de driver les collaborateurs
