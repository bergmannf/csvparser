(ns csvparser.core)
(use 'clojure-csv.core)
(use 'clojure.java.io)

(defn process-file [file-path actions & encoding]
  "Will apply the specified actions to the file"
  (map (comp actions
             (with-open [rdr (reader file-path :encoding "utf-8")]
               (doall (line-seq rdr))))))

(defn write-data-to-file [file-path data]
  (with-open [wrtr (writer file-path :encoding "iso-8859-1")]
    (doseq [line data]
      (.write wrtr (str line (System/lineSeparator))))))

(defn insert-data-at-end [word sep line]
  "Append a value at the end of the line"
  (str line sep word))

(defn insert-data-at-end-cond [cond-fn match-word no-match-word sep line]
  "Append a value at the end of the line when cond is trueefor the line."
  (if (cond-fn line) (insert-data-at-end match-word sep line)
      (insert-data-at-end no-match-word sep line)))

(defn insert-gegenkonto [sep line]
  "Appends the correct account if encountered"
  (insert-data-at-end-cond (fn gebuehren? [l]
                             (.contains l "Verkaufsgebühr"))
                           "497100" "1004035" sep line))

(defn insert-konto [sep line]
  "Appends the 'account' ??"
  (insert-data-at-end "136500" sep line))

;; All changes that should be applied to the file in order.
(def simple-process [(partial insert-konto \tab)
                     (partial insert-gegenkonto \tab)])
