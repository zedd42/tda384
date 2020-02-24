-module(server).
-export([start/1]).
-record (s_status, {active_clients = [], channel_map = maps:new()}).

% Start a new server process with the given name
% Do not change the signature of this function.
start(ServerAtom) ->

    
   genserver:start(ServerAtom, #s_status{}, fun() -> io:fwrite("Anonymous Function") end).
