offer(n2,S) :- n2:apply(S), not neg_offer(n2,S).
neg_offer(n2,S) :- n2:apply(S), not offer(n2,S).

wait_list(n2,S) :- n2:apply(S), neg_offer(n2,S), not out(n2,S).
out(n2,S) :- n2:apply(S), neg_offer(n2,S), not wait_list(n2,S).

refuse(S) :- offer(n2,S), n2:refuse(S).

second_offer(n2,S) :- wait_list(n2,S), refuse(S).


offer(n3,S) :- n3:apply(S), not neg_offer(n3,S).
neg_offer(n3,S) :- n3:apply(S), not offer(n3,S).

wait_list(n3,S) :- n3:apply(S), neg_offer(n3,S), not out(n3,S).
out(n3,S) :- n3:apply(S), neg_offer(n3,S), not wait_list(n3,S).

refuse(S) :- offer(n3,S), n3:refuse(S).

second_offer(n3,S) :- wait_list(n3,S), refuse(S).


ok(S) :- offer(n2,S).
ok(S) :- offer(n3,S).
has_wait(S) :- wait_list(n2,S).
has_wait(S) :- wait_list(n3,S).

:- not ok(S), scholarship(S).
:- not has_wait(S), scholarship(S).
:- offer(X,S), offer(Y,S), different(X,Y).
:- wait_list(X,S), wait_list(Y,S), different(X,Y).

different(n2,n3).

scholarship(s1).