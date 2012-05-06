p(X) :- not q(X), dom(X).
q(X) :- not p(X), dom(X).

dom(0).