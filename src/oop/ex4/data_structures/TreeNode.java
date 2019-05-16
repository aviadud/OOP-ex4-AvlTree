package oop.ex4.data_structures;

/**
 * class TreeNode is a data class meant to be used with the packaged AVL tree, it contains the underlying
 * data structure to run the avl tree
 * @author Elkana Tovey
 * @author Aviad Dudkevich
 */
class TreeNode {

    /*constant for null child height*/
    private static final int NULL_CHILD = -1;

    /*left constant*/
    public static final int LEFT = -1;

    /* right constant*/
    public static final int RIGHT = 1;

    /*pointers to related nodes*/
    private TreeNode parent, rightChild, leftChild;

    /*data field for the node, and distance form furthest leaf*/
    private int data, height;

    /**
     * The default constructor.
     *
     * @param data - the value of the node.
     */
    TreeNode(int data) {
        this.data = data;
        parent = null;
        rightChild = null;
        leftChild = null;
        height = 0;
    }

    /**
     * constructor for a node when a parent exists
     * @param data the data to insert
     * @param parent pointer to parent node
     */
    TreeNode(int data, TreeNode parent) {
        this(data);
        this.parent = parent;
    }

    /**
     * getter for data
     * @return data(int)
     */
    public int getData() {
        return data;
    }

    /**
     * setter for data
     * @param data the data to set
     */
    public void setData(int data) {
        this.data = data;
    }

    /**
     * the node's current height in relation to furthest leaf
     * @return height(int)
     */
    public int getHeight() {
        return height;
    }

    /**
     * updates the height of a node based upon it's children
     */
    public void fixHeight() {
            height = Math.max(getLeftChildHeight(), getRightChildHeight()) + 1;//1 isn't a constant as it
        // is a single use case.
    }

    /**
     * getter for the pointer to the parent node
     * @return pointer to the parent node- if none, null
     */
    public TreeNode getParent() {
        return parent;
    }

    /**
     * setter for the pointer to the parent node
     * @param parent the pointer to the new parent node
     */
    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    /**
     * getter for the right child  node pointer
     * @return pointer to the right child node
     */
    public TreeNode getRightChild() {
        return rightChild;
    }

    /**
     * setter for the right child  node pointer
     * @param rightChild  pointer to the right child node
     */
    public void setRightChild(TreeNode rightChild) {
        this.rightChild = rightChild;
    }

    /**
     * returns the height of the right child node
     * @return the height as an int, -1 if null
     */
    public int getRightChildHeight() {
        if (rightChild != null) {
            return rightChild.height;
        } else {
            return NULL_CHILD;
        }
    }

    /**
     * getter for the left child  node pointer
     * @return pointer to the left child node
     */
    public TreeNode getLeftChild() {
        return leftChild;
    }

    /**
     * setter for the left child  node pointer
     * @param leftChild  pointer to the left child node
     */
    public void setLeftChild(TreeNode leftChild) {
        this.leftChild = leftChild;
    }

    /**
     * returns the height of the left child node
     * @return the height as an int, -1 if null
     */
    public int getLeftChildHeight() {
        if (leftChild != null) {
            return leftChild.height;
        } else {
            return NULL_CHILD;
        }
    }

    /**
     * calculates if the node is a right or left child of it's parent
     * @return son type
     */
    public int kindOfSon(){
        if (parent == null){
            return 0;
        }
        if (parent.leftChild == this){
            return LEFT;
        }
        else {
            return RIGHT;
        }
    }

}

