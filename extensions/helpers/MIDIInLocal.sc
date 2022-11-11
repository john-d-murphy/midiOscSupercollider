MidiInLocal {
  var port,
  <> action,
  <> noteOn, <> noteOff, <> polytouch,
  <> control, <> program,
  <> touch, <> bend,
  <> sysex, sysexPacket, <> sysrt, <> smpte, <> invalid,
  <> noteOnList, <> noteOffList, <> polyList,
  <> controlList, <> programList,
  <> touchList, <> bendList,
  <> noteOnZeroAsNoteOff = true;


  // Constructors
  *new { |inport_, device_|
     ^super.new.init(inport_, device_);
  }

  init { |inport_, device_|
      connect(inport_, device_);
  }


  // safer than global setters
  addFuncTo { |what, func|
    this.perform(what.asSetter, this.perform(what).addFunc(func))
  }

  removeFuncFrom { |what, func|
    this.perform(what.asSetter, this.perform(what).removeFunc(func))
  }

  replaceFuncTo { |what, func, newFunc|
    this.perform(what.asSetter, this.perform(what).replaceFunc(func, newFunc))
  }


  doAction { arg src, status, a, b, c;
    action.value(src, status, a, b, c);
  }

  doNoteOnAction { arg src, chan, num, veloc;
    if ( noteOnZeroAsNoteOff and: ( veloc == 0 ) ){
      noteOff.value(src, chan, num, veloc);
      this.prDispatchEvent(noteOffList, \noteOff, src, chan, num, veloc);
    }{
      noteOn.value(src, chan, num, veloc);
      this.prDispatchEvent(noteOnList, \noteOn, src, chan, num, veloc);
    };
  }

  doNoteOffAction { arg src, chan, num, veloc;
    noteOff.value(src, chan, num, veloc);
    this.prDispatchEvent(noteOffList, \noteOff, src, chan, num, veloc);
  }
  doPolyTouchAction { arg src, chan, num, val;
    polytouch.value(src, chan, num, val);
    this.prDispatchEvent(polyList, \poly, src, chan, num, val);
  }
  doControlAction { arg src, chan, num, val;
    control.value(src, chan, num, val);
    this.prDispatchEvent(controlList, \control, src, chan, num, val);
  }
  doProgramAction { arg src, chan, val;
    program.value(src, chan, val);
    this.prDispatchEvent(programList, \program, src, chan, val);
  }
  doTouchAction { arg src, chan, val;
    touch.value(src, chan, val);
    this.prDispatchEvent(touchList, \touch, src, chan, val);
  }
  doBendAction { arg src, chan, val;
    bend.value(src, chan, val);
    this.prDispatchEvent(bendList, \bend, src, chan, val);
  }

  doSysexAction { arg src,  packet;
    sysexPacket = sysexPacket ++ packet;
    if (packet.last == -9, {
      sysex.value(src, sysexPacket);
      sysexPacket = nil
    });
  }
  doInvalidSysexAction { arg src, packet;
    invalid.value(src, packet);
  }

  doSysrtAction { arg src, index, val;
    sysrt.value(src, index, val);
  }

  doSMPTEaction { arg src, frameRate, timecode;
    smpte.value(src, frameRate, timecode);
  }

  findPort { arg deviceName,portName;
    ^MIDIClient.sources.detect({ |endPoint| endPoint.device == deviceName and: {endPoint.name == portName}});
  }

  connect { arg inport=0, device=0;
  //   var uid,source;
  //   if(MIDIClient.initialized.not,{ MIDIClient.init });
  //   if(device.isNumber, {
  //     if(device >= 0, {
  //       if ( device > MIDIClient.sources.size,{ // on linux the uid's are very large numbers
  //         source = MIDIClient.sources.detect{ |it| it.uid == device };
  //         if(source.isNil,{
  //           ("MIDI device with uid"+device+ "not found").warn;
  //         },{
  //           uid = source.uid;
  //         })
  //       },{
  //         source = MIDIClient.sources.at(device);
  //         if(source.isNil,{
  //           "MIDIClient failed to init".warn;
  //         },{
  //           uid = MIDIClient.sources.at(device).uid;
  //         });
  //       });
  //     },{ // elsewhere they tend to be negative
  //       uid = device;
  //     });
  //   },{
  //     if(device.isKindOf(MIDIEndPoint), {uid = device.uid}); // else error
  //   });
  //   this.connectByUID(inport,uid);
  }

  disconnect { arg inport=0, device=0;
  //   var uid, source;
  //   if(device.isKindOf(MIDIEndPoint), {uid = device.uid});
  //   if(device.isNumber, {
  //     if(device.isPositive, {
  //       if ( device > MIDIClient.sources.size,
  //         {
  //           source = MIDIClient.sources.select{ |it| it.uid == device }.first;
  //           if(source.isNil,{
  //             ("MIDI device with uid"+device+ "not found").warn;
  //           },{
  //             uid = source.uid;
  //           })
  //         },
  //         {
  //           source = MIDIClient.sources.at(device);
  //           if(source.isNil,{
  //             "MIDIClient failed to init".warn;
  //           },{
  //             uid = MIDIClient.sources.at(device).uid;
  //           });
  //         });
  //     },{
  //       uid = device;
  //     });
  //   });
  //   this.disconnectByUID(inport,uid);
  }
  // connectByUID {arg inport, uid;
  //   _ConnectMIDIIn
  //   ^this.primitiveFailed;
  // }
  // disconnectByUID {arg inport, uid;
  //   _DisconnectMIDIIn
  //   ^this.primitiveFailed;
  // }

  // prDispatchEvent { arg eventList, status, port, chan, b, c;
  //   var selectedEvents;
  //   eventList ?? {^this};
  //   eventList.takeThese {| event |
  //     if (event.match(port, chan, b, c))
  //     {
  //       selectedEvents = selectedEvents.add(event);
  //       true
  //     }
  //     { false };
  //   };
  //   selectedEvents.do{ |event|
  //       event.set(status, port, chan, b, c);
  //       event.thread.next;
  //   }
  // }
}
