apply({0},S,GPA,E) :- {0}:apply(S,GPA,E), scholarship(S).
refuse(S) :- offer({0},S), {0}:refuse(S).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
