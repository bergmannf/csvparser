(ns csvparser.swingui
  (:gen-class)
  (:use [csvparser.core]
        [seesaw.core]
        [seesaw.chooser]
        [seesaw.mig]
        [seesaw.bind :only (bind)])
  (:import org.pushingpixels.substance.api.SubstanceLookAndFeel
           javax.swing.UIManager
           java.io.File
           java.nio.charset.Charset))

(def source-txt (text :text "<quelle>" :enabled? false))

(def target-txt (text :text "<ziel>" :enabled? true))

(def chosen-source-file (atom nil))

(def chosen-target-file (atom nil))

(defn update-txt [field text]
  (config! field :text text))

(defn choose-source-file [e]
  (reset! chosen-source-file (choose-file))
  (reset! chosen-target-file (str (.toString @chosen-source-file) ".new.txt"))
  (update-txt source-txt (.toString @chosen-source-file))
  (update-txt target-txt (.toString @chosen-target-file)))

(defn choose-target-file [e]
  (reset! chosen-target-file (choose-file))
  (update-txt target-txt (.toString @chosen-target-file)))

(defn process-data [source target]
  (println "Working on " @chosen-source-file simple-process)
  (let [data (process-file @chosen-source-file simple-process)]
    (write-data-to-file @chosen-target-file data)))

(def source-button (button :text "Quelle Wählen"
                           :listen [:action choose-source-file]))

(def target-button (button :text "Ziel Wählen"))

(def cancel-button (button :text "Abbrechen"
                           :listen [:action (fn [x] (System/exit 0))]))

(def ok-button (button :text "Weiter"
                       :listen [:action
                                (fn [x] (process-data @chosen-source-file @chosen-target-file))]))

(def data-table (table
                 :model [:columns [{:key :id :text "Id"}]
                         :rows [{:id "Test"}]]))

(def all-encodings (java.nio.charset.Charset/availableCharsets))

(def utf-8-enc (first (filter #(.contains (.toString %1) "UTF-8") all-encodings)))

(def encodings-combobox
  (selection! (combobox :model (vec all-encodings)) utf-8-enc))

(def main-window (frame :title "CSV-Parser"
                        :content "",
                        :on-close :exit
                        :menubar (menubar :items
                                          [(menu :text "File" :items [])
                                           (menu :text "Help" :items [])])
                        :content (mig-panel
                                  :constraints ["wrap 3" "[grow][][]" ""]
                                  :items [[source-txt "growx"] [source-button "grow"] [encodings-combobox ""]
                                          [target-txt "growx"] [target-button "grow, wrap"]
                                          [cancel-button ""] [ok-button "wrap"]
                                          [data-table "grow"]])))

(defn -main [& args]
  (SubstanceLookAndFeel/setSkin
   "org.pushingpixels.substance.api.skin.BusinessBlueSteelSkin")
  (invoke-later
   (-> main-window
       pack!
       show!)))
