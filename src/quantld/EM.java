/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quantld;

/**
 *
 * @author Felix Yanhui Fan felixfanyh@gmail.com
 */
public class EM {

    protected double tol; // Algorithm stops when sum of absolute differences between new and old haplotype frequencies is <= tol.
    protected int maxItr; // 

    // default value
    {
        tol = 0.001;
        maxItr = 1000;
    }

    public EM() {
    }

    public EM(double tol, int maxItr) {
        this.tol = tol;
        this.maxItr = maxItr;
    }

    /**
     * count genotypes
     *
     * @param gf is array of genotype values at 1st locus, coded as 0, 1, and 2
     * to represent genotypes AA, Aa, and aa.
     * @param gs is the corresponding array for 2nd locus, coded as 0, 1, and 2
     * to represent genotypes BB, Bb, and bb.
     * @return a 3 X 3 matrix of genotype counts.
     */
    private int[][] countGenotype(int[] gf, int[] gs) {
        // x is a 3 X 3 matrix of genotype counts.
        int[][] x = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
        for (int i = 0; i < gf.length; i++) {
            if (gf[i] >= 0 && gs[i] >= 0) {//only use the non-missing data, missing genotype will be coded as -9.
                x[gf[i]][gs[i]] += 1;
            }
        }
        return x;
    }

    /**
     * Excoffier-Slatkin EM algorithm for estimating haplotype frequencies. This
     * code implements the special case of the algorithm for two biallelic loci.
     * With two biallelic loci, there are 4 types of gamete, which were
     * represented as follows: AB Ab aB ab 0 1 2 3 where 'A' and 'a' are the two
     * alleles at the first locus, and 'B' and 'b' are the alleles at the second
     * locus.
     *
     * h is a array of 4 haplotype frequencies, indexed as shown above.
     *
     * The genotype at the 1st and 2nd locus were represented as follows: AA Aa
     * aa BB Bb bb 0 1 2
     *
     * x is a 3 X 3 matrix of genotype counts. e.g. x[1][2] is the number of
     * copies of the genotype Aa/bb.
     *
     * Output: hh, a revised estimate of h after a single EM step.
     *
     * @param h a array of 4 haplotype frequencies.
     * @param x x is a 3 X 3 matrix of genotype counts.
     * @return a revised estimate of h after a single EM step.
     */
    private double[] esemStep(double[] h, int[][] x) {
        // g is a 4X4 matrix of probability of the genotype made up of haplotype i and j. 
        // e.g. g[0][3] is the probability of the genotype that combines gamete 0 (AB) with gamete 3 (ab).
        double[][] g = {{0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}};
        for (int i = 0; i < 4; i++) { // Set only lower triangle of g.  Upper triangle unused
            g[i][i] = h[i] * h[i];
            for (int j = 0; j < i; j++) {
                g[i][j] = 2 * h[i] * h[j];
            }
        }

        // p is a 3X3 matrix of genotype frequencies, 
        // recoded as described for the input matrix x.  
        double[][] p = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
        p[0][0] = g[0][0];
        p[0][1] = g[1][0];
        p[0][2] = g[1][1];

        p[1][0] = g[2][0];
        p[1][1] = g[3][0] + g[2][1];
        p[1][2] = g[3][1];

        p[2][0] = g[2][2];
        p[2][1] = g[3][2];
        p[2][2] = g[3][3];

        // hh is a revised estimate of h after a single EM step.
        double[] hh = {0, 0, 0, 0};

        // number of each haplotype
        hh[0] = 2 * x[0][0] + x[0][1] + x[1][0] + x[1][1] * g[3][0] / p[1][1];
        hh[1] = x[0][1] + 2 * x[0][2] + x[1][1] * g[2][1] / p[1][1] + x[1][2];
        hh[2] = x[1][0] + x[1][1] * g[2][1] / p[1][1] + 2 * x[2][0] + x[2][1];
        hh[3] = x[1][1] * g[3][0] / p[1][1] + x[1][2] + x[2][1] + 2 * x[2][2];

        // haploid sample size
        // n is the sample size and should equal the sum of x.
        double n = hh[0] + hh[1] + hh[2] + hh[3];

        // frequencies of each haplotype
        for (int i = 0; i < 4; i++) {
            hh[i] /= n;
        }

        return hh;
    }

