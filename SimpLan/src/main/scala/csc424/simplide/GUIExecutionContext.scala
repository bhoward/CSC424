package csc424.simplide

import java.util.Scanner
import javax.swing.SwingUtilities

import scala.swing.Swing.Runnable

class GUIExecutionContext(gui: GUI) extends ExecutionContext {
  val input = new Scanner(gui.console.getReader)
  val output = gui.console.getWriter
  def debugFlag = gui.debugModel.isSelected

  var step = 0

  def performStep(description: String) {
    step += 1
    SwingUtilities invokeAndWait (Runnable { gui.status.setText("Step " + step) })
    if (gui.debugModel.isSelected) {
      gui.console.println("Step: " + step)
      gui.console.println(description)
    }

    if (Thread.interrupted || gui.singleStepModel.isSelected) {
      gui.console.showErrorMessage("INTERRUPTED")
      gui.stopAction.setEnabled(false)
      gui.resumeAction.setEnabled(true)
      this synchronized { try { wait() } catch { case e: InterruptedException => () } }
      gui.stopAction.setEnabled(true)
      gui.resumeAction.setEnabled(false)
    }
  }

  def printError(message: String) {
    gui.console.showErrorMessage(message)
  }
}