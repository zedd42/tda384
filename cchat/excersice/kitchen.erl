-module(kitchen).
-export([fridge/1, store/2, take/2, skriv/2, start/1]).

start(Foodlist) ->
    spawn(?MODULE, fridge, [Foodlist]).


fridge(Foodlist) -> 
    %how does it know that its a list, why does the argument begin with upper case%
    receive
        {From, {store, Food}} ->
            From ! {self(), is_stored},
            fridge([Food|Foodlist]);
        {From, {take, Food}} -> 
            case lists:member(Food, Foodlist) of
                true ->
                    From ! {self(), is_taken},
                    fridge(lists:delete(Food, Foodlist));
                false -> 
                    From ! {self(), not_found},
                    fridge(Foodlist)
            end;
        {From, {skriv, Food}} -> 
            From ! {self(), {Food}, skriv};
        terminate ->
            ok
    end.

store(Pid, Food) -> 
    Pid ! {self(), {store, Food}},
    receive
       {Pid, Msg} -> Msg
    end.

take(Pid, Food) -> 
    Pid ! {self(), {take, Food}},
    receive
        {Pid, Msg} -> Msg
    end.

skriv(Pid, Food) ->
    Pid ! {self(), {skriv, Food}},
    receive
        {Pid, Msg} -> Msg
    end.

