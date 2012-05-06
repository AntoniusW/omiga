succ(1,0).
succ(2,1).
succ(3,2).

lt(0,3).
lt(1,3).
lt(2,3).

aprime(X) :- a(X), lt(X,3).
a(X) :- n2:b(Y), succ(X,Y).