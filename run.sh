find . -iname "*.class" -exec rm {} \;

cd src

javac gui/GUI.java

java gui.GUI
