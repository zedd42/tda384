% hello world program
-module(helloworld).
-export([start/0]).
-export([bla/0]).

start() -> 
    io:fwrite("Hello, world\n").

bla() -> 
    io:fwrite("hej").