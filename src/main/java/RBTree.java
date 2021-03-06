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
        } else if (value.compareTo(parentNode.value) > 0) {
            // add to the right
            Node node = new Node(value, parentNode, "red");
            parentNode.right = node;
            insertionFixup(node);
        } else if (value.compareTo(parentNode.value) < 0) {
            // add to the left
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

        // Ordinary BST deletion
        if (value.compareTo(root.value) < 0) {
            root.left = delete(root.left, value);
        } else if (value.compareTo(root.value) > 0) {
            root.right = delete(root.right, value);
        } else {
            // If leaf node is red, simply remove it
            // If leaf node is black, make it null and DB
            if (root.left == null && root.right == null) {
                if (root.color.equals("red")) {
                    root = null;
                } else if (root.color.equals("black")) {
                    root.doubly_black = true;
                    root.value = null;

                }
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

        if (root.value == null)
        {
            root = null;
        }

        return root;
    }

    // Helper methods

    private void insertionFixup(Node n) {
        // 6 Cases ( 3 Symmetric )

        if (n.parent == null) {
            // If the node has no parent, this means its a root node , color it black
            n.color = "black";
            root = n;
            return;
        }

        // Only do correction if the parent is red
        if (n.parent.color.equals("red")) {

            if (n.parent == n.parent.parent.left) // Parent is a left child
            {
                Node uncle = n.parent.parent.right;

                if (uncle == null || uncle.color.equals("black")) {
                    // Rotation is needed
                    if (n == n.parent.right) {
                        // Node is a right child
                        // LR case
                        n = n.parent;   // n.parent will be the node at the leaf after rotation
                        leftRotation(n); // turns it into a LL case
                    }
                    // LL case
                    n.parent.switchColor();
                    n.parent.parent.switchColor();
                    rightRotation(n.parent.parent);
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
                Node uncle = n.parent.parent.left;

                if (uncle == null || uncle.color.equals("black")) {
                    // Rotation is needed
                    if (n == n.parent.left) {
                        // Node is a left child
                        // RL case
                        n = n.parent;   // n.parent will be the node at the leaf after rotation
                        rightRotation(n); // turns it into a RR case
                    }
                    // RR case
                    n.parent.switchColor();
                    n.parent.parent.switchColor();
                    leftRotation(n.parent.parent);
                }
                else {
                    // parent is red, uncle is black
                    uncle.switchColor();
                    n.parent.switchColor();
                    n.parent.parent.switchColor();
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
        if (nRoot.right != null) {
            nRoot.right.parent = current;
        }
        nRoot.right = current;

        Node temp = current.parent;
        if (current == root) {
            root = nRoot;
        }
        else {
            if (temp.right == current) {
                // current root of subtree is a right child
                temp.right = nRoot;
            }
            else{
                temp.left = nRoot;
            }
        }
        current.parent = nRoot;
        nRoot.parent = temp;
    }

    private void leftRotation(Node current) {

        Node nRoot = current.right;
        current.right = nRoot.left;
        if (current.right != null) {
            current.right.parent = current;
        }
        nRoot.left = current;

        Node temp = current.parent;
        if (current == root) {
            root = nRoot;
        }
        else {
            if (temp.right == current) {
                // current root of subtree is a right child
                temp.right = nRoot;
            }
            else{
                temp.left = nRoot;
            }
        }
        current.parent = nRoot;
        nRoot.parent = temp;
    }

    private void flip_color(Node n, Node m) {
        String col = n.color;
        n.color = m.color;
        m.color = col;
    }

    private void deletionFix(Node root) {
        // If it is a left child
        if (root == root.parent.left) {
            // If sibling is black and its children are also black
            // remove DB and Add black to parent
            if (root.parent.right.color.equals("black")) {
                if (root.parent.right.right == null && root.parent.right.left == null) {
                    root.doubly_black = false;
                    // Parent is red, make it black
                    if (root.parent.color.equals("red"))
                        root.parent.color = "black";
                    else
                        // Parent is black, make it DB
                        root.parent.doubly_black = true;
                    root.parent.right.color = "red";
                }
                if(root.parent.right.left != null) {
                    // If near child of sibling is red and far child of sibling is black
                    // Rotate sibling in the opposite direction of DB
                    // flip colors of old  sibling and old near child
                    if (root.parent.right.left.color.equals("red") &&
                            (root.parent.right.right.color.equals("black") || root.parent.right.right == null)) {
                        flip_color(root.parent.right.left, root.parent.right);
                        rightRotation(root.parent.right);
                        deletionFix(root);
                    }
                }
                if(root.parent.right.right != null) {
                    // If near child of sibling is black and far child of sibling is red
                    // Rotate parent in the  direction of DB
                    // remove DB
                    // flip colors of parent and sibling
                    if ((root.parent.right.left.color.equals("black") || root.parent.right.left == null) &&
                            root.parent.right.right.color.equals("red")) {
                        flip_color(root.parent, root.parent.right);
                        leftRotation(root.parent);
                        root.parent.parent.right.color = "black";
                        root.doubly_black = false;

                    }
                }
                // If sibling is red,  swap colors of parent and sibling
                // rotate parent in the direction of DB
            } else if (root.parent.right.color.equals("red")) {
                flip_color(root.parent, root.parent.right);
                leftRotation(root.parent);
                deletionFix(root);
            }
        // If it is a right child
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
                if(root.parent.left.right != null) {
                    if (root.parent.left.right.color.equals("red") &&
                            (root.parent.left.left.color.equals("black") || root.parent.left.left == null)) {
                        flip_color(root.parent.left.right, root.parent.left);
                        leftRotation(root.parent.left);
                        deletionFix(root);
                    }
                }
                if(root.parent.left.left != null){
                    if ((root.parent.left.right.color.equals("black") || root.parent.left.right == null) &&
                            root.parent.left.left.color.equals("red")) {
                        flip_color(root.parent, root.parent.left);
                        rightRotation(root.parent);
                        root.parent.parent.left.color = "black";
                        root.doubly_black = false;

                    }
                }
            } else if (root.parent.left.color.equals("red")) {
                flip_color(root.parent, root.parent.left);
                rightRotation(root.parent);
                deletionFix(root);
            }

            // Remove doubly black node after fixing
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
        tree.insert("8");
        tree.insert("9");
        System.out.println("done");
        tree.root = tree.delete(tree.root, "3");
        System.out.println("done");

    }
}

