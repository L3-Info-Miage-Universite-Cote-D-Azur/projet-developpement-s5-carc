# Livraison 4
- # En terme de fonctionnalitées livrées et issues faites
  - Modification du fonctionnement des tuiles (Issue #40)
    - Passage d'une tuile ayant qu'un type à une tuile composée de 5 zones (Issue #42)
  - Ajout des statistiques de partie (Issue #40)
    - Compter le nombre de points pour chaque joueur en fonction de la localisation du meeple (Issue #39)
  - Placement des meeples (Issue #40)
    - Placement des meeples sur les tuiles (Issue #38)
  - Ajout des restrictions de placements pour les tuiles (Issue #40)
    - Faire un placement "intelligent" (Issue #37)


- # En terme de tests (en charge : Lucas Blanc)
  - Meeple
    - Deux meeples sur une tuile
  - Joueur
    - Score différent selon chunk du meeple

- # En terme d'organisation de code
  - Suppression des classes : AbbeyTile, RoadTile, StartingTile, TileFactory, TileType, TownChunkTile
  - Ajout des classes : Chunk, ChunkOffset, ChunkType, MeeplesPlacement, ChunkConfig


- # En terme d'organisation du travail (qui a fait quoi)
  - Mike Chiappe et Loïc Le Contel se sont occupés de l'issue #42
  - Sebastien Aglae s'est occupé des statistiques de partie de l'issue #39
  - Nathan Rihet et Lucas Blanc se s'ont occupé du placement des meeples de l'issue #38 
  - Loïc Le Contel s'est occupé de remplir iteration04.md et de la user story
  - Mike Chiappe s'occupe de la gestion du projet et de driver les collaborateurs
