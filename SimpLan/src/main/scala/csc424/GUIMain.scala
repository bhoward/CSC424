package csc424

import csc424.simplide.GUIDriver
import csc424.ctrllang.{Language => CtrlLang}
import csc424.exprlang.{Language => ExprLang}

object GUIMain extends App {
  val driver = new GUIDriver("Control Language" -> CtrlLang, "Expression Language" -> ExprLang)
  driver.start(args)
}