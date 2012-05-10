apply(S) :- {0}:scholarship(S).

pick({1},S) :- {0}:offer({1},S), not refuse({1},S).
refuse({1},S) :- {0}:offer({1},S), not pick({1},S).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
