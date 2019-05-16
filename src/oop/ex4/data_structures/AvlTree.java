package oop.ex4.data_structures;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An implementation of the AVL tree data structure.
 * @author Elkana Tovey
 * @author Aviad Dudkevitch
 */
public class AvlTree implements Iterable<Integer> {

    /*Fibonacci constant to calculate min nodes*/
    private final static double FIBONACCI_1 = (1+Math.sqrt(5))/2;

    /*Same as previous*/
    private final static double FIBONACCI_2 = (1-Math.sqrt(5))/2;

    /*Constant to calculate the largest tree of a given height*/
    private final static double LARGE_TREE_1 = 1/(Math.sqrt(5));

    /*Same as previous*/
    private final static double LARGE_TREE_2 = 3;

    /*The allowed balance factor in the tree*/
    private static final int ALLOWED_BALANCE_DIFFERENCE =2;

    /*Relationship between a node and it's parent*/
    private enum kindOfChild{RIGHT_CHILD, LEFT_CHILD, NO_PARENT};

    /*The number of nodes in the tree*/
    private int numberOfNodes;

    /*The root node of the AVL tree*/
    private TreeNode rootNode;


    /**
     * The default constructor.
     */
    public AvlTree() {
        rootNode = null;
        numberOfNodes = 0;
    }

    /**
     * A constructor that builds a new AVL tree containing all unique values in an input
     * array.
     *
     * @param data the values to add to tree.
     */
    public AvlTree(int[] data) {
        this();
        if (data !=null) {
            for (int i : data) {
                this.add(i);
            }
        }
    }

    /**
     * A copy constructor that creates a deep copy of the given AvlTree. The new tree
     * contains all the values of the given tree, but not necessarily in the same structure.
     *
     * @param avlTree an AVL tree.
     */
    public AvlTree(AvlTree avlTree) {  //edge case
        this();
        if(avlTree != null){
            for (int i: avlTree) {
                add(i);
            }
        }
    }

    /**
     * Calculates the minimum number of nodes in an AVL tree of height h.
     *
     * @param h the height of the tree (a non-negative number in question.
     * @return the minimum number of nodes in an AVL tree of the given height.
     */
    public static int findMinNodes(int h) {
        return (int) (LARGE_TREE_1*(Math.pow(FIBONACCI_1, (double) h+LARGE_TREE_2)-Math.pow(FIBONACCI_2,
                (double) h+LARGE_TREE_2))-1);
    }

    /**
     * Calculates the maximum number of nodes in an AVL tree of height h.
     *
     * @param h the height of the tree (a non-negative number in question.
     * @return the maximum number of nodes in an AVL tree of the given height.
     */
    public static int findMaxNodes(int h) {
        return (int)Math.pow(2,h+1)-1;
    }

    /**
     * Add a new node with the given key to the tree.
     *
     * @param newValue the value of the new node to add.
     * @return if the value to add is not already in the tree and it was successfully added,
     * false otherwise.
     */
    public boolean add(int newValue) {
        //If this is empty tree - make the newValue it's data.
        if (this.rootNode == null) {
            this.rootNode = new TreeNode(newValue);  // initialize empty node
            numberOfNodes++;
            return true;
        } else {
            TreeNode closestNode = findClosestNode(newValue, rootNode);
            if (closestNode.getData() == newValue) {
                return false;
            }
            else {
                insertNewNode(closestNode, newValue);
                numberOfNodes++;
                return true;
            }
        }
    }

    /**
     * This function aids add, delete and contains, by finding the node in the tree with data closest to the
     * input value,
     * it makes use of the BST property,
     * @param value the value to search for.
     * @param currentNode the node we are at
     * @return the node with the closest data to inputed value. if value is in the tree returns tha
     * matching node
     */
    private TreeNode findClosestNode(int value, TreeNode currentNode) {
        if (value == currentNode.getData()) {
            return currentNode;
        } else if (value < currentNode.getData()) {
            if (currentNode.getLeftChild() == null) {
                return currentNode;
            } else {
                return findClosestNode(value, currentNode.getLeftChild());
            }
        } else {
            // value > currentNode.getData()
            if (currentNode.getRightChild() == null) {
                return currentNode;
            } else {
                return findClosestNode(value, currentNode.getRightChild());
            }
        }
    }

    /**
     * Inset a child to a leaf node.
     * @param parentNode the father of the new node.
     * @param value the data for the new node.
     */
    private void insertNewNode(TreeNode parentNode, int value){
        if (value < parentNode.getData()){
            parentNode.setLeftChild(new TreeNode(value, parentNode));
            updateTreeHeightLeafUp(parentNode);
        }
        else {
            //value > parentNode.getData()
            parentNode.setRightChild(new TreeNode(value, parentNode));
            updateTreeHeightLeafUp(parentNode);
        }
    }

    /**
     * Update the height of every node from bottom to root. Check at each level that the AVL property is
     * not violated, if yes, rebalance the tree with rotations
     * @param currentNode - the current node to update.
     */
    private void updateTreeHeightLeafUp(TreeNode currentNode){
        currentNode.fixHeight();
        currentNode = balanceFactorCheck(currentNode);
        if (currentNode != rootNode){
            updateTreeHeightLeafUp(currentNode.getParent());
        }
    }


