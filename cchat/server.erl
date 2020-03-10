-module(server).
-export([start/1, stop/1, server_handler/2]).
-record (server, {active_clients = [], channel_map = maps:new()}).
-record (clients_connected_to_channel, {clients_pids = [], channel_name}).

% Start a new server process with the given name
% Do not change the signature of this function.
start(ServerAtom) ->

    
   genserver:start(ServerAtom, #server{}, fun server_handler/2).
   
stop(ServerAtom) ->
    
    
    genserver:stop(ServerAtom).

    
    % begär att få tillgång till en kanal.
    % kolla om kanalen existerar sen tidigare
    % om den existerar 
    
server_handler(State, {req_channel, Channel_name, From_pid }) ->
  
  case maps:is_key(Channel_name, State#server.channel_map) of 
            
            true -> 
                Channel_pid = maps:get(Channel_name, State#server.channel_map),         
                Channel_pid ! {request, self(), make_ref(),{join_channel_request, From_pid}},

           
               receive
                    {result, _, Msg} -> 
                        case Msg of 
                            user_added_to_channel_successfully ->
                                {reply,{user_added_to_channel_successfully , Channel_pid}, State};
                            user_already_joined ->
                                {reply, user_already_joined, State}
                        end
                    end;
            
         
               false -> 
                    _new_ChannelPid = genserver:start(
                        Channel_name, 
                        #clients_connected_to_channel{clients_pids = [From_pid],channel_name = Channel_name }, 
                        fun server_handler/2 ),
                    _new_map = maps:put (Channel_name, _new_ChannelPid, State#server.channel_map),
                    _newState = State#server{channel_map = _new_map},
                    {reply, {user_added_to_channel_successfully, _new_ChannelPid }, _newState}
       end  ;    
                 

server_handler(State, {leave_channel_req, UserPid}) -> 

        case lists:member(UserPid, State#clients_connected_to_channel.clients_pids ) of 
            true ->
                _remainingClients = lists:delete(UserPid, State#clients_connected_to_channel.clients_pids),
                _newState = State#clients_connected_to_channel{clients_pids = _remainingClients},
                {reply, user_leave_successfully, _newState};
            false -> 
                {reply, user_not_member_error, State }
        end;
                
            

        
        
        
        
      
server_handler(State, {join_channel_request, From_pid}) -> 

        case lists:member(From_pid, State#clients_connected_to_channel.clients_pids) of
            
            true ->
                
                {reply, user_already_joined, State};
            false ->
                _newState = State#clients_connected_to_channel{
                        clients_pids = [From_pid |State#clients_connected_to_channel.clients_pids] },
                   
                {reply, user_added_to_channel_successfully, _newState}
          end;

          
 server_handler(State, {deliver_message, Msg, _userNick, _fromPid}) ->
    
    
       case  lists:member(_fromPid, State#clients_connected_to_channel.clients_pids) of 
        false ->
            {reply,user_d ,State};
        true ->
            spawn(fun() -> sendMessage(State,Msg,_userNick,_fromPid) end),
            {reply,send_message_successfully,State}
     end;
        
        
server_handler(State,{stop,Atom}) -> 

        _channelsToDestroy = maps:keys(State#server.channel_map),
        lists:foreach(
                fun(E) -> genserver:stop(E) end, _channelsToDestroy),
        genserver:stop(Atom),
        {reply,dont_care,State}.        


sendMessage(State ,Msg, _userNick, _senderPid) ->

        

        UserPids =     lists:delete(_senderPid,State#clients_connected_to_channel.clients_pids),
        ChannelName = State#clients_connected_to_channel.channel_name,

        [spawn(fun() -> genserver:request(Client, 
                {message_receive, ChannelName, _userNick, Msg}) end) || Client <- UserPids].




 
                    
              
        
