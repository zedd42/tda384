-module(exServer).
-export([start/0, handler/0]).


start() ->
   Pid = spawn(?MODULE, handler, []).
    

handler() ->

    receive 
        {SenderPid, {join, Cname}} -> 
            Pid = spawn(?MODULE, channelHandler, [Cname, SenderPid]),
            register(Cname, Pid)
            
    end.

channelHandler(Cname, SenderPid, MassegeThread) -> 
    a_chanel_has_been_created.




