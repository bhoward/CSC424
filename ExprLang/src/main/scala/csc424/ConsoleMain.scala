package csc424

import csc424.exprlang.Language
import csc424.simplide.ConsoleDriver

object ConsoleMain extends App {
  new ConsoleDriver(Language).start(args)
}