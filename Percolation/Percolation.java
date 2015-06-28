/**
 * Models an N-by-N percolation system.
 */
public class Percolation {
    private int N;
    private boolean[] nodeState;
    private WeightedQuickUnionUF uf, backwash;
    private int cnt = 0;
    
    /**
     * Creates an N-by-N grid, with all sites blocked.
     * @param N grid size.
     * @throws IllegalArgumentException unless N > 0.
     */
    public Percolation(int N) {
        this.N = N;
        if (N <= 0) {
            throw new IllegalArgumentException();
        }
        nodeState = new boolean[N * N + 2];
        uf = new WeightedQuickUnionUF(N * N + 2);
        backwash = new WeightedQuickUnionUF(N * N + 1);
        //initialize virtual top and virtual bottom
        nodeState[0] = true;
        nodeState[N * N + 1] = true;
    }
    
    /**
     * Validate row i, column j
     */
    private void validate(int i, int j) {
        if (i < 0 || j < 0 || i > N - 1 || j > N - 1)
            throw new IndexOutOfBoundsException("row or column out of bound");
        }
     
    /**
     * Opens site (i, j) if it is not open already.
     * @param i row number of site.
     * @param j column number of site.
     * @throws IndexOutOfBoundsException unless 0 <= i, j <= N - 1.
     */
    public void open(int i, int j) {
        validate(i, j);
        int index = encode(i, j);
        if (!isOpen(i, j)) { 
            nodeState[index] = true;
            cnt++;
        }
        if (index <= N) { // top row site union with virtual top
            uf.union(0, index);
            backwash.union(0, index);
        }
        if (index > N * (N - 1)) { // bottom row site union virtual bottom
            uf.union((N * N + 1), index);
        }
        if (i != 0 && isOpen(i - 1, j)) { // union upper site
            uf.union(index, encode(i - 1, j));
            backwash.union(index, encode(i - 1, j));
        }
        if (i != N - 1 && isOpen(i + 1, j)) { // union lower site
            uf.union(index, encode(i + 1, j));
            backwash.union(index, encode(i + 1, j));
        }
        if (j != 0 && isOpen(i, j - 1)) { // union left site
            uf.union(index, encode(i, j - 1));
            backwash.union(index, encode(i, j - 1));
        }
        if (j != N - 1 && isOpen(i, j + 1)) { //union right site
            uf.union(index, encode(i, j + 1));
            backwash.union(index, encode(i, j + 1));
        }
    }

    /**
     * Returns true if site (i, j) is open, and false otherwise.
     * @param i row number of site.
     * @param j column number of site.
     * @return true if site (i, j) is open, and false otherwise.
     * @throws IndexOutOfBoundsException unless 0 <= i, j <= N - 1.
     */
    public boolean isOpen(int i, int j) {
        validate(i, j);
        int index = encode(i, j);
        return nodeState[index];
    }

    /**
     * Returns true if site (i, j) is full, and false otherwise.
     * @param i row number of site.
     * @param j column number of site.
     * @return true if site (i, j) is full, and false otherwise.
     * @throws IndexOutOfBoundsException unless 0 <= i, j <= N - 1.
     */
    public boolean isFull(int i, int j) {
        validate(i, j);
        int index = encode(i, j);
        return uf.connected(0, index) && backwash.connected(0, index);
    }

    /**
     * Returns number of open sites.
     * @return number of open sites.
     */
    public int numberOfOpenSites() {
        return cnt;
    }
    
    /**
     * Returns true if the system percolates, and false otherwise.
     * @return true if the system percolates, and false otherwise.
     */
    public boolean percolates() {
        return uf.connected(0, N * N + 1);
    }

    /**
     * Returns an integer ID (1...N) for site (i, j).
     * @param i row number of site.
     * @param j column number of site.
     * @return integer ID (1...N) for the site (i, j).
     */
    private int encode(int i, int j) {
        validate(i, j);
        return i * N + j + 1;
    }

    /**
     * Test client. [DO NOT EDIT]
     * @param args command-line arguments.
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        int N = in.readInt();
        Percolation perc = new Percolation(N);
        while (!in.isEmpty()) {
            int i = in.readInt();
            int j = in.readInt();
            perc.open(i, j);
        }
        StdOut.println(perc.numberOfOpenSites() + " open sites");
        if (perc.percolates()) {
            StdOut.println("percolates");
        }
        else {
            StdOut.println("does not percolate");
        }            
    }
}
