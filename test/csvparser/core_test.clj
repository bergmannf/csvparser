(ns csvparser.core-test
  (:use clojure.test
        csvparser.core))

(def test-csv "value 01, 'quoted value', 1234.0")

(deftest append-value-to-line
  (testing "Appending a new value at the end adds it with a comma."
    (let [new-csv (insert-data-at-end "new" test-csv)]
      (is (= "value 01, 'quoted value', 1234.0, new" new-csv)))))

(deftest append-value-to-line-with-cond
  (testing "Appending a new value only when the line contains the word value"
    (let [match-csv (insert-data-at-end-cond
                   #(.contains %1 "value") "true" "false" test-csv)
          no-match-csv (insert-data-at-end-cond
                        #(.contains %1 "not in line") "true" "" test-csv)]
      (is (= (str test-csv ", true") match-csv))
      (is (= (str test-csv ",") (.trim no-match-csv))))))

