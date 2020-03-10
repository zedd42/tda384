-module(client).
-export([handle/2, initial_state/3]).

% This record defines the structure of the state of a client.
% Add whatever other fields you need.
-record(client_st, {
    gui, % atom of the GUI process
    nick, % nick/username of the client
    server, % atom of the chat server
    channel_map % joined channels  
}).

% Return an initial state record. This is called from GUI.
% Do not change the signature of this function.
initial_state(Nick, GUIAtom, ServerAtom) ->
    #client_st{
        gui = GUIAtom,
        nick = Nick,
        server = ServerAtom,
        channel_map = maps:new() % joined channels  
    }.

% handle/2 handles each kind of request from GUI
% Parameters:
%   - the current state of the client (St)
%   - request data from GUI
% Must return a tuple {reply, Data, NewState}, where:
%   - Data is what is sent to GUI, either the atom `ok` or a tuple {error, Atom, "Error message"}
%   - NewState is the updated state of the client

% Join channel
handle(St, {join, Channel}) ->

      try
      
    _channel = list_to_atom(Channel),
    _server = St#client_st.server,
    _ref = make_ref(),
    _myPid = self(),    
    _server ! {request, _myPid, _ref, {req_channel, _channel, _myPid}},    
    receive 
      {result, _ref, Msg} ->
	case Msg of 
	 {user_added_to_channel_successfully, Channel_pid} -> 
	  % update the map
	    _newmap = maps:put(_channel, Channel_pid, St#client_st.channel_map),
	    _newState = St#client_st{channel_map = _newmap},
	    {reply, ok, _newState};
	 user_already_joined ->
	    {reply, {error, user_already_joined, "You have joined earlier"}, St}
	 end
    after 3000 ->
        {reply, {error,server_not_reached, "Couldnt reach server"}, St}
    end
    catch
                error:badarg -> {reply, {error, server_not_reached, "Channel unresponsive"}, St}
    end;
    
    
handle(St, {leave, Channel}) ->   
    _channel = list_to_atom(Channel),
    _channelMap = St#client_st.channel_map,
    
    case maps:is_key(_channel,_channelMap) of 
       
       true -> 
	try
	  _channelPid = maps:get(_channel,_channelMap),
	  _ref = make_ref(),
	  _myPid = self(),
	  _channelPid ! {request,_myPid,_ref,{leave_channel_req,_myPid}},
	   receive
	    {result,_ref,Msg} ->
	      case Msg of
		user_leave_successfully ->
		  {reply,ok,St};
		user_not_member_error ->
		  {reply,{error,user_not_joined,"Instruction failed: User not member of channel"},St}
	      end
	    
	  end
	    catch 
	    error:badarg -> {reply, {error, server_not_reached, "Channel unresponsive"}, St}
	  end;
	     
	false -> 
	  {reply,{error,user_not_joined,"Instruction failed: User not member of channel"},St}
	
      end;
	      
		
	      
	  


  

    % TODO: Implement this function
    % {reply, ok, St} ;
    
% Sending message (from GUI, to channel)
handle(St, {message_send, Channel, Msg}) ->
    
   try
    try
        _channelMap = St#client_st.channel_map,
        _channel = list_to_atom(Channel),  
        _channelPid = maps:get(_channel,_channelMap),
        _ref = make_ref(),
        _myPid = self(),
        _userNick = St#client_st.nick
        
    catch 
        _:_ -> {reply, {error, server_t_reached, "Channel unresponsive"}, St}
    end,
    
    case maps:is_key (_channel, _channelMap) of 
        false -> 
            {reply,{error,user_not_joiaaned,"User hasent joined channel"}, St};
        true -> 
        %     _channelPid = maps:get(_channel,_channelMap),
            _channelPid ! {request,_myPid,_ref,{deliver_message,Msg,_userNick,_myPid}},
            receive 
                {result,_ref, _response} ->
                case _response of
                send_message_successfully ->
                    {reply,ok,St};   %% fghg
                user_not_joined ->
                    {reply,{error,user_not_joined,"User hasent joined channel"}, St}
                
                
                end
                
            end
      end
    
      catch
      
      _:_ -> {reply, {error, user_not_joined, "Channel unresponsive"}, St}
     
     end;
    
   

% This case is only relevant for the distinction assignment!
% Change nick (no check, local only)
handle(St, {nick, NewNick}) ->
    {reply, ok, St#client_st{nick = NewNick}} ;

% ---------------------------------------------------------------------------
% The cases below do not need to be changed...flu
% But you should understand how they work!

% Get current nick
handle(St, whoami) ->
    {reply, St#client_st.nick, St} ;

% Incoming message (from channel, to GUI)
handle(St = #client_st{gui = GUI}, {message_receive, Channel, Nick, Msg}) ->
    gen_server:call(GUI, {message_receive, atom_to_list(Channel), Nick++"> "++Msg}),
    {reply, ok, St} ;

% Quit client via GUI
handle(St, quit) ->
    % Any cleanup should happen here, but this is optional
    {reply, ok, St} ;

% Catch-all for any unhandled requests
handle(St, Data) ->
    {reply, {error, not_implemented, "Client does not handle this command"}, St} .
