      %  
%    _server ! {request, _myPid, _ref, alive},%

%    receive
%        {result,_,AliveMessage} ->
%        case AliveMessage of
%            
%            im_alive -> ok
%        end%

%        after 3000 -> 
%            {reply, {error, server_not_reached, "Channel unresponsive"}, St}%

%    end,
%

 _server = St#client_st.server,
case is_process_alive(_server) of 
        
        false -> 
            {reply, {error, server_not_reached, "Channel unresponsive"}, St};
        true -> 

is_server_alive(Server) -> 
    Server ! {request, _myPid, _ref, alive},
    receive
        {result,_,Msg} ->
            case Msg of
                im_alive -> ok
            end
    after 3000 -> 
        {reply, {error, server_not_reached, "Channel unresponsive"}, St}%

      end,



       
server_handler(State,{stop,Atom}) -> 

        _channelsToDestroy = maps:keys(State#server.channel_map),
        lists:foreach(
                fun(E) -> genserver:stop(E) end, _channelsToDestroy),
        {reply,dont_care,State},
        genserver:stop(Atom),
        stop(State).
        