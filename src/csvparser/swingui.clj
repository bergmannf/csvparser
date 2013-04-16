(ns csvparser.swingui
  (:gen-class)
  (:use seesaw.core)
  (:use seesaw.chooser)
  (:use csvparser.core)
  (:require [seesaw.bind :as b])
  (:import org.pushingpixels.substance.api.SubstanceLookAndFeel
           javax.swing.UIManager
           java.io.File))

(def source-txt (text :text "<quelle>" :enabled? false))

(def target-txt (text :text "<ziel>" :enabled? true))

(def chosen-source-file (atom nil))

(def chosen-target-file (atom nil))

(defn update-txt [field text]
  (config! field :text text))

(defn choose-source-file [e]
  (reset! chosen-source-file (choose-file))
  (reset! chosen-target-file (str (.toString @chosen-source-file) ".new"))
  (update-txt source-txt (.toString @chosen-source-file))
  (update-txt target-txt (.toString @chosen-target-file)))

(defn choose-target-file [e]
  (reset! chosen-target-file (choose-file))
  (update-txt target-txt (.toString @chosen-target-file)))

(defn process-data [source target]
  (println "Working on " @chosen-source-file simple-process)
  (let [data (process-file @chosen-source-file simple-process)]
    (write-data-to-file @chosen-target-file data)))

(def source-button (button :text "Quelle Waehlen"
                           :listen [:action choose-source-file]))

(def target-button (button :text "Ziel Waehlen"))

(def cancel-button (button :text "Abbrechen"
                           :listen [:action (fn [x] (System/exit 0))]))

(def ok-button (button :text "Weiter"
                       :listen [:action
                                (fn [x] (process-data @chosen-source-file @chosen-target-file))]))

(defn -main [& args]
  (SubstanceLookAndFeel/setSkin
   "org.pushingpixels.substance.api.skin.BusinessBlueSteelSkin")
  (invoke-later
   (-> (frame :title "CSV-Parser"
              :content "",
              :on-close :exit
              :menubar (menubar :items
                                [(menu :text "File" :items [])
                                 (menu :text "Help" :items [])])
              :content (vertical-panel
                        :items [(horizontal-panel :items
                                                  [source-txt
                                                   source-button])
                                (horizontal-panel :items
                                                  [target-txt
                                                   target-button])
                                (horizontal-panel :items
                                                  [cancel-button
                                                   ok-button])]))
       pack!
       show!)))
