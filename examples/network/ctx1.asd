%b :- a, not c.
%c :- a, not b.
%a.


p(X) :- n2:q(X).
other_p(X) :- n2:r(X).

p(1).
p(3).
p(a).
