(ns csvparser.swingui
  (:gen-class)
  (:use seesaw.core)
  (:import org.pushingpixels.substance.api.SubstanceLookAndFeel
           javax.swing.UIManager))

(defn laf-selector []
  (horizontal-panel
   :items ["Substance skin: "
           (combobox
            :model (vals (SubstanceLookAndFeel/getAllSkins))
            :renderer (fn [this {:keys [value]}]
                        (text! this (.getClassName value)))
            :listen [:selection (fn [e]
                                        ; Invoke later because CB doens't like changing L&F while
                                        ; it's doing stuff.
                                  (invoke-later
                                   (-> e
                                       selection
                                       .getClassName
                                       SubstanceLookAndFeel/setSkin)))])]))

(defn -main [& args]
  (SubstanceLookAndFeel/setSkin
   "org.pushingpixels.substance.api.skin.BusinessBlueSteelSkin")
  (invoke-later
   (-> (frame :title "Hello"
              :content "Hello, Seesaw",
              :on-close :exit
              :menubar (menubar :items
                                [(menu :text "File" :items [])])
              :content (vertical-panel
                        :items [(laf-selector)
                                :separator
                                (label :text "A Label")
                                (button :text "A Button")
                                (checkbox :text "A checkbox")
                                (combobox :model ["A combobox" "more" "items"])
                                (horizontal-panel
                                 :border "Some radio buttons"
                                 :items (map (partial radio :text)
                                             ["First" "Second" "Third"]))
                                (scrollable (listbox :model (range 100)))]))
       pack!
       show!)))
