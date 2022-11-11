APCMini {

    // Endpoint Details
    var endPointDetails;

    // Control Surface
    var <faders, <pads, <buttons;

    // CC = Control Change / MN = Midi Note
    var ctls, midiOut, ccFaders, mnPads, mnShift;

    // Colors
    var off         = 0;
    var green       = 1;
    var greenBlink  = 2;
    var red         = 3;
    var redBlink    = 4;
    var orange      = 5;
    var orangeBlink = 6;

    // If no UID is specified, UID will be nil, and the messages will apply to all controllers.
    // Adding this as an option instead of explicitly calling the convenience method, because it's possible to
    // have more than one APC MINI, and we want to allow for that.
    *new {|endPointDetails|
        ^super.newCopyArgs(endPointDetails).init() }

    init {
        ctls = ();

        faders   = List[];
        buttons  = List[];
        pads     = List[];
        ccFaders = (48..56);
        mnPads   = (0..63);
        mnShift  = 98;

        //midiOut  = MIDIOut.newByName("APC MINI", "APC MINI MIDI 1");
        midiOut  = MIDIOut(0, endPointDetails.uid);
        midiOut.connect(endPointDetails.endPoint);
        midiOut.latency = 0;

        this.assignCtls();
    }

    assignCtls {
        ccFaders.do {|cc, i|
            var key = ("fader" ++ (i+1)).asSymbol;
            var fader = APCMiniFader(key, cc, endPointDetails.uid);
            faders.add(fader);
            ctls.put(key, fader);
        };

        mnPads.do {|mn, i|
            var row, column, key, pad;
            row    = (i / 8).asInteger;
            column = i % 8;
            key    = ("pad" ++ (row + 1) ++ "_" ++ (column + 1)).asSymbol;
            pad    = APCMiniButton(key, mn, endPointDetails.uid, midiOut);
            pads.add(pad);
            ctls.put(key, pad);
        }
    }
}

APCMiniFader {
    var key, cc, uid;
    var state = 0, def;

    *new {|key, cc, uid|
        ^super.newCopyArgs(("apcmini_" ++ key).asSymbol, cc, uid);
    }

    onCC_ {|func|
        def = MIDIdef.cc(key, func, cc, 0, uid);
    }

    free {
        def.free;
    }

}

APCMiniButton {
     var key, mn, uid, midiOut;
     var state = 0, noteOnDef, noteOffDef;

     *new {|key, mn, uid, midiOut|
        ^super.newCopyArgs(("apcmini_" ++ key).asSymbol, mn, uid, midiOut);
     }

     onNoteOn_ {|func|
        noteOnDef = MIDIdef.noteOn(key ++ "_on", func, mn, 0, uid);
     }

     onNoteOff_ {|func|
        noteOffDef = MIDIdef.noteOff(key ++ "_off", func, mn, 0, uid);
     }

     setColor {|color|
         ("% %\n").postf(mn, color);
         midiOut.noteOn(0, mn, color);
     }

     free {
         noteOnDef.free;
         noteOffDef.free;
     }
}
///
/// APCMiniButton : APCMiniController {
///     var key, cc;
///
///     *new {|key, cc, aMidiOut|
///         ^super.newCopyArgs(("apcmini_" ++ key).asSymbol, cc, aMidiOut);
///     }
///
///     onPress_ {|func|
///         MIDIdef.cc((key ++ "_on").asSymbol, {|val|
///             if (val == 127) {
///                 func.(val, this)
///             }
///         }, cc);
///     }
///
///     onRelease_ {|func|
///         MIDIdef.cc((key ++ "_off").asSymbol, {|val|
///             if (val == 0) {
///                 func.(val, this)
///             }
///         }, cc);
///     }
///
///     ledState {
///         ^state;
///     }
///
///     ledState_ {|val|
///         val   = val.clip(0, 1);
///         state = val;
///
///         midiOut !? {
///             midiOut.control(0, cc, 127 * val);
///         };
///     }
/// }
