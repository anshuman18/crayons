/**
 * 
 */
package com.anshu.cyns.render;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import com.anshu.cyns.model.BTNode;

/**
 * Binary Tree console renderer
 * 
 * @author anshumankumar
 * 
 */
public class BTConsoleRenderer {

  private static final String SPACE = "  ";
  private static final String LINK = "+";
  private static final String ANSI_GREEN_BCK = "\u001B[42m";
  private static final String ANSI_RED_BACKGROUND = "\u001B[41m";
  // Custom declaration
  private static final String ANSI_YELLOW = "\u001B[33m";
  // Declaring ANSI_RESET so that we can reset the color
  private static final String ANSI_RESET = "\u001B[0m";


  /**
   * Build minimum height Binary Tree from the input array and return the root node.
   * 
   * @param <T>
   * @param in
   * @return
   */
  public <T> BTNode<T> buildMinHeightBST(T[] in) {
    return buildMinHeightBST(in, 0, in.length - 1);
  }

  private <T> BTNode<T> buildMinHeightBST(T[] in, int sIndx, int eIndx) {
    if (sIndx > eIndx) {
      return null;
    }

    int mid = (sIndx + eIndx) / 2;
    BTNode<T> root = new BTNode<>(in[mid]);
    root.setLeft(buildMinHeightBST(in, sIndx, mid - 1));
    root.setRight(buildMinHeightBST(in, mid + 1, eIndx));
    return root;
  }

  /**
   * DFS to find the maximum depth of the Binary Tree
   */
  public <T> int maxDepth(BTNode<T> root) {
    if (root == null) {
      return 0;
    }

    int left = maxDepth(root.getLeft());
    int right = maxDepth(root.getRight());
    return Math.max(left, right) + 1;
  }


  /**
   * 
   * Given the Binary Tree root node will return level wise nodes data. The response will also
   * include null value to preserving ordering and parents child relationship so that serialisation
   * and de-serialisation of Binary Tree can be done
   * 
   * @param <T>
   * @param in
   * @return
   */
  public <T> List<List<T>> getLevelWiseSerialisedData(BTNode<T> in) {
    List<List<T>> levelWiseData = new ArrayList<>();
    Queue<BTNode<T>> q = new LinkedList<>();
    q.add(in);
    levelWiseData.add(Arrays.asList(in.getData()));

    while (!q.isEmpty()) {

      boolean childFound = enqueLevelNodes(q, q.size());
      if (childFound) {
        levelWiseData.add(q.stream()//
            .map(n -> n != null ? n.getData() : null)//
            .toList());
      }

    }

    return levelWiseData;
  }

  private <T> boolean enqueLevelNodes(Queue<BTNode<T>> q, int size) {
    boolean childFound = false;

    for (int i = 0; i < size; i++) {
      BTNode<T> current = q.poll();
      if (current != null) {

        if (!childFound && (current.getLeft() != null || current.getRight() != null)) {
          childFound = true;
        }

        q.add(current.getLeft());
        q.add(current.getRight());
      }

    }

    return childFound;
  }

  /**
   * 
   * Given BT level nodes will return renderable contents as List of String
   * 
   * @param <T>
   * @param root to the BT
   * @return
   */
  public <T> List<String> render(List<List<T>> levelNodes) {
    if (levelNodes == null || levelNodes.isEmpty()) {
      return Collections.emptyList();
    }

    int maxDepth = levelNodes.size();
    int nodesSizeAtMaxDepth = (int) Math.pow(2, maxDepth - 1.0);
    int maxColSize = nodesSizeAtMaxDepth + 3 * (nodesSizeAtMaxDepth - 1) + 2;

    List<StringBuilder> output = new ArrayList<>();
    int depth = 0;
    for (List<T> lNodes : levelNodes) {
      output.addAll(renderLevelNodesHelper(lNodes, 0, maxColSize - 1, (depth == maxDepth - 1)));
      depth++;
    }
    return output.stream().map(StringBuilder::toString).toList();
  }

  /**
   * 
   * Given the Binary tree root node will return renderable contents as List of String
   * 
   * @param <T>
   * @param root to the BT
   * @return
   */
  public <T> List<String> render(BTNode<T> root) {
    if (root == null) {
      return Collections.emptyList();
    }

    return render(getLevelWiseSerialisedData(root));
  }

  private <T> List<StringBuilder> renderLevelNodesHelper(List<T> lNodes, int startIndx, int endIndx,
      boolean leaf) {
    if (startIndx > endIndx || lNodes.isEmpty()) {
      return Collections.emptyList();
    }
    if (lNodes.size() == 1) {
      return renderNodeHelper(lNodes.get(0), startIndx, endIndx, leaf);
    }

    int mid = (startIndx + endIndx) / 2;
    List<T> leftNodes = lNodes.subList(0, lNodes.size() / 2);
    List<T> rightNodes = lNodes.subList(lNodes.size() / 2, lNodes.size());

    List<StringBuilder> left = renderLevelNodesHelper(leftNodes, startIndx, mid - 1, leaf);
    List<StringBuilder> right = renderLevelNodesHelper(rightNodes, mid + 1, endIndx, leaf);
    return merge(left, right);
  }


  private <T> List<StringBuilder> renderNodeHelper(T data, int startIndx, int endIndx,
      boolean leaf) {
    if (startIndx >= endIndx) {
      return Collections.emptyList();
    }

    List<StringBuilder> nodeLinks = new ArrayList<>();
    StringBuilder sb = new StringBuilder();
    int mid = (startIndx + endIndx) / 2;

    pad(sb, startIndx, mid - 1);// left pad
    sb.append(ANSI_RED_BACKGROUND);
    sb.append(data == null ? "NY" : data);
    sb.append(ANSI_RESET);
    pad(sb, mid + 1, endIndx);// left pad

    nodeLinks.add(sb);

    if (!leaf) {
      renderLinkHelper(data, startIndx, endIndx, mid, nodeLinks);
    }

    return nodeLinks;
  }

  private <T> void renderLinkHelper(T data, int startIndx, int endIndx, int mid,
      List<StringBuilder> nodeLinks) {

    int size = data == null ? 1 : data.toString().length();

    int lines = (mid - startIndx) / 2;
    for (int i = 1; i <= lines; i++) {
      StringBuilder sb = new StringBuilder();
      pad(sb, startIndx, mid - i - 1);// left pad mid -1
      sb.append(LINK);// mid-i

      pad(sb, mid - i + 1, mid + i - 1 + (size - 1));// middle pad

      sb.append(LINK); // mid +i
      pad(sb, mid + i + 1, endIndx);// right pad
      nodeLinks.add(sb);
    }

  }

  private void pad(StringBuilder sb, int startIndx, int endIndx) {
    while (startIndx <= endIndx) {
      sb.append(SPACE);
      startIndx++;
    }
  }

  private List<StringBuilder> merge(List<StringBuilder> left, List<StringBuilder> right) {
    if (left == null || left.isEmpty()) {
      return right;
    }
    if (right == null || right.isEmpty()) {
      return left;
    }
    for (int i = 0; i < left.size(); i++) {
      left.get(i).append(SPACE);// mid append
      left.get(i).append(right.get(i).toString());
    }
    return left;
  }

  public static void main(String[] args) {
    BTConsoleRenderer p = new BTConsoleRenderer();
    BTNode<Integer> node = p
        .buildMinHeightBST(new Integer[] {2, 4, 6, 8, 10, 15, 20, 25, 35, 50, 55, 70, 89, 99, 100});
    for (String line : p.render(node)) {
      System.out.println(line);
    }
  }

}
