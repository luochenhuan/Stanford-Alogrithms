/******************************************************************************
 * Author: Haiyu Zhen
 * Data: 2015-02-04
 *
 * Running an experiment: $ java PercolationStats arg1 arg2
 ******************************************************************************/
public class PercolationStats {
	public PercolationStats(int N, int T){
		// perform T independent experiments on an N-by-N grid

	};
	public double mean(){
		// sample mean of percolation threshold

	};                      

	public double stddev(){
		// sample standard deviation of percolation threshold
	};                    
	public double confidenceLo(){
		// low  endpoint of 95% confidence interval
	};           
	public double confidenceHi(){
		// high endpoint of 95% confidence interval
	};              

	public static void main(String[] args) {	 
		// test client (described below)
		int N = Integer.parseInt(args[0]);
       	int T = Integer.parseInt(args[1]);
       	PercolationStats p = new PercolationStats(N, T);
		System.out.println("mean                    = " + p.mean());
        System.out.println("stddev                  = " + p.stddev());
	}   
}