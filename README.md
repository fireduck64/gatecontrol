# gatecontrol
Agent to allow control of a gate (or other relay based thing)

## Situation

Suppose you bought a house and it has this gate on the driveway and there is a gate control box that stopped working.
It has a pin pad and an intercom (non-functional).  Suppose you then further investigate and find that the wiring to
the control box is fine, it is just this unlabeled weird control board that no longer closes a relay which causes 
the gate to open.

The problem is this stuff gets into somewhat specialty gate and garage door opener crap which is poorly documented and
even if I got someone to actually fix it, it would probably be $1000 because this crap is expensive for no good reason.

So I want to replace this control board with a Raspberry Pi because I have them and know how to use them.

The box is a nice metal box with a metalic pin pad.  I want to reuse the box exterior and the pin pad because it fits and looks
ok.  With some research, I discover that the pin pad is what is called a matrix keypad where you have rows and columns and
you scan by applying voltage to columns one at a time and seeing which rows get voltage.

The gate opener is just two wires where you connect them to open the gate.  It is 5v on one wire but who cares.  
We just want a relay because we have no idea what ground is what.

## This Software

So this software is for that situation.  It reads from a matrix keypad attached to a raspberry pi and activates a relay
when the correct code is input.

The software is also written with adding other modules that can open the gate as well, like bluetooth proximity.

## Notes

If you get weird button presses, check the wiring.
If the sound is coming from the wrong device, you can screw with alsa and fail (like me) or remove the offending
driver for the offending sound device so only the one you want loads (ha).

On a Pi 4, this disabled the onboard audio so my USB speaker worked:


    $ cat /etc/modprobe.d/blacklist-bcm.conf
    blacklist snd_bcm2835

For bluetooth support, add the user you run this as so the group "bluetooth".

To check, run bluetoothctl and then issue command "scan on"
You should see a streaming pile of crap.

## Bluetooth

To enable, run as root because reasons.  The list should be the MAC addresses of the allowed bluetooth beacons.

    bluetooth_enabled=true
    bluetooth_list=AA:BB:CC:DD:EE:FF

### Security

This isn't doing any sort of cryptographic handshake.  It simply listens for addresses of beacons, which can be 
easily observed by an attacker and cloned.

### Privacy

In order to work, you get a simple bluetooth beacon and do something like keep it on your person or in your car.
The beacon will be constantly squaking its code, which someone can observe and then track you (assuming they have
a network of listening stations near where you go).



