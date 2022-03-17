import java.util.Objects;

import static java.lang.Math.*;

class Node{
    String value;
    int height;
    String color;
    boolean doubly_black;
    Node left, right, parent;


    Node(String number)
    {
        value = number;
        height = 1;
        color = "red";
        doubly_black = false;
    }

    Node(String number, Node parent)
    {
        this.value = number;
        this.height = 1;
        this.color = "red";
        this.doubly_black = false;
        this.parent = parent;
    }

}


class AVL {
    Node root;
    int size = 0;

    int getHeight(Node a)
    {
        if (a == null)
        {
            return 0;
        }
        return a.height;
    }

    Node rightRotation(Node current)
    {
        Node nRoot = current.left;
        current.left = nRoot.right;
        nRoot.right = current;

        current.height = max(getHeight(current.left), getHeight(current.right)) + 1;
        nRoot.height =  max(getHeight(nRoot.left), getHeight(nRoot.right)) + 1;


        return nRoot;
    }

    Node leftRotation(Node current)
    {

        Node nRoot = current.right;

        current.right = nRoot.left;


        nRoot.left = current;

        current.height = max(getHeight(current.left), getHeight(current.right)) + 1;
        nRoot.height =  max(getHeight(nRoot.left), getHeight(nRoot.right)) + 1;


        return nRoot;
    }


    int search(Node root, String value)
    {
        if (root == null)
        {
            System.out.println("NO");
            return 0;
        }

        if (value.compareTo(root.value) == 0)
        {
            System.out.println("YES");
            return 1;
        }
        else if(value.compareTo(root.value) < 0)
        {
            return search(root.left, value);
        }
        else if(value.compareTo(root.value) > 0)
        {
            return search(root.right,value);
        }

        return 0;
    }

    void getSize(Node root)
    {
        if (root != null)
        {
            this.size += 1;
            getSize(root.left);
            getSize(root.right);
        }
    }



    Node insert(Node node, Node parent, String value)
    {
        if (node == null)
        {
            return new Node(value, parent);
        }

        if (value.compareTo(node.value) < 0)
        {

            node.left = insert(node.left, node, value);

        }
        else if(value.compareTo(node.value) > 0)
        {

            node.right = insert(node.right, node,  value);
        }
        else if(value.compareTo(node.value) == 0)
            return node;

        node.height = max(getHeight(node.right), getHeight(node.left)) + 1;


        insert_fixup(node);


        return node;
    }

    private void insert_fixup(Node node) {
        if (node == this.root)
        {
            node.color = "black";
            return;
        }

        if (node.parent.parent.right == node.parent) {
            if (node.parent.color.equals("red")) {
                if (node.parent.parent.left.equals("black") || node.parent.parent.left == null) {

                    if (node.parent.parent.right.right == node) {
                        flip_color(node.parent, node.parent.parent);
                        leftRotation(node.parent.parent);
                    } else if (node.parent.parent.right.left == node) {
                        flip_color(node, node.parent.parent);
                        rightRotation(node.parent);
                        leftRotation(node.parent.parent);
                    }
                }
                else  if (node.parent.parent.left.equals("red"))
                {
                    node.parent.parent.left.color = "black";
                    node.parent.color = "black";
                    node.parent.parent.color = "red";
                    insert_fixup(root.parent);
                }
            }
        }
        if (node.parent.parent.left == node.parent) {
            if (node.parent.color.equals("red")) {
                if (node.parent.parent.right.equals("black") || node.parent.parent.right == null) {
                    if (node.parent.parent.left.left == node) {
                        flip_color(node.parent, node.parent.parent);
                        rightRotation(node.parent.parent);
                    } else if (node.parent.parent.left.right == node) {
                        flip_color(node, node.parent.parent);
                        leftRotation(node.parent);
                        rightRotation(node.parent.parent);
                    }
                }
            }
            else  if (node.parent.parent.right.equals("red"))
            {
                node.parent.parent.right.color = "black";
                node.parent.color = "black";
                node.parent.parent.color = "red";
                insert_fixup(root.parent);
            }
        }
    }


    Node delete(Node root, String value)
    {
        if(root == null)
        {
            return root;
        }

        if (value.compareTo(root.value) < 0)
        {
            root.left = delete(root.left, value);
        }
        else if(value.compareTo(root.value) > 0)
        {
            root.right = delete(root.right, value);
        }
        else
        {
                if (root.left == null && root.right == null)
                {
                    if(root.color.equals("red")) {
                        root = null;
                    }
                    else if(root.color.equals("black"))
                    {
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
                }

                else
                {
                    Node temp = lowestNode(root.right);
                    root.value = temp.value;
                    root.right = delete(root.right, temp.value);
                }
            }



        if (root == null)
        {
            return root;
        }

        if(root.doubly_black)
        {
            deletionFix(root);
        }

        return root;
    }

    private void flip_color(Node n, Node m)
    {
            String col = n.color;
            n.color = m.color;
            m.color = col;
    }


    private void deletionFix(Node root) {
        if(root == root.parent.left)
        {
            if(root.parent.right.color.equals("black"))
            {
                if (root.parent.right.right == null && root.parent.right.left == null)
                {
                    root.doubly_black = false;
                    if (root.parent.color.equals("red"))
                        root.parent.color = "black";
                    else
                        root.parent.doubly_black = true;
                    root.parent.right.color = "red";
                }
                if (root.parent.right.left.color.equals("red") &&
                        (root.parent.right.right.color.equals("black") || root.parent.right.right == null))
                {
                    flip_color(root.parent.right.left, root.parent.right);
                    rightRotation(root.parent.right);
                    deletionFix(root);
                }
                if ((root.parent.right.left.color.equals("black") || root.parent.right.left == null) &&
                        root.parent.right.right.color.equals("red") )
                {
                    flip_color(root.parent, root.parent.right);
                    leftRotation(root.parent);
                    root.parent.parent.right.color = "black";
                    root.doubly_black = false;

                }

            }
            else if(root.parent.right.color.equals("red")){
                flip_color(root.parent, root.parent.right);
                leftRotation(root.parent);
                deletionFix(root);
            }

        }
        else if(root == root.parent.right)
        {
            if(root.parent.left.color.equals("black")) {
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
            }
            else if(root.parent.left.color.equals("red")){
                flip_color(root.parent, root.parent.left);
                rightRotation(root.parent);
                deletionFix(root);
            }

            if (root.value == null)
            {
                root = null;
            }



        }
    }


    Node lowestNode(Node root)
    {
        Node temp = root;
        while (temp.left != null)
        {
            temp = temp.left;
        }

        return temp;

    }

    public static void main(String[] args)
    {
        AVL  tree = new AVL();
        tree.root = tree.insert(tree.root, null,"d");
        tree.root = tree.insert(tree.root, null,"g");

        tree.root = tree.insert(tree.root, null,"c");
        tree.root = tree.insert(tree.root, null,"e");

        tree.root = tree.insert(tree.root, null,"f");
        tree.root = tree.insert(tree.root, null,"i");
        tree.root = tree.insert(tree.root, null,"h");
        tree.root = tree.insert(tree.root, null,"x");
        tree.root = tree.insert(tree.root, null,"y");
        tree.root = tree.insert(tree.root, null,"b");
        tree.root = tree.insert(tree.root, null,"a");
        tree.root = tree.insert(tree.root, null,"z");









    }
}
