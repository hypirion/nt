(ns com.hypirion.nt.test.prime
  (:require [simple-check.core       :as sc]
            [simple-check.generators :as gen]
            [simple-check.properties :as prop]
            [simple-check.clojure-test :as ct :refer [defspec]]
            [com.hypirion.primes :as p]
            [com.hypirion.nt.prime :refer :all]
            [clojure.test :refer :all]))

(deftest test-divisor-count
  (testing "that divisor-count returns the correct amount of divisors"
    (is (= 2 (divisor-count 2)))
    (is (= 4 (divisor-count 8)))
    (is (= 3 (divisor-count 9)))
    (is (= 729 (divisor-count 656100000000)))
    (is (= 12288 (divisor-count 10234565468127000))))
  (testing "that primes only divide 1 and themselves"
    (is (every? #(= 2 (divisor-count %)) (p/take 100)))))

(deftest test-radical
  (testing "that the radical actually return the radical"
    (is (= 42 (radical 504)))
    (is (= 6 (radical 768)))
    (is (= 17558578 (radical 70234312)))
    (is (= 600851475143 (radical 600851475143))))
  (testing "that the radical of a prime is the prime itself"
    (is (every? #(= % (radical %)) (p/take 100)))))

(deftest test-euler-totient
  (testing "that the euler totient works properly"
    (are [n expected-result]
         (= expected-result (euler-totient n))
         1 1
         2 1
         20 8
         36 12
         81 54
         90 24
         100 40
         9007199254740881 9007199254740880)))

(defspec test-gcd-idempotent
  1000
  (prop/for-all [a gen/pos-int]
    (= (gcd a a) a)))

(defspec test-lcm-idempotent
  1000
  (prop/for-all [a gen/s-pos-int]
    (= (lcm a a) a)))

(defspec test-gcd-commutative
  1000
  (prop/for-all [a gen/pos-int, b gen/pos-int]
    (= (gcd a b) (gcd b a))))

(defspec test-lcm-commutative
  1000
  (prop/for-all [a gen/s-pos-int, b gen/s-pos-int]
    (= (lcm a b) (lcm b a))))

(defspec test-gcd-associative
  1000
  (prop/for-all [a gen/pos-int, b gen/pos-int, c gen/pos-int]
    (= (gcd a (gcd b c)) (gcd (gcd a b) c))))

(defspec test-lcm-associative
  1000
  (prop/for-all [a gen/s-pos-int, b gen/s-pos-int, c gen/s-pos-int]
    (= (lcm a (lcm b c)) (lcm (lcm a b) c))))

(defspec test-gcd-absorption
  1000
  (prop/for-all [a gen/s-pos-int, b gen/pos-int]
    (= a (gcd a (lcm a b)))))

(defspec test-lcm-absorption
  1000
  (prop/for-all [a gen/s-pos-int, b gen/pos-int]
    (= a (lcm a (gcd a b)))))
