(defproject csvposparser "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [local.oracle/javafxrt "2.2.7"]]
  :main csvparser.ui
  :aot [csvparser.ui])
