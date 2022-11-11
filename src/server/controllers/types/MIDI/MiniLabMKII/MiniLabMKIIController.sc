MiniLabMKII : MidiController {

    classvar <layout, <layoutSections;

    *initClass {
        layout =
        Array2D.fromArray(4, 11, [
           ["I", 0],["B", 113],["N",102,2], ["E", 102],["E", 103],["E", 104],["E", 105],["E", 106],["E", 107],["E", 108],["E", 109],
           ["S", 1],["B", 115],["N",115,2], ["E", 110],["E", 111],["E", 112],["E", 113],["E", 114],["E", 115],["E", 116],["E", 117],
           ["X", 0],["X", 000],["X", 000],  ["P",20,1],["P",21,1],["P",22,1],["P",23,1],["P",24,1],["P",25,1],["P",26,1],["P",27,1],
           ["KEYBOARD", 0],["X",0],["X",0], ["X", 000],["X", 000],["X", 000],["X", 000],["X", 000],["X", 000],["X", 000],["X", 000],

        ]);

        layoutSections = [
        ];

    }

    *new { |device_number|
        ^super.new("Arturia MiniLab mkII", device_number);
    }

    *oscPath{
       ^super.oscPath ++ "/minilabmkii";
    }

    //////// MINILAB MK II ELEMENT MODIFIERS ////////
    getPad { | id, channel, type |
        var padOscPath = this.oscPath ++ "/" ++ type.asString ++ "/" ++ id;
        ^MidiPad.new(
            midiConnectionDetails.uid,
            type,
            id,
            channel,
            (\in: [\noteOn, \noteOff, \polytouch]),
            padOscPath,
            midiControlListener)
    }
}
