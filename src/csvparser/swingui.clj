(ns csvparser.swingui
  (:gen-class)
  (:use seesaw.core)
  (:use seesaw.chooser)
  (:import org.pushingpixels.substance.api.SubstanceLookAndFeel
           javax.swing.UIManager))

(def chosen-source-file "")

(def chosen-target-file "")

(defn choose-source-file [e]
  (set chosen-source-file (.toString (choose-file))))

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
                                 [(text :text "<quelle>")
                                 (button :text "Quelle Waehlen"
                                         :listen [:action choose-source-file])])
                                (horizontal-panel :items
                                 [(text :text "<ziel>")
                                 (button :text "Ziel Waehlen")])
                                ]))
       pack!
       show!)))
