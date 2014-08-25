#include <iostream>
using namespace std;

class Const_Expr;
class Plus_Expr;

class Visitor {
public:
  virtual void visit(Const_Expr *const_expr) = 0;

  virtual void visit(Plus_Expr *plus_expr) = 0;
};

class Expr {
public:
  virtual ~Expr() { }

  virtual int eval() const = 0;

  virtual void accept(Visitor &) = 0;
};

class Const_Expr : public Expr {
public:
  Const_Expr(int value) : value(value) { }

  ~Const_Expr() { }

  int eval() const { return value; }

  void accept(Visitor &visitor) { visitor.visit(this); }

  int getValue() const { return value; }

private:
  int value;
};

class Plus_Expr : public Expr {
public:
  Plus_Expr(Expr *left, Expr *right) : left(left), right(right) { }

  ~Plus_Expr() { delete left; delete right; }

  int eval() const { return left->eval() + right->eval(); }

  void accept(Visitor &visitor) { visitor.visit(this); }

  Expr *getLeft() const { return left; }

  Expr *getRight() const { return right; }

private:
  Expr *left, *right;
};

// Define other operators similarly: Minus_Expr, Times_Expr, ...

class Infix_Visitor : public Visitor {
public:
  Infix_Visitor(ostream &out) : out(out) { }

  void visit(Const_Expr *const_expr)
  {
    out << const_expr->getValue();
  }

  void visit(Plus_Expr *plus_expr)
  {
    out << "(";
    plus_expr->getLeft()->accept(*this);
    out << "+";
    plus_expr->getRight()->accept(*this);
    out << ")";
  }

private:
  ostream &out;
};

class Postfix_Visitor : public Visitor {
public:
  Postfix_Visitor(ostream &out) : out(out) { }

  void visit(Const_Expr *const_expr)
  {
    out << const_expr->getValue() << " ";
  }

  void visit(Plus_Expr *plus_expr)
  {
    plus_expr->getLeft()->accept(*this);
    plus_expr->getRight()->accept(*this);
    out << "+ ";
  }

private:
  ostream &out;
};

int main()
{
  Expr *e1 = new Plus_Expr(new Const_Expr(7), new Const_Expr(14));
  Expr *e2 = new Const_Expr(21);
  Expr *expr = new Plus_Expr(e1, e2);

  cout << "Value is: " << expr->eval() << endl;

  Infix_Visitor infix(cout);
  cout << "Infix is: ";
  expr->accept(infix);
  cout << endl;

  Postfix_Visitor postfix(cout);
  cout << "Postfix is: ";
  expr->accept(postfix);
  cout << endl;

  delete expr;
}
