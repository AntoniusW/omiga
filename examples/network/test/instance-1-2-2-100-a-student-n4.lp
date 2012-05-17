apply(S) :- n2:scholarship(S).

pick(n4,S) :- n2:offer(n4,S), not refuse(n4,S).
refuse(n4,S) :- n2:offer(n4,S), not pick(n4,S).

ok(n4) :- pick(n4,S).

:- not ok(n4), n2:offer(n4,S).
:- pick(n4,S1), pick(n4,S2), different_scholarship(S1,S2).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
apply(S) :- n3:scholarship(S).

pick(n4,S) :- n3:offer(n4,S), not refuse(n4,S).
refuse(n4,S) :- n3:offer(n4,S), not pick(n4,S).

ok(n4) :- pick(n4,S).

:- not ok(n4), n3:offer(n4,S).
:- pick(n4,S1), pick(n4,S2), different_scholarship(S1,S2).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
different_scholarship(s0,s1).
different_scholarship(s1,s0).
