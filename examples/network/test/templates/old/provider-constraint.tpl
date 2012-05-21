:- not ok(S), scholarship(S).
:- not has_wait(S), scholarship(S).
:- offer(X,S), offer(Y,S), different_student(X,Y).
:- wait_list(X,S), wait_list(Y,S), different_student(X,Y).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
