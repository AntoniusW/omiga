apply(S) :- n1:scholarship(S).

pick(n2,S) :- n1:offer(n2,S), not refuse(n2,S).
refuse(n2,S) :- n1:offer(n2,S), not pick(n2,S).