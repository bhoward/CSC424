package csc424

import csc424.ctrllang.Language
import csc424.simplide.ConsoleDriver

object ConsoleMain extends App {
  new ConsoleDriver(Language).start(args)
}