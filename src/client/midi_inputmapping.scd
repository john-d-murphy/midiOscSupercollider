(
// register functions:
~noteOff = { arg src, chan, num, vel;    ["NOTE OFF",chan,num,vel / 127].postln; };
~noteOn = { arg src, chan, num, vel;    ["NOTE ON", chan,num,vel / 127].postln; };
~polytouch = { arg src, chan, num, vel;    ["POLYTOUCH", chan,num,vel / 127].postln; };
~control = { arg src, chan, num, val;    ["CONTROL", chan,num,val].postln; };
~program = { arg src, chan, prog;        ["PROGRAM", chan,prog].postln; };
~touch = { arg src, chan, pressure;    ["TOUCH", chan,pressure].postln; };
~bend = { arg src, chan, bend;        ["BEND", chan,bend - 8192].postln; };
~sysex = { arg src, sysex;        ["SYSEX", sysex].postln; };
~sysrt = { arg src, chan, val;        ["SYSRT", chan,val].postln; };
~smpte = { arg src, chan, val;        ["SMTPE", chan,val].postln; };
MIDIIn.addFuncTo(\noteOn, ~noteOn);
MIDIIn.addFuncTo(\noteOff, ~noteOff);
MIDIIn.addFuncTo(\polytouch, ~polytouch);
MIDIIn.addFuncTo(\control, ~control);
MIDIIn.addFuncTo(\program, ~program);
MIDIIn.addFuncTo(\touch, ~touch);
MIDIIn.addFuncTo(\bend, ~bend);
MIDIIn.addFuncTo(\sysex, ~sysex);
MIDIIn.addFuncTo(\sysrt, ~sysrt);
MIDIIn.addFuncTo(\smpte, ~smpte);
)


//cleanup
(
MIDIIn.removeFuncFrom(\noteOn, ~noteOn);
MIDIIn.removeFuncFrom(\noteOff, ~noteOff);
MIDIIn.removeFuncFrom(\polytouch, ~polytouch);
MIDIIn.removeFuncFrom(\control, ~control);
MIDIIn.removeFuncFrom(\program, ~program);
MIDIIn.removeFuncFrom(\touch, ~touch);
MIDIIn.removeFuncFrom(\bend, ~bend);
MIDIIn.removeFuncFrom(\sysex, ~sysex);
MIDIIn.removeFuncFrom(\sysrt, ~sysrt);
MIDIIn.removeFuncFrom(\smpte, ~smpte);
)


(
i = 128;
j = 0;
~netAddr = NetAddr("127.0.0.1", NetAddr.langPort);
Routine {
    { j < 128 }.while {
    j.postln;
        { i > -1 }.while {
        ~path = "/controller/midi/launchpad/launchpad_1/light/" ++ i ++ "/noteon/out".asSymbol;
        ~foo = j;
        //~foo.postln;
        //~foo = 0;
        [~foo, ~path].postln;
        ~netAddr.sendMsg(~path, 0);
        i = i - 1;
        };
    0.1.wait;
    j = j + 1;
    i = 128;
    };
}.play;
)

(
var notes, on, off;

// MIDIClient.init;
// MIDIIn.connectAll;

notes = Array.newClear(128);    // array has one slot per possible MIDI note

on = MIDIFunc.noteOn({ |veloc, num, chan, src|
    notes[num] = Synth(\default, [\freq, num.midicps,
        \amp, veloc * 0.00315 * 60]);
});

off = MIDIFunc.noteOff({ |veloc, num, chan, src|
    notes[num].release;
});

q = { on.free; off.free; };
)
