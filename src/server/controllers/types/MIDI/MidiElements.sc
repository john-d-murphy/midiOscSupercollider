//////// LiSTENERS ////////

// A control listener is an object that listens for 'control' signals on a specific
// element (i.e. a knob or button) that does not function as a keyboard. It's possible
// for a control element to give a 'NoteOn/NoteOff' as a switch, but it does not mean
// that it is actually providing pitch values on a specific channel.
MidiControlListener : Listener {

  createInputListener { | netAddr, uid, type, id, channel, inPath |
      // Create MIDI Listener that broadcasts the value received over OSC
      // The purpose of this is to erase the distinction between MIDI and
      // OSC and allow all input devices to follow the same spec.
      inListener = { | val, num |
          netAddr.sendMsg(inPath, val);
      };
      MIDIdef.new(inPath, inListener, id, channel, type, uid, nil, nil);
  }

  createOutputListener { |type, id, channel, outConnection, outPath |
      // Create OSC listener that waits to receive a packet, and then
      // forwards the value of the packet via MIDI to the appropriate
      // controller.

      var valid = false;
      switch (type)
        {\noteOn} { OSCdef(outPath,
                    {|msg| outConnection.noteOn(0, id, msg[1]); },
                    outPath.asString); valid = true};

      // Throw Error if invalid type found
      if (valid, {}, { Error("Unknown Output Message Type").throw } );
  }
}

// An endless control listener is an object that listens for 'control' signals on a specific
// endless element (i.e. a knob). Currently, the only type of endless encoder is the 'Arturia
// Relative 1' style, which sends values 61-63 when turned in a negative direction
// and values 65-67 when turned in a positive direction. This means to get the new offset,
// we subtract 64 from the recieved value, and return the new value over the wire. There's
// currently no such thing as an output for an endless encoder, so that method is not defined.

MidiEndlessControlListener : Listener {

  createInputListener { | netAddr, uid, type, id, channel, inPath, offsetVal = 64 |
      var netValue = 0;
      inListener = { | val, num |
          netValue = netValue + (val - offsetVal);
          netAddr.sendMsg(inPath, netValue);
      };
      MIDIdef.new(inPath, inListener, id, channel, type, uid, nil, nil);
  }
}


// A keyboard listener is a special type of listener that listens for *all*
// events of a specific type on a channel. This is because a keyboard functions
// as a contiguous set of values, not a set of invididual, discrete values.
// Note that this does *not* mean that a keyboard can't be used to send individual
// midi controls and a keyboard must be used as a keyboard, but rather that in the
// case where we have a keyboard that we can use it.
//
// Important to note that the OSC message sent from the midi keyboard listener has
// the keynumber *as well as* the intensity value. This is to simplify the application
// of keyboard values, but it is important to note because it means that a keyboard
// needs to be handled with different logic than any other control.
MidiKeyboardListener : Listener {
  createInputListener { | netAddr, uid, type, id, channel, inPath |
      inListener = { | val, num |
          netAddr.sendMsg(inPath, num, val);
      };
      MIDIdef.new(inPath, inListener, nil, channel, type, uid, nil, nil);
  }
}


//////// ELEMENT TYPES ////////

MidiKnob : E_Knob {}
MidiEndlessKnob : E_EndlessKnob {}
MidiSlider : E_Slider {}
MidiButton : E_Button {}
MidiLightButton : E_LightButton {}
MidiPad : E_Pad {}
MidiPitchBend : E_PitchBend {}
MidiNote : E_Note {}
MidiKeyboard : E_Keyboard {}
