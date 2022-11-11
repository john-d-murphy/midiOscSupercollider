+Object {
  |> { |f| ^f.(this) }
  <| { |f|
        ^if(f.isKindOf(Function),
          { {|i| this.( f.(i) )} },
          { this.(f) })
  }
}

// From https://scsynth.org/t/function-chaining-operators-opinions-thoughts-do-you-like-this-a-big-hopefully-discursive-post/6627
/*
What it is
Two operators |> and <|. I shall hold off evaluating them and saying why I think they are good for a moment (if you bear with).

forward chaining ‘|>’
A way of chaining function calls together. Here, each _ becomes the result of the previous expression.

With ugens
SinOsc.ar(440) |> LPF.ar(_, \lpf.kr(15000)) |> HPF.ar(_, \hpf.kr(20))
|> Out.ar(\out.kr(0), _);
A sine wave that is filtered by a low pass, then high pass, then outputted.

With functions
~f = {|i|  "f".postln; i + 1; };
~g = {|i|  "g".postln; i * 2; };
~h = {|i|  "h".postln; i - 2; };

2 |> ~g |> ~f |> ~h;

// returns : g f h -> 3
backward chainging ‘<|’
This one is a little harder to wrap your head around, it is executed from the right to the left. Hold off on judgement just yet, I actually think this produces the cleanest code.

With functions
~h <| ~f <| ~g <| 2
// returns : g f h -> 3
With Ugens
Now this example is dumb, I’m not suggesting this actually be used, its just here for symmetry with forward chaining.

Out.ar(\out.kr(0), _) <| HPF.ar(_, \hpf.kr(20)) <| LPF.ar(_, \lpf.kr(15000)) <| SinOsc.ar(440)
How its implemented
Amazingly its just 6 lines of actual code and a few brackets. Thanks to both @jamshark70 and @semiquaver for helping with this.

+Object {
  |> { |f| ^f.(this) }
  <| { |f|
    ^if(f.isKindOf(Function),
      { {|i| this.( f.(i) )} },
      { this.(f) })
  }
}
Why I think its great!
Forwards
Consider this code, how does it read and how do you go about writing it?

{
    var foo = ...;
    var bar = ...;
  var sig = someF(foo, bar);
  sig = LPF.ar(sig, \lpf.kr());
  sig = HPF.ar(sig, \hpf.kr(1500));
  Out.ar(\out.kr(), sig);
}.play
I think the above is quite representative of standard supercollider practice. You assign variables inline and immutably where possible, but towards then end, there will be a break where you have to reassign the main signal variable just to apply some simple corrections.
This isn’t too bad…

… but the moment you have more than one signal pathway, things get more complex to unwrap.

{
  var sigA = ...;
  var sigB = ...;
  sigA = LPF.ar(sigA, \lpfA.kr());
  sigA = HPF.ar(sigA, \hpfA.kr(1500));
  sigB = LPF.ar(sigB, \lpfB.kr());
  sigB = HPF.ar(sigB, \hpfB.kr(1500));

  Out.ar(\out.kr(), sigA * sigB);
}
Above you must continually model each variables state as you work down the page. If mutations to sigA and sigB end up in different orders it can be very difficult to understand - leading to hard to find bugs and slower prototyping.
There are two options (as I can see)…

assign a varible to each stage
{
  var sigA = ...;
  var sigB = ...;
  var sigA_lpf = LPF.ar(sigA, \lpfA.kr());
  var sigA_hpf = HPF.ar(sigA_lpf, \hpfA.kr(1500));
  var sigB_lpf = LPF.ar(sigB, \lpfB.kr());
  var sigB_hpf = HPF.ar(sigB_lpf, \hpfB.kr(1500));

  Out.ar(\out.kr(), sigB_hpf * sigA_hpf);
}
Invoke a new scope for each variable
  var sigA_corrected = {
    var lpf = LPF.ar(sigA, \lpfA.kr());
    HPF.ar(lpf, \hpfA.kr(1500));
  }.();
Both of these sort of suck. 1 is hard to keep track of and you end up with many variable names, 2 is a little better but you can quickly end up with massive indentation.

With forward chain you get this…

{
  var sigA = ... |> HPF.ar(_, \hpfA.kr()) |> LPF.ar(_, \lpfA.kr());
  var sigB = ... |> HPF.ar(_, \hpfB.kr()) |> LPF.ar(_, \lpfB.kr());
  Out.ar(\out.kr(), sigA * sigB);
}
There is simply no need for variables here. Further, it makes it easier to see how you might abstract the problem - at least easier than in the first example above.

{
  var correct = {|in, hpf, lpf|
    in |> HPF.ar(_, hpf) |> LPF.ar(_, lpf)
  };
  var sigA = SinOsc.ar(20) |> correct.(_, \hpfA.kr(20), \lpfA.kr(2000));
  var sigB = SinOsc.ar(200) |> correct.(_, \hpfB.kr(20), \lpfB.kr(2000));
  Out.ar(\out.kr(), sigA * sigB);
}.play
Backwards - this one is the most useful!
Consider how you would read the following blocks of code. That is, how would you parse them into english.

A
SinOsc.ar(LFNoise2.kr(SinOsc.kr(50).range(200, 350)).range(200, 500), mul: 0.3);
A sine wave whose frequency is shaped like a smooth noise updating by a sine wave whose frequency is 50Hz whose range is between 200 and 350 Hz. Now that original smooth noise’s range is between 200 and 500, and the original sine wave has an amplitude of 0.3.

Ouch, that is hard to say! But this isn’t a good practice, so lets look at that…

B)
var freqcarrier = SinOsc.kr(50).range(200, 350);
var freq = LFNoise2.kr(freqcarrier).range(200, 500);
var sig = SinOsc.ar(freq, mul: 0.3);
There exists a frequency-carrier whose is a sine wave oscillating at 50Hz between 200, and 350.
There exists a frequency shaped like smoothed noise controlled by said frequency-carrier whose range is between 200 and 500 Hz.
There exists a signal, shaped like a sine wave oscillating at some frequency with an amplitude of 0.3.

Not too bad, but still you have to read through all the code before you get to the important line (the last line). After the first line, you are left thinking, great so there is a frequency-carrier, where does that go?, and its not until the last line that you actually see where the sound comes from and there you find the purpose of the code.

C)
SinOsc.ar(_, mul: 0.3) <| _.range(200, 500) <| SinOsc.ar(_) <| _.range(200, 350) <| SinOsc.ar(50)
a sine wave with 0.3 amplitude whose frequency…
… is between 200 and 500 Hz and is controled by …
… a sine wave updating …
… between 200 and 350 Hz controled by …
… a sine wave at 50 Hz

I think this is so much cleaner. From the first statement you know exactly what you are dealing with, a sine wave whose frequency changes. Then you read the important musical information next, the frequency of the sine wave. This continues where as you read from left to right, you get smaller musical details, despite the code being evaluated backwards.

I also think this is easier to write, since that you are able to start directly with the sounding object, then add detail as you move towards the right. I.e., you want to do FM synthesis so you write a sine wave, and mark the frequency with an underscore, then define its pitch, then how to changes…

Some criticisms
If you over do it, it get weird
The following is valid.

~f = {|i|  "f".postln; i + 1; };
~g = {|i|  "g".postln; i * 2; };
~h = {|i|  "h".postln; i - 2; };

~f <| ( ~g <| 1 |> ~h |> ~g ) |> ~h
Its the same as… (1 |> ~g |> ~h |> ~g |> ~f |> ~h) … but still.

Brackets
For forward chaining, you need to add brackets around each function

1 |> (_ + 1) |> (_ * 2); // works
1 |> _ + 1 |> _ * 2; // does not
It gets weirder with backwards…

_ * 2 <|  _ + 1 <| 1 // this will evaluate, but returns the wrong result.
(_ * 2) <| ( _ + 1) <| 1 // correct
However this mostly goes away if you call messages with a . on the underscore.

You cannot do multiple arguments.
I actually think this is a good thing, other languages where this is possible become quite complex, and I’m not proposing this is a replacement, but a complimentary syntax.

… actually you can - but its a mess…

-1 |> (_.neg) |> (_ * 3.5) |> { |r|
  2 |> (_.sin) |> { |two|
    4 |> _.pow(two) |> _.round(r)
  }
}
*/


