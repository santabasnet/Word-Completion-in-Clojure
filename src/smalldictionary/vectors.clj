;; Vector Utility Program.
;; Contains all the vectors utility functions for computing
;; similarity between two strings. 
;; Author: Santa
;; Date: 2020-08-10

(ns smalldictionary.vectors
  (:gen-class)
  (:require    [smalldictionary.dictionary :refer :all]
               [smalldictionary.ngrams :refer :all]))

;; Default frequency of word.
(def default_frequency 0)
(def default_idf 0)
(def default_tf_idf (* default_frequency default_idf))

(defn array_of [entry]
  (vec (map (partial (fn [word gram] [gram word]) (key entry)) (val entry))))

;; Defines word and their ngrams in hash map.
(def word_grams (zipmap file_data (map ngrams_of file_data)))

;; Defines grams that are associated with words.
;; Can be optimized using reducer.
(def gram_words
  (let [vector_of_vectors (vec (map array_of word_grams))]
    (let [gram_groups (group-by (fn [item] (first item)) (map (fn [item] (vec item)) (partition 2 (flatten vector_of_vectors))))]
      (let [all_entries (map (fn [item] (clojure.lang.MapEntry. (key item) (vec (map (fn [x] (last x)) (val item))))) gram_groups)]
        (into {} all_entries)))))

;; Stores idf value for the available grams.
(def grams_idf
  (into {} (map (fn [item] (clojure.lang.MapEntry. (key item) (count (val item)))) gram_words)))

;; Defines total entries in the dictionary.
(def total_size (count file_data))

;; Define vector of grams.
(def grams (sort (vec (distinct (flatten (vals word_grams))))))

;; Define grams frequency 
(def grams_frequency (frequencies (flatten (vals word_grams))))

;; Returns the frequency of given gram segment, 0 in case of absence.
(defn frequency_of [gram] (get grams_frequency gram default_frequency))

;; Returns the idf score of the given gram segment, 0 in of absence.
(defn idf_of [gram] (get grams_idf gram default_idf))

;; TF-IDF scores of all the grams.
(def tf_idf_score
  (into {} (map (fn [item_freq]  (clojure.lang.MapEntry. (key item_freq) (* (val item_freq) (idf_of (key item_freq))))) grams_frequency)))

;; Returns the tf-idf score of the gram.
(defn score_of [gram]  (get tf_idf_score gram default_tf_idf))

;; Defines the indices of the grams.
(def grams_index (map-indexed vector grams))

(defn gram_set_filter [word_set gram_index]
  (if (contains? word_set (last gram_index)) (score_of (last gram_index)) default_frequency))

;; Generates a vector from the given word by utilizing the ngram policy
;; and the gram frequencies.
(defn word_to_vector [word]
  (let [word_set (set (get word_grams word))]
    (map (partial gram_set_filter word_set) grams_index)))

(defn to_indexed_vector [given_vector]
  (let [position_value (filter (fn [item] (> (last item) 0)) (map-indexed vector given_vector))]
    (into {} (map (fn [item] (clojure.lang.MapEntry. (first item) (last item))) position_value))))

;; Indexed word vector representation, {word => {[index1 value] [index2 value] ...}}
(defn indexed_word_vector [word] (to_indexed_vector (word_to_vector word)))

;; Generates a vector from the query word by utilizing the ngram policy
;; and the gram frequencies.
(defn query_to_vector [query]
  (let [query_set (set (ngrams_of query))]
    (map (partial gram_set_filter query_set) grams_index)))

;; Returns indexed vector of the given query word.
(defn indexed_query_vector [query]  (to_indexed_vector (query_to_vector query)))

;; Generates the indexed vector for all the dictionary words.
(def indexed_word_vectors (zipmap file_data (map indexed_word_vector file_data)))

;; Calculates the numerator part (dot product) using indexed vector.
(defn _numerator [word query]
  (let [indices (sort (distinct (flatten [(vec (keys word)) (vec (keys query))])))]
    (reduce + (map (fn [index] (* (get word index 0) (get query index 0))) indices))))

;; Calculate the magitude of the given vector
(defn _magnitude [indexed_vector] 
  (Math/sqrt (reduce + (map (fn [item] (* item item)) (vals indexed_vector)))))

;; Denominator of cos-theta calulation.
(defn _denominator [word query] (* (_magnitude word) (_magnitude query)))

;; Calculate the dot product of two vectors.
(defn cos_theta_of [word query]
  (let [divisor (_denominator word query)]
    (if (<= divisor 0) 0.0 (float (/ (_numerator word query) divisor)))))
