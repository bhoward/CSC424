datatype Tree = Empty | Node of Tree * int * Tree;

fun insert Empty x = Node(Empty, x, Empty)
  | insert (t as Node(smaller, value, larger)) x =
      if x < value
        then Node(insert smaller x, value, larger)
      else if x > value
        then Node(smaller, value, insert larger x)
      else t;
      
fun insertAll t [] = t
  | insertAll t (head::tail) = insertAll (insert t head) tail;

fun contains Empty _ = false
  | contains (Node(smaller, value, larger)) x =
      if x < value
        then contains smaller x
      else if x > value
        then contains larger x
      else true;

fun traverse Empty e _ = e
  | traverse (Node(smaller, value, larger)) e n =
      n (traverse smaller e n, value, traverse larger e n);
      
let
  val tree = insertAll Empty [3, 1, 4, 1, 5, 9, 2, 6, 5];

  val preorder = traverse tree [] (fn (s, v, l) => (v :: s) @ l);
  val inorder = traverse tree [] (fn (s, v, l) => (s @ [v]) @ l);
in
  let (* No built-in for loop in SML -- define a custom one *)
    fun loop i max =
      if i > max then ()
      else (
        print ("Contains " ^ (Int.toString i) ^ ": "
          ^ (if contains tree i then "true" else "false") ^ "\n");
        loop (i+1) max
      );
  in
    loop 1 9
  end;

  print "Preorder:";
  List.app (fn n => print (" " ^ Int.toString n)) preorder;
  print "\n";

  print "Inorder:";
  List.app (fn n => print (" " ^ Int.toString n)) inorder;
  print "\n"
end;
