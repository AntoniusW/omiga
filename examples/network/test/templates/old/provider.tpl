offer({0},S) :- {0}:apply(S), not neg_offer({0},S), scholarship(S).
neg_offer({0},S) :- {0}:apply(S), not offer({0},S), scholarship(S).

wait_list({0},S) :- {0}:apply(S), neg_offer({0},S), not out({0},S), scholarship(S).
out({0},S) :- {0}:apply(S), neg_offer({0},S), not wait_list({0},S), scholarship(S).

refuse(S) :- offer({0},S), {0}:refuse({0},S).

second_offer({0},S) :- wait_list({0},S), refuse(S).

ok(S) :- offer({0},S).
has_wait(S) :- wait_list({0},S).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
