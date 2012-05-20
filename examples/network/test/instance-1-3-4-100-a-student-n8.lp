apply(S) :- n2:scholarship(S).

pick(n8,S) :- n2:offer(n8,S), not refuse(n8,S).
refuse(n8,S) :- n2:offer(n8,S), not pick(n8,S).

ok(n8) :- pick(n8,S).

:- not ok(n8), n2:offer(n8,S).
:- pick(n8,S1), pick(n8,S2), different_scholarship(S1,S2).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
apply(S) :- n3:scholarship(S).

pick(n8,S) :- n3:offer(n8,S), not refuse(n8,S).
refuse(n8,S) :- n3:offer(n8,S), not pick(n8,S).

ok(n8) :- pick(n8,S).

:- not ok(n8), n3:offer(n8,S).
:- pick(n8,S1), pick(n8,S2), different_scholarship(S1,S2).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
apply(S) :- n4:scholarship(S).

pick(n8,S) :- n4:offer(n8,S), not refuse(n8,S).
refuse(n8,S) :- n4:offer(n8,S), not pick(n8,S).

ok(n8) :- pick(n8,S).

:- not ok(n8), n4:offer(n8,S).
:- pick(n8,S1), pick(n8,S2), different_scholarship(S1,S2).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
different_scholarship(s0,s1).
different_scholarship(s0,s2).
different_scholarship(s1,s0).
different_scholarship(s1,s2).
different_scholarship(s2,s0).
different_scholarship(s2,s1).
