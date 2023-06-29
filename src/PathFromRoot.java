public class PathFromRoot {
    public static boolean doesPathExist(BinNode<Character> root, String str) {
        // TODO: Add your code for part A1 here...

        // does a path exists that starts at the root that begins with given string of characters
        // if str == null --> return true

        // there is a difference between capital and lowercase letters in the string

        // use recursion
        // no loops
        // only use this method
        // no imports
        // only use String class and BinNode class

        if (str == null) {
            return true;
        }

        // if the root is not the same as the beginning of the string --> no need to continue;
        if (root.getData() != str.charAt(0)) {
            return false;
        }

        // cut the first char of the string and check on each side

        if (doesPathExist(root.getLeft(), str.substring(1))) {
            return true;
        }
        if (doesPathExist(root.getRight(), str.substring(1))) {
            return true;
        }

        // only if both sides return false
        return false;
    }

}
