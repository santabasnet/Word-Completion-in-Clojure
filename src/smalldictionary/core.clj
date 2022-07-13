(ns smalldictionary.core
  (:gen-class)
  (:require   [smalldictionary.dictionary :refer :all]
              [smalldictionary.ngrams :refer :all]
              [smalldictionary.vectors :refer :all]))

;; Maximum words that needs to be suggested.
(def suggestions_size 7)

;; Calculates the angle between two vectors (cos_theta) and 
;; retuns a map entry tuple.
(defn angle_with_word [query_vector, word]
  (clojure.lang.MapEntry. word (cos_theta_of (get indexed_word_vectors word) query_vector)))

;; Identifies the relevant words for the given incomplete words,
;; based on the ngram set and the inverted indices.
(defn relevant_words [i_word]
  (flatten (map (fn [gram] (get gram_words gram [])) (ngrams_of i_word))))

;; Perform the calculation of the cos_theta (angle between) the
;; given query word and the words in the dictionary utilizing
;; the query vectorizer and the suggestion word matrix.
(defn vectors_cos_theta [i_word]
  (let [query (indexed_query_vector i_word)]
    (map (partial angle_with_word query) (relevant_words i_word))))

;;Iterate over the relevant word vectors to find the similar suggestions.
(defn similar_suggestions [i_word]
  (let [suggestions (take suggestions_size (sort-by val > (vectors_cos_theta i_word)))]
    (into {} (filter (fn [entry] (> (val entry) 0.0)) suggestions))))

;; Read a word from the console.
(defn read_word []
  (println "Type incomplete word: ")
  (let [i_word (read-line)]    
      (println "\nSuggestions: ")
      (println (similar_suggestions (clojure.string/lower-case i_word)))))

;; Program starts from here.
(defn -main  [& args]  
  (read_word))

