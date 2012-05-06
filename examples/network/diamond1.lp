a(X) :- not b(X), dom(X).
b(X) :- not a(X), dom(X).

dom(0).