    /**
     * Excoffier-Slatkin EM algorithm for estimating haplotype frequencies.
     *
     * @param gf is array of genotype values at 1st locus, coded as 0, 1, and 2
     * to represent genotypes AA, Aa, and aa.
     * @param gs is the corresponding array for 2nd locus, coded as 0, 1, and 2
     * to represent genotypes BB, Bb, and bb.
     * @param h holds haplotype frequencies. Initialized as {0.25, 0.25, 0.25,
     * 0.25}
     * @param tol controls convergence. Algorithm stops when sum of absolute
     * differences between new and old haplotype frequencies is <= tol.
     * @
     * param maxItr maximum iterate
     * @return a array of 4 haplotype frequencies.
     */
    private double[] esem(int[] gf, int[] gs, double[] h, double tol, int maxItr) {
        // x is a 3 X 3 matrix of genotype counts.
        int[][] x = countGenotype(gf, gs);

        double[] hh = {0, 0, 0, 0};
        double dh = 0;
        int i;

        // check whether two snps have same genotype
        if (x[0][1] + x[0][2] + x[1][0] + x[1][2] + x[2][0] + x[2][1] == 0) {
            hh[0] = 0.5;
            hh[3] = 0.5;
            return hh;
        }

        for (i = 0; i < maxItr; i++) {
            hh = esemStep(h, x);
            dh = 0;
            for (int j = 0; j < 4; j++) {
                dh += Math.abs(h[j] - hh[j]);
                h[j] = hh[j];
            }
            if (dh <= tol) {
                break;
            }
        }

        if (dh > tol) {
            System.out.println("Warnning: EM does not converge!");
        }

        return hh;
    }

    /**
     * calculate D, D', r2, signed r2
     * @param gf is array of genotype values at 1st locus, coded as 0, 1, and 2
     * to represent genotypes AA, Aa, and aa.
     * @param gs is the corresponding array for 2nd locus, coded as 0, 1, and 2
     * to represent genotypes BB, Bb, and bb.
     * @return array of D, D', r2, signed r2
     */
    private double[] esemLD(int[] gf, int[] gs) {
        double[] h0 = {0.25, 0.25, 0.25, 0.25};

        double[] h = esem(gf, gs, h0, this.tol, this.maxItr);

        double D = h[0] * h[3] - h[1] * h[2];

        double pA = h[0] + h[1];
        double pB = h[0] + h[2];
        double qA = 1 - pA;
        double qB = 1 - pB;
        double mul = pA * pB * qA * qB;

        double Dmax;
        if (D < 0) {
            Dmax = Math.max(-pA * pB, -qA * qB);
        } else {
            Dmax = Math.min(pA * qB, qA * pB);
        }

        double r2 = D * D / mul;
        double signedR2 = r2;
        if (D < 0) {
            signedR2 *= -1;
        }

        // D prime ranges between 0 and 1
        double Dprime = D / Dmax;

        double[] ans = {D, Dprime, r2, signedR2};
        return ans;
    }

    /**
     * calculate D
     * @param gf is array of genotype values at 1st locus, coded as 0, 1, and 2
     * to represent genotypes AA, Aa, and aa.
     * @param gs is the corresponding array for 2nd locus, coded as 0, 1, and 2
     * to represent genotypes BB, Bb, and bb.
     * @return D
     */
    public double esemD(int[] gf, int[] gs) {
        double[] ans = esemLD(gf, gs);
        return ans[0];
    }

    /**
     * calculate D'
     * @param gf is array of genotype values at 1st locus, coded as 0, 1, and 2
     * to represent genotypes AA, Aa, and aa.
     * @param gs is the corresponding array for 2nd locus, coded as 0, 1, and 2
     * to represent genotypes BB, Bb, and bb.
     * @return D'
     */
    public double esemDprime(int[] gf, int[] gs) {
        double[] ans = esemLD(gf, gs);
        return ans[1];
    }

    /**
     * calculate r2
     * @param gf is array of genotype values at 1st locus, coded as 0, 1, and 2
     * to represent genotypes AA, Aa, and aa.
     * @param gs is the corresponding array for 2nd locus, coded as 0, 1, and 2
     * to represent genotypes BB, Bb, and bb.
     * @return r2
     */
    public double esemR2(int[] gf, int[] gs) {
        double[] ans = esemLD(gf, gs);
        return ans[2];
    }

    /**
     * calculate signed r2
     * @param gf is array of genotype values at 1st locus, coded as 0, 1, and 2
     * to represent genotypes AA, Aa, and aa.
     * @param gs is the corresponding array for 2nd locus, coded as 0, 1, and 2
     * to represent genotypes BB, Bb, and bb.
     * @return signed r2
     */
    public double esemSignedR2(int[] gf, int[] gs) {
        double[] ans = esemLD(gf, gs);
        return ans[3];
    }
}
