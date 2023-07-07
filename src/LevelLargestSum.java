import java.util.ArrayDeque;
public class LevelLargestSum {
    public static int getLevelWithLargestSum(BinNode<Integer> root) {

        // find the level with the largest sum and return level
        // if more than one level has the same sum --> return the first level out of all (closest to root)
        // if root is empty --> return -1

        // don't have to use recursion
        // can use loops
        // only use this method
        // no imports
        // only use BinNode class and ArrayDeque class

        if (root == null){
            return -1;
        }

        int largestSum = root.getData();
        int largestSumLvl = 0;
        int currentLvl = -1;
        ArrayDeque<BinNode<Integer>> queue = new ArrayDeque<>();

        queue.add(root);

        while(!queue.isEmpty()){
            int size = queue.size();
            int sum = 0;
            currentLvl++;

            for(int i = size; size > 0; size--){
                BinNode<Integer> current = queue.poll();
                sum += current.getData();

                if (current.getLeft() != null)
                    queue.add(current.getLeft());
                if (current.getRight() != null)
                    queue.add(current.getRight());
            }

            if (sum > largestSum){
                largestSum = sum;
                largestSumLvl = currentLvl;
            }
        }

        return largestSumLvl;
    }
}
