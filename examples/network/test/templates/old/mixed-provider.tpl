{1}offer({0},S) :- {0}apply(S), not {1}neg_offer({0},S), {1}scholarship(S).
{1}neg_offer({0},S) :- {0}apply(S), not {1}offer({0},S), {1}scholarship(S).

{1}wait_list({0},S) :- {0}apply(S), {1}neg_offer({0},S), not {1}out({0},S), {1}scholarship(S).
{1}out({0},S) :- {0}apply(S), {1}neg_offer({0},S), not {1}wait_list({0},S), {1}scholarship(S).

{1}refuse(S) :- {1}offer({0},S), {0}refuse({0},S).

{1}second_offer({0},S) :- {1}wait_list({0},S), {1}refuse(S).

{1}ok(S) :- {1}offer({0},S).
{1}has_wait(S) :- {1}wait_list({0},S).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
