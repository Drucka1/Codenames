# CodingWeek 2025
**TELECOM Nancy - 06/01 au 10/01**

Pour que le fichier `.jar` fonctionne correctement, il faut télécharger et placer le dossier "ressources" dans le même répertoire.

La commande pour executer le `.jar` est la suivante :  
```bash
java --module-path CHEMIN_VERS_JAVAFX_LIB --add-modules javafx.base,javafx.controls,javafx.fxml -jar grp05.jar
```  

### Bugs actuels :
- Lors du chargement d'une partie, les cartes rouges qui ont été trouvées sont affichées en bleu, à cause d'une erreur de copier/coller dans le chemin du fichier image à afficher.
- Le mode blitz ne fonctionne pas en mode "Une équipe" et "Un joueur", en raison d'un oubli (une ligne manquante où le chronomètre n'est pas appelé).
- Erreur dans l'affichage du temps.

Le bon fonctionnement de l'IA repose sur la qualité des indices de chaque thème.
