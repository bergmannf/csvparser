(ns csvparser.core
  (gen-class))
(use 'clojure-csv)

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  ;; work around dangerous default behaviour in Clojure
  (alter-var-root #'*read-eval* (constantly false))
  (println "Hello, World!"))

(defn insert-data-at-end [line word]
  (str line word))

(defn insert-column-at [colno line word seperator]
  "Inserts the new value at the specified column number of the line.
Separator defines how new fields can be found."
  )
