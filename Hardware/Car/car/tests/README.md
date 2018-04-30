## Code Modules:
    UDP Server (Module 1)
    Serial Connection (Module 2)
    State Machine (Module 3)
    UDP Server --> Serial Connection --> State Machine (Module 4)

##  Tests:
    udp_test suite --> Tests Module 1
                 --> for speed
                 --> for reliability
    isolated_serial suite --> Tests Module 2
                 --> for reliability
    hardware suite --> Tests Module 3
                 --> checks if Hardware works (Module 3)
    timed_stepper suite --> Tests Module 3
                 --> checks functionality of Module 3
    serial_test suite --> Tests Module 4
                 --> checks reliability of connections
    clutter suite --> Tests Module 4
                 --> checks responsiveness of machine accross
                     connections with increasing command frequency

##  Coverage:
    As my tests are functional, I can only show that each of my tests
    covers a specific "module" of code. Tests covering Module 4 test
    that not only does each module test sufficiently, but that they
    test in concert together.
    My tests also target various performance barriers (namely time),
    and test the limitations set by the environment (the environment
    here is the UDP connection being used, which has the limitation of
    being lossy).
    While 100% of the code is tested, the data used to test the code
    is completely based on the possible values that could exist in
    framework the car was designed to work in. Thus this code is
    untested for use outside of the designed system for our project,
    which means that it relies heavily on the the accuracy (and
    therefore testing) of the other project components.
