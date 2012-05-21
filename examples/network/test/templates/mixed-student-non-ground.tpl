{0}apply(S,GPA,E) :- {0}scholarship(S), {0}profile(GPA,E).

{0}pick(S) :- {0}offer({0},S), not {0}refuse(S).
{0}refuse(S) :- {0}offer({0},S), {0}pick(T), S != T.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
