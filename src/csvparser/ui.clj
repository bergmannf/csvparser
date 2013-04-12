(ns csvparser.ui
  (:gen-class
   :extends javafx.application.Application
   :state state
   :init constructor
   :prefix "-"
   :methods [[setSourceFilePath [String] void]
             [getSourceFilePath [] String]
             [javafxapp [javafx.stage.Stage] void]])
  (:import
   [javafx.application Application]
   [javafx.scene.layout StackPane]
   [javafx.event EventHandler]
   [javafx.scene Scene]
   [javafx.scene.control Button]
   [javafx.scene.control TextField]
   [javafx.stage FileChooser]
   [javafx.stage Stage]
   [javafx.scene.layout HBox]
   [javafx.scene.layout VBox]
   [javafx.beans.property StringProperty]
   [javafx.beans.binding StringBinding]
   ))

(defn -constructor []
  [[] (atom {:sourceFilePath "none"})])

(defn ^StringProperty -sourceFilePath [this]
  (let [p (proxy [StringProperty] []
      (set [^String s]
        (swap! (.state this) into {:sourceFilePath s}))
      (get ^String []
        (@(.state this) :sourceFilePath)))]
    p))


(defn -setSourceFilePath [this ^String s]
  (.set (-sourceFilePath [this]) s))

(defn ^String -getSourceFilePath [this]
  (.get (-sourceFilePath this)))

(defn open-file-chooser [store-func]
  "Opens a filechooser"
  (proxy [EventHandler] []
    (handle [event]
      (let [fc (FileChooser. )
            file (.showOpenDialog fc nil)]
        ;; TODO: store the file somewhere.
        (store-func file)))))

;; This will store the source file.
(def chosen-source-file (ref []))

;; This will store the target file.
(def chosen-target-file (ref []))

(defn -javafxapp [this stage]
  (let [root (StackPane.)
        scene (Scene. root 300 250)
        source-file-txtbx (TextField.)
        source-file-diag-btn (Button.)
        target-file-txtbx (TextField.)
        target-file-diag-btn (Button.)
        source-hbox (HBox.)
        target-hbox (HBox.)
        vbox (VBox.)]
    (.setWidth stage 300)
    (.setHeight stage 250)
    (.setScene stage scene)
    (.bind (.textProperty source-file-txtbx) (-sourceFilePath this))
    (.setText source-file-diag-btn "Quelldatei")
    (.setOnAction source-file-diag-btn (open-file-chooser
                                 #(dosync
                                   (swap! chosen-source-file %1))))
    (.setText target-file-diag-btn "Zieldatei")
    (.setOnAction target-file-diag-btn (open-file-chooser
                                 #(dosync
                                   (swap! chosen-target-file %1))))
    (.addAll (.getChildren source-hbox) [source-file-txtbx source-file-diag-btn])
    (.addAll (.getChildren target-hbox) [target-file-txtbx target-file-diag-btn])
    (.addAll (.getChildren vbox) [source-hbox target-hbox])
    (.add (.getChildren root) vbox)
    (.show stage)))

(defn -start [this stage]
  (-javafxapp this stage))

(defn -main [& args]
  (Application/launch csvparser.ui args))
