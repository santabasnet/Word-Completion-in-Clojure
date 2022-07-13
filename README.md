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
  
  #### Complete Source Code is in the current repo. 
  #### Usage: 
