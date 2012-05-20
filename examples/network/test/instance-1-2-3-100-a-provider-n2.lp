offer(n4,S) :- n4:apply(S), not neg_offer(n4,S), scholarship(S).
neg_offer(n4,S) :- n4:apply(S), not offer(n4,S), scholarship(S).

wait_list(n4,S) :- n4:apply(S), neg_offer(n4,S), not out(n4,S), scholarship(S).
out(n4,S) :- n4:apply(S), neg_offer(n4,S), not wait_list(n4,S), scholarship(S).

refuse(S) :- offer(n4,S), n4:refuse(n4,S).

second_offer(n4,S) :- wait_list(n4,S), refuse(S).

ok(S) :- offer(n4,S).
has_wait(S) :- wait_list(n4,S).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
offer(n5,S) :- n5:apply(S), not neg_offer(n5,S), scholarship(S).
neg_offer(n5,S) :- n5:apply(S), not offer(n5,S), scholarship(S).

wait_list(n5,S) :- n5:apply(S), neg_offer(n5,S), not out(n5,S), scholarship(S).
out(n5,S) :- n5:apply(S), neg_offer(n5,S), not wait_list(n5,S), scholarship(S).

refuse(S) :- offer(n5,S), n5:refuse(n5,S).

second_offer(n5,S) :- wait_list(n5,S), refuse(S).

ok(S) :- offer(n5,S).
has_wait(S) :- wait_list(n5,S).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
offer(n6,S) :- n6:apply(S), not neg_offer(n6,S), scholarship(S).
neg_offer(n6,S) :- n6:apply(S), not offer(n6,S), scholarship(S).

wait_list(n6,S) :- n6:apply(S), neg_offer(n6,S), not out(n6,S), scholarship(S).
out(n6,S) :- n6:apply(S), neg_offer(n6,S), not wait_list(n6,S), scholarship(S).

refuse(S) :- offer(n6,S), n6:refuse(n6,S).

second_offer(n6,S) :- wait_list(n6,S), refuse(S).

ok(S) :- offer(n6,S).
has_wait(S) :- wait_list(n6,S).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
:- not ok(S), scholarship(S).
:- not has_wait(S), scholarship(S).
:- offer(X,S), offer(Y,S), different_student(X,Y).
:- wait_list(X,S), wait_list(Y,S), different_student(X,Y).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
different_student(n4,n5).
different_student(n4,n6).
different_student(n5,n4).
different_student(n5,n6).
different_student(n6,n4).
different_student(n6,n5).
scholarship(s0).