    /*
     * Sends the node to an appropriate rebalancing function if necessary
     * @param currentNode - the current node to check.
     */
    private TreeNode balanceFactorCheck(TreeNode currentNode){
        int balanceFactor = currentNode.getLeftChildHeight() - currentNode.getRightChildHeight();
        if (Math.abs(balanceFactor) < ALLOWED_BALANCE_DIFFERENCE) {
            return currentNode;
        }
        else if (balanceFactor < 0) {
            // The imbalance is right heavy.
            if (currentNode.getRightChild().getRightChildHeight() <
                    currentNode.getRightChild().getLeftChildHeight()) {
                // RL case
                rightRotate(currentNode.getRightChild());
                return leftRotate(currentNode);
            }
            else {
                // RR case
                return leftRotate(currentNode);
            }
        }
        else {
            // The imbalance is left heavy.
            if (currentNode.getLeftChild().getRightChildHeight() >
                    currentNode.getLeftChild().getLeftChildHeight()) {
                // LR case
                leftRotate(currentNode.getLeftChild());
                return rightRotate(currentNode);
            }
            else {
                // LL case
                return rightRotate(currentNode);
            }
        }
    }

    /**
     * A left rotation to keep the AVL principle
     * @param currentNode the TreeNode to rotate
     * @return the node that took currentNode place.
     */
    private TreeNode leftRotate(TreeNode currentNode){
        TreeNode originalRightChild = currentNode.getRightChild();
        TreeNode currentOriginalParent = currentNode.getParent();
        kindOfChild currentNodeLeftOrRight = leftOrRightSon(currentNode);
        currentNode.setRightChild(originalRightChild.getLeftChild());
        if (originalRightChild.getLeftChild() != null) {
            originalRightChild.getLeftChild().setParent(currentNode);
        }
        originalRightChild.setLeftChild(currentNode);
        currentNode.setParent(originalRightChild);
        originalRightChild.setParent(currentOriginalParent);
        currentNode.fixHeight();
        originalRightChild.fixHeight();
        parentConnectionUpdate(currentOriginalParent, originalRightChild, currentNodeLeftOrRight);
        return originalRightChild;
    }

    /**
     * a right rotation to keep the AVL principle
     * @param currentNode the TreeNode to rotate
     * @return the node that took currentNode place.
     */
    private TreeNode rightRotate(TreeNode currentNode){
        TreeNode originalLeftChild = currentNode.getLeftChild();
        TreeNode currentOriginalParent = currentNode.getParent();
        kindOfChild currentNodeLeftOrRight = leftOrRightSon(currentNode);
        currentNode.setLeftChild(originalLeftChild.getRightChild());
        if (originalLeftChild.getRightChild() != null) {
            originalLeftChild.getRightChild().setParent(currentNode);
        }
        originalLeftChild.setRightChild(currentNode);
        currentNode.setParent(originalLeftChild);
        originalLeftChild.setParent(currentOriginalParent);
        currentNode.fixHeight();
        originalLeftChild.fixHeight();
        parentConnectionUpdate(currentOriginalParent, originalLeftChild, currentNodeLeftOrRight);
        return originalLeftChild;
        }

    /**
     * Helper function for rotations, updates the relevant child and parent pointers
     * @param originalParent the original parent
     * @param originalChild the original child
     * @param sonType type of son
     */
    private void parentConnectionUpdate(TreeNode originalParent, TreeNode originalChild, kindOfChild sonType){
            switch (sonType){
                case LEFT_CHILD:
                    originalParent.setLeftChild(originalChild);
                    break;
                case RIGHT_CHILD:
                    originalParent.setRightChild(originalChild);
                    break;
                case NO_PARENT:
                    rootNode = originalChild;
            }
        }

    /**
     * Check whether the tree contains the given input value.
     *
     * @param searchVal the value to search for.
     * @return the depth of the node (0 for the root) with the given value if it was found in
     * the tree, -1 otherwise.
     */
    public int contains(int searchVal) {
        if (rootNode != null) {  // edge case
            TreeNode result = findClosestNode(searchVal, rootNode);
            if (result.getData() == searchVal) {
                int distanceFromRoot = 0;
                while (result.getParent() != null){
                    distanceFromRoot++;
                    result = result.getParent();
                }
                return distanceFromRoot;
            }
        }
        return -1; //-1 in this case isn't a magic number -
            // https://moodle2.cs.huji.ac.il/nu17/mod/forum/discuss.php?d=34648#p52614
    }


    /**
     * Remove the node with the given value from the tree, if it exists.
     *
     * @param toDelete the value to remove from the tree.
     * @return true if the given value was found and deleted, false otherwise.
     */
    public boolean delete(int toDelete){
        //If this is empty tree - there nothing to do.
        if (this.rootNode == null) {
            return false;
        }
        TreeNode closestNode = findClosestNode(toDelete, rootNode);
        if (closestNode.getData() == toDelete) {
           remove(closestNode);
           numberOfNodes--;
           return true;
        }
        else {
            return false;
        }
    }

