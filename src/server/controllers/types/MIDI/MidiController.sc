MidiController : Controller {
    // Information about Current Page
    // Array of Buttons/Knobs/Sliders
    // OSC Listener / OSC Path
    var midiConnectionDetails;
    var midiOut;
    var midiControlListener;
    var midiEndlessControlListener;
    var midiKeyboardListener;

    *oscPath{
       ^super.oscPath ++ "/midi";
    }

    *new { |name_, deviceNumber_|
        ^super.new.init(name_, deviceNumber_);
    }

    init { |name_, deviceNumber_|

        // Find Midi Device Connection Details
        midiConnectionDetails = MIDIHelpers.findEndpointDetails(name_, deviceNumber_);

        // Generate the name of the instance - we're adopting the convention of
        // lowercase path names, and not including spaces, so we'll make lowercase
        // and remove the space characters
        name = (name_ ++ "_" ++ deviceNumber_).toLower.replace(" ","_");

        // Generate the instance's OSC Path so we can generate listeners and callbacks
        oscPath = (this.class.oscPath ++ "/" ++ name);

        // MIDI is already connected on startup, so we don't need to initialize
        // the inbound connection here. However, outbound is instance based, so we do.
        midiOut = MIDIOut(0, midiConnectionDetails.uid);

        // Create a reference to the Listener Classes that will be used to build out
        // the OSC <-> Midi Translation. This is required so that the top level
        // 'Element' can be as abstract as possible. TODO: It's a bit inefficient to
        // create individual listeners for each class, so it may be wortwhile to make
        // these class-level functions within the Control/EndlessControl/Keyboard
        // listeners and return the reference to the appropriate function to the creation
        // method.
        midiControlListener = MidiControlListener.new;
        midiEndlessControlListener = MidiEndlessControlListener.new;
        midiKeyboardListener = MidiKeyboardListener.new;

        // Generate an Array2D in the same structure as the layout.
        // In this array, we'll populate the inbound and outbound functions
        // for each of the MIDI controls.

        this.populateMapping;
    }

    populateMapping {
        var layout = this.class.layout;
        var rows = layout.rows, cols = layout.cols;


        elements = Array2D.new(rows, cols);

        for (0, rows - 1, { |row|
            for(0, cols - 1, { |col|
                elements[row,col] = Mapping.genControl(this, layout[row,col]);
            });
        });
    }

    // Factories for Element Types
    getKnob { | id, channel, type |
        var knobOscPath = this.oscPath ++ "/" ++ type.asString ++ "/" ++ id;
        ^MidiKnob.new(
            midiConnectionDetails.uid,
            type,
            id,
            channel,
            (\in: [\control]),
            knobOscPath,
            midiControlListener)
    }

    getEndlessKnob { | id, channel, type |
        var knobOscPath = this.oscPath ++ "/" ++ type.asString ++ "/" ++ id;
        ^MidiEndlessKnob.new(
            midiConnectionDetails.uid,
            type,
            id,
            channel,
            (\in: [\control]),
            knobOscPath,
            midiEndlessControlListener)
    }

    getSlider { | id, channel, type |
        var sliderOscPath = this.oscPath ++ "/" ++ type.asString ++ "/" ++ id;
        ^MidiSlider.new(
            midiConnectionDetails.uid,
            type,
            id,
            channel,
            (\in: [\control]),
            sliderOscPath,
            midiControlListener);
    }

    getButton { | id, channel, type |
        var buttonOscPath = this.oscPath ++ "/" ++ type.asString ++ "/" ++ id;
        ^MidiButton.new(
            midiConnectionDetails.uid,
            type,
            id,
            channel,
            (\in: [\noteOn, \noteOff]),
            buttonOscPath,
            midiControlListener)
    }

    getLightButton { | id, channel, type |
        var lightOscPath = this.oscPath ++ "/" ++ type.asString ++ "/" ++ id;
        ^MidiLightButton.new(
            midiConnectionDetails.uid,
            type,
            id,
            channel,
            (\in: [\noteOn, \noteOff], \out: [\noteOn]),
            lightOscPath,
            midiControlListener,
            midiOut)
    }

    getNote { | id, channel, type |
        var noteOscPath = this.oscPath ++ "/" ++ type.asString ++ "/" ++ id;
        ^MidiNote.new(
            midiConnectionDetails.uid,
            type,
            id,
            channel,
            (\in: [\noteOn, \noteOff]),
            noteOscPath,
            midiControlListener,
            midiOut)
    }

    getKeyboard { | id, channel, type |
        var keyboardOscPath = this.oscPath ++ "/" ++ type.asString ++ "/" ++ id;
        ^MidiKeyboard.new(
            midiConnectionDetails.uid,
            type,
            id,
            channel,
            (\in: [\noteOn, \noteOff]),
            keyboardOscPath,
            midiKeyboardListener,
            midiOut)
    }

    getPad { | id, channel, type |
        var padOscPath = this.oscPath ++ "/" ++ type.asString ++ "/" ++ id;
        ^MidiPad.new(
            midiConnectionDetails.uid,
            type,
            id,
            channel,
            (\in: [\noteOn, \noteOff, \touch]),
            padOscPath,
            midiControlListener)
    }

    getPitchBend { | id, channel, type |
        var pitchbendOscPath = this.oscPath ++ "/" ++ type.asString ++ "/" ++ id;
        ^MidiPitchBend.new(
            midiConnectionDetails.uid,
            type,
            id,
            channel,
            (\in: [\bend]),
            pitchbendOscPath,
            midiControlListener)
    }
}

