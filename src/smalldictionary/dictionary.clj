;;Dictionary Utility Program.
;;Contains all the dictionary utility functions.
;;Basically it reads data from the dictionary file and suffle it 
;;for suggestion purpose.
;;Author: Santa
;;Date: 2020-08-10

(ns smalldictionary.dictionary
  (:gen-class)
  (:require [clojure.java.io :as io]))

;; Input dictionary file 
(def data_file (io/file (io/resource "10000.txt")))

;; Trims leading and trailing white spaces of a line.
(defn clean_text [line_text]
  (clojure.string/trim line_text))

;;(def file_data (sort '("display" "gate" "mate" "mat" "matrix" "eat" "ate" "hate" "hater" "ply" "play" "lay" "laying" "layers" "players" "playing" "player" "play")))

;; Loads all the words from the dictionary.
(def file_data
  (sort (take 1500 (shuffle (with-open [data_reader (io/reader data_file)]
    (doall (map clean_text (line-seq data_reader))))))))


