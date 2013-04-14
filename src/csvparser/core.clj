(ns csvparser.core
  (:gen-class))
(use 'clojure-csv.core)
(use 'clojure.java.io)

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  ;; work around dangerous default behaviour in Clojure
  (alter-var-root #'*read-eval* (constantly false))
  (println "Hello, World!"))

(defn apply-all-funcs [funcs seq]
  "Applies a list of functions sequentially to the passed sequence."
  (if (empty? seq) seq
      (recur (rest funcs) (map (first funcs) seq))))

(defn process-file [file-path actions params]
  "Will apply the specified actions to the file"
  (with-open [rdr (reader file-path)]
    (doseq [line (line-seq rdr)]
      (apply-all-funcs actions line))))

(defn insert-data-at-end [word line]
  "Append a value at the end of the line"
  (str line ", " word))

(defn insert-data-at-end-cond [cond match-word no-match-word line]
  "Append a value at the end of the line when cond is trueefor the line."
  (if (cond line) (insert-data-at-end match-word line)
      (insert-data-at-end no-match-word line)))

(defn insert-gegenkonto [line]
  "Appends the correct account if encountered"
  (fn gebuehren? [line]
    (.contains line "Verkaufsgebühren")
    (partial insert-data-at-end-cond gebuehren? "497100" "1004035" line)))

(defn insert-konto [line]
  "Appends the 'account' ??"
  (partial insert-data-at-end "136500"))
