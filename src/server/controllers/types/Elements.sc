//////// ELEMENT DEFINITION ////////
Element {

  // Everything communicates through OSC!
  // Inbound and outbound connections are
  // only there to serve as translations to
  // and translations from osc - they will
  // map the in_function and out_function, but
  // those functions should know nothing
  // about the underlying communication schema.

  // OSC Path for a MIDI Device:
  // /controller/midi/midimix/midi_mix_1/knob/21
  //
  // in listens for a midi message and broadcasts an osc message out

  classvar listeners;

  var <inPaths, <outPaths, <controlType;
  var oscRootPath;
  var outConnection;
  var id, channel, uid, messageTypes, netAddr;

  *new { |uid, controlType, id, channel, messageTypes, oscRootPath, listeners, outConnection|
    ^super.new.init(uid, controlType, id, channel, messageTypes, oscRootPath, listeners, outConnection);
  }

  init { |uid_, controlType_, id_, channel_,  messageTypes_, oscRootPath_, listeners_, outConnection_|

      // Initialize Variables
      controlType = controlType_;
      id = id_;
      oscRootPath = oscRootPath_;
      messageTypes = messageTypes_;
      outConnection = outConnection_;
      listeners = listeners_;
      inPaths = [];
      outPaths = [];
      // Potentially change to use random, open port, depending on performance
      netAddr = NetAddr("127.0.0.1", NetAddr.langPort);

      // Create Input Listeners
      messageTypes[\in].do { | i |
        var inPath = (oscRootPath ++ "/" ++ i.asString.toLower ++ "/in").asSymbol;
        inPaths = inPaths ++ inPath;
        listeners.createInputListener(netAddr, uid, i, id, channel, inPath);
      };

      // // Create Output Listeners
      messageTypes[\out].do { | i |
        var outPath = (oscRootPath ++ "/" ++ i.asString.toLower ++ "/out").asSymbol;
        outPaths = outPaths ++ outPath;
        listeners.createOutputListener(i, id, channel, outConnection, outPath);
      };
  }
}

Listener {

  var <inListener, <outListener;

  createInputListener { | netAddr, uid, type, id, channel, inPath |
    Error("createInputListener not implemented").throw;
  }

  createOutputListener { | type, id, channel, outConnection, outPath |
    Error("createInputListener not implemented").throw;
  }

}

//////// ELEMENT TYPES ////////
// Note: all prefixed with 'E_' to prevent namespace collision

E_Knob : Element { }
E_EndlessKnob : Element { }
E_Slider : Element { }
E_Button : Element {}
E_LightButton : Element {
    // For Binary Buttons
    on {
      Error("ON Not Implemented").throw;
    }

    off {
      Error("OFF Not Implemented").throw;
    }

    // For RGB Buttons
    green {
      Error("GREEN Not Implemented").throw;
    }

    greenBlink {
      Error("GREEN BLINK Not Implemented").throw;
    }

    red {
      Error("RED Not Implemented").throw;

    }

    redBlink {
      Error("RED BLINK Not Implemented").throw;
    }

    orange {
      Error("ORANGE Not Implemented").throw;
    }

    orangeBlink {
      Error("ORANGE BLINK Not Implemented").throw;
    }
}

E_Pad : Element {}
E_PitchBend : Element {}
E_Note : Element {}
E_Keyboard : Element {}

/////////// HELPERS ///////////

// Factory for the brain to generate controller mappings
Mapping {
    *genControl { | instance, mapping |
        var type = mapping[0], id = mapping[1], channel = mapping[2];
        // Channel default value is 0
        if (channel == nil) { channel = 0; };
        switch (type)
            {"K"} {^instance.getKnob(id, channel, \knob)}
            {"E"} {^instance.getEndlessKnob(id, channel, \endlessknob)}
            {"S"} {^instance.getSlider(id, channel, \slider)}
            {"B"} {^instance.getButton(id, channel, \button)}
            {"L"} {^instance.getLightButton(id, channel, \light)}
            {"P"} {^instance.getPad(id, channel, \pad)}
            {"I"} {^instance.getPitchBend(id, channel, \pitchbend)}
            {"N"} {^instance.getNote(id, channel, \note)}
            {"KEYBOARD"} {^instance.getKeyboard(id, channel, \keyboard)}
            {"X"} {^nil}  // For Empty Spots on the Grid
            {nil} {^nil}; // Blank Space - for trailing items (i.e. last row is a keyboard)
            Error("Unknown Control Type: " ++ type).throw;
    }
}
