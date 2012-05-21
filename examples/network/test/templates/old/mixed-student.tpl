{1}apply(S) :- {0}scholarship(S).

{1}pick({1},S) :- {0}offer({1},S), not {1}refuse({1},S).
{1}refuse({1},S) :- {0}offer({1},S), not {1}pick({1},S).

{1}ok({1}) :- {1}pick({1},S).

:- not {1}ok({1}), {0}offer({1},S).
:- {1}pick({1},S1), {1}pick({1},S2), different_scholarship(S1,S2).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
