:- n2offer(X,S1), n3offer(X,S2).
:- n2offer(X,S1), n4offer(X,S2).
:- n3offer(X,S1), n4offer(X,S2).
n2offer(n5,S) :- n5apply(S), not n2neg_offer(n5,S), n2scholarship(S).
n2neg_offer(n5,S) :- n5apply(S), not n2offer(n5,S), n2scholarship(S).

n2wait_list(n5,S) :- n5apply(S), n2neg_offer(n5,S), not n2out(n5,S), n2scholarship(S).
n2out(n5,S) :- n5apply(S), n2neg_offer(n5,S), not n2wait_list(n5,S), n2scholarship(S).

n2refuse(S) :- n2offer(n5,S), n5refuse(n5,S).

n2second_offer(n5,S) :- n2wait_list(n5,S), n2refuse(S).

n2ok(S) :- n2offer(n5,S).
n2has_wait(S) :- n2wait_list(n5,S).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
n2offer(n6,S) :- n6apply(S), not n2neg_offer(n6,S), n2scholarship(S).
n2neg_offer(n6,S) :- n6apply(S), not n2offer(n6,S), n2scholarship(S).

n2wait_list(n6,S) :- n6apply(S), n2neg_offer(n6,S), not n2out(n6,S), n2scholarship(S).
n2out(n6,S) :- n6apply(S), n2neg_offer(n6,S), not n2wait_list(n6,S), n2scholarship(S).

n2refuse(S) :- n2offer(n6,S), n6refuse(n6,S).

n2second_offer(n6,S) :- n2wait_list(n6,S), n2refuse(S).

n2ok(S) :- n2offer(n6,S).
n2has_wait(S) :- n2wait_list(n6,S).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
n2offer(n7,S) :- n7apply(S), not n2neg_offer(n7,S), n2scholarship(S).
n2neg_offer(n7,S) :- n7apply(S), not n2offer(n7,S), n2scholarship(S).

n2wait_list(n7,S) :- n7apply(S), n2neg_offer(n7,S), not n2out(n7,S), n2scholarship(S).
n2out(n7,S) :- n7apply(S), n2neg_offer(n7,S), not n2wait_list(n7,S), n2scholarship(S).

n2refuse(S) :- n2offer(n7,S), n7refuse(n7,S).

n2second_offer(n7,S) :- n2wait_list(n7,S), n2refuse(S).

n2ok(S) :- n2offer(n7,S).
n2has_wait(S) :- n2wait_list(n7,S).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
:- not n2ok(S), n2scholarship(S).
:- not n2has_wait(S), n2scholarship(S).
:- n2offer(X,S), n2offer(Y,S), different_student(X,Y).
:- n2wait_list(X,S), n2wait_list(Y,S), different_student(X,Y).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
n2scholarship(s0).
n3offer(n5,S) :- n5apply(S), not n3neg_offer(n5,S), n3scholarship(S).
n3neg_offer(n5,S) :- n5apply(S), not n3offer(n5,S), n3scholarship(S).

n3wait_list(n5,S) :- n5apply(S), n3neg_offer(n5,S), not n3out(n5,S), n3scholarship(S).
n3out(n5,S) :- n5apply(S), n3neg_offer(n5,S), not n3wait_list(n5,S), n3scholarship(S).

n3refuse(S) :- n3offer(n5,S), n5refuse(n5,S).

n3second_offer(n5,S) :- n3wait_list(n5,S), n3refuse(S).

n3ok(S) :- n3offer(n5,S).
n3has_wait(S) :- n3wait_list(n5,S).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
n3offer(n6,S) :- n6apply(S), not n3neg_offer(n6,S), n3scholarship(S).
n3neg_offer(n6,S) :- n6apply(S), not n3offer(n6,S), n3scholarship(S).

n3wait_list(n6,S) :- n6apply(S), n3neg_offer(n6,S), not n3out(n6,S), n3scholarship(S).
n3out(n6,S) :- n6apply(S), n3neg_offer(n6,S), not n3wait_list(n6,S), n3scholarship(S).

n3refuse(S) :- n3offer(n6,S), n6refuse(n6,S).

n3second_offer(n6,S) :- n3wait_list(n6,S), n3refuse(S).

n3ok(S) :- n3offer(n6,S).
n3has_wait(S) :- n3wait_list(n6,S).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
n3offer(n7,S) :- n7apply(S), not n3neg_offer(n7,S), n3scholarship(S).
n3neg_offer(n7,S) :- n7apply(S), not n3offer(n7,S), n3scholarship(S).

n3wait_list(n7,S) :- n7apply(S), n3neg_offer(n7,S), not n3out(n7,S), n3scholarship(S).
n3out(n7,S) :- n7apply(S), n3neg_offer(n7,S), not n3wait_list(n7,S), n3scholarship(S).

n3refuse(S) :- n3offer(n7,S), n7refuse(n7,S).

n3second_offer(n7,S) :- n3wait_list(n7,S), n3refuse(S).

n3ok(S) :- n3offer(n7,S).
n3has_wait(S) :- n3wait_list(n7,S).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
:- not n3ok(S), n3scholarship(S).
:- not n3has_wait(S), n3scholarship(S).
:- n3offer(X,S), n3offer(Y,S), different_student(X,Y).
:- n3wait_list(X,S), n3wait_list(Y,S), different_student(X,Y).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
n3scholarship(s1).
n4offer(n5,S) :- n5apply(S), not n4neg_offer(n5,S), n4scholarship(S).
n4neg_offer(n5,S) :- n5apply(S), not n4offer(n5,S), n4scholarship(S).

