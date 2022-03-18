class Node {
    Node left, right, parent;
    String value;
    String color;
    boolean doubly_black = false;

    Node(String value) {
        this.value = value;
        color = "red";
        doubly_black = false;
    }

    Node(String value, Node parent, String color) {
        this.value = value;
        this.parent = parent;
        this.color = color;
    }

    Node(String number, Node parent) {
        this.value = number;
        this.color = "red";
        this.doubly_black = false;
        this.parent = parent;
    }

    public void switchColor() {
        color = (color.equals("red")) ? "black" : "red";
    }
}

public class RBTree {

    private Node root;
    int size = 0;

    public Node getRoot() {
        return root;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public void clear() {
        root = null;
        size = 0;
    }

    public Node search(Node root, String value) {
        if (root == null) {
            System.out.println("NO");
            return null;
        }

        if (value.compareTo(root.value) == 0) {
            System.out.println("YES");
            return root;
        } else if (value.compareTo(root.value) < 0) {
            return search(root.left, value);
        } else if (value.compareTo(root.value) > 0) {
            return search(root.right, value);
        }

        return null;
    }

    public boolean contains(String value) {
        return search(root, value) != null;
    }

    public void insert(String value) {
        // If the tree is empty, insert the node at the root and make it black
        if (this.isEmpty()) {
            root = new Node(value, null, "black");
            size++;
            return; // No correction is needed
        }

        Node parentNode = getLeafNode(root, value);

        if (parentNode.value.compareTo(value) == 0) {
            // If there's a node with the same value , just return
            return;
        } else if (parentNode.value.compareTo(value) < 0) {
            // Parent node value less than added value >> add to the right
            Node node = new Node(value, parentNode, "red");
            parentNode.right = node;
            insertionFixup(node);
        } else if (parentNode.value.compareTo(value) > 0) {
            // Parent node value more than added value >> add to the left
            Node node = new Node(value, parentNode, "red");
            parentNode.left = node;
            insertionFixup(node);
        }
        size++;

    }

    public Node delete(Node root, String value) {
        if (root == null) {
            return null;
        }

        if (value.compareTo(root.value) < 0) {
            root.left = delete(root.left, value);
        } else if (value.compareTo(root.value) > 0) {
            root.right = delete(root.right, value);
        } else {
            if (root.left == null && root.right == null) {
                if (root.color.equals("red")) {
                    root = null;
                } else if (root.color.equals("black")) {
                    root.doubly_black = true;
                    root.value = null;

                }
//                    Node temp = null;
//                    if (temp == root.left)
//                        temp = root.right;
//                    else
//                        temp = root.left;
//
//                    if (temp == null)
//                    {
//                        temp = root;
//                        root = null;
//                    }
//                    else
//                        root = temp;
            } else {
                Node temp = lowestNode(root.right);
                root.value = temp.value;
                root.right = delete(root.right, temp.value);
            }
        }

        if (root == null) {
            return root;
        }

        if (root.doubly_black) {
            deletionFix(root);
        }

        return root;
    }

    // Helper methods

    private void insertionFixup(Node n) {
        // 6 Cases ( 3 Symmetric )

        if (n.parent == null) {
            // If the node has no parent, this means its a root node , color it black
            n.color = "black";
            return;
        }

        // Only do correction if the parent is red
        if (n.parent.color.equals("red")) {
            Node uncle = n.parent.parent.right;

            if (n.parent == n.parent.parent.left) // Parent is a left child
            {
                if (uncle == null || uncle.color.equals("black")) {
                    // Rotation is needed
                    if (n == n.parent.left) {
                        // node is also a left child
                        // LL case
                        n.parent.parent.switchColor();
                        n.parent.switchColor();
                        rightRotation(n.parent.parent);
                    }
                    else {
                        // Node is a right child
                        // LR case
                        n.switchColor(); // change node color
                        n.parent.parent.switchColor(); // Change grandparents color
                        rightRotation(n.parent);
                        leftRotation(n.parent.parent);
                    }
                }
                else {
                    // parent is red, uncle is red
                    uncle.switchColor();
                    n.parent.switchColor();
                    n.parent.parent.switchColor();
                    insertionFixup(n.parent.parent);
                }
            }
            else // Parent is a right child
            {
                if (uncle == null || uncle.color.equals("black")) {
                    // Rotation is needed
                    if (n == n.parent.right) {
                        // node is also a right child
                        // RR case
                        n.parent.parent.switchColor();
                        n.parent.switchColor();
                        leftRotation(n.parent.parent);
                    }
                    else {
                        // Node is a left child
                        // RL case
                        n.color = "black"; // change node color
                        n.parent.parent.color = "red"; // Change grandparents color
                        rightRotation(n);
                        leftRotation(n.parent);
                    }
                }
                else {
                    // parent is red, uncle is black
                    uncle.color = "black";
                    n.parent.color = "black";
                    n.parent.parent.color = "red";
                    insertionFixup(n.parent.parent);
                }
            }
        }
    }

    private Node getLeafNode(Node root, String value) {
        // Normal BST insertion but returns parent node instead

        Node parent = root;
        while (root != null) {
            parent = root;
            if (value.compareTo(root.value) < 0) {
                root = root.left;
            } else if (value.compareTo(root.value) > 0) {
                root = root.right;
            } else if (value.compareTo(root.value) == 0) {
                return root;
            }
        }

        return parent;
    }

    private void rightRotation(Node current) {
        Node nRoot = current.left;
        current.left = nRoot.right;
        nRoot.right = current;
    }

    private void leftRotation(Node current) {
        Node nRoot = current.right;
        current.right = nRoot.left;
        nRoot.left = current;
    }

    private void flip_color(Node n, Node m) {
        String col = n.color;
        n.color = m.color;
        m.color = col;
    }

    private void deletionFix(Node root) {
        if (root == root.parent.left) {
            if (root.parent.right.color.equals("black")) {
                if (root.parent.right.right == null && root.parent.right.left == null) {
                    root.doubly_black = false;
                    if (root.parent.color.equals("red"))
                        root.parent.color = "black";
                    else
                        root.parent.doubly_black = true;
                    root.parent.right.color = "red";
                }
                if (root.parent.right.left.color.equals("red") &&
                        (root.parent.right.right.color.equals("black") || root.parent.right.right == null)) {
                    flip_color(root.parent.right.left, root.parent.right);
                    rightRotation(root.parent.right);
                    deletionFix(root);
                }
                if ((root.parent.right.left.color.equals("black") || root.parent.right.left == null) &&
                        root.parent.right.right.color.equals("red")) {
                    flip_color(root.parent, root.parent.right);
                    leftRotation(root.parent);
                    root.parent.parent.right.color = "black";
                    root.doubly_black = false;

                }

            } else if (root.parent.right.color.equals("red")) {
                flip_color(root.parent, root.parent.right);
                leftRotation(root.parent);
                deletionFix(root);
            }

        } else if (root == root.parent.right) {
            if (root.parent.left.color.equals("black")) {
                if (root.parent.left.right == null && root.parent.left.left == null) {
                    root.doubly_black = false;
                    if (root.parent.color.equals("red"))
                        root.parent.color = "black";
                    else
                        root.parent.doubly_black = true;
                    root.parent.left.color = "red";
                }
                if (root.parent.left.right.color.equals("red") &&
                        (root.parent.left.left.color.equals("black") || root.parent.left.left == null)) {
                    flip_color(root.parent.left.right, root.parent.left);
                    leftRotation(root.parent.left);
                    deletionFix(root);
                }

                if ((root.parent.left.right.color.equals("black") || root.parent.left.right == null) &&
                        root.parent.left.left.color.equals("red")) {
                    flip_color(root.parent, root.parent.left);
                    rightRotation(root.parent);
                    root.parent.parent.left.color = "black";
                    root.doubly_black = false;

                }
            } else if (root.parent.left.color.equals("red")) {
                flip_color(root.parent, root.parent.left);
                rightRotation(root.parent);
                deletionFix(root);
            }

            if (root.value == null) {
                root = null;
            }


        }
    }

    private Node lowestNode(Node root) {
        Node temp = root;
        while (temp.left != null) {
            temp = temp.left;
        }

        return temp;

    }

    public static void main(String[] args) {
          RBTree tree = new RBTree();
          tree.insert("1");
          tree.insert("2");
          tree.insert("3");
          tree.insert("4");
          tree.insert("5");
          tree.insert("6");
          tree.insert("7");
//        tree.root = tree.insert(tree.root, null,"d");
//        tree.root = tree.insert(tree.root, null,"g");
//
//        tree.root = tree.insert(tree.root, null,"c");
//        tree.root = tree.insert(tree.root, null,"e");
//
//        tree.root = tree.insert(tree.root, null,"f");
//        tree.root = tree.insert(tree.root, null,"i");
//        tree.root = tree.insert(tree.root, null,"h");
//        tree.root = tree.insert(tree.root, null,"x");
//        tree.root = tree.insert(tree.root, null,"y");
//        tree.root = tree.insert(tree.root, null,"b");
//        tree.root = tree.insert(tree.root, null,"a");
//        tree.root = tree.insert(tree.root, null,"z");


    }
}

//    private void insert_fixup(Node node) {
//        if (node == this.root) {
//            node.color = "black";
//            return;
//        }
//
//        if (node.parent.parent.right == node.parent) {
//            if (node.parent.color.equals("red")) {
//                if (node.parent.parent.left.equals("black") || node.parent.parent.left == null) {
//
//                    if (node.parent.parent.right.right == node) {
//                        flip_color(node.parent, node.parent.parent);
//                        leftRotation(node.parent.parent);
//                    } else if (node.parent.parent.right.left == node) {
//                        flip_color(node, node.parent.parent);
//                        rightRotation(node.parent);
//                        leftRotation(node.parent.parent);
//                    }
//                } else if (node.parent.parent.left.equals("red")) {
//                    node.parent.parent.left.color = "black";
//                    node.parent.color = "black";
//                    node.parent.parent.color = "red";
//                    insert_fixup(root.parent);
//                }
//            }
//        }
//        if (node.parent.parent.left == node.parent) {
//            if (node.parent.color.equals("red")) {
//                if (node.parent.parent.right.equals("black") || node.parent.parent.right == null) {
//                    if (node.parent.parent.left.left == node) {
//                        flip_color(node.parent, node.parent.parent);
//                        rightRotation(node.parent.parent);
//                    } else if (node.parent.parent.left.right == node) {
//                        flip_color(node, node.parent.parent);
//                        leftRotation(node.parent);
//                        rightRotation(node.parent.parent);
//                    }
//                }
//            } else if (node.parent.parent.right.equals("red")) {
//                node.parent.parent.right.color = "black";
//                node.parent.color = "black";
//                node.parent.parent.color = "red";
//                insert_fixup(root.parent);
//            }
//        }
//    }
