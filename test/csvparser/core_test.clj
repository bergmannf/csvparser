(ns csvparser.core-test
  (:use [clojure.test]
        [csvparser.core]
        [clojure-csv.core]))

(def csv-with-header (str "id,name,age"
                          \newline
                          "1,John Conner,15"))

(def csv-row "value 01, 'quoted value', 1234.0")

(deftest append-value-to-line
  (testing "Appending a new value at the end adds it with the given separator."
    (let [new-csv (insert-data-at-end "new" \, csv-row)]
      (is (= "value 01, 'quoted value', 1234.0,new" new-csv)))))

(deftest append-value-to-line-with-cond
  (testing "Appending a new value only when the line contains the word value"
    (let [match-csv (insert-data-at-end-cond
                   #(.contains %1 "value") "true" "false" \, csv-row)
          no-match-csv (insert-data-at-end-cond
                        #(.contains %1 "not in line") "true" "" \, csv-row)]
      (is (= (str csv-row ",true") match-csv))
      (is (= (str csv-row ",") (.trim no-match-csv))))))

(deftest apply-multiple-functions
  (testing "Applying functions in sequence"
    (let [first-fn (partial insert-data-at-end "first" \,)
          second-fn (partial insert-data-at-end "second" \,)
          list-fns [second-fn first-fn]
          new-csv ((apply comp list-fns) csv-row)]
      (is (= (str csv-row ",first,second") new-csv)))))

(deftest apply-simple-process
  (testing "Applying functions in sequence"
    (let [new-csv ((apply comp simple-process) csv-row)
          expected (str csv-row "	1004035	136500")]
      (is (= expected new-csv)))))

(deftest header-func-for-csv-maps-to-index
  (testing "We can convert a header to the correct index using a
  generated header-func"
    (let [access-func (header-func-csv csv-with-header)]
      (is (= (access-func :id) 0))
      (is (= (access-func :notpresent) nil)))))

(deftest accessing-a-csv-column-by-header-func
  (testing "We can access the csv by using the header-func and
  a (correct) column-header"
    (let [access-func (header-func-csv csv-with-header)
          row (second (parse-csv csv-with-header))]
      (is (= (row (access-func :id)) "1"))
      (is (= (row (access-func :name)) "John Conner"))
      (is (= (row (access-func :age)) "15")))))

(deftest access-csv-by-index
  (testing "The header function does not prevent the use of an index."
    (let [access-func (header-func-csv csv-with-header)
          row (second (parse-csv csv-with-header))]
      (is (= (row (access-func 0)) "1"))
      (is (= (row (access-func 1)) "John Conner"))
      (is (= (row (access-func 2)) "15"))
      (is (= (access-func 3) nil)))))