    /**
     * Helper function for delete, deals with the three different cases of deletion by the BST property,
     * than checks to keep the AVL property.
     * @param nodeToDelete the node to delete
     */
    private void remove(TreeNode nodeToDelete){
        TreeNode parentNode = nodeToDelete.getParent();
        // Has Two subtrees.
        if (nodeToDelete.getRightChild() != null && nodeToDelete.getLeftChild() != null)
        {
            TreeNode nodeToSwitch = successor(nodeToDelete);
            nodeToDelete.setData(nodeToSwitch.getData());
            remove(nodeToSwitch);  //easier than dealing with pointers
        }
        else {
            // Has one subtree.
            if (nodeToDelete.getRightChild() != null) {
                switchNode(parentNode, nodeToDelete, nodeToDelete.getRightChild());
            } else if (nodeToDelete.getLeftChild() != null) {
                switchNode(parentNode, nodeToDelete, nodeToDelete.getLeftChild());
            }
            // leaf
            else {
                switchNode(parentNode, nodeToDelete, null);
            }
            if (nodeToDelete.getParent() != null) {
                updateTreeHeightLeafUp(parentNode);
            }
        }
    }

    /**
     * Checks if a node is a left or right child of his parent.
     * @param node the node to check
     * @return the type of child the node is(left/right)
     */
    private kindOfChild leftOrRightSon(TreeNode node){
        switch (node.kindOfSon()){
            case TreeNode.RIGHT:
                return kindOfChild.RIGHT_CHILD;
            case TreeNode.LEFT:
                return kindOfChild.LEFT_CHILD;
        }
        return kindOfChild.NO_PARENT;
    }

    /**
     *  Helper function for remove, helps manage parent/child pointers
     * @param parent parent pointer
     * @param oldNode old child pointer
     * @param newNode new child pointer
     */
    private void switchNode(TreeNode parent,TreeNode oldNode, TreeNode newNode){
        switch (leftOrRightSon(oldNode)){
            case LEFT_CHILD:
                parent.setLeftChild(newNode);
                break;
            case RIGHT_CHILD:
                parent.setRightChild(newNode);
                break;
            case NO_PARENT:
                rootNode = newNode;
                if (newNode!=null) {
                    newNode.setParent(null);
                }
        }
        if (newNode != null) {
            newNode.setParent(parent);
        }
    }

    /**
     * @return the number of nodes in the tree.
     */
    public int size(){
        return numberOfNodes;
    }

    /**
     * @return an Iterator for the Avl Tree. The returned iterator over the tree nodes
     * in an ascending order, and NOT implement the remove() method.
     */
    @Override
    public Iterator<Integer> iterator() {
        return new AvlTreeIterator();
    }

    /**
     * Successor method of the tree, uses the BST property.
     * @param previousNode - the node to check his successor. if null - return the node with the smallest data
     *                     in the tree.
     * @return iterator for the next node- if there is none throws NoSuchElementException()
     */
    private TreeNode successor(TreeNode previousNode) throws NoSuchElementException {
        if (previousNode == null){
            TreeNode currentNode = rootNode;
            if(rootNode==null) { //edgecase - empty tree
                throw new NoSuchElementException();
            }
            while (currentNode.getLeftChild() != null) {
                currentNode = currentNode.getLeftChild();  //starts from min
            }
            return currentNode;
        }
        if (previousNode.getRightChild() == null){  //find the node by BST property
            TreeNode currentNode = previousNode;
            while (currentNode != rootNode) {
                switch (leftOrRightSon(currentNode)) {
                    case RIGHT_CHILD:
                        currentNode = currentNode.getParent();
                        break;
                    case NO_PARENT:
                        throw new NoSuchElementException();
                    case LEFT_CHILD:
                        return currentNode.getParent();
                }
            }
            throw new NoSuchElementException();
        }
        TreeNode currentNode = previousNode.getRightChild();
        while (currentNode.getLeftChild() != null){
            currentNode = currentNode.getLeftChild();
        }
        return currentNode;
    }

    /**
     * returns the tree height
     * @return the height of the tree.
     */
    public int getHeight() {
        if (rootNode == null){
            return 0;
        }
        return rootNode.getHeight();
    }

    /**
     * used for testing purposes
     */
    TreeNode getRootNode() {
        return rootNode;
    }

    /**
     * The Iterator object of a Avl Tree.
     */
    private class AvlTreeIterator implements Iterator<Integer>{
        TreeNode currentNode;

        /**
         * Returns {@code true} if the iteration has more elements.
         * (In other words, returns {@code true} if {@link #next} would
         * return an element rather than throwing an exception.)
         *
         * @return {@code true} if the iteration has more elements
         */
        @Override
        public boolean hasNext() {
                try {
                    successor(currentNode);
                    return true;
                } catch (NoSuchElementException e) {
                    return false;
                }
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        @Override
        public Integer next() {
                currentNode = successor(currentNode);
                return currentNode.getData();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
