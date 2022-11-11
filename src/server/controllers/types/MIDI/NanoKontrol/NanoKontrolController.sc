NanoKontrol : MidiController {
    classvar <layout, <layoutSections;

    // Similar to the MPD18, the nanokontrol has 'scenes' that change the source value of the inputs.
    // As there is a big problem with jitter with discrete knobs, we will have one scene only and
    // handle the rest with software.

    *initClass {
        layout =
        Array2D.fromArray(4, 12, [
           ["B",01],["B",02],["B",03], ["K",07],["K",08],["K",09],["K",10],["K",11],["K",12],["K",13],["K",14],["K",15],
           ["B",04],["B",05],["B",06], ["B",43],["B",44],["B",45],["B",46],["B",47],["B",48],["B",49],["B",50],["B",51],
           ["X",00],["X",00],["X",00], ["S",74],["S",75],["S",76],["S",77],["S",78],["S",79],["S",80],["S",81],["S",82],
           ["X",00],["X",00],["X",00], ["B",43],["B",44],["B",45],["B",46],["B",47],["B",48],["B",49],["B",50],["B",51],
        ]);

        layoutSections = [
            NanoKontrolTransport,
        ];

    }

    *new { |device_number|
        ^super.new("nanoKONTROL", device_number);
    }

    *oscPath{
       ^super.oscPath ++ "/nanokontrol";
    }

    getButton { | id, type |
        var buttonOscPath = this.oscPath ++ "/" ++ type.asString ++ "/" ++ id;
        ^MidiButton.new(
            type,
            id,
            midiConnectionDetails.uid,
            (\in: [\control]),
            buttonOscPath)
    }

}
