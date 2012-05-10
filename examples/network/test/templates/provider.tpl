offer({0},S) :- {0}:apply(S), not neg_offer({0},S).
neg_offer({0},S) :- {0}:apply(S), not offer({0},S).

wait_list({0},S) :- {0}:apply(S), neg_offer({0},S), not out({0},S).
out({0},S) :- {0}:apply(S), neg_offer({0},S), not wait_list({0},S).

refuse(S) :- offer({0},S), {0}:refuse(S).

second_offer({0},S) :- wait_list({0},S), refuse(S), not neg_second_offer({0},S).
neg_second_offer({0},S) :- wait_list({0},S), refuse(X), not second_offer({0},S).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
