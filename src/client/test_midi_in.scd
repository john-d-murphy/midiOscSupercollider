~connection = MIDIHelpers.findEndpointDetails("Launchpad", 1);
~connection.uid
~midiOut.status
~midiOut = MIDIOut(0, ~connection.uid);
~midiOut.connect(~connection.endPoint);

~midiOut

~midiIn = MIDIIn(0, ~connection.uid);

~func = { | i | ~midiOut.noteOn(0, 22, i); }

~func.value(0).play

MIDIFunc

//for (0, 128, { arg i; ~midiOut.noteOn(0, 22, i); 1.wait; });

for (0, 10, { arg i; i.postln; })

MIDIOut.connectAll;

(
var i = 128;
Routine {
    { i > -1 }.while {
        ~midiOut.noteOn(0, 22, i);
        ~midiOut.noteOn(0, 19, i);
        ~midiOut.noteOn(0, 16, i);
        ~midiOut.noteOn(0, 13, i);
        ~midiOut.noteOn(0, 10, i);
        ~midiOut.noteOn(0, 07, i);
        ~midiOut.noteOn(0, 04, i);
        ~midiOut.noteOn(0, 01, i);

        ~midiOut.noteOn(0, 24, i);
        ~midiOut.noteOn(0, 21, i);
        ~midiOut.noteOn(0, 18, i);
        ~midiOut.noteOn(0, 15, i);
        ~midiOut.noteOn(0, 12, i);
        ~midiOut.noteOn(0, 09, i);
        ~midiOut.noteOn(0, 06, i);
        ~midiOut.noteOn(0, 03, i);
        i = i - 1;
        i.postln;
        0.1.wait;
        ~midiOut.noteOn(0, 22, 0);
        ~midiOut.noteOn(0, 19, 0);
        ~midiOut.noteOn(0, 16, 0);
        ~midiOut.noteOn(0, 13, 0);
        ~midiOut.noteOn(0, 10, 0);
        ~midiOut.noteOn(0, 07, 0);
        ~midiOut.noteOn(0, 04, 0);
        ~midiOut.noteOn(0, 01, 0);

        ~midiOut.noteOn(0, 24, 0);
        ~midiOut.noteOn(0, 21, 0);
        ~midiOut.noteOn(0, 18, 0);
        ~midiOut.noteOn(0, 15, 0);
        ~midiOut.noteOn(0, 12, 0);
        ~midiOut.noteOn(0, 09, 0);
        ~midiOut.noteOn(0, 06, 0);
        ~midiOut.noteOn(0, 03, 0);
        0.1.wait;
    }
}.play;
)
