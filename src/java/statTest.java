import com.google.common.primitives.Longs;
import org.apache.commons.math3.stat.inference.ChiSquareTest;
import org.apache.commons.math3.stat.inference.KolmogorovSmirnovTest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

/**
 * Created by jdshen on 3/23/15.
 */
public class statTest {
    public static int f(byte[] M, byte[] K, int r, int p, int q) {
        AES aes = new AES(r);
        aes.setKey(K);
        HashSet<Byte> seen = new HashSet<>();
        for (int b = Byte.MIN_VALUE; b <= Byte.MAX_VALUE; b++) {
            M[p] = (byte) b;
            byte[] cipher = aes.encrypt(M);
            seen.add(cipher[q]);
        }
        return seen.size();
    }

    public static void main(String[] args) {
        Random r = new Random(6857);
        int numSamples = 100000;
        long[][] bins = new long[11][256 + 1];
        double[][] samples = new double[11][numSamples];
        for (int i = 0; i < numSamples; i++) {
            if (i % 100 == 0) {
                System.out.println(i);
            }
            byte[] M = new byte[16];
            byte[] K = new byte[16];
            r.nextBytes(M);
            r.nextBytes(K);
            for (int j = 1; j <= 10; j++) {
                int x = f(M, K, j, 3, 10);
                bins[j][x]++;
                samples[j][i] = x;
            }
        }
        for (int i = 1; i <= 10; i++) {
            ArrayList<Long> a = new ArrayList<>();
            ArrayList<Long> b = new ArrayList<>();
            for (int j = 0; j <= 256; j++) {
                if (a.size() > 0) {
                    long lastA = a.get(a.size() - 1);
                    long lastB = b.get(b.size() - 1);

                    // expected is too small, rebin
                    if (lastA + lastB < 10) {
                        a.set(a.size() - 1, lastA + bins[i][j]);
                        b.set(b.size() - 1, lastB + bins[10][j]);
                        continue;
                    }
                }

                a.add(bins[i][j]);
                b.add(bins[10][j]);
            }

            // rebin one more time if the tail is too small
            long lastA = a.get(a.size() - 1);
            long lastB = b.get(b.size() - 1);
            if (lastA + lastB < 10) {
                a.remove(a.size() - 1);
                b.remove(b.size() - 1);
                a.set(a.size() - 1, lastA + a.get(a.size() - 1));
                b.set(b.size() - 1, lastB + b.get(b.size() - 1));
            }

            System.out.println(a);
            System.out.println(b);

            ChiSquareTest csTest = new ChiSquareTest();
            double p = csTest.chiSquareTestDataSetsComparison(Longs.toArray(b), Longs.toArray(a));

            System.out.println(String.format("Chi-square test: r = %d, p = %f", i, p));
            KolmogorovSmirnovTest ksTest = new KolmogorovSmirnovTest();
            p = ksTest.kolmogorovSmirnovTest(samples[i], samples[10]);
            System.out.println(String.format("K-S test: r = %d, p = %f", i, p));

        }
    }
}
