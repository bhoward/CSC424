package csc424.simplide

import javax.swing.SwingUtilities
import scala.swing.Swing.Runnable

class GUIDriver(interpreter: SimpleLanguage) {
  def start(args: Array[String]) {
    // TODO use the args?  Handle drag/drop??
    
    val onOSX = System.getProperty("mrj.version") != null
    if (onOSX) {
      System.setProperty("apple.laf.useScreenMenuBar", "true")
      System.setProperty("com.apple.mrj.application.apple.menu.about.name", "SimplIDE")
    }
    
    scala.swing.Swing.onEDT {
      val gui = new GUI(interpreter)
      gui.start
    }
  }
}