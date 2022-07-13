# Word-Completion-in-Clojure
A dictionary based word auto completion using n-gram analysis in Clojure. This work demonstrates an auto-completion of a word while typing. It is the teaching/learning outcome among staff in a work place. 

-----
#### Summary of Discussion. 
(document as vector of inverted index.)
	
	d1: t11, t12, ..., t1k1
	d2: t21, t22, ..., t2k2
	d3 
	
	dn
	------------------------------------------
	doc/ terms
		t1 	t2	t3	... tK
	d1: s1	s2
	d2:
	
	where K = union (ki)
	
	-------------------------------------------
	score => normalized tf-idf.
	-------------------------------------------
	interverted index: to avoid sparse 0 zeros.
	-------------------------------------------
	
  #### Test data prepration:
	[w1 w2 ... w1000]
	1. 200 test, 800 training
	test = (take (suffle data), 200)	
	training = (filter (fn[w] (not (.contains test w))), data)
	
	2. [t1 t2 ... t200]
		t1 -> [w1, w600, w300, w999]
		t2 -> []
		.
		.
		.
		t200 -> []
		
	Suggestion:
		t1 = [w600, w1, w401, w470, w999]
	
		insert ? [w401, w470]
		deletion ? [w300]
		...
		
		accuracy => intersection(tgiven, tsuggested) 3 / 4 = 75%
	-----------------------------------------------------------------
  
  #### Complete Source Code is in the current repository. 
  
  #### Usage: 
  ```Clojure
  (println "Type incomplete word: ")
  (let [i_word (read-line)]    
      (println "\nSuggestions: ")
      (println (similar_suggestions (clojure.string/lower-case i_word))))
  ```
  
