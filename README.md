### Example on how to use this utility
```
    BTConsoleRenderer p = new BTConsoleRenderer();
    BTNode<Integer> node = p
        .buildMinHeightBST(new Integer[] {2, 4, 6, 8, 10, 15, 20, 25, 35, 50, 55, 70, 89, 99, 100});
    for (String line : p.render(node)) {
      System.out.println(line);
    }
```
### sample output
![alt text](https://github.com/anshuman18/crayons.git/blob/main/documents/btree-visual.jpg?raw=true)   
