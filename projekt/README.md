# Programmierprojekt

Ein Rouenplaner, der mittels A* (OneToOne) implementiert ist.

Gruppenmitglieder:
* Anonym
* Anonym
* Nico Strohbach (3546914)

```
Die Befehle `java` und `javac` müssen auf dem verwendeten System funktionsfähig sein
```
Das Programm kann mithilfe des Befehls `./build.sh` (Linux) oder `./build.bat` (Windows) kompiliert werden.
```
Der Aufruf der Main.java geschieht mittels "java -jar server.jar GRAPHPATH"
GRAPHPATH ist der Dateipfad der Graphdatei.
Der localhost wird automatisch gestartet.
Durch Klick auf die Map wird der Marker auf den nächsten Knoten gesetzt.
Über die Buttons "Set source" und "Set destination" können der Start- und der Zielknoten ausgewählt werden.
"Clear" entfernt die gesetzten Marker und den berechneten Pfad (falls gegeben).
"Calculate Route" berechnet und zeigt die Route vom ausgewählten Startknoten zum ausgewählten Zielknoten.
```
Zum Testen verwenden Sie bitte Graphen von der Website https://fmi.uni-stuttgart.de/alg/research/stuff/ ; da diese Graphen im richtigen Format sind, am besten nehmen sie  germany.fmi.bz2 .
