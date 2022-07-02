package experiments;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.*;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 2, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@State(Scope.Benchmark)
public class ModPowBench {
    /** 2048-bit MODP Group from RFC 3526 */
    public static final BigInteger modulus = new BigInteger(
            "FFFFFFFFFFFFFFFFC90FDAA22168C234C4C6628B80DC1CD1"
                    + "29024E088A67CC74020BBEA63B139B22514A08798E3404DD"
                    + "EF9519B3CD3A431B302B0A6DF25F14374FE1356D6D51C245"
                    + "E485B576625E7EC6F44C42E9A637ED6B0BFF5CB6F406B7ED"
                    + "EE386BFB5A899FA5AE9F24117C4B1FE649286651ECE45B3D"
                    + "C2007CB8A163BF0598DA48361C55D39A69163FA8FD24CF5F"
                    + "83655D23DCA3AD961C62F356208552BB9ED529077096966D"
                    + "670C354E4ABC9804F1746C08CA18217C32905E462E36CE3B"
                    + "E39E772C180E86039B2783A2EC07A28FB5C55DF06F4C52C9"
                    + "DE2BCBF6955817183995497CEA956AE515D2261898FA0510"
                    + "15728E5A8AACAA68FFFFFFFFFFFFFFFF",
            16);

    public BigInteger base;
    public BigInteger exponent;

    @Setup
    public void prepare() {
        SecureRandom rnd = new SecureRandom();
        this.base = new BigInteger(2048, rnd);
        this.exponent = new BigInteger(2048, rnd);
    }

    @Benchmark
    public BigInteger java() {
        return ModPow.java(this.base, this.exponent, ModPowBench.modulus);
    }

    @Benchmark
    public BigInteger num_bigint() {
        return ModPow.num_bigint(this.base, this.exponent, ModPowBench.modulus);
    }

    @Benchmark
    public BigInteger num_bigint_dig() {
        return ModPow.num_bigint_dig(this.base, this.exponent, ModPowBench.modulus);
    }

    @Benchmark
    public BigInteger malachite() {
        return ModPow.malachite(this.base, this.exponent, ModPowBench.modulus);
    }

    @Benchmark
    public BigInteger rug() {
        return ModPow.rug(this.base, this.exponent, ModPowBench.modulus);
    }

    @Benchmark
    public BigInteger ct_rug() {
        return ModPow.ct_rug(this.base, this.exponent, ModPowBench.modulus);
    }
}
