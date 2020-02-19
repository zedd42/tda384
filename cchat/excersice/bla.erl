-module(bla).
-export([start/0, serviceHandler/0]).






start() ->

    Pid1 = spawn(bla,serviceHandler,[]),
    Pid1.



serviceHandler() ->
    receive
        {message,From,Data} ->
            From!{ok,Data},
            serviceHandler();

        {exit,From} ->
            From!{ok,"Terminated"}
    end.


