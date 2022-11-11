
MIDIHelpers {

    /* Helper Method to Find the Endpoint Details for the nth occurence of a given regex.
       Returns an Endpoint Details Object.
       Note that the current Endpoint is set to -2 because the first two MIDI Endpoints are System->Timer and System->Announce and neither of those are considered when deriving the output device number increment, but do appear in the sources list. */

    *findEndpointDetails { |regex, deviceNumber|
        var sources = MIDIClient.sources, deviceCount = 1, currentEndPoint = -2, endPointDetails = EndPointDetails.new(-1,-1);
        ("Looking for [" ++ regex ++ "] device Number: " ++ deviceNumber).postln;
        sources.do { |source|
            // source.device.postln;
            if (source.device.matchRegexp(regex)) {
                if (deviceNumber == deviceCount) {
                    source.postln;
                    endPointDetails.uid = source.uid;
                    endPointDetails.endPoint = currentEndPoint;
                };
                deviceCount = deviceCount + 1;
            };
            currentEndPoint = currentEndPoint + 1;
        };
        if (endPointDetails.uid == -1) {
            Error("Could not find device number " ++ deviceNumber ++ " matching regex [" ++ regex ++ "]").throw;
        };
        ^endPointDetails;
    }

}

EndPointDetails {

    var <>uid, <>endPoint;

    *new {|uid, endPoint|
        ^super.newCopyArgs(uid,endPoint);
    }
}


+ MIDIOut {
    *connectAll { |verbose=true|
        var outPort = 0;
        MIDIClient.destinations.do({ |destination|
            // Don't include loopbacks
            if (destination.device.matchRegexp("SuperCollider"))
                {
                    ("Skipping Loopback " ++ destination).postln;
                }
                {
                    MIDIOut.connect(outPort, destination.uid);
                    outPort = outPort + 1;
                }
        });
    }
}


