MidiMix : MidiController {

    classvar <layout, <layoutSections;

    *initClass {
        layout =
        Array2D.fromArray(6, 9, [
           ["K", 16], ["K", 20], ["K", 24], ["K", 28], ["K", 46], ["K", 50], ["K", 54], ["K", 58],  ["X", 00],
           ["K", 17], ["K", 21], ["K", 25], ["K", 29], ["K", 47], ["K", 51], ["K", 55], ["K", 59],  ["B", 25],
           ["K", 18], ["K", 22], ["K", 26], ["K", 30], ["K", 48], ["K", 52], ["K", 56], ["K", 60],  ["B", 26],

           ["L", 01], ["L", 04], ["L", 07], ["L", 10], ["L", 13], ["L", 16], ["L", 19], ["L", 22],  ["B", 27],
           ["L", 03], ["L", 06], ["L", 09], ["L", 12], ["L", 15], ["L", 18], ["L", 21], ["L", 24],  ["X", 00],

           ["S", 19], ["S", 23], ["S", 27], ["S", 31], ["S", 49], ["S", 53], ["S", 53], ["S", 61],  ["S", 62],
        ]);

        layoutSections = [
            MidiMixChannel_01,
            MidiMixChannel_02,
            MidiMixChannel_03,
            MidiMixChannel_04,
            MidiMixChannel_05,
            MidiMixChannel_06,
            MidiMixChannel_07,
            MidiMixChannel_08
        ];

    }

    *new { |device_number|
        ^super.new("MIDI Mix", device_number);
    }

    *oscPath{
       ^super.oscPath ++ "/midimix";
    }

    // Instance Specific Methods - values that change
    // between physical instances of a MidiMix controller. That is,
    // all controllers do not share the same UID. Anything local to a
    // controller should return a reference to the instance's knobs/etc.
    // Factories for MidiMix Specific Button Types

    // getLightButton { | cc |
    //     ^MidiMixLightButton.new(uid, cc)
    // }

}
