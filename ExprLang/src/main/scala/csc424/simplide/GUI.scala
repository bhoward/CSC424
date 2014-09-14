package csc424.simplide

import java.io._
import java.util.Scanner
import javax.swing._
import java.awt.BorderLayout
import java.awt.Font
import java.awt.event._

import scala.swing.Swing.Runnable

import acm.io.IOConsole

class GUI(interpreter: SimpleLanguage) { gui =>
  val source = new JTextArea
  val chooser = new JFileChooser
  val status = new JLabel
  val console = new IOConsole

  val smallFont = new Font("Monospaced", Font.PLAIN, 12)
  val largeFont = new Font("Monospaced", Font.BOLD, 24)
  var currFont = smallFont

  source.setFont(currFont)
  source.addCaretListener(new BracketMatcher)
  source.registerKeyboardAction(new AutoIndentAction,
    KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
    JComponent.WHEN_FOCUSED)
  console.setFont(currFont)

  var debugModel: ButtonModel = null
  var singleStepModel: ButtonModel = null
  var fontModel: ButtonModel = null
  var showASTModel: ButtonModel = null

  var thread: Thread = null
  var context: ExecutionContext = null

  val frame = new JFrame("SimplIDE")
  frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE)
  frame.setSize(800, 600)

  val loadAction = new AbstractAction("Load") {
    def actionPerformed(ae: ActionEvent): Unit = {
      if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
        val scanner = new Scanner(new FileReader(chooser.getSelectedFile))
        val buffer = new StringBuffer

        while (scanner.hasNextLine) {
          buffer.append(scanner.nextLine + "\n")
        }
        scanner.close

        source.setText(buffer.toString)
        reset()
      }
    }
  }

  val saveAction = new AbstractAction("Save") {
    def actionPerformed(ae: ActionEvent): Unit = {
      if (chooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
        val writer = new PrintWriter(new FileWriter(chooser.getSelectedFile))
        writer.print(source.getText)
        writer.close
      }
    }
  }

  val runAction = new AbstractAction("Run") {
    def actionPerformed(ae: ActionEvent): Unit = {
      if (thread != null && thread.isAlive) {
        thread.interrupt
        thread.join(500) // Give it half a second to be polite...
        thread = null
        context = null
      }

      console.clear

      context = new GUIExecutionContext(gui)
      context synchronized {
        thread = new Thread(Runnable {
          try {
            val ast = interpreter.parse(new StringReader(source.getText))

            if (showASTModel.isSelected) {
              val frame = new javax.swing.JFrame("AST")
              val tree = new javax.swing.JTree(new ASTTreeModel(ast))
              tree.setFont(currFont)
              frame.add(new javax.swing.JScrollPane(tree))
              frame.setSize(600, 400)
              frame.setVisible(true)
            }

            val state = interpreter.init(ast, context)

            val result = interpreter.run(state)

            console.println("DONE: " + result)
            stopAction.setEnabled(false)
          } catch {
            case e: Exception => console.showErrorMessage(e.getMessage)
          }
        })
      }

      thread.start

      stopAction.setEnabled(true)
      resumeAction.setEnabled(false)
    }
  }

  val stopAction: AbstractAction = new AbstractAction("Stop") {
    def actionPerformed(ae: ActionEvent): Unit = {
      if (thread != null && thread.isAlive) {
        thread.interrupt
      }
    }

    setEnabled(false)
  }

  val resumeAction: AbstractAction = new AbstractAction("Resume") {
    def actionPerformed(ae: ActionEvent): Unit = {
      context.synchronized {
        context.notify()
      }
    }

    setEnabled(false)
  }

  val fontAction = new AbstractAction("Large") {
    def actionPerformed(ae: ActionEvent): Unit = {
      if (fontModel.isSelected) {
        currFont = largeFont
      } else {
        currFont = smallFont
      }

      source.setFont(currFont)
      console.setFont(currFont)
    }
  }

  val buttons = new Box(BoxLayout.X_AXIS)
  buttons.add(new JButton(loadAction))
  buttons.add(new JButton(saveAction))
  buttons.add(new JButton(runAction))
  buttons.add(new JButton(stopAction))
  buttons.add(new JButton(resumeAction))

  val debugButton = new JCheckBox("Debug")
  debugModel = debugButton.getModel
  buttons.add(debugButton)

  val singleStepButton = new JCheckBox("Single Step")
  singleStepModel = singleStepButton.getModel
  buttons.add(singleStepButton)

  val fontButton = new JCheckBox(fontAction)
  fontModel = fontButton.getModel
  buttons.add(fontButton)

  val showASTButton = new JCheckBox("Show AST")
  showASTModel = showASTButton.getModel
  buttons.add(showASTButton)

  val split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(source), console)
  split.setResizeWeight(0.5)

  frame.setLayout(new BorderLayout)
  frame.add(split)
  frame.add(buttons, BorderLayout.NORTH)
  frame.add(status, BorderLayout.SOUTH)

  // Set up the (purely optional, except it gives us some key accelerators) menu bar
  val shortcutMask = java.awt.Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()

  val menuBar = new JMenuBar

  val fileMenu = new JMenu("File")

  val loadItem = new JMenuItem(loadAction)
  loadItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, shortcutMask))
  fileMenu.add(loadItem)

  val saveItem = new JMenuItem(saveAction)
  saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, shortcutMask))
  fileMenu.add(saveItem)

  fileMenu.addSeparator

  val runItem = new JMenuItem(runAction)
  runItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, shortcutMask))
  fileMenu.add(runItem)

  val stopItem = new JMenuItem(stopAction)
  stopItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, shortcutMask))
  fileMenu.add(stopItem)

  val resumeItem = new JMenuItem(resumeAction)
  resumeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, shortcutMask))
  fileMenu.add(resumeItem)

  fileMenu.addSeparator

  val quitItem = new JMenuItem(new AbstractAction("Quit") {
    def actionPerformed(ae: ActionEvent): Unit = frame.dispose
  })
  quitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, shortcutMask))
  fileMenu.add(quitItem)

  menuBar.add(fileMenu)

  val optionsMenu = new JMenu("Options")

  val debugItem = new JCheckBoxMenuItem("Debug")
  debugItem.setModel(debugModel)
  optionsMenu.add(debugItem)

  val singleStepItem = new JCheckBoxMenuItem("Single Step")
  singleStepItem.setModel(singleStepModel)
  optionsMenu.add(singleStepItem)

  val fontItem = new JCheckBoxMenuItem(fontAction)
  fontItem.setModel(fontModel)
  optionsMenu.add(fontItem)

  val showASTItem = new JCheckBoxMenuItem("Show AST")
  showASTItem.setModel(showASTModel)
  optionsMenu.add(showASTItem)

  menuBar.add(optionsMenu)

  frame.setJMenuBar(menuBar)

  def start = frame.setVisible(true)

  def reset() {} // TODO what should this do?

  // TODO standardize AST nodes, and add an AST browser (use a tree view, and link nodes to the source:
  // select a node to highlight the corresponding range of the source)
}
