//Car central process
C:
  - has global variable state
  - forks process at start:
            - process 1 monitors socket and updates state variable w every server input
            - passes current state variable to python using a FIFO
  - separate process runs updating server with data and monitors car sensors
Python:
  - C code calls python which opens the serial connection to the arduino
  - Waits until C code passes state
  - forwards to arduino
