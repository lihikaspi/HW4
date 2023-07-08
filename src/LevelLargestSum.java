import java.util.ArrayDeque;
public class LevelLargestSum {
    /**
     * Finds the level of the tree with the largest sum of numbers
     * @param root The root node of the tree
     * @return number of level with the largest sum
     */
    public static int getLevelWithLargestSum(BinNode<Integer> root) {

        if (root == null){  //Case where tree is empty
            return -1;
        }

        int largestSum = root.getData();
        int largestSumLvl = 0;
        int currentLvl = -1;
        ArrayDeque<BinNode<Integer>> queue = new ArrayDeque<>();

        queue.add(root);

        while(!queue.isEmpty()){  //Runs until all nodes are visited
            int size = queue.size();  //Number of elements in level
            int sum = 0;
            currentLvl++;

            for(int i = size; size > 0; size--){
                BinNode<Integer> current = queue.poll();  //Removes element from tree
                sum += current.getData();

                if (current.getLeft() != null)  //Adds nodes of next level
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
