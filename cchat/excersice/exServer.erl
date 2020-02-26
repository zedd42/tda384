-module(exServer).
-export([start/0, handler/0]).


start() ->
   Pid = spawn(?MODULE, handler, []).
    

handler() ->

    receive 
        {SenderPid, Msg} -> 
            SenderPid ! {SenderPid, Msg}
    end.


