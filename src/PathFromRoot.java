public class PathFromRoot {
    /**
     * Checks if a certain string can be constructed from a tree's beginning
     * @param root The root node of the tree
     * @param str String to look for
     * @return bool if tree has the whole string from the beginning
     */
    public static boolean doesPathExist(BinNode<Character> root, String str) {

        //Case where tree is empty
        if (str == null || str.equals("")) {
            return true;
        }

        // if the root is not the same as the beginning of the string --> no need to continue;
        if (root.getData() != str.charAt(0)) {
            return false;
        }

        if (root.getData() == str.charAt(0)) {
            // cut the first char of the string and check on each side

            if (doesPathExist(root.getLeft(), str.substring(1))) {
                return true;
            }
            if (doesPathExist(root.getRight(), str.substring(1))) {
                return true;
            }
        }

        // only if both sides return false
        return false;
    }

}
