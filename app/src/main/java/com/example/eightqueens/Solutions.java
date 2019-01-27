package sandbox;

import com.example.eightqueens.Pos;

import java.util.Stack;

public class Solutions {
    Pos[][] opts = new Pos[92][8];

    public Solutions() {
        boolean[][] grid = new boolean[8][8];
        this.solve(0, grid, new Stack<Pos>(), 0);

    }

    protected int solve(int col, boolean[][] grid, Stack<Pos> stack, int count) {

        if (col >= 8) {
            this.opts[count] = stack.toArray(new Pos[8]);
            count++;
            System.out.println("Solution: " + count);
            return count;
        }
        for (int i = 0; i < 8; i++) {
            if (check(i, col, grid)) {
                grid[i][col] = true;
                stack.push(new Pos(i, col));
                count = solve(col + 1, grid, stack, count);
                stack.pop();
                grid[i][col] = false;
            }

        }
//		return false;
        return count;
    }

    protected boolean check(int x, int y, boolean[][] grid) {

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (i == x) {
                    if (grid[i][j]) {
                        return false;
                    }
                } else if (j == y) {
                    if (grid[i][j]) {
                        return false;
                    }
                } else if (y - x == j - i) {
                    // row-col matches diagonal going from top left to bottom right
                    if (grid[i][j]) {
                        return false;
                    }
                } else if (y + x == j + i) {
                    // row+col matches diagonal going from bottom left up to top right
                    if (grid[i][j]) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public boolean[][] pairs_to_grid(Pos[] pairs) {
        boolean[][] grid = new boolean[8][8];

        for (int i = 0; i < pairs.length; i++) {
            grid[pairs[i].x][pairs[i].y] = true;
        }
        return grid;
    }

    public void draw_grid(boolean[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j]) {
                    System.out.print("Q");
                } else {
                    System.out.print("*");
                }
            }
            System.out.println("");
        }
    }

    public void draw_all() {
        for (int i = 0; i < this.opts.length; i++) {
            this.draw_grid(this.pairs_to_grid(this.opts[i]));
        }
    }
}
