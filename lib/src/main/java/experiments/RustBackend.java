package experiments;

public class RustBackend {
    static {
        System.loadLibrary("experiments_rs");
    }

    public native static byte[] modPow(byte base[], byte exponent[], byte modulus[], int backend);
}
