package csc424.simplide

import javax.swing.event.TreeModelListener
import javax.swing.tree.TreeModel

import scala.language.implicitConversions

// This is a fairly general-purpose tree model for browsing objects of case classes
class ASTTreeModel(root: Any) extends TreeModel {
  class ASTTreeNode(val node: Any) {
    lazy val children: List[ASTTreeNode] =
      if (node.isInstanceOf[Product] && node.asInstanceOf[Product].productPrefix != "") {
        val prod = node.asInstanceOf[Product]
        if (prod.productArity == 1 && prod.productElement(0).isInstanceOf[List[_]])
          // special case to treat elements of a single list child as multiple children
          prod.productElement(0).asInstanceOf[List[_]] map (new ASTTreeNode(_))
        else
          (for (i <- 0 until prod.productArity) yield new ASTTreeNode(prod.productElement(i))).toList
      } else {
        Nil
      }

    override lazy val toString =
      if (children != Nil)
        node.asInstanceOf[Product].productPrefix
      else
        node.toString
  }

  // All node objects will actually be wrapped as ASTTreeNodes
  implicit def anyref2ASTTreeNode(node: AnyRef) = node.asInstanceOf[ASTTreeNode]

  val getRoot = new ASTTreeNode(root)

  def getChild(node: AnyRef, index: Int) = node.children(index)

  def getChildCount(node: AnyRef) = node.children.size

  def isLeaf(node: AnyRef) = node.children.size == 0

  def getIndexOfChild(parent: AnyRef, child: AnyRef) = parent.children.indexOf(child)

  def addTreeModelListener(listener: TreeModelListener) {} // Ignored, because tree is immutable
  def removeTreeModelListener(listener: TreeModelListener) {}
  def valueForPathChanged(path: javax.swing.tree.TreePath, value: AnyRef) {}
}