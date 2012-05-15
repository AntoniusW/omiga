apply(S) :- n1:scholarship(S).

pick(n3,S) :- n1:offer(n3,S), not refuse(n3,S).
refuse(n3,S) :- n1:offer(n3,S), not pick(n3,S).