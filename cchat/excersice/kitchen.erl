-module(kitchen).
-export([fridge/1, store/2, start/1]).

start(Foodlist) ->
    spawn(?MODULE, fridge, [Foodlist]).


fridge(Foodlist) -> 
    %how does it know that its a list, why does the argument begin with upper case%
    receive
        {From, {store, Food}} ->
            From ! {self(),is_stored},
            fridge([Food|Foodlist]);
        exit -> 
            ok
    end.

store(Pid, Food) -> 
    Pid ! {self(), {store, Food}},
    receive
       {Pid, Msg} -> Msg
    end.


