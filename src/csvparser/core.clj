(ns csvparser.core)
(use 'clojure-csv.core)
(use 'clojure.java.io)

(defn apply-all-funcs [funcs line]
  "Applies a list of functions sequentially to the passed sequence."
  (if (empty? funcs) line
      (recur (rest funcs) ((first funcs) line))))

(defn process-file [file-path actions]
  "Will apply the specified actions to the file"
  (with-open [rdr (reader file-path)]
    (doseq [line (line-seq rdr)]
      (println (apply-all-funcs actions line)))))

(defn insert-data-at-end [word line]
  "Append a value at the end of the line"
  (str line ", " word))

(defn insert-data-at-end-cond [cond-fn match-word no-match-word line]
  "Append a value at the end of the line when cond is trueefor the line."
  (if (cond-fn line) (insert-data-at-end match-word line)
      (insert-data-at-end no-match-word line)))

(defn insert-gegenkonto [line]
  "Appends the correct account if encountered"
  (insert-data-at-end-cond (fn gebuehren? [l]
                             (.contains l "Verkaufsgebühren"))
                             "497100" "1004035" line))

(defn insert-konto [line]
  "Appends the 'account' ??"
  (insert-data-at-end "136500" line))

;; All changes that should be applied to the file in order.
(def simple-process [insert-konto insert-gegenkonto])
