offer(X,S) :- apply(X,S,GPA,E), scholarship(S), require(S,GPA1,E1), GPA > GPA1, E > E1, not skip(X,S), not black_list(X).
skip(X,S)   :- apply(X,S,GPA,E), offer(Y,S), X != Y.

wait_list(X,S) :- skip(X,S), not out(X,S).
out(X,S) :- skip(X,S), wait_list(Y,S), X != Y.

second_offer(X,S) :- wait_list(X,S), refuse(S).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
