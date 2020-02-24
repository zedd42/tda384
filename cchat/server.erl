-module(server).
-export([start/1, stop/1, server_handler/2]).
-record (s_status, {active_clients = [], channel_map = maps:new()}).

% Start a new server process with the given name
% Do not change the signature of this function.
start(ServerAtom) ->

    
   genserver:start(ServerAtom, #s_status{}, server_handler/2).
   
stop(ServerAtom) ->
	genserver:request(ServerAtom,{stop,ServerAtom}),
	ok.

	
server_handler(s_status, {})
