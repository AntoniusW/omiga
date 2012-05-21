apply(S) :- {0}:scholarship(S).

pick({1},S) :- {0}:offer({1},S), not refuse({1},S).
refuse({1},S) :- {0}:offer({1},S), not pick({1},S).

ok({1}) :- pick({1},S).

:- not ok({1}), {0}:offer({1},S).
:- pick({1},S1), pick({1},S2), different_scholarship(S1,S2).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
