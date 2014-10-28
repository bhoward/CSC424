package csc424

import csc424.ctrllang.{Language => CtrlLang}
import csc424.exprlang.{Language => ExprLang}
import csc424.simplide.ConsoleDriver

object ConsoleMain extends App {
  val driver = new ConsoleDriver("--ctrl" -> CtrlLang, "--expr" -> ExprLang)
  driver.start(args)
}