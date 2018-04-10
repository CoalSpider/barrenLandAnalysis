/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ben.barrenlandanalysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author Ben Norman
 */
public class Main {

    // holds "image" of land
    private final int[][] data = new int[400][600];
    // {0,292,399,307}
    private final int[] rect = {0, 292, 399, 307};
    //{“48 192 351 207”, “48 392 351 407”, “120 52 135 547”, “260 52 275 547”} 
    private final int[] r1 = {48, 192, 351, 207};
    private final int[] r2 = {48, 392, 351, 407};
    private final int[] r3 = {120, 52, 135, 547};
    private final int[] r4 = {260, 52, 275, 547};

    public static void main(String[] args) {
        Main m = new Main();
//        m.markBarrenLand();
        m.markBarrenLand(m.r1, m.r2, m.r3, m.r4);
        m.run();
    }

    // fertile land is 0, mark barren land as one
    private void markBarrenLand(int[]... rects) {
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                for (int[] r : rects) {
                    if (insideRect(i, j, r)) {
                        data[i][j] = 1;
                        break;
                    }
                }
            }
        }
    }

    // fertile land is 0, mark barren land as one
    private void markBarrenLand() {
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                if (insideRect(i, j, rect)) {
                    data[i][j] = 1;
                }
            }
        }
    }

    // point in axis alligned rect check
    private boolean insideRect(int i, int j, int[] rect) {
        return (i >= rect[0] && i <= rect[2]) && (j >= rect[1] && j <= rect[3]);
    }

    // there can be disjoint peices of farmland so we flood fill each section one by one until there is no section left
    private void run() {
        List<Integer> vals = new ArrayList<>();
        Cell current;
        while ((current = findNextPoint()) != null) {
            vals.add(iterativeFill(current));
        }
        // sort the collection in lest to greatest order
        Collections.sort(vals);
        // print out list of vals
        for (Integer i : vals) {
            System.out.printf("%d,", i);
        }
        System.out.println("");
    }

    // main flood fill algorithm
    // from a starting cell we check all four directions, then from the new cells we check all for directions, etc...
    // a stack is used to remember what cells still need processing
    private int iterativeFill(Cell c) {
        int area = 0;
        Stack<Cell> cellToCheck = new Stack<>();
        cellToCheck.push(c);
        while (cellToCheck.isEmpty() == false) {
            Cell curr = cellToCheck.pop();
            int i = curr.i;
            int j = curr.j;
            area += helper(cellToCheck, i - 1, j);
            area += helper(cellToCheck, i + 1, j);
            area += helper(cellToCheck, i, j - 1);
            area += helper(cellToCheck, i, j + 1);
        }
        return area;
    }

    // if the xy pair is valid mark it as visited and push the new cell onto the given stack
    // we also return if the location was valid: 0 for invalid 1 for valid
    private int helper(Stack<Cell> cells, int i, int j) {
        if (insideBoundsAndValid(i, j)) {
            data[i][j] = 2;
            cells.push(new Cell(i, j));
            return 1;
        }
        return 0;
    }

    // finds the next peice of fertile land ie data[i][j]==0
    private Cell findNextPoint() {
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data.length; j++) {
                if (data[i][j] == 0) {
                    return new Cell(i, j);
                }
            }
        }
        return null;
    }

    // checks if the coordiante pair is inside the farmland square and if it is fertile and unvisited
    private boolean insideBoundsAndValid(int i, int j) {
        return i >= 0
                && i < data.length
                && j >= 0
                && j < data[0].length
                && data[i][j] == 0;
    }
}

// tuple holding a int pair
class Cell {

    final int i;
    final int j;

    Cell(int i, int j) {
        this.i = i;
        this.j = j;
    }
}
