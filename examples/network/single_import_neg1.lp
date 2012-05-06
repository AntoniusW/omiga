rprime(X) :- r(X).
sprime(X) :- s(X).

r(X) :- not n2:p(X), dom(X).
s(X) :- not n2:q(X), dom(X).

dom(0).