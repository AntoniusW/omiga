:- n2offer(X,S1), n3offer(X,S2).
n2offer(n4,S) :- n4apply(S), not n2neg_offer(n4,S), n2scholarship(S).
n2neg_offer(n4,S) :- n4apply(S), not n2offer(n4,S), n2scholarship(S).

n2wait_list(n4,S) :- n4apply(S), n2neg_offer(n4,S), not n2out(n4,S), n2scholarship(S).
n2out(n4,S) :- n4apply(S), n2neg_offer(n4,S), not n2wait_list(n4,S), n2scholarship(S).

n2refuse(S) :- n2offer(n4,S), n4refuse(n4,S).

n2second_offer(n4,S) :- n2wait_list(n4,S), n2refuse(S).

n2ok(S) :- n2offer(n4,S).
n2has_wait(S) :- n2wait_list(n4,S).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
n2offer(n5,S) :- n5apply(S), not n2neg_offer(n5,S), n2scholarship(S).
n2neg_offer(n5,S) :- n5apply(S), not n2offer(n5,S), n2scholarship(S).

n2wait_list(n5,S) :- n5apply(S), n2neg_offer(n5,S), not n2out(n5,S), n2scholarship(S).
n2out(n5,S) :- n5apply(S), n2neg_offer(n5,S), not n2wait_list(n5,S), n2scholarship(S).

n2refuse(S) :- n2offer(n5,S), n5refuse(n5,S).

n2second_offer(n5,S) :- n2wait_list(n5,S), n2refuse(S).

n2ok(S) :- n2offer(n5,S).
n2has_wait(S) :- n2wait_list(n5,S).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
:- not n2ok(S), n2scholarship(S).
:- not n2has_wait(S), n2scholarship(S).
:- n2offer(X,S), n2offer(Y,S), different_student(X,Y).
:- n2wait_list(X,S), n2wait_list(Y,S), different_student(X,Y).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
n2scholarship(s0).
n3offer(n4,S) :- n4apply(S), not n3neg_offer(n4,S), n3scholarship(S).
n3neg_offer(n4,S) :- n4apply(S), not n3offer(n4,S), n3scholarship(S).

n3wait_list(n4,S) :- n4apply(S), n3neg_offer(n4,S), not n3out(n4,S), n3scholarship(S).
n3out(n4,S) :- n4apply(S), n3neg_offer(n4,S), not n3wait_list(n4,S), n3scholarship(S).

n3refuse(S) :- n3offer(n4,S), n4refuse(n4,S).

n3second_offer(n4,S) :- n3wait_list(n4,S), n3refuse(S).

n3ok(S) :- n3offer(n4,S).
n3has_wait(S) :- n3wait_list(n4,S).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
n3offer(n5,S) :- n5apply(S), not n3neg_offer(n5,S), n3scholarship(S).
n3neg_offer(n5,S) :- n5apply(S), not n3offer(n5,S), n3scholarship(S).

n3wait_list(n5,S) :- n5apply(S), n3neg_offer(n5,S), not n3out(n5,S), n3scholarship(S).
n3out(n5,S) :- n5apply(S), n3neg_offer(n5,S), not n3wait_list(n5,S), n3scholarship(S).

n3refuse(S) :- n3offer(n5,S), n5refuse(n5,S).

n3second_offer(n5,S) :- n3wait_list(n5,S), n3refuse(S).

n3ok(S) :- n3offer(n5,S).
n3has_wait(S) :- n3wait_list(n5,S).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
:- not n3ok(S), n3scholarship(S).
:- not n3has_wait(S), n3scholarship(S).
:- n3offer(X,S), n3offer(Y,S), different_student(X,Y).
:- n3wait_list(X,S), n3wait_list(Y,S), different_student(X,Y).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
n3scholarship(s1).
n4apply(S) :- n2scholarship(S).

n4pick(n4,S) :- n2offer(n4,S), not n4refuse(n4,S).
n4refuse(n4,S) :- n2offer(n4,S), not n4pick(n4,S).

n4ok(n4) :- n4pick(n4,S).

:- not n4ok(n4), n2offer(n4,S).
:- n4pick(n4,S1), n4pick(n4,S2), different_scholarship(S1,S2).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
n4apply(S) :- n3scholarship(S).

n4pick(n4,S) :- n3offer(n4,S), not n4refuse(n4,S).
n4refuse(n4,S) :- n3offer(n4,S), not n4pick(n4,S).

n4ok(n4) :- n4pick(n4,S).

:- not n4ok(n4), n3offer(n4,S).
:- n4pick(n4,S1), n4pick(n4,S2), different_scholarship(S1,S2).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
n5apply(S) :- n2scholarship(S).

n5pick(n5,S) :- n2offer(n5,S), not n5refuse(n5,S).
n5refuse(n5,S) :- n2offer(n5,S), not n5pick(n5,S).

n5ok(n5) :- n5pick(n5,S).

:- not n5ok(n5), n2offer(n5,S).
:- n5pick(n5,S1), n5pick(n5,S2), different_scholarship(S1,S2).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
n5apply(S) :- n3scholarship(S).

n5pick(n5,S) :- n3offer(n5,S), not n5refuse(n5,S).
n5refuse(n5,S) :- n3offer(n5,S), not n5pick(n5,S).

n5ok(n5) :- n5pick(n5,S).

:- not n5ok(n5), n3offer(n5,S).
:- n5pick(n5,S1), n5pick(n5,S2), different_scholarship(S1,S2).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
different_student(n4,n5).
different_student(n5,n4).
different_scholarship(s0,s1).
different_scholarship(s1,s0).
