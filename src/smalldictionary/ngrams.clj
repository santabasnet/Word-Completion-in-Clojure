;;NGram Utility Program.
;;Contains all the ngram related functions.
;;Author: Santa
;;Date: 2020-08-10

(ns smalldictionary.ngrams
  (:gen-class))

;; Finds the sub-string of a word with given indices.
(defn sub_string
  ([index_data] (sub_string (first index_data) (last index_data)))
  ([indices word] (subs word (first indices) (last indices))))

;; Defines the ngram generation strategy for the given word based
;; on the length. 
(defn ngram_sizes [word]
  (let [length (count word)]
    (cond
      (< length 2) []
      (<= length 5) [2 3]
      :else [3, 4])))

(defn word_indices [size, word]
  (vec (for [x (range (- (count word) (- size 1)))] [x, (+ x size)])))

(defn word_size_gram
  ([size_with_word] (word_size_gram (first size_with_word) (last size_with_word)))
  ([size word] (vec (map sub_string (for [index_range (word_indices size, word)] [index_range, word])))))

;; Generates n-grams of the word based on the length count strategy defined
;; in the ngram_sizes.
(defn ngrams_of [word]
  (flatten (map word_size_gram (for [size (ngram_sizes word)] [size, word]))))
