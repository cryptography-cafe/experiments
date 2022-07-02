mod utils;

use std::{panic, ptr};

use jni::{
    objects::JClass,
    sys::{jbyteArray, jint},
    JNIEnv,
};

use crate::utils::exception::unwrap_exc_or;

#[no_mangle]
pub extern "system" fn Java_experiments_RustBackend_modPow(
    env: JNIEnv,
    _: JClass,
    base: jbyteArray,
    exponent: jbyteArray,
    modulus: jbyteArray,
    backend: jint,
) -> jbyteArray {
    let res = panic::catch_unwind(|| {
        let base = env.convert_byte_array(base).map_err(|e| e.to_string())?;
        let exponent = env
            .convert_byte_array(exponent)
            .map_err(|e| e.to_string())?;
        let modulus = env.convert_byte_array(modulus).map_err(|e| e.to_string())?;

        let res = match backend {
            // num-bigint
            0 => {
                use num_bigint::BigUint;

                let base = BigUint::from_bytes_be(&base);
                let exponent = BigUint::from_bytes_be(&exponent);
                let modulus = BigUint::from_bytes_be(&modulus);

                let res = base.modpow(&exponent, &modulus);
                res.to_bytes_be()
            }
            // num-bigint-dig - Fork of num-bigint with focus on cryptography.
            1 => {
                use num_bigint_dig::BigUint;

                let base = BigUint::from_bytes_be(&base);
                let exponent = BigUint::from_bytes_be(&exponent);
                let modulus = BigUint::from_bytes_be(&modulus);

                let res = base.modpow(&exponent, &modulus);
                res.to_bytes_be()
            }
            // malachite - Rust library with no `unsafe` using algorithms derived from GMP.
            2 => {
                use malachite_base::num::{
                    arithmetic::traits::ModPow, conversion::traits::PowerOf2Digits,
                };
                use malachite_nz::natural::Natural;

                let base = Natural::from_power_of_2_digits_desc(8, base.into_iter()).unwrap();
                let exponent =
                    Natural::from_power_of_2_digits_desc(8, exponent.into_iter()).unwrap();
                let modulus = Natural::from_power_of_2_digits_desc(8, modulus.into_iter()).unwrap();

                let res = base.mod_pow(&exponent, &modulus);
                res.to_power_of_2_digits_desc(8)
            }
            // rug - Rust wrapper around GMP.
            3 => {
                use rug::{integer::Order, Integer};

                let base = Integer::from_digits(&base, Order::Msf);
                let exponent = Integer::from_digits(&exponent, Order::Msf);
                let modulus = Integer::from_digits(&modulus, Order::Msf);

                let res = base.pow_mod(&exponent, &modulus).unwrap();
                res.to_digits(Order::Msf)
            }
            _ => panic!("Invalid backend"),
        };

        env.byte_array_from_slice(&res).map_err(|e| e.to_string())
    });
    unwrap_exc_or(&env, res, ptr::null_mut())
}
