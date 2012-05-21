apply(S,GPA,E) :- scholarship(S), profile(GPA,E).

pick(S) :- offer({0},S), not refuse(S).
refuse(S) :- offer({0},S), pick(T), S != T.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