n4wait_list(n5,S) :- n5apply(S), n4neg_offer(n5,S), not n4out(n5,S), n4scholarship(S).
n4out(n5,S) :- n5apply(S), n4neg_offer(n5,S), not n4wait_list(n5,S), n4scholarship(S).

n4refuse(S) :- n4offer(n5,S), n5refuse(n5,S).

n4second_offer(n5,S) :- n4wait_list(n5,S), n4refuse(S).

n4ok(S) :- n4offer(n5,S).
n4has_wait(S) :- n4wait_list(n5,S).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
n4offer(n6,S) :- n6apply(S), not n4neg_offer(n6,S), n4scholarship(S).
n4neg_offer(n6,S) :- n6apply(S), not n4offer(n6,S), n4scholarship(S).

n4wait_list(n6,S) :- n6apply(S), n4neg_offer(n6,S), not n4out(n6,S), n4scholarship(S).
n4out(n6,S) :- n6apply(S), n4neg_offer(n6,S), not n4wait_list(n6,S), n4scholarship(S).

n4refuse(S) :- n4offer(n6,S), n6refuse(n6,S).

n4second_offer(n6,S) :- n4wait_list(n6,S), n4refuse(S).

n4ok(S) :- n4offer(n6,S).
n4has_wait(S) :- n4wait_list(n6,S).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
n4offer(n7,S) :- n7apply(S), not n4neg_offer(n7,S), n4scholarship(S).
n4neg_offer(n7,S) :- n7apply(S), not n4offer(n7,S), n4scholarship(S).

n4wait_list(n7,S) :- n7apply(S), n4neg_offer(n7,S), not n4out(n7,S), n4scholarship(S).
n4out(n7,S) :- n7apply(S), n4neg_offer(n7,S), not n4wait_list(n7,S), n4scholarship(S).

n4refuse(S) :- n4offer(n7,S), n7refuse(n7,S).

n4second_offer(n7,S) :- n4wait_list(n7,S), n4refuse(S).

n4ok(S) :- n4offer(n7,S).
n4has_wait(S) :- n4wait_list(n7,S).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
:- not n4ok(S), n4scholarship(S).
:- not n4has_wait(S), n4scholarship(S).
:- n4offer(X,S), n4offer(Y,S), different_student(X,Y).
:- n4wait_list(X,S), n4wait_list(Y,S), different_student(X,Y).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
n4scholarship(s2).
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
n5apply(S) :- n4scholarship(S).

n5pick(n5,S) :- n4offer(n5,S), not n5refuse(n5,S).
n5refuse(n5,S) :- n4offer(n5,S), not n5pick(n5,S).

n5ok(n5) :- n5pick(n5,S).

:- not n5ok(n5), n4offer(n5,S).
:- n5pick(n5,S1), n5pick(n5,S2), different_scholarship(S1,S2).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
n6apply(S) :- n2scholarship(S).

n6pick(n6,S) :- n2offer(n6,S), not n6refuse(n6,S).
n6refuse(n6,S) :- n2offer(n6,S), not n6pick(n6,S).

n6ok(n6) :- n6pick(n6,S).

:- not n6ok(n6), n2offer(n6,S).
:- n6pick(n6,S1), n6pick(n6,S2), different_scholarship(S1,S2).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
n6apply(S) :- n3scholarship(S).

n6pick(n6,S) :- n3offer(n6,S), not n6refuse(n6,S).
n6refuse(n6,S) :- n3offer(n6,S), not n6pick(n6,S).

n6ok(n6) :- n6pick(n6,S).

:- not n6ok(n6), n3offer(n6,S).
:- n6pick(n6,S1), n6pick(n6,S2), different_scholarship(S1,S2).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
n6apply(S) :- n4scholarship(S).

n6pick(n6,S) :- n4offer(n6,S), not n6refuse(n6,S).
n6refuse(n6,S) :- n4offer(n6,S), not n6pick(n6,S).

n6ok(n6) :- n6pick(n6,S).

:- not n6ok(n6), n4offer(n6,S).
:- n6pick(n6,S1), n6pick(n6,S2), different_scholarship(S1,S2).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
n7apply(S) :- n2scholarship(S).

n7pick(n7,S) :- n2offer(n7,S), not n7refuse(n7,S).
n7refuse(n7,S) :- n2offer(n7,S), not n7pick(n7,S).

n7ok(n7) :- n7pick(n7,S).

:- not n7ok(n7), n2offer(n7,S).
:- n7pick(n7,S1), n7pick(n7,S2), different_scholarship(S1,S2).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
n7apply(S) :- n3scholarship(S).

n7pick(n7,S) :- n3offer(n7,S), not n7refuse(n7,S).
n7refuse(n7,S) :- n3offer(n7,S), not n7pick(n7,S).

n7ok(n7) :- n7pick(n7,S).

:- not n7ok(n7), n3offer(n7,S).
:- n7pick(n7,S1), n7pick(n7,S2), different_scholarship(S1,S2).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
n7apply(S) :- n4scholarship(S).

n7pick(n7,S) :- n4offer(n7,S), not n7refuse(n7,S).
n7refuse(n7,S) :- n4offer(n7,S), not n7pick(n7,S).

n7ok(n7) :- n7pick(n7,S).

:- not n7ok(n7), n4offer(n7,S).
:- n7pick(n7,S1), n7pick(n7,S2), different_scholarship(S1,S2).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
different_student(n5,n6).
different_student(n5,n7).
different_student(n6,n5).
different_student(n6,n7).
different_student(n7,n5).
different_student(n7,n6).
different_scholarship(s0,s1).
different_scholarship(s0,s2).
different_scholarship(s1,s0).
different_scholarship(s1,s2).
different_scholarship(s2,s0).
different_scholarship(s2,s1).
