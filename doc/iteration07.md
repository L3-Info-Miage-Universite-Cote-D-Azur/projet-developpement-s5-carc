# Livraison 7

- # En terme de fonctionnalitées livrées et issues faites
    - Nouvelle IA semi-logique (issue #80)
        - Implémentation du calcul du score heuristique #78
        - Modification de la manière de poser les tuiles #79
    - Volcan et Dragon (issue #77)
        - Ajout des tuiles Volcan #74
        - Apparition du dragon #75
        - Possibilité de déplacer le dragon #76

- # En terme d'organisation de code
    - Classes supprimées :
        - TargetEntry.java, TargetList.java, RemoveMeepleCommand.java, GameTurnExtraActionState.java, AbbeyChunk.java,
          ChunkArea.java, ChunkFactory.java, FieldChunk.java, RoadChunk.java, RoadEndChunk.java, TownChunk.java
    - Nouvelle classes :
        - TilePosition.java, HeuristicDragonEvaluator.java, HeuristicEvaluator.java,
          HeuristicMeeplePlacementEvaluator.java, HeuristicTileEvaluator.java, TargetEntry.java, TargetList.java,
          SkipMeeplePlacementCommand.java, MoveDragonCommand.java, Dragon.java, GameTurnMoveDragonState.java,
          GameTurnPlaceMeepleState.java, AbbeyArea.java, Area.java, AreaFactory.java, FieldArea.java, RoadArea.java,
          RoadEndArea.java, TownArea.java, VolcanoArea.java

- # En terme d'organisation du travail (qui a fait quoi)
    - Mike Chiappe a travaillé sur la nouvelle IA
    - Mike Chiappe s'est occupé du calcul du score lorsque les zones sont fermées
    - Mike Chiappe a modifié le découpage des zones en plusieurs sous type de zone
    - Mike Chiappe a supprimé les différents sous types de chunk
    - Mike Chiappe a ajouté la classe Dragon
    - Sebastien Aglae a ajouté le dragon à la génération d'image
    - Sebastien Aglae s'est occupé de faire des tests sur le client
    - Sebastien Aglae a créé le ReadMe pour que le client comprenne comment lancer notre programme
    - Nathan Rihet a modifié les Flags sur les tuiles
    - Nathan Rihet a ajouté et corrigé les sprites pour l'affichage
    - Nathan Rihet a ajouté la classe Volcano
    - Nathan Rihet a ajouté le Flag pour les tuiles princess
    - Lucas Blanc a ajouté les tuiles Volcan, Princesse et Dragon
    - Loïc Le Contel s'est occupé de créer la commande pour déplacer le dragon sur le plateau
    - Loïc Le Contel a corrigé les fichiers tile.txt
    - Loïc Le Contel s'est occupé de créer les User Story pour la livraison 08
    - Loïc Le Contel s'est occupé de remplir iteration07.md et des User Story
    - Loïc Le Contel s'occupe de la gestion du projet et de driver les collaborateurs
