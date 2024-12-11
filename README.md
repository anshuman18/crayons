### Example on how to use this utility
```
BTConsoleRenderer consoleRenderer = new BTConsoleRenderer();
BTNode<Integer> node = consoleRenderer
    .buildMinHeightBST(new Integer[] {2, 4, 6, 8, 10, 15, 20, 25, 35, 50, 55, 70, 89, 99, 100});
for (String line : consoleRenderer.render(node)) {
  System.out.println(line);
}
```
### sample output
![alt text](https://github.com/anshuman18/crayons/blob/main/documents/btree-visual.jpg?raw=true)   
