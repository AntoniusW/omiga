apply(S) :- n2:scholarship(S).

pick(n7,S) :- n2:offer(n7,S), not refuse(n7,S).
refuse(n7,S) :- n2:offer(n7,S), not pick(n7,S).

ok(n7) :- pick(n7,S).

:- not ok(n7), n2:offer(n7,S).
:- pick(n7,S1), pick(n7,S2), different_scholarship(S1,S2).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
apply(S) :- n3:scholarship(S).

pick(n7,S) :- n3:offer(n7,S), not refuse(n7,S).
refuse(n7,S) :- n3:offer(n7,S), not pick(n7,S).

ok(n7) :- pick(n7,S).

:- not ok(n7), n3:offer(n7,S).
:- pick(n7,S1), pick(n7,S2), different_scholarship(S1,S2).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
different_scholarship(s0,s1).
different_scholarship(s1,s0).
