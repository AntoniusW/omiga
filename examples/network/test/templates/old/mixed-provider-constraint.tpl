:- not {0}ok(S), {0}scholarship(S).
:- not {0}has_wait(S), {0}scholarship(S).
:- {0}offer(X,S), {0}offer(Y,S), different_student(X,Y).
:- {0}wait_list(X,S), {0}wait_list(Y,S), different_student(X,Y).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
