# Livraison 3

Essentiellement axée sur les tests

- # En terme de fonctionnalitées livrées et issues faites
    - Ajout de tuiles (Issue #25)
        - Tuile de ville (fragment) (Issue #24)
        - Tuile d'abbaye (Issue #23)
    - Ajout de composants de jeu (Issue #27)
        - Système de pile de Tuile (mélange + tirage)(Issue #26)
    - Mise à jour de l'IA primitive (Issue #29)
        - Placement des tuiles de type route (Issue #28)


- # En terme de tests (en charge : Nathan Rihet, Loïc Le Contel, Mike Chiappe)
    - AI
        - Tuile bien placée à la position <0,0>
        - Tuile bien placée après une autre
    - Pioche de tuiles
        - Mélange fonctionne correctement
        - Initialisation de la configuration fonctionne
        - Pas de problème si on tente de tirer une tuile dans la pioche vide
        - Première tuile est bien la tuile START
    - Game
        - Méthode update() n'est pas appelée avant de démarrer le jeu
        - Démarrer le jeu plusieurs fois
        - Méthode getWinner() est appelée avant que le jeu se termine
        - Le gagnant est bien le gagnant

- # En terme d'organisation de code
    - Création de nouvelles Classes (TownFragmentTile, AbbeyTile)
    - Nouvelles tasks et subtasks


- # En terme d'organisation du travail (qui a fait quoi)
    - Assignation de chaque collaborateurs aux Issues sur GitHub
    - Mike Chiappe s'occupe de la gestion du projet et de driver les collaborateurs
