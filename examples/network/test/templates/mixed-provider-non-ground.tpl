{0}offer(X,S) :- {0}apply(X,S,GPA,E), {0}scholarship(S), {0}require(S,GPA1,E1), GPA > GPA1, E > E1, not {0}skip(X,S), not {0}black_list(X).
{0}skip(X,S)   :- {0}apply(X,S,GPA,E), {0}offer(Y,S), X != Y.

{0}wait_list(X,S) :- {0}skip(X,S), not {0}out(X,S).
{0}out(X,S) :- {0}skip(X,S), {0}wait_list(Y,S), X != Y.

{0}second_offer(X,S) :- {0}wait_list(X,S), {0}refuse(S).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
