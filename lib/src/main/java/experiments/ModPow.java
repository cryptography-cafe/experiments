package experiments;

import java.math.BigInteger;

public class ModPow {
    public static BigInteger java(BigInteger base, BigInteger exponent, BigInteger modulus) {
        return base.modPow(exponent, modulus);
    }

    private static BigInteger rust(
            BigInteger base,
            BigInteger exponent,
            BigInteger modulus,
            int backend) {
        return new BigInteger(RustBackend.modPow(
                base.toByteArray(), exponent.toByteArray(), modulus.toByteArray(), backend));
    }

    public static BigInteger num_bigint(BigInteger base, BigInteger exponent, BigInteger modulus) {
        return rust(base, exponent, modulus, 0);
    }

    public static BigInteger num_bigint_dig(BigInteger base, BigInteger exponent, BigInteger modulus) {
        return rust(base, exponent, modulus, 1);
    }

    public static BigInteger malachite(BigInteger base, BigInteger exponent, BigInteger modulus) {
        return rust(base, exponent, modulus, 2);
    }

    public static BigInteger rug(BigInteger base, BigInteger exponent, BigInteger modulus) {
        return rust(base, exponent, modulus, 3);
    }
}
