offer(n4,S) :- n4:apply(S), not neg_offer(n4,S).
neg_offer(n4,S) :- n4:apply(S), not offer(n4,S).

wait_list(n4,S) :- n4:apply(S), neg_offer(n4,S), not out(n4,S).
out(n4,S) :- n4:apply(S), neg_offer(n4,S), not wait_list(n4,S).

refuse(S) :- offer(n4,S), n4:refuse(n4,S).

second_offer(n4,S) :- wait_list(n4,S), refuse(S).

ok(S) :- offer(n4,S).
has_wait(S) :- wait_list(n4,S).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
offer(n5,S) :- n5:apply(S), not neg_offer(n5,S).
neg_offer(n5,S) :- n5:apply(S), not offer(n5,S).

wait_list(n5,S) :- n5:apply(S), neg_offer(n5,S), not out(n5,S).
out(n5,S) :- n5:apply(S), neg_offer(n5,S), not wait_list(n5,S).

refuse(S) :- offer(n5,S), n5:refuse(n5,S).

second_offer(n5,S) :- wait_list(n5,S), refuse(S).

ok(S) :- offer(n5,S).
has_wait(S) :- wait_list(n5,S).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
:- not ok(S), scholarship(S).
:- not has_wait(S), scholarship(S).
:- offer(X,S), offer(Y,S), different_student(X,Y).
:- wait_list(X,S), wait_list(Y,S), different_student(X,Y).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
different_student(n4,n5).
different_student(n5,n4).
scholarship(s1).
