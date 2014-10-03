package csc424

import csc424.simplide.GUIDriver
import csc424.ctrllang.Language

object GUIMain extends App {
  new GUIDriver(Language).start(args)
}