package csc424

import csc424.simplide.GUIDriver
import csc424.ctrllang.{ Language => CtrlLang }
import csc424.exprlang.{ Language => ExprLang }
import csc424.lambda.{ Language => LambdaLang }
import csc424.html.{ Language => HTMLLang }
import csc480.relalg.{ Language => RelLang }

object GUIMain extends App {
  val driver = new GUIDriver(
    "Relational Algebra" -> RelLang,
    "Control Language" -> CtrlLang,
    "Expression Language" -> ExprLang,
    "Lambda Language" -> LambdaLang,
    "HTML Tables" -> HTMLLang)
  driver.start(args)
}
