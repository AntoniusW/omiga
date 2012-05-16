apply(S) :- n2:scholarship(S).

pick(n5,S) :- n2:offer(n5,S), not refuse(n5,S).
refuse(n5,S) :- n2:offer(n5,S), not pick(n5,S).

%ok(n5) :- pick(n5,S).

%:- not ok(n5), n2:offer(n5,S).
%:- pick(n5,S1), pick(n5,S2), different_scholarship(S1,S2).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
apply(S) :- n3:scholarship(S).

pick(n5,S) :- n3:offer(n5,S), not refuse(n5,S).
refuse(n5,S) :- n3:offer(n5,S), not pick(n5,S).

%ok(n5) :- pick(n5,S).

%:- not ok(n5), n3:offer(n5,S).
%:- pick(n5,S1), pick(n5,S2), different_scholarship(S1,S2).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
different_scholarship(s0,s1).
different_scholarship(s1,s0).
