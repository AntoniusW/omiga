apply(S) :- n2:scholarship(S).

pick(n6,S) :- n2:offer(n6,S), not refuse(n6,S).
refuse(n6,S) :- n2:offer(n6,S), not pick(n6,S).

ok(n6) :- pick(n6,S).

:- not ok(n6), n2:offer(n6,S).
:- pick(n6,S1), pick(n6,S2), different_scholarship(S1,S2).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
apply(S) :- n3:scholarship(S).

pick(n6,S) :- n3:offer(n6,S), not refuse(n6,S).
refuse(n6,S) :- n3:offer(n6,S), not pick(n6,S).

ok(n6) :- pick(n6,S).

:- not ok(n6), n3:offer(n6,S).
:- pick(n6,S1), pick(n6,S2), different_scholarship(S1,S2).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
apply(S) :- n4:scholarship(S).

pick(n6,S) :- n4:offer(n6,S), not refuse(n6,S).
refuse(n6,S) :- n4:offer(n6,S), not pick(n6,S).

ok(n6) :- pick(n6,S).

:- not ok(n6), n4:offer(n6,S).
:- pick(n6,S1), pick(n6,S2), different_scholarship(S1,S2).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
different_scholarship(s0,s1).
different_scholarship(s0,s2).
different_scholarship(s1,s0).
different_scholarship(s1,s2).
different_scholarship(s2,s0).
different_scholarship(s2,s1).
